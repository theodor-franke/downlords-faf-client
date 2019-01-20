package com.faforever.client.map;

import com.faforever.client.fx.Controller;
import com.faforever.client.fx.JavaFxUtil;
import com.faforever.client.i18n.I18n;
import com.faforever.client.map.MapService.PreviewSize;
import com.faforever.client.review.ReviewService;
import com.faforever.client.review.ReviewsSummary;
import com.faforever.client.util.IdenticonUtil;
import com.faforever.client.vault.review.StarsController;
import com.jfoenix.controls.JFXRippler;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Consumer;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MapCardController implements Controller<Node> {

  private final MapService mapService;
  private final MapPreviewService mapPreviewService;
  private final ReviewService reviewService;
  private final I18n i18n;

  public ImageView thumbnailImageView;
  public Label nameLabel;
  public Node mapTileRoot;
  public Label authorLabel;
  public StarsController starsController;
  public Label numberOfReviewsLabel;
  public Label numberOfPlaysLabel;
  public Label sizeLabel;
  public Label maxPlayersLabel;

  private FaMap map;
  private Consumer<FaMap> onOpenDetailListener;
  private ListChangeListener<FaMap> installedMapsChangeListener;
  private JFXRippler jfxRippler;

  public MapCardController(MapService mapService, MapPreviewService mapPreviewService, ReviewService reviewService, I18n i18n) {
    this.mapService = mapService;
    this.mapPreviewService = mapPreviewService;
    this.reviewService = reviewService;
    this.i18n = i18n;
  }

  public void initialize() {
    jfxRippler = new JFXRippler(mapTileRoot);
    installedMapsChangeListener = change -> {
      while (change.next()) {
        for (FaMap faMap : change.getAddedSubList()) {
          if (map.getId().equals(faMap.getId())) {
            setInstalled(true);
            return;
          }
        }
        for (FaMap faMap : change.getRemoved()) {
          if (map.getId().equals(faMap.getId())) {
            setInstalled(false);
            return;
          }
        }
      }
    };
  }

  public void setMap(FaMap map) {
    this.map = map;
    Image image;
    if (map.getLargeThumbnailUrl() != null) {
      image = mapPreviewService.loadPreview(map.getLargeThumbnailUrl(), PreviewSize.SMALL);
    } else {
      image = IdenticonUtil.createIdenticon(map.getId());
    }
    thumbnailImageView.setImage(image);
    nameLabel.setText(map.getDisplayName());
    authorLabel.setText(Optional.ofNullable(map.getAuthor()).orElse(i18n.get("map.unknownAuthor")));
    numberOfPlaysLabel.setText(i18n.number(map.getNumberOfPlays()));

    MapSize size = map.getSize();
    sizeLabel.setText(i18n.get("mapPreview.size", size.getWidthInKm(), size.getHeightInKm()));
    maxPlayersLabel.setText(i18n.number(map.getPlayers()));

    ObservableList<FaMap> installedMaps = mapService.getInstalledMaps();
    JavaFxUtil.addListener(installedMaps, new WeakListChangeListener<>(installedMapsChangeListener));

    ReviewsSummary reviewsSummary = map.getReviewsSummary();
    Platform.runLater(() -> {
      numberOfReviewsLabel.setText(i18n.number(reviewsSummary.getReviews()));
      starsController.setValue((float) reviewsSummary.getScore() / reviewsSummary.getReviews());
    });
  }

  private void setInstalled(boolean installed) {
    // FIXME implement
  }

  public Node getRoot() {
    return jfxRippler;
  }

  public void setOnOpenDetailListener(Consumer<FaMap> onOpenDetailListener) {
    this.onOpenDetailListener = onOpenDetailListener;
  }

  public void onShowMapDetail() {
    onOpenDetailListener.accept(map);
  }
}
