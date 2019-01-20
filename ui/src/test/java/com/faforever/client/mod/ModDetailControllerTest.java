package com.faforever.client.mod;

import com.faforever.client.i18n.I18n;
import com.faforever.client.notification.ImmediateNotification;
import com.faforever.client.notification.NotificationService;
import com.faforever.client.player.Player;
import com.faforever.client.player.PlayerService;
import com.faforever.client.reporting.ReportingService;
import com.faforever.client.review.ReviewService;
import com.faforever.client.test.AbstractPlainJavaFxTest;
import com.faforever.client.util.TimeService;
import com.faforever.client.vault.review.ReviewController;
import com.faforever.client.vault.review.ReviewsController;
import com.faforever.client.vault.review.StarController;
import com.faforever.client.vault.review.StarsController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.testfx.util.WaitForAsyncUtils;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ModDetailControllerTest extends AbstractPlainJavaFxTest {

  @Mock
  private ReportingService reportingService;
  @Mock
  private NotificationService notificationService;
  @Mock
  private ModService modService;
  @Mock
  private ModThumbnailService modThumbnailService;
  @Mock
  private I18n i18n;
  @Mock
  private TimeService timeService;
  @Mock
  private ReviewService reviewService;
  @Mock
  private PlayerService playerService;
  @Mock
  private ReviewsController reviewsController;
  @Mock
  private ReviewController reviewController;
  @Mock
  private StarsController starsController;
  @Mock
  private StarController starController;

  private ModDetailController instance;
  private ObservableList<ModVersion> installedModVersions;

  @Before
  public void setUp() throws Exception {
    instance = new ModDetailController(modService, modThumbnailService, notificationService, i18n, reportingService, timeService, reviewService, playerService);

    installedModVersions = FXCollections.observableArrayList();
    when(modService.getInstalledModVersions()).thenReturn(installedModVersions);

    when(playerService.getCurrentPlayer()).thenReturn(Optional.of(new Player("junit")));

    loadFxml("theme/vault/mod/mod_detail.fxml", clazz -> {
      if (clazz == ReviewsController.class) {
        return reviewsController;
      }
      if (clazz == ReviewController.class) {
        return reviewController;
      }
      if (clazz == StarsController.class) {
        return starsController;
      }
      if (clazz == StarController.class) {
        return starController;
      }
      return instance;
    });
  }

  @Test
  public void testSetMod() {
    ModVersion modVersion = new ModVersion();
    modVersion.setDisplayName("ModVersion name");
    modVersion.setUploader("ModVersion author");
    modVersion.setThumbnailUrl(getClass().getResource("/theme/images/close.png"));

    when(modThumbnailService.loadThumbnail(modVersion)).thenReturn(new Image("/theme/images/close.png"));
    instance.setModVersion(modVersion);

    WaitForAsyncUtils.waitForFxEvents();

    assertThat(instance.nameLabel.getText(), is("ModVersion name"));
    assertThat(instance.authorLabel.getText(), is("ModVersion author"));
    assertThat(instance.thumbnailImageView.getImage(), is(notNullValue()));
    verify(modThumbnailService).loadThumbnail(modVersion);
  }

  @Test
  public void testSetModNoThumbnailLoadsDefault() {
    ModVersion modVersion = new ModVersion();
    Image image = mock(Image.class);
    when(modThumbnailService.loadThumbnail(modVersion)).thenReturn(image);

    instance.setModVersion(modVersion);

    WaitForAsyncUtils.waitForFxEvents();

    assertThat(instance.thumbnailImageView.getImage(), is(image));
  }

  @Test
  public void testOnInstallButtonClicked() {
    when(modService.downloadAndInstallMod(any(ModVersion.class), any(), any())).thenReturn(CompletableFuture.completedFuture(null));

    instance.setModVersion(new ModVersion());
    instance.onInstallButtonClicked();

    verify(modService).downloadAndInstallMod(any(ModVersion.class), any(), any());
  }

  @Test
  public void testOnInstallButtonClickedInstallindModThrowsException() {
    CompletableFuture<Void> future = new CompletableFuture<>();
    future.completeExceptionally(new Exception("test exception"));
    when(modService.downloadAndInstallMod(any(ModVersion.class), any(), any())).thenReturn(future);

    instance.setModVersion(new ModVersion());

    instance.onInstallButtonClicked();

    verify(modService).downloadAndInstallMod(any(ModVersion.class), any(), any());
    verify(notificationService).addNotification(any(ImmediateNotification.class));
  }

  @Test
  public void testOnUninstallButtonClicked() {
    ModVersion modVersion = new ModVersion();
    instance.setModVersion(modVersion);
    when(modService.uninstallMod(modVersion)).thenReturn(CompletableFuture.completedFuture(null));

    instance.onUninstallButtonClicked();

    verify(modService).uninstallMod(modVersion);
  }

  @Test
  public void testOnUninstallButtonClickedThrowsException() {
    ModVersion modVersion = new ModVersion();
    instance.setModVersion(modVersion);

    CompletableFuture<Void> future = new CompletableFuture<>();
    future.completeExceptionally(new Exception("test exception"));
    when(modService.uninstallMod(modVersion)).thenReturn(future);

    instance.onUninstallButtonClicked();

    verify(modService).uninstallMod(modVersion);
    verify(notificationService).addNotification(any(ImmediateNotification.class));
  }

  @Test
  public void testOnCloseButtonClicked() {
    WaitForAsyncUtils.asyncFx(() -> getRoot().getChildren().add(instance.getRoot()));
    WaitForAsyncUtils.waitForFxEvents();

    assertThat(instance.modDetailRoot.getParent(), is(notNullValue()));
    WaitForAsyncUtils.asyncFx(() -> instance.onCloseButtonClicked());
    WaitForAsyncUtils.waitForFxEvents();

    assertThat(instance.modDetailRoot.isVisible(), is(false));
  }

  @Test
  public void testGetRoot() throws Exception {
    assertThat(instance.getRoot(), is(instance.modDetailRoot));
    assertThat(instance.getRoot().getParent(), is(nullValue()));
  }

  @Test
  public void testShowUninstallButtonWhenModIsInstalled() {
    UUID uuid = UUID.randomUUID();

    when(modService.isModInstalled(uuid)).thenReturn(true);

    ModVersion modVersion = new ModVersion();
    modVersion.setUuid(uuid);
    instance.setModVersion(modVersion);

    assertThat(instance.installButton.isVisible(), is(false));
    assertThat(instance.uninstallButton.isVisible(), is(true));
  }

  @Test
  public void testShowInstallButtonWhenModIsNotInstalled() {
    UUID uuid = UUID.randomUUID();
    when(modService.isModInstalled(uuid)).thenReturn(false);

    ModVersion modVersion = new ModVersion();
    modVersion.setUuid(uuid);
    instance.setModVersion(modVersion);

    assertThat(instance.installButton.isVisible(), is(true));
    assertThat(instance.uninstallButton.isVisible(), is(false));
  }

  @Test
  public void testChangeInstalledStateWhenModIsUninstalled() {
    UUID uuid = UUID.randomUUID();
    when(modService.isModInstalled(uuid)).thenReturn(true);

    ModVersion modVersion = new ModVersion();
    modVersion.setUuid(uuid);
    instance.setModVersion(modVersion);
    installedModVersions.add(modVersion);

    assertThat(instance.installButton.isVisible(), is(false));
    assertThat(instance.uninstallButton.isVisible(), is(true));

    installedModVersions.remove(modVersion);

    assertThat(instance.installButton.isVisible(), is(true));
    assertThat(instance.uninstallButton.isVisible(), is(false));
  }

  @Test
  public void testChangeInstalledStateWhenModIsInstalled() {
    UUID uuid = UUID.randomUUID();
    ModVersion modVersion = new ModVersion();
    modVersion.setUuid(uuid);

    when(modService.isModInstalled(uuid)).thenReturn(false);
    instance.setModVersion(modVersion);

    assertThat(instance.installButton.isVisible(), is(true));
    assertThat(instance.uninstallButton.isVisible(), is(false));

    installedModVersions.add(modVersion);

    assertThat(instance.installButton.isVisible(), is(false));
    assertThat(instance.uninstallButton.isVisible(), is(true));
  }
}
