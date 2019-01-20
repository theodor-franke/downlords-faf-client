package com.faforever.client.coop;

import com.faforever.client.fx.WebViewConfigurer;
import com.faforever.client.game.GameService;
import com.faforever.client.game.HostGameRequest;
import com.faforever.client.game.KnownFeaturedMod;
import com.faforever.client.i18n.I18n;
import com.faforever.client.map.MapPreviewService;
import com.faforever.client.mod.FeaturedMod;
import com.faforever.client.mod.ModService;
import com.faforever.client.notification.NotificationService;
import com.faforever.client.play.GamesTableController;
import com.faforever.client.replay.ReplayService;
import com.faforever.client.reporting.ReportingService;
import com.faforever.client.test.AbstractPlainJavaFxTest;
import com.faforever.client.theme.UiService;
import com.faforever.client.util.TimeService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.layout.Pane;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.testfx.util.WaitForAsyncUtils;

import java.util.concurrent.CompletableFuture;

import static com.faforever.client.game.KnownFeaturedMod.COOP;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CoopControllerTest extends AbstractPlainJavaFxTest {
  @Rule
  public TemporaryFolder cacheDirectory = new TemporaryFolder();
  private CoopController instance;
  @Mock
  private CoopService coopService;
  @Mock
  private GameService gameService;
  @Mock
  private UiService uiService;
  @Mock
  private GamesTableController gamesTableController;
  @Mock
  private MapPreviewService mapPreviewService;
  @Mock
  private I18n i18n;
  @Mock
  private WebViewConfigurer webViewConfigurer;
  @Mock
  private ModService modService;

  @Mock
  private ReplayService replayService;
  @Mock
  private NotificationService notificationService;
  @Mock
  private ReportingService reportingService;
  @Mock
  private TimeService timeService;

  @Before
  public void setUp() throws Exception {
    instance = new CoopController(replayService, gameService, coopService, notificationService, i18n, reportingService,
      mapPreviewService, uiService, timeService, webViewConfigurer, modService);

    when(coopService.getLeaderboard(any(), anyInt())).thenReturn(CompletableFuture.completedFuture(emptyList()));
    when(coopService.getMissions()).thenReturn(CompletableFuture.completedFuture(emptyList()));

    FeaturedMod featuredMod = new FeaturedMod();
    featuredMod.setTechnicalName(COOP.getTechnicalName());
    when(modService.getFeaturedMod(COOP.getTechnicalName())).thenReturn(CompletableFuture.completedFuture(featuredMod));
    when(gameService.getGames()).thenReturn(FXCollections.emptyObservableList());
    when(uiService.loadFxml("theme/play/games_table.fxml")).thenReturn(gamesTableController);
    when(gamesTableController.getRoot()).thenReturn(new Pane());

    loadFxml("theme/play/coop/coop.fxml", clazz -> instance);

    verify(webViewConfigurer).configureWebView(instance.descriptionWebView);
  }

  @Test
  public void onPlayButtonClicked() {
    when(coopService.getMissions()).thenReturn(completedFuture(singletonList(new CoopMission())));
    Platform.runLater(() -> instance.initialize());

    WaitForAsyncUtils.waitForFxEvents();
    instance.onPlayButtonClicked();

    ArgumentCaptor<HostGameRequest> captor = ArgumentCaptor.forClass(HostGameRequest.class);
    verify(gameService).hostGame(captor.capture());

    HostGameRequest hostGameRequest = captor.getValue();
    assertThat(hostGameRequest.getFeaturedMod().getTechnicalName(), is("coop"));
  }

  @Test
  public void testGetRoot() throws Exception {
    assertThat(instance.getRoot(), is(instance.coopRoot));
    assertThat(instance.getRoot().getParent(), is(nullValue()));
  }
}
