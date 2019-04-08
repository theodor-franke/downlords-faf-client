package com.faforever.client.remote;

import com.faforever.client.SpringProfiles;
import com.faforever.client.game.Faction;
import com.faforever.client.game.GameInfoServerMessage;
import com.faforever.client.game.GameInfoServerMessage.Player;
import com.faforever.client.game.GameState;
import com.faforever.client.game.HostGameRequest;
import com.faforever.client.game.KnownFeaturedMod;
import com.faforever.client.game.StartGameProcessServerMessage;
import com.faforever.client.game.relay.GpgGameMessage;
import com.faforever.client.game.relay.ice.IceServerList;
import com.faforever.client.i18n.I18n;
import com.faforever.client.matchmaking.MatchMakerInfoServerMessage;
import com.faforever.client.net.ConnectionState;
import com.faforever.client.notification.Action;
import com.faforever.client.notification.NotificationService;
import com.faforever.client.notification.PersistentNotification;
import com.faforever.client.notification.Severity;
import com.faforever.client.player.PlayerServerMessage;
import com.faforever.client.player.PlayersServerMessage;
import com.faforever.client.task.CompletableTask;
import com.faforever.client.task.TaskService;
import com.faforever.client.user.AccountDetailsServerMessage;
import com.faforever.client.user.LoginSuccessEvent;
import com.google.common.collect.ImmutableMap;
import com.google.common.eventbus.EventBus;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;

import static com.faforever.client.task.CompletableTask.Priority.HIGH;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

@Lazy
@Component
@Profile(SpringProfiles.PROFILE_OFFLINE)
// NOSONAR
public class MockFafServerAccessor implements FafServerAccessor {

  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private static final String USER_NAME = "MockUser";
  private final Timer timer;
  private final ApplicationEventPublisher eventPublisher;

  private final TaskService taskService;
  private final NotificationService notificationService;
  private final I18n i18n;
  private final EventBus eventBus;

  private ObjectProperty<ConnectionState> connectionState;


  public MockFafServerAccessor(ApplicationEventPublisher eventPublisher, TaskService taskService, NotificationService notificationService, I18n i18n, EventBus eventBus) {
    this.eventPublisher = eventPublisher;
    timer = new Timer("LobbyServerAccessorTimer", true);
    connectionState = new SimpleObjectProperty<>();
    this.taskService = taskService;
    this.notificationService = notificationService;
    this.i18n = i18n;
    this.eventBus = eventBus;
  }

  @Override
  public ReadOnlyObjectProperty<ConnectionState> connectionStateProperty() {
    return connectionState;
  }

  @Override
  public CompletableFuture<AccountDetailsServerMessage> connectAndLogIn(String username, String password) {
    return taskService.submitTask(new CompletableTask<AccountDetailsServerMessage>(HIGH) {
      @Override
      protected AccountDetailsServerMessage call() {
        updateTitle(i18n.get("login.progress.message"));

        PlayerServerMessage playerServerMessage = new PlayerServerMessage();
        playerServerMessage.setId(4812);
        playerServerMessage.setDisplayName(USER_NAME);
        playerServerMessage.setClanTag("ABC");
        playerServerMessage.setCountry("A1");
        playerServerMessage.getRanks().put(KnownFeaturedMod.DEFAULT.getTechnicalName(), 5);
        playerServerMessage.getRanks().put(KnownFeaturedMod.LADDER_1V1.getTechnicalName(), 3);
        playerServerMessage.setNumberOfGames(330);

        PlayersServerMessage playersMessage = new PlayersServerMessage();
        playersMessage.setPlayers(singletonList(playerServerMessage));

        eventBus.post(new LoginSuccessEvent(username, username, password, playerServerMessage.getId()));

        eventPublisher.publishEvent(playersMessage);

        timer.schedule(new TimerTask() {
          @Override
          public void run() {
            UpdatedAchievement updatedAchievement = new UpdatedAchievement();
            updatedAchievement.setAchievementId("50260d04-90ff-45c8-816b-4ad8d7b97ecd");
            updatedAchievement.setNewlyUnlocked(true);

            UpdatedAchievementsServerMessage updatedAchievementsServerMessage = new UpdatedAchievementsServerMessage(singletonList(updatedAchievement));
            eventPublisher.publishEvent(updatedAchievementsServerMessage);
          }
        }, 7000);

        timer.schedule(new TimerTask() {
          @Override
          public void run() {
            MatchMakerInfoServerMessage matchmakerServerMessage = new MatchMakerInfoServerMessage(ImmutableMap.of(
              "ladder1v1", 5
            ));
            eventPublisher.publishEvent(matchmakerServerMessage);
          }
        }, 7000);

        List<GameInfoServerMessage> gameInfoMessages = Arrays.asList(
          createGameInfo(1, "Mock game 500 - 800", false, "faf", "scmp_010", 6, "Mock user"),
          createGameInfo(2, "Mock game 500+", false, "faf", "scmp_011", 6, "Mock user"),
          createGameInfo(3, "Mock game +500", false, "faf", "scmp_012", 6, "Mock user"),
          createGameInfo(4, "Mock game <1000", false, "faf", "scmp_013", 6, "Mock user"),
          createGameInfo(5, "Mock game >1000", false, "faf", "scmp_014", 6, "Mock user"),
          createGameInfo(6, "Mock game ~600", true, "faf", "scmp_015", 6, "Mock user"),
          createGameInfo(7, "Mock game 7", true, "faf", "scmp_016", 6, "Mock user")
        );

        gameInfoMessages.forEach(eventPublisher::publishEvent);

        notificationService.addNotification(
          new PersistentNotification(
            "How about a long-running (7s) mock task?",
            Severity.INFO,
            Arrays.asList(
              new Action("Execute", event ->
                taskService.submitTask(new CompletableTask<Void>(HIGH) {
                  @Override
                  protected Void call() throws Exception {
                    updateTitle("Mock task");
                    Thread.sleep(2000);
                    for (int i = 0; i < 5; i++) {
                      updateProgress(i, 5);
                      Thread.sleep(1000);
                    }
                    return null;
                  }
                })),
              new Action("Nope")
            )
          )
        );

        return new AccountDetailsServerMessage(123, USER_NAME, ImmutableMap.of(
          "ladder1v1", 5
        ), 5, null, "AA");
      }
    }).getFuture();
  }

  @Override
  public void requestHostGame(HostGameRequest hostGameRequest) {
    taskService.submitTask(new CompletableTask<StartGameProcessServerMessage>(HIGH) {
      @Override
      protected StartGameProcessServerMessage call() {
        updateTitle("Hosting game");

        StartGameProcessServerMessage startGameProcessMessage = new StartGameProcessServerMessage();
        startGameProcessMessage.setMod("faf");
        startGameProcessMessage.setGameId(1234);

        return startGameProcessMessage;
      }
    });
  }

  @Override
  public void requestJoinGame(int gameId, String password) {
    taskService.submitTask(new CompletableTask<StartGameProcessServerMessage>(HIGH) {
      @Override
      protected StartGameProcessServerMessage call() {
        updateTitle("Joining game");

        StartGameProcessServerMessage startGameProcessMessage = new StartGameProcessServerMessage();
        startGameProcessMessage.setMod("faf");
        startGameProcessMessage.setGameId(1234);
        return startGameProcessMessage;
      }
    }).getFuture();
  }

  @Override
  public void disconnect() {

  }

  @Override
  public void reconnect() {

  }

  @Override
  public void addFriend(int playerId) {

  }

  @Override
  public void addFoe(int playerId) {

  }

  @Override
  public void startSearchLadder1v1(Faction faction) {
    logger.debug("Searching 1v1 match with faction: {}", faction);
    StartGameProcessServerMessage startGameProcessMessage = new StartGameProcessServerMessage();
    startGameProcessMessage.setGameId(123);
    startGameProcessMessage.setMod(KnownFeaturedMod.DEFAULT.getTechnicalName());

  }

  @Override
  public void stopSearchingRanked() {
    logger.debug("Stopping searching 1v1 match");
  }

  @Override
  public void sendGpgMessage(GpgGameMessage message) {

  }

  @Override
  public void removeFriend(int playerId) {

  }

  @Override
  public void removeFoe(int playerId) {

  }

  @Override
  public void selectAvatar(Integer avatarId) {

  }

  @Override
  public CompletableFuture<IceServerList> getIceServers() {
    return CompletableFuture.completedFuture(new IceServerList(60, Instant.now(), emptyList()));
  }

  @Override
  public void restoreGameSession(int gameId) {

  }

  private GameInfoServerMessage createGameInfo(int uid, String title, boolean passwordProtected, String featuredMod, String mapName, int maxPlayers, String host) {
    GameInfoServerMessage gameInfoMessage = new GameInfoServerMessage();
    gameInfoMessage.setId(uid);
    gameInfoMessage.setTitle(title);
    gameInfoMessage.setMod(featuredMod);
    gameInfoMessage.setMap(mapName);
    gameInfoMessage.setPlayers(emptyList());
    gameInfoMessage.setMaxPlayers(maxPlayers);
    gameInfoMessage.setHost(new Player().setName(host));
    gameInfoMessage.setState(GameState.OPEN);
    gameInfoMessage.setSimMods(Collections.emptyList());
    gameInfoMessage.setPasswordProtected(passwordProtected);

    return gameInfoMessage;
  }
}
