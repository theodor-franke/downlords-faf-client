package com.faforever.client.achievements;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.OffsetDateTime;

public class PlayerAchievement {

  private final ObjectProperty<AchievementState> state;
  private final IntegerProperty currentSteps;
  private final IntegerProperty playerId;
  private final ObjectProperty<Achievement> achievement;
  private final ObjectProperty<OffsetDateTime> updateTime;

  public PlayerAchievement() {
    state = new SimpleObjectProperty<>();
    currentSteps = new SimpleIntegerProperty();
    playerId = new SimpleIntegerProperty();
    achievement = new SimpleObjectProperty<>();
    updateTime = new SimpleObjectProperty<>();
  }

  public AchievementState getState() {
    return state.get();
  }

  public ObjectProperty<AchievementState> stateProperty() {
    return state;
  }

  public void setState(AchievementState state) {
    this.state.set(state);
  }

  public int getCurrentSteps() {
    return currentSteps.get();
  }

  public IntegerProperty currentStepsProperty() {
    return currentSteps;
  }

  public void setCurrentSteps(int currentSteps) {
    this.currentSteps.set(currentSteps);
  }

  public int getPlayerId() {
    return playerId.get();
  }

  public IntegerProperty playerIdProperty() {
    return playerId;
  }

  public void setPlayerId(int playerId) {
    this.playerId.set(playerId);
  }

  public Achievement getAchievement() {
    return achievement.get();
  }

  public ObjectProperty<Achievement> achievementProperty() {
    return achievement;
  }

  public void setAchievement(Achievement achievement) {
    this.achievement.set(achievement);
  }

  public OffsetDateTime getUpdateTime() {
    return updateTime.get();
  }

  public void setUpdateTime(OffsetDateTime updateTime) {
    this.updateTime.set(updateTime);
  }

  public ObjectProperty<OffsetDateTime> updateTimeProperty() {
    return updateTime;
  }
}
