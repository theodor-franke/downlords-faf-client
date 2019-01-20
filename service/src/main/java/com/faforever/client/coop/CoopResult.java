package com.faforever.client.coop;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.Duration;

public class CoopResult {
  private final StringProperty id;
  private final ObjectProperty<Duration> duration;
  private final StringProperty playerNames;
  private final BooleanProperty secondaryObjectives;
  private final IntegerProperty ranking;
  private final IntegerProperty playerCount;
  private final IntegerProperty gameId;
  private final ObjectProperty<CoopMission> mission;

  public CoopResult() {
    id = new SimpleStringProperty();
    duration = new SimpleObjectProperty<>();
    playerNames = new SimpleStringProperty();
    secondaryObjectives = new SimpleBooleanProperty();
    ranking = new SimpleIntegerProperty();
    playerCount = new SimpleIntegerProperty();
    gameId = new SimpleIntegerProperty();
    mission = new SimpleObjectProperty<>();
  }

  public String getId() {
    return id.get();
  }

  public StringProperty idProperty() {
    return id;
  }

  public void setId(String id) {
    this.id.set(id);
  }

  public Duration getDuration() {
    return duration.get();
  }

  public ObjectProperty<Duration> durationProperty() {
    return duration;
  }

  public void setDuration(Duration duration) {
    this.duration.set(duration);
  }

  public String getPlayerNames() {
    return playerNames.get();
  }

  public StringProperty playerNamesProperty() {
    return playerNames;
  }

  public void setPlayerNames(String playerNames) {
    this.playerNames.set(playerNames);
  }

  public boolean isSecondaryObjectives() {
    return secondaryObjectives.get();
  }

  public BooleanProperty secondaryObjectivesProperty() {
    return secondaryObjectives;
  }

  public void setSecondaryObjectives(boolean secondaryObjectives) {
    this.secondaryObjectives.set(secondaryObjectives);
  }

  public int getRanking() {
    return ranking.get();
  }

  public IntegerProperty rankingProperty() {
    return ranking;
  }

  public void setRanking(int ranking) {
    this.ranking.set(ranking);
  }

  public int getPlayerCount() {
    return playerCount.get();
  }

  public IntegerProperty playerCountProperty() {
    return playerCount;
  }

  public void setPlayerCount(int playerCount) {
    this.playerCount.set(playerCount);
  }

  public Integer getGameId() {
    return gameId.get();
  }

  public IntegerProperty gameIdProperty() {
    return gameId;
  }

  public void setGameId(Integer gameId) {
    this.gameId.set(gameId);
  }

  public CoopMission getMission() {
    return mission.get();
  }

  public ObjectProperty<CoopMission> missionProperty() {
    return mission;
  }

  public void setMission(CoopMission mission) {
    this.mission.set(mission);
  }

}
