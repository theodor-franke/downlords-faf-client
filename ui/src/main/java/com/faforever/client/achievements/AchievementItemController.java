package com.faforever.client.achievements;

import com.faforever.client.fx.Controller;
import com.faforever.client.i18n.I18n;
import com.google.common.base.MoreObjects;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
// TODO this class should not use API objects
public class AchievementItemController implements Controller<Node> {

  private final I18n i18n;
  private final AchievementImageService achievementImageService;

  public GridPane achievementItemRoot;
  public Label nameLabel;
  public Label descriptionLabel;
  public Label pointsLabel;
  public ProgressBar progressBar;
  public Label progressLabel;
  public ImageView imageView;
  private Achievement achievement;


  public AchievementItemController(I18n i18n, AchievementImageService achievementImageService) {
    this.i18n = i18n;
    this.achievementImageService = achievementImageService;
  }

  public void initialize() {
    progressBar.managedProperty().bind(progressBar.visibleProperty());
    progressLabel.managedProperty().bind(progressLabel.visibleProperty());
  }

  public Node getRoot() {
    return achievementItemRoot;
  }

  public void setAchievement(Achievement achievement) {
    this.achievement = achievement;

    nameLabel.setText(achievement.getName());
    descriptionLabel.setText(achievement.getDescription());
    pointsLabel.setText(i18n.number(achievement.getExperiencePoints()));
    imageView.setImage(achievementImageService.getImage(achievement, AchievementState.REVEALED));
    progressLabel.setText(i18n.get("achievement.stepsFormat", 0, achievement.getTotalSteps()));
    progressBar.setProgress(0);

    if (AchievementType.STANDARD == achievement.getType()) {
      progressBar.setVisible(false);
      progressLabel.setVisible(false);
    }

    ColorAdjust colorAdjust = new ColorAdjust();
    colorAdjust.setSaturation(-1);
    imageView.setEffect(colorAdjust);
    imageView.setOpacity(0.5);
  }

  public void setPlayerAchievement(PlayerAchievement playerAchievement) {
    if (achievement == null) {
      throw new IllegalStateException("achievement needs to be set first");
    }
    if (!Objects.equals(achievement.getId(), playerAchievement.getAchievement().getId())) {
      throw new IllegalStateException("Achievement ID does not match");
    }

    if (AchievementState.UNLOCKED == AchievementState.valueOf(playerAchievement.getState().name())) {
      imageView.setImage(achievementImageService.getImage(achievement, AchievementState.UNLOCKED));
      imageView.setOpacity(1);
      imageView.setEffect(null);
    }

    if (AchievementType.INCREMENTAL == achievement.getType()) {
      Integer currentSteps = MoreObjects.firstNonNull(playerAchievement.getCurrentSteps(), 0);
      Integer totalSteps = achievement.getTotalSteps();
      progressBar.setProgress((double) currentSteps / totalSteps);
      Platform.runLater(() -> progressLabel.setText(i18n.get("achievement.stepsFormat", currentSteps, totalSteps)));
    }
  }
}
