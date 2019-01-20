package com.faforever.client.achievements;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Achievement {
  private final StringProperty id;
  private final StringProperty description;
  private final IntegerProperty experiencePoints;
  private final ObjectProperty<AchievementState> initialState;
  private final StringProperty name;
  private final StringProperty revealedIconUrl;
  private final IntegerProperty totalSteps;
  private final ObjectProperty<AchievementType> type;
  private final StringProperty unlockedIconUrl;
  private final IntegerProperty order;

  public Achievement() {
    id = new SimpleStringProperty();
    description = new SimpleStringProperty();
    experiencePoints = new SimpleIntegerProperty();
    initialState = new SimpleObjectProperty<>();
    name = new SimpleStringProperty();
    revealedIconUrl = new SimpleStringProperty();
    totalSteps = new SimpleIntegerProperty();
    type = new SimpleObjectProperty<>();
    unlockedIconUrl = new SimpleStringProperty();
    order = new SimpleIntegerProperty();
  }

  public String getId() {
    return id.get();
  }

  public void setId(String id) {
    this.id.set(id);
  }

  public StringProperty idProperty() {
    return id;
  }

  public String getDescription() {
    return description.get();
  }

  public StringProperty descriptionProperty() {
    return description;
  }

  public void setDescription(String description) {
    this.description.set(description);
  }

  public int getExperiencePoints() {
    return experiencePoints.get();
  }

  public IntegerProperty experiencePointsProperty() {
    return experiencePoints;
  }

  public void setExperiencePoints(int experiencePoints) {
    this.experiencePoints.set(experiencePoints);
  }

  public AchievementState getInitialState() {
    return initialState.get();
  }

  public ObjectProperty<AchievementState> initialStateProperty() {
    return initialState;
  }

  public void setInitialState(AchievementState initialState) {
    this.initialState.set(initialState);
  }

  public String getName() {
    return name.get();
  }

  public StringProperty nameProperty() {
    return name;
  }

  public void setName(String name) {
    this.name.set(name);
  }

  public String getRevealedIconUrl() {
    return revealedIconUrl.get();
  }

  public StringProperty revealedIconUrlProperty() {
    return revealedIconUrl;
  }

  public void setRevealedIconUrl(String revealedIconUrl) {
    this.revealedIconUrl.set(revealedIconUrl);
  }

  public int getTotalSteps() {
    return totalSteps.get();
  }

  public IntegerProperty totalStepsProperty() {
    return totalSteps;
  }

  public void setTotalSteps(int totalSteps) {
    this.totalSteps.set(totalSteps);
  }

  public AchievementType getType() {
    return type.get();
  }

  public ObjectProperty<AchievementType> typeProperty() {
    return type;
  }

  public void setType(AchievementType type) {
    this.type.set(type);
  }

  public String getUnlockedIconUrl() {
    return unlockedIconUrl.get();
  }

  public StringProperty unlockedIconUrlProperty() {
    return unlockedIconUrl;
  }

  public void setUnlockedIconUrl(String unlockedIconUrl) {
    this.unlockedIconUrl.set(unlockedIconUrl);
  }

  public int getOrder() {
    return order.get();
  }

  public IntegerProperty orderProperty() {
    return order;
  }

  public void setOrder(int order) {
    this.order.set(order);
  }
}
