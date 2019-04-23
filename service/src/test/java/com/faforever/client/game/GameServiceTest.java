package com.faforever.client.game;

import com.faforever.client.config.ClientProperties;
import com.faforever.client.fx.PlatformService;
import com.faforever.client.game.patch.GameUpdater;
import com.faforever.client.game.relay.event.RehostRequestEvent;
import com.faforever.client.game.relay.ice.IceAdapter;
import com.faforever.client.i18n.I18n;
import com.faforever.client.map.MapService;
import com.faforever.client.mod.FeaturedMod;
import com.faforever.client.mod.ModService;
import com.faforever.client.notification.NotificationService;
import com.faforever.client.notification.PersistentNotification;
import com.faforever.client.player.Player;
import com.faforever.client.player.PlayerBuilder;
import com.faforever.client.player.PlayerService;
import com.faforever.client.preferences.Preferences;
import com.faforever.client.preferences.PreferencesService;
import com.faforever.client.remote.FafService;
import com.faforever.client.replay.ReplayService;
import com.faforever.client.reporting.ReportingService;
import com.faforever.client.test.AbstractPlainJavaFxTest;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.util.ReflectionUtils;
import org.testfx.util.WaitForAsyncUtils;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import static com.natpryce.hamcrest.reflection.HasAnnotationMatcher.hasAnnotation;
import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GameServiceTest extends AbstractPlainJavaFxTest {

  private static final long TIMEOUT = 5000;
  private static final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;
  private static final Integer GPG_PORT = 1234;
  private static final int LOCAL_REPLAY_PORT = 15111;

  private GameService instance;

  @Mock
  private PreferencesService preferencesService;
  @Mock
  private FafService fafService;
  @Mock
  private MapService mapService;
  @Mock
  private ForgedAllianceService forgedAllianceService;
  @Mock
  private GameUpdater gameUpdater;
  @Mock
  private PlayerService playerService;
  @Mock
  private Executor executor;
  @Mock
  private ReplayService replayService;
  @Mock
  private IceAdapter iceAdapter;
  @Mock
  private ModService modService;
  @Mock
  private NotificationService notificationService;
  @Mock
  private I18n i18n;
  @Mock
  private ReportingService reportingService;
  @Mock
  private PlatformService platformService;
  @Mock
  private ApplicationEventPublisher eventPublisher;
  @Captor
  private ArgumentCaptor<Set<UUID>> simModsCaptor;

  private Player junitPlayer;

  @Before
  public void setUp() throws Exception {
    junitPlayer = PlayerBuilder.create("JUnit").defaultValues().get();

    ClientProperties clientProperties = new ClientProperties();

    instance = new GameService(clientProperties, fafService, forgedAllianceService, mapService,
      preferencesService, gameUpdater, notificationService, i18n, executor, playerService,
      reportingService, iceAdapter, modService, platformService, eventPublisher);
    instance.replayService = replayService;

    Preferences preferences = new Preferences();

    when(preferencesService.getPreferences()).thenReturn(preferences);
    when(fafService.connectionStateProperty()).thenReturn(new SimpleObjectProperty<>());
    when(replayService.startReplayServer(anyInt())).thenReturn(completedFuture(LOCAL_REPLAY_PORT));
    when(iceAdapter.start()).thenReturn(CompletableFuture.completedFuture(GPG_PORT));
    when(playerService.getCurrentPlayer()).thenReturn(Optional.of(junitPlayer));

    doAnswer(invocation -> {
      try {
        ((Runnable) invocation.getArgument(0)).run();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      return null;
    }).when(executor).execute(any());

    instance.afterPropertiesSet();
  }

  @Test
  public void testJoinGameMapIsAvailable() throws Exception {
    Game game = GameBuilder.create().defaultValues().get();

    ObservableMap<UUID, String> simMods = FXCollections.observableHashMap();
    simMods.put(UUID.randomUUID(), "Fake mod name");

    game.setSimMods(simMods);
    game.setMapName("map");

    when(mapService.isInstalled("map")).thenReturn(true);
    when(gameUpdater.update(any(), any(), any())).thenReturn(completedFuture(null));
    when(modService.getFeaturedMod(game.getFeaturedMod())).thenReturn(CompletableFuture.completedFuture(FeaturedModBeanBuilder.create().defaultValues().get()));

    StartGameProcessServerMessage startGameProcessMessage = StartGameProcessMessageBuilder.create().defaultValues().get();
    doAnswer(invocation -> {
      startGameProcess(startGameProcessMessage);
      return null;
    }).when(fafService).requestJoinGame(anyInt(), any());

    CompletableFuture<Void> future = instance.joinGame(game, null).toCompletableFuture();

    assertThat(future.get(TIMEOUT, TIME_UNIT), is(nullValue()));
    verify(mapService, never()).download(any());
    verify(replayService).startReplayServer(game.getId());
  }

  @Test
  public void testModEnabling() throws Exception {
    Game game = GameBuilder.create().defaultValues().get();

    ObservableMap<UUID, String> simMods = FXCollections.observableHashMap();
    simMods.put(UUID.randomUUID(), "Fake mod name");

    game.setSimMods(simMods);
    game.setMapName("map");

    when(mapService.isInstalled("map")).thenReturn(true);
    when(gameUpdater.update(any(), any(), any())).thenReturn(completedFuture(null));
    when(modService.getFeaturedMod(game.getFeaturedMod())).thenReturn(CompletableFuture.completedFuture(FeaturedModBeanBuilder.create().defaultValues().get()));

    instance.joinGame(game, null).toCompletableFuture().get();

    verify(modService).enableSimMods(simModsCaptor.capture());
    assertEquals(simModsCaptor.getValue().iterator().next(), simMods.keySet().iterator().next());
  }

  @Test
  public void testAddOnGameStartedListener() throws Exception {
    Process process = mock(Process.class);

    HostGameRequest hostGameRequest = HostGameClientMessageBuilder.create().defaultValues().get();
    StartGameProcessServerMessage startGameProcessMessage = StartGameProcessMessageBuilder.create().defaultValues().get();

    when(forgedAllianceService.startGame(
      startGameProcessMessage.getGameId(),
      null,
      "faf",
      GPG_PORT,
      LOCAL_REPLAY_PORT,
      false,
      junitPlayer,
      startGameProcessMessage.getTeam(),
      startGameProcessMessage.getExpectedPlayers()
    )).thenReturn(process);
    when(gameUpdater.update(any(), any(), any())).thenReturn(completedFuture(null));
    when(mapService.download(hostGameRequest.getMapName())).thenReturn(CompletableFuture.completedFuture(null));

    CountDownLatch gameStartedLatch = new CountDownLatch(1);
    CountDownLatch gameTerminatedLatch = new CountDownLatch(1);
    instance.gameRunningProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        gameStartedLatch.countDown();
      } else {
        gameTerminatedLatch.countDown();
      }
    });

    doAnswer(invocation -> {
      startGameProcess(startGameProcessMessage);
      return null;
    }).when(fafService).requestHostGame(hostGameRequest);

    CountDownLatch processLatch = new CountDownLatch(1);

    instance.hostGame(hostGameRequest).toCompletableFuture().get(TIMEOUT, TIME_UNIT);

    gameStartedLatch.await(TIMEOUT, TIME_UNIT);
    processLatch.countDown();

    gameTerminatedLatch.await(TIMEOUT, TIME_UNIT);
    verify(forgedAllianceService).startGame(
      startGameProcessMessage.getGameId(), null, "faf",
      GPG_PORT, LOCAL_REPLAY_PORT, false, junitPlayer, startGameProcessMessage.getTeam(), startGameProcessMessage.getExpectedPlayers());
    verify(replayService).startReplayServer(startGameProcessMessage.getGameId());
  }

  @Test
  public void testWaitForProcessTerminationInBackground() throws Exception {
    instance.gameRunning.set(true);

    CompletableFuture<Void> disconnectedFuture = new CompletableFuture<>();

    instance.gameRunningProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue) {
        disconnectedFuture.complete(null);
      }
    });

    Process process = mock(Process.class);

    instance.spawnTerminationListener(process);

    disconnectedFuture.get(5000, TimeUnit.MILLISECONDS);

    verify(process).waitFor();
  }

  @Test
  public void testOnGames() {
    assertThat(instance.getGames(), empty());

    GameInfosServerMessage multiGameInfoMessage = new GameInfosServerMessage();
    multiGameInfoMessage.setGames(asList(
      GameInfoMessageBuilder.create(1).defaultValues().get(),
      GameInfoMessageBuilder.create(2).defaultValues().get()
    ));

    instance.onGameInfos(multiGameInfoMessage);
    WaitForAsyncUtils.waitForFxEvents();

    assertThat(instance.getGames(), hasSize(2));
  }

  @Test
  public void testOnGameInfoAdd() {
    assertThat(instance.getGames(), empty());

    GameInfoServerMessage gameInfoMessage1 = GameInfoMessageBuilder.create(1).defaultValues().title("Game 1").get();
    instance.onGameInfo(gameInfoMessage1);

    GameInfoServerMessage gameInfoMessage2 = GameInfoMessageBuilder.create(2).defaultValues().title("Game 2").get();
    instance.onGameInfo(gameInfoMessage2);
    WaitForAsyncUtils.waitForFxEvents();

    Game game1 = new Game();
    game1.setId(gameInfoMessage1.getId());
    game1.setTitle(gameInfoMessage1.getTitle());

    Game game2 = new Game();
    game2.setId(gameInfoMessage2.getId());
    game2.setTitle(gameInfoMessage2.getTitle());

    assertThat(instance.getGames(), containsInAnyOrder(game1, game2));
  }

  @Test
  public void testOnGameInfoMessageDoesntSetCurrentGameIfUserIsInAndStatusNotOpen() {
    assertThat(instance.getCurrentGame(), nullValue());

    when(playerService.getCurrentPlayer()).thenReturn(Optional.ofNullable(PlayerBuilder.create("PlayerName").get()));

    GameInfoServerMessage gameInfoMessage = GameInfoMessageBuilder.create(1234).defaultValues()
      .state(GameState.PLAYING)
      .addTeamMember(1, 1, "PlayerName").get();
    instance.onGameInfo(gameInfoMessage);

    assertThat(instance.getCurrentGame(), nullValue());
  }

  @Test
  public void testOnGameInfoMessageDoesntSetCurrentGameIfUserDoesntMatch() {
    assertThat(instance.getCurrentGame(), nullValue());

    when(playerService.getCurrentPlayer()).thenReturn(Optional.ofNullable(PlayerBuilder.create("PlayerName").get()));

    GameInfoServerMessage gameInfoMessage = GameInfoMessageBuilder.create(1234).defaultValues().addTeamMember(1, 1, "Other").get();
    instance.onGameInfo(gameInfoMessage);

    assertThat(instance.getCurrentGame(), nullValue());
  }

  @Test
  public void testOnGameInfoModify() throws InterruptedException {
    assertThat(instance.getGames(), empty());

    GameInfoServerMessage gameInfoMessage = GameInfoMessageBuilder.create(1).defaultValues().title("Game 1").state(GameState.PLAYING).get();
    instance.onGameInfo(gameInfoMessage);
    WaitForAsyncUtils.waitForFxEvents();

    CountDownLatch changeLatch = new CountDownLatch(1);
    Game game = instance.getGames().iterator().next();
    game.titleProperty().addListener((observable, oldValue, newValue) -> {
      changeLatch.countDown();
    });

    gameInfoMessage = GameInfoMessageBuilder.create(1).defaultValues().title("Game 1 modified").state(GameState.PLAYING).get();
    instance.onGameInfo(gameInfoMessage);

    changeLatch.await();
    assertEquals(gameInfoMessage.getTitle(), game.getTitle());
  }

  @Test
  public void testOnGameInfoRemove() {
    assertThat(instance.getGames(), empty());

    when(playerService.getCurrentPlayer()).thenReturn(Optional.ofNullable(PlayerBuilder.create("PlayerName").get()));

    GameInfoServerMessage gameInfoMessage = GameInfoMessageBuilder.create(1).defaultValues().title("Game 1").get();
    instance.onGameInfo(gameInfoMessage);

    gameInfoMessage = GameInfoMessageBuilder.create(1).title("Game 1").defaultValues().state(GameState.CLOSED).get();
    instance.onGameInfo(gameInfoMessage);
    WaitForAsyncUtils.waitForFxEvents();

    assertThat(instance.getGames(), empty());
  }

  @Test
  public void testStartSearchLadder1v1() throws Exception {
    StartGameProcessServerMessage startGameProcessMessage = new StartGameProcessServerMessage();
    startGameProcessMessage.setMod("ladder1v1");
    startGameProcessMessage.setGameId(123);
    startGameProcessMessage.setMap("scmp_037");
    startGameProcessMessage.setTeam(1);
    startGameProcessMessage.setExpectedPlayers(2);

    FeaturedMod featuredMod = FeaturedModBeanBuilder.create().defaultValues().get();

    when(gameUpdater.update(featuredMod, null, Collections.emptySet())).thenReturn(CompletableFuture.completedFuture(null));
    when(mapService.isInstalled("scmp_037")).thenReturn(false);
    when(mapService.download("scmp_037")).thenReturn(CompletableFuture.completedFuture(null));
    when(modService.getFeaturedMod(KnownFeaturedMod.LADDER_1V1.getTechnicalName())).thenReturn(CompletableFuture.completedFuture(featuredMod));

    doAnswer(invocation -> {
      startGameProcess(startGameProcessMessage);
      return null;
    }).when(fafService).startSearchLadder1v1(Faction.CYBRAN);

    CompletableFuture<Void> future = instance.startSearchLadder1v1(Faction.CYBRAN).toCompletableFuture();

    verify(fafService).startSearchLadder1v1(Faction.CYBRAN);
    verify(mapService).download("scmp_037");
    verify(replayService).startReplayServer(123);
    verify(forgedAllianceService, timeout(100))
      .startGame(eq(123), eq(Faction.CYBRAN), eq("ladder1v1"), anyInt(), eq(LOCAL_REPLAY_PORT), eq(false), eq(junitPlayer), eq(1), eq(2));
    assertThat(future.get(TIMEOUT, TIME_UNIT), is(nullValue()));
  }

  @Test
  public void testStartSearchLadder1v1GameRunningDoesNothing() throws Exception {
    Process process = mock(Process.class);
    when(process.isAlive()).thenReturn(true);

    HostGameRequest hostGameRequest = HostGameClientMessageBuilder.create().defaultValues().get();
    StartGameProcessServerMessage startGameProcessMessage = StartGameProcessMessageBuilder.create().defaultValues().get();

    when(forgedAllianceService.startGame(anyInt(), any(), any(), anyInt(), eq(LOCAL_REPLAY_PORT), eq(false), eq(junitPlayer), anyInt(), any())).thenReturn(process);
    when(gameUpdater.update(any(), any(), any())).thenReturn(completedFuture(null));
    when(mapService.download(hostGameRequest.getMapName())).thenReturn(CompletableFuture.completedFuture(null));

    doAnswer(invocation -> {
      startGameProcess(startGameProcessMessage);
      return null;
    }).when(fafService).requestHostGame(hostGameRequest);

    CountDownLatch gameRunningLatch = new CountDownLatch(1);
    instance.gameRunningProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        gameRunningLatch.countDown();
      }
    });

    instance.hostGame(hostGameRequest);
    gameRunningLatch.await(TIMEOUT, TIME_UNIT);

    instance.startSearchLadder1v1(Faction.AEON);

    assertThat(instance.searching1v1Property().get(), is(false));
  }

  @Test
  public void testStopSearchLadder1v1() {
    instance.searching1v1Property().set(true);
    instance.stopSearchLadder1v1();
    assertThat(instance.searching1v1Property().get(), is(false));
    verify(fafService).stopSearchingRanked();
  }

  @Test
  public void testStopSearchLadder1v1NotSearching() {
    instance.searching1v1Property().set(false);
    instance.stopSearchLadder1v1();
    assertThat(instance.searching1v1Property().get(), is(false));
    verify(fafService, never()).stopSearchingRanked();
  }

  @Test
  public void testRehostRequestEventListener() {
    assertThat(ReflectionUtils.findMethod(
      instance.getClass(), "onRehostRequest", RehostRequestEvent.class),
      hasAnnotation(EventListener.class));
  }

  @Test
  public void testRehostIfGameIsNotRunning() throws Exception {
    Game game = GameBuilder.create().defaultValues().get();
    instance.currentGame.set(game);

    when(modService.getFeaturedMod(game.getFeaturedMod())).thenReturn(CompletableFuture.completedFuture(FeaturedModBeanBuilder.create().defaultValues().get()));
    when(gameUpdater.update(any(), any(), any())).thenReturn(completedFuture(null));
    when(modService.getFeaturedMod(game.getFeaturedMod())).thenReturn(CompletableFuture.completedFuture(FeaturedModBeanBuilder.create().defaultValues().get()));
    when(mapService.download(game.getMapName())).thenReturn(CompletableFuture.completedFuture(null));
    doAnswer(invocation -> {
      startGameProcess(StartGameProcessMessageBuilder.create().defaultValues().get());
      return null;
    }).when(fafService).requestHostGame(any());

    instance.onRehostRequest(new RehostRequestEvent());

    verify(forgedAllianceService).startGame(anyInt(), eq(null), any(), anyInt(), eq(LOCAL_REPLAY_PORT), eq(true), eq(junitPlayer), anyInt(), any());
  }

  @Test
  public void testRehostIfGameIsRunning() throws Exception {
    instance.gameRunning.set(true);

    Game game = GameBuilder.create().defaultValues().get();
    instance.currentGame.set(game);

    instance.onRehostRequest(new RehostRequestEvent());

    verify(forgedAllianceService, never()).startGame(anyInt(), any(), any(), anyInt(), eq(LOCAL_REPLAY_PORT), anyBoolean(), eq(junitPlayer), anyInt(), any());
  }

  @Test
  public void testCurrentGameEndedBehaviour() {
    Game game = new Game();
    game.setId(123);
    game.setState(GameState.PLAYING);

    instance.currentGame.set(game);

    verify(eventPublisher, never()).publishEvent(any(CurrentGameEndedEvent.class));

    game.setState(GameState.CLOSED);

    WaitForAsyncUtils.waitForFxEvents();
    verify(eventPublisher).publishEvent(any(CurrentGameEndedEvent.class));
  }

  @Test
  public void testCurrentGameEndedAndReplayNotPresent() {
    Game game = new Game();
    game.setId(123);
    game.setState(GameState.PLAYING);

    instance.currentGame.set(game);

    verify(notificationService, never()).addNotification(any(PersistentNotification.class));

    game.setState(GameState.CLOSED);

    WaitForAsyncUtils.waitForFxEvents();

    ArgumentCaptor<CurrentGameEndedEvent> currentGameEndedEventCaptor = ArgumentCaptor.forClass(CurrentGameEndedEvent.class);
    verify(eventPublisher).publishEvent(currentGameEndedEventCaptor.capture());
  }

  private void startGameProcess(StartGameProcessServerMessage startGameProcessMessage) {
    instance.onStartGameProcessMessage(startGameProcessMessage);
  }
}
