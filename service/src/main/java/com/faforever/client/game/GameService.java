package com.faforever.client.game;

import com.faforever.client.config.ClientProperties;
import com.faforever.client.discord.DiscordJoinEvent;
import com.faforever.client.discord.DiscordRichPresenceService;
import com.faforever.client.fx.JavaFxUtil;
import com.faforever.client.fx.PlatformService;
import com.faforever.client.game.GameInfoServerMessage.SimMod;
import com.faforever.client.game.patch.GameUpdater;
import com.faforever.client.game.relay.event.RehostRequestEvent;
import com.faforever.client.game.relay.ice.IceAdapter;
import com.faforever.client.i18n.I18n;
import com.faforever.client.map.MapService;
import com.faforever.client.mod.FeaturedMod;
import com.faforever.client.mod.ModService;
import com.faforever.client.net.ConnectionState;
import com.faforever.client.notification.ImmediateErrorNotification;
import com.faforever.client.notification.NotificationService;
import com.faforever.client.player.Player;
import com.faforever.client.player.PlayerServerMessage;
import com.faforever.client.player.PlayerService;
import com.faforever.client.preferences.NotificationsPrefs;
import com.faforever.client.preferences.PreferencesService;
import com.faforever.client.remote.FafService;
import com.faforever.client.replay.ReplayService;
import com.faforever.client.reporting.ReportingService;
import com.google.common.annotations.VisibleForTesting;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.faforever.client.game.KnownFeaturedMod.LADDER_1V1;
import static com.github.nocatch.NoCatch.noCatch;
import static java.util.Collections.emptySet;
import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * Downloads necessary maps, mods and updates before starting
 */
@Lazy
@Service
@Slf4j
public class GameService implements InitializingBean {

  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @VisibleForTesting
  final BooleanProperty gameRunning;

  /** TODO: Explain why access needs to be synchronized. */
  @VisibleForTesting
  final SimpleObjectProperty<Game> currentGame;

  /**
   * An observable copy of {@link #uidToGameInfoBean}. <strong>Do not modify its content directly</strong>.
   */
  private final ObservableList<Game> games;
  private final ObservableMap<Integer, Game> uidToGameInfoBean;
  private final Map<GameState, Consumer<Game>> gameStateChangeListeners;

  private final FafService fafService;
  private final ForgedAllianceService forgedAllianceService;
  private final MapService mapService;
  private final PreferencesService preferencesService;
  private final GameUpdater gameUpdater;
  private final NotificationService notificationService;
  private final I18n i18n;
  private final Executor executor;
  private final PlayerService playerService;
  private final ReportingService reportingService;
  private final IceAdapter iceAdapter;
  private final ModService modService;
  private final PlatformService platformService;
  private final String faWindowTitle;
  private final ApplicationEventPublisher eventPublisher;

  //TODO: circular reference
  ReplayService replayService;

  private Process process;
  private BooleanProperty searching1v1;
  private boolean rehostRequested;
  private int localReplayPort;
  private CompletableFuture<StartGameProcessServerMessage> gameLaunchFuture;
  private String leaderboardName;

  public GameService(
    ClientProperties clientProperties,
    FafService fafService,
    ForgedAllianceService forgedAllianceService,
    MapService mapService,
    PreferencesService preferencesService,
    GameUpdater gameUpdater,
    NotificationService notificationService,
    I18n i18n,
    @Qualifier("taskExecutor") Executor executor,
    PlayerService playerService,
    ReportingService reportingService,
    IceAdapter iceAdapter,
    ModService modService,
    PlatformService platformService,
    ApplicationEventPublisher eventPublisher,
    DiscordRichPresenceService discordRichPresenceService
  ) {
    this.fafService = fafService;
    this.forgedAllianceService = forgedAllianceService;
    this.mapService = mapService;
    this.preferencesService = preferencesService;
    this.gameUpdater = gameUpdater;
    this.notificationService = notificationService;
    this.i18n = i18n;
    this.executor = executor;
    this.playerService = playerService;
    this.reportingService = reportingService;
    this.iceAdapter = iceAdapter;
    this.modService = modService;
    this.platformService = platformService;
    this.eventPublisher = eventPublisher;

    faWindowTitle = clientProperties.getForgedAlliance().getWindowTitle();
    uidToGameInfoBean = FXCollections.observableMap(new ConcurrentHashMap<>());
    searching1v1 = new SimpleBooleanProperty();
    gameRunning = new SimpleBooleanProperty();

    currentGame = new SimpleObjectProperty<>();
    InvalidationListener numberOfPlayersChangedListener = new InvalidationListener() {
      @Override
      public void invalidated(Observable observable) {
        if (currentGame.get() == null) {
          observable.removeListener(this);
          return;
        }
        final Player currentPlayer = playerService.getCurrentPlayer().orElseThrow(() -> new IllegalStateException("Player must be set"));
        discordRichPresenceService.updatePlayedGameTo(currentGame.get(), currentPlayer.getId(), currentPlayer.getDisplayName());
      }
    };

    ChangeListener<GameState> currentGameStateListener = new ChangeListener<>() {
      @Override
      public void changed(ObservableValue<? extends GameState> observable1, GameState oldStatus, GameState newStatus) {
        if (currentGame.get() == null) {
          observable1.removeListener(this);
          return;
        }
        final Player currentPlayer = playerService.getCurrentPlayer().orElseThrow(() -> new IllegalStateException("Player must be set"));
        discordRichPresenceService.updatePlayedGameTo(currentGame.get(), currentPlayer.getId(), currentPlayer.getDisplayName());
        if (oldStatus == GameState.PLAYING && newStatus == GameState.CLOSED) {
          GameService.this.onCurrentGameEnded();
        }
        if (newStatus == GameState.CLOSED) {
          currentGame.get().stateProperty().removeListener(this);
          currentGame.get().numPlayersProperty().removeListener(numberOfPlayersChangedListener);
        }
      }
    };

    currentGame.addListener((observable, oldValue, newValue) -> {
      if (newValue == null) {
        discordRichPresenceService.clearGameInfo();
        return;
      }

      JavaFxUtil.removeListener(newValue.numPlayersProperty(), numberOfPlayersChangedListener);
      numberOfPlayersChangedListener.invalidated(newValue.numPlayersProperty());
      JavaFxUtil.addListener(newValue.numPlayersProperty(), numberOfPlayersChangedListener);

      JavaFxUtil.removeListener(newValue.stateProperty(), currentGameStateListener);
      currentGameStateListener.changed(newValue.stateProperty(), newValue.getState(), newValue.getState());
      JavaFxUtil.addListener(newValue.stateProperty(), currentGameStateListener);
    });

    games = FXCollections.observableList(new ArrayList<>(),
      item -> new Observable[]{item.stateProperty(), item.getTeams()}
    );
    games.addListener((ListChangeListener<Game>) change -> {
      while (change.next()) {
        change.getRemoved().forEach(game -> eventPublisher.publishEvent(new GameRemovedEvent(game)));

        if (change.wasUpdated()) {
          for (int i = change.getFrom(); i < change.getTo(); i++) {
            eventPublisher.publishEvent(new GameUpdatedEvent(change.getList().get(i)));
          }
        }

        change.getAddedSubList().forEach(game -> eventPublisher.publishEvent(new GameAddedEvent(game)));
      }
    });
    JavaFxUtil.attachListToMap(games, uidToGameInfoBean);

    gameStateChangeListeners = Map.of(
      GameState.CLOSED, this::onGameClosed,
      GameState.PLAYING, this::onGamePlaying
    );
  }

  @EventListener
  public void onStartGameProcessMessage(StartGameProcessServerMessage message) {
    if (gameLaunchFuture != null) {
      gameLaunchFuture.complete(message);
      gameLaunchFuture = null;
      return;
    }

    startGame(message, null, message.getMod());
  }

  public ReadOnlyBooleanProperty gameRunningProperty() {
    return gameRunning;
  }

  public CompletableFuture<Void> hostGame(HostGameRequest hostGameRequest) {
    if (isRunning()) {
      logger.debug("Game is running, ignoring host request");
      return completedFuture(null);
    }

    stopSearchLadder1v1();

    if (gameLaunchFuture != null) {
      gameLaunchFuture.cancel(false);
    }
    gameLaunchFuture = new CompletableFuture<>();
    gameLaunchFuture.thenAccept(gameLaunchMessage -> startGame(gameLaunchMessage, null, hostGameRequest.getFeaturedMod().getTechnicalName()));

    return updateGameIfNecessary(hostGameRequest.getFeaturedMod(), null, hostGameRequest.getSimMods())
      .thenCompose(aVoid -> downloadMapIfNecessary(hostGameRequest.getMapName()))
      .thenRun(() -> fafService.requestHostGame(hostGameRequest));
  }

  public CompletableFuture<Void> joinGame(Game game, String password) {
    if (isRunning()) {
      logger.debug("Game is running, ignoring join request");
      return completedFuture(null);
    }

    logger.info("Joining game: '{}' ({})", game.getTitle(), game.getId());

    stopSearchLadder1v1();

    Set<UUID> simModUIds = game.getSimMods().keySet();

    if (gameLaunchFuture != null) {
      gameLaunchFuture.cancel(false);
    }
    gameLaunchFuture = new CompletableFuture<>();
    Function<Throwable, Void> exceptionHandler = throwable -> {
      log.warn("Game could not be joined", throwable);
      notificationService.addImmediateErrorNotification(throwable, "games.couldNotJoin");
      return null;
    };

    gameLaunchFuture.thenAccept(gameLaunchMessage -> {
      // Store password in case we rehost
      game.setPassword(password);
      startGame(gameLaunchMessage, null, leaderboardName);
    }).exceptionally(exceptionHandler);

    return modService.getFeaturedMod(game.getFeaturedMod())
      .thenCompose(featuredMod -> updateGameIfNecessary(featuredMod, null, simModUIds))
      .thenAccept(aVoid -> {
        try {
          modService.enableSimMods(simModUIds);
        } catch (IOException e) {
          logger.warn("SimMods could not be enabled", e);
        }
      })
      .thenCompose(aVoid -> downloadMapIfNecessary(game.getMapName()))
      .thenRun(() -> fafService.requestJoinGame(game.getId(), password))
      .exceptionally(exceptionHandler);
  }

  private CompletableFuture<Void> downloadMapIfNecessary(String mapFolderName) {
    if (mapService.isInstalled(mapFolderName)) {
      return completedFuture(null);
    }
    return mapService.download(mapFolderName);
  }

  /**
   * @param path a replay file that is readable by the preferences without any further conversion
   */

  public void runWithReplay(Path path, @Nullable Integer replayId, String featuredMod, Integer version, Set<UUID> simMods, String mapName) {
    if (isRunning()) {
      logger.warn("Forged Alliance is already running, not starting replay");
      return;
    }
    modService.getFeaturedMod(featuredMod)
      .thenCompose(featuredModBean -> updateGameIfNecessary(featuredModBean, version, simMods))
      .thenCompose(aVoid -> downloadMapIfNecessary(mapName))
      .thenRun(() -> {
        try {
          process = forgedAllianceService.startReplay(path, replayId);
          setGameRunning(true);
          this.leaderboardName = null;
          spawnTerminationListener(process);
        } catch (IOException e) {
          notifyCantPlayReplay(replayId, e);
        }
      })
      .exceptionally(throwable -> {
        notifyCantPlayReplay(replayId, throwable);
        return null;
      });
  }

  private void notifyCantPlayReplay(@Nullable Integer replayId, Throwable throwable) {
    logger.error("Could not play replay '{}'", replayId, throwable);
    notificationService.addNotification(new ImmediateErrorNotification(
      i18n.get("errorTitle"),
      i18n.get("replayCouldNotBeStarted", replayId),
      throwable,
      i18n, reportingService
    ));
  }

  public CompletableFuture<Void> runWithLiveReplay(URI replayUrl, Integer gameId, String gameType, String mapName) {
    if (isRunning()) {
      logger.warn("Forged Alliance is already running, not starting live replay");
      return completedFuture(null);
    }

    Game gameBean = getByUid(gameId);

    Set<UUID> simModUids = gameBean.getSimMods().keySet();

    return modService.getFeaturedMod(gameType)
      .thenCompose(featuredModBean -> updateGameIfNecessary(featuredModBean, null, simModUids))
      .thenCompose(aVoid -> downloadMapIfNecessary(mapName))
      .thenRun(() -> noCatch(() -> {
        process = forgedAllianceService.startReplay(replayUrl, gameId, getCurrentPlayer());
        setGameRunning(true);
        this.leaderboardName = null;
        spawnTerminationListener(process);
      }));
  }

  private Player getCurrentPlayer() {
    return playerService.getCurrentPlayer().orElseThrow(() -> new IllegalStateException("Player has not been set"));
  }

  public ObservableList<Game> getGames() {
    return games;
  }


  public Game getByUid(int uid) {
    Game game = uidToGameInfoBean.get(uid);
    if (game == null) {
      logger.warn("Can't find {} in gameInfoBean map", uid);
    }
    return game;
  }

  public CompletableFuture<Void> startSearchLadder1v1(Faction faction) {
    if (isRunning()) {
      logger.debug("Game is running, ignoring 1v1 search request");
      return completedFuture(null);
    }

    searching1v1.set(true);

    Function<Throwable, Void> exceptionHandler = throwable -> {
      if (throwable instanceof CancellationException) {
        logger.info("Ranked1v1 search has been cancelled");
      } else {
        logger.warn("Ranked1v1 could not be started", throwable);
      }
      searching1v1.set(false);
      return null;
    };

    if (gameLaunchFuture != null) {
      gameLaunchFuture.cancel(false);
    }
    gameLaunchFuture = new CompletableFuture<>();
    gameLaunchFuture.thenAccept(gameLaunchMessage -> downloadMapIfNecessary(gameLaunchMessage.getMap())
      .thenRun(() -> startGame(gameLaunchMessage, faction, LADDER_1V1.getTechnicalName())));

    return modService.getFeaturedMod(LADDER_1V1.getTechnicalName())
      .thenAccept(featuredModBean -> updateGameIfNecessary(featuredModBean, null, emptySet()))
      .thenRun(() -> fafService.startSearchLadder1v1(faction))
      .exceptionally(exceptionHandler);
  }

  public void stopSearchLadder1v1() {
    if (searching1v1.get()) {
      fafService.stopSearchingRanked();
      searching1v1.set(false);
    }
  }

  public BooleanProperty searching1v1Property() {
    return searching1v1;
  }

  /**
   * Returns the preferences the player is currently in. Returns {@code null} if not in a preferences.
   */
  @Nullable
  public Game getCurrentGame() {
    synchronized (currentGame) {
      return currentGame.get();
    }
  }

  private boolean isRunning() {
    return process != null && process.isAlive();
  }

  private CompletableFuture<Void> updateGameIfNecessary(FeaturedMod featuredMod, @Nullable Integer version, Set<UUID> simModUids) {
    return gameUpdater.update(featuredMod, version, simModUids);
  }

  public boolean isGameRunning() {
    synchronized (gameRunning) {
      return gameRunning.get();
    }
  }

  private void setGameRunning(boolean running) {
    synchronized (gameRunning) {
      gameRunning.set(running);
    }
  }

  /**
   * Actually starts the game, including relay and replay server. Call this method when everything else is prepared
   * (mod/map download, connectivity check etc.)
   */
  private void startGame(
    StartGameProcessServerMessage startGameProcessMessage,
    @Nullable Faction faction,
    String leaderboardName
  ) {
    if (isRunning()) {
      logger.warn("Forged Alliance is already running, not starting game");
      return;
    }

    setCurrentGame(createOrUpdateGame(startGameProcessMessage.getGameId()));

    stopSearchLadder1v1();
    replayService.startReplayServer(startGameProcessMessage.getGameId())
      .thenCompose(port -> {
        localReplayPort = port;
        return iceAdapter.start();
      })
      .thenAccept(adapterPort -> {
        process = noCatch(() -> forgedAllianceService.startGame(
          startGameProcessMessage.getGameId(),
          faction,
          leaderboardName,
          adapterPort,
          localReplayPort,
          rehostRequested,
          getCurrentPlayer(),
          startGameProcessMessage.getTeam(),
          startGameProcessMessage.getExpectedPlayers()
        ));
        setGameRunning(true);

        this.leaderboardName = leaderboardName;
        spawnTerminationListener(process);
      })
      .exceptionally(throwable -> {
        logger.warn("Game could not be started", throwable);
        notificationService.addNotification(
          new ImmediateErrorNotification(i18n.get("errorTitle"), i18n.get("game.start.couldNotStart"), throwable, i18n, reportingService)
        );
        setGameRunning(false);
        setCurrentGame(null);
        return null;
      });
  }

  private void onCurrentGameEnded() {
    NotificationsPrefs notification = preferencesService.getPreferences().getNotification();
    if (!notification.isAfterGameReviewEnabled() || !notification.isTransientNotificationsEnabled()) {
      return;
    }

    Game game;
    synchronized (currentGame) {
      game = currentGame.get();
    }

    eventPublisher.publishEvent(new CurrentGameEndedEvent(game));
  }

  @VisibleForTesting
  void spawnTerminationListener(Process process) {
    executor.execute(() -> {
      try {
        rehostRequested = false;
        int exitCode = process.waitFor();
        logger.info("Forged Alliance terminated with exit code {}", exitCode);
      } catch (InterruptedException e) {
        logger.warn("Error during post-game processing", e);
      }

      setCurrentGame(null);
      setGameRunning(false);

      fafService.notifyGameEnded();
      replayService.stopReplayServer();
      iceAdapter.stop();

      if (rehostRequested) {
        rehost();
      }
    });
  }

  private void setCurrentGame(Game game) {
    synchronized (currentGame) {
      currentGame.set(game);
    }
  }

  private void rehost() {
    synchronized (currentGame) {
      Game game = currentGame.get();

      modService.getFeaturedMod(game.getFeaturedMod())
        .thenAccept(featuredMod -> hostGame(new HostGameRequest(
          game.getTitle(),
          game.getPassword(),
          featuredMod,
          game.getMapName(),
          new HashSet<>(game.getSimMods().keySet()),
          game.getVisibility(),
          game.getMinRank(),
          game.getMaxRank()
        )))
        .exceptionally(throwable -> {
          log.warn("Could not rehost game", throwable);
          setCurrentGame(null);
          return null;
        });
    }
  }

  @EventListener
  public void onRehostRequest(RehostRequestEvent event) {
    this.rehostRequested = true;
    synchronized (gameRunning) {
      if (!gameRunning.get()) {
        // If the game already has terminated, the rehost is issued here. Otherwise it will be issued after termination
        rehost();
      }
    }
  }

  @Override
  public void afterPropertiesSet() {
    JavaFxUtil.addListener(fafService.connectionStateProperty(), (observable, oldValue, newValue) -> {
      if (newValue == ConnectionState.DISCONNECTED) {
        synchronized (uidToGameInfoBean) {
          uidToGameInfoBean.clear();
        }
      }
    });
  }

  @EventListener(classes = PlayerServerMessage.class)
  public void onLoggedIn() {
    if (isGameRunning()) {
      fafService.restoreGameSession(currentGame.get().getId());
    }
  }

  @EventListener
  public void onGameInfos(GameInfosServerMessage message) {
    message.getGames().forEach(this::onGameInfo);
  }

  @EventListener
  public void onGameInfo(GameInfoServerMessage gameInfoMessage) {
    // Since all game updates are usually reflected on the UI and to prevent deadlocks
    JavaFxUtil.runLater(() -> {
      Game game = createOrUpdateGame(gameInfoMessage);
      gameStateChangeListeners.get(game.getState()).accept(game);
    });
  }

  private void onGamePlaying(Game game) {
    if (isCurrentGame(game) && !platformService.isWindowFocused(faWindowTitle)) {
      platformService.focusWindow(faWindowTitle);
    }
  }

  private void onGameClosed(Game game) {
    if (!isCurrentGame(game)) {
      removeGame(game.getId());
      return;
    }

    // Don't remove the current game until the current player closed it. TODO: Why?
    JavaFxUtil.addListener(currentGame, new ChangeListener<>() {
      @Override
      public void changed(ObservableValue<? extends Game> observable, Game oldValue, Game newValue) {
        if (newValue == null) {
          GameService.this.removeGame(game.getId());
          GameService.this.setCurrentGame(null);
          JavaFxUtil.removeListener(currentGame, this);
        }
      }
    });
  }

  private boolean isCurrentGame(Game game) {
    return currentGame.get() != null && currentGame.get().getId() == game.getId();
  }

  private Game createOrUpdateGame(int gameId) {
    final Game game;
    synchronized (uidToGameInfoBean) {
      game = uidToGameInfoBean.computeIfAbsent(gameId, Game::new);
    }
    return game;
  }

  private Game createOrUpdateGame(GameInfoServerMessage gameInfoMessage) {
    Game game = createOrUpdateGame(gameInfoMessage.getId());
    updateFromGameInfo(game, gameInfoMessage);
    return game;
  }

  private void removeGame(int gameId) {
    synchronized (uidToGameInfoBean) {
      uidToGameInfoBean.remove(gameId);
    }
  }

  private void updateFromGameInfo(Game game, GameInfoServerMessage gameInfoMessage) {
    /* Since this method synchronizes on and updates members of "game", deadlocks can happen easily (updates can fire
     events on the event bus, and each event subscriber is synchronized as well). By ensuring that we run all updates
     in the application thread, we eliminate this risk. */
    JavaFxUtil.assertApplicationThread();

    Assert.isTrue(game.getId() == gameInfoMessage.getId(), "Game IDs don't match");

    game.setHostName(gameInfoMessage.getHost().getName());
    game.setTitle(StringEscapeUtils.unescapeHtml4(gameInfoMessage.getTitle()));
    game.setMapName(gameInfoMessage.getMap());
    game.setLeaderboardName(gameInfoMessage.getMod());
    game.setFeaturedMod(gameInfoMessage.getMod());
    game.setFeaturedModVersion(gameInfoMessage.getModVersion());
    game.setMaxPlayers(gameInfoMessage.getMaxPlayers());
    game.setState(gameInfoMessage.getState());
    game.setPasswordProtected(gameInfoMessage.isPasswordProtected());
    game.setStartTime(gameInfoMessage.getStartTime());

    synchronized (game.getSimMods()) {
      game.getSimMods().clear();
      if (gameInfoMessage.getSimMods() != null) {
        Map<UUID, String> uuidToDisplayName = gameInfoMessage.getSimMods().stream()
          .collect(Collectors.toMap(SimMod::getUuid, SimMod::getDisplayName));
        game.getSimMods().putAll(uuidToDisplayName);
      }
    }

    synchronized (game.getTeams()) {
      game.getTeams().clear();
      if (gameInfoMessage.getPlayers() != null) {
        game.getTeams().putAll(teamIdsToPlayers(gameInfoMessage));
      }
    }

    game.setNumPlayers(gameInfoMessage.getPlayers().size());
  }

  private HashMap<Integer, List<Player>> teamIdsToPlayers(GameInfoServerMessage gameInfoMessage) {
    List<GameInfoServerMessage.Player> players = gameInfoMessage.getPlayers();
    return players.stream()
      .collect(Collectors.groupingBy(GameInfoServerMessage.Player::getTeam, HashMap::new, Collectors.mapping(
        // TODO what to do if the player is not known?
        o -> playerService.getPlayerById(o.getId()).orElseGet(() -> new Player("Player #" + o.getId())),
        Collectors.toList()
      )));
  }


  @EventListener
  public void onDiscordGameJoinEvent(DiscordJoinEvent discordJoinEvent) {
    Integer gameId = discordJoinEvent.getGameId();
    Game game = getByUid(gameId);
    boolean disallowJoinsViaDiscord = preferencesService.getPreferences().isDisallowJoinsViaDiscord();
    if (disallowJoinsViaDiscord) {
      log.debug("Join was requested via Discord but was rejected due to it being disabled in settings");
      return;
    }
    if (game == null) {
      throw new IllegalStateException(String.format("Could not find game to join, with id: %d", discordJoinEvent.getGameId()));
    }
    joinGame(game, "");
  }
}
