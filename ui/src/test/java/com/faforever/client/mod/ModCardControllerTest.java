package com.faforever.client.mod;

import com.faforever.client.i18n.I18n;
import com.faforever.client.mod.ModVersion.ModType;
import com.faforever.client.test.AbstractPlainJavaFxTest;
import com.faforever.client.util.TimeService;
import com.faforever.client.vault.review.StarController;
import com.faforever.client.vault.review.StarsController;
import com.jfoenix.controls.JFXRippler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.function.Consumer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ModCardControllerTest extends AbstractPlainJavaFxTest {

  @Mock
  public ModService modService;
  @Mock
  private TimeService timeService;
  @Mock
  private I18n i18n;
  @Mock
  private StarsController starsController;
  @Mock
  private StarController starController;
  @Mock
  private ModThumbnailService modThumbnailService;

  private ModCardController instance;

  @Before
  public void setUp() throws Exception {
    instance = new ModCardController(modService, timeService, i18n, modThumbnailService);

    ObservableList<ModVersion> installedModVersions = FXCollections.observableArrayList();
    when(modService.getInstalledModVersions()).thenReturn(installedModVersions);
    when(i18n.get(ModType.UI.getI18nKey())).thenReturn(ModType.UI.name());

    loadFxml("theme/vault/mod/mod_card.fxml", clazz -> {
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
    modVersion.setModType(ModType.UI);
    modVersion.setUploader("ModVersion author");
    modVersion.setThumbnailUrl(getClass().getResource("/theme/images/close.png"));

    when(modThumbnailService.loadThumbnail(modVersion)).thenReturn(new Image("/theme/images/close.png"));
    instance.setModVersion(modVersion);

    assertThat(instance.nameLabel.getText(), is("ModVersion name"));
    assertThat(instance.authorLabel.getText(), is("ModVersion author"));
    assertThat(instance.thumbnailImageView.getImage(), is(notNullValue()));
    verify(modThumbnailService).loadThumbnail(modVersion);
  }

  @Test
  public void testSetModNoThumbnail() {
    ModVersion modVersion = new ModVersion();
    modVersion.setModType(ModType.UI);

    Image image = mock(Image.class);
    when(modThumbnailService.loadThumbnail(modVersion)).thenReturn(image);

    instance.setModVersion(modVersion);

    assertThat(instance.thumbnailImageView.getImage(), notNullValue());
  }

  @Test
  public void testGetRoot() throws Exception {
    assertThat(instance.getRoot(), is(instanceOf(JFXRippler.class)));
    assertThat(instance.getRoot().getParent(), is(nullValue()));
    assertThat(((JFXRippler) instance.getRoot()).getControl(), is(instance.modTileRoot));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testShowModDetail() {
    Consumer<ModVersion> listener = mock(Consumer.class);
    instance.setOnOpenDetailListener(listener);
    instance.onShowModDetail();
    verify(listener).accept(any());
  }

  @Test
  public void testUiModLabel() {
    ModVersion modVersion = new ModVersion();
    modVersion.setModType(ModType.UI);

    instance.setModVersion(modVersion);
    assertThat(instance.typeLabel.getText(), equalTo(ModType.UI.name()));
  }
}
