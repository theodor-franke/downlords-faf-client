package com.faforever.client.leaderboard;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.supcomhub.api.dto.Leaderboard;

public class LeaderboardEntry {

  private StringProperty playerName;
  private IntegerProperty rating;
  private IntegerProperty totalGames;
  private IntegerProperty wonGames;
  private ObjectProperty<Leaderboard> leaderboard;
  private IntegerProperty position;

  public LeaderboardEntry() {
    playerName = new SimpleStringProperty();
    rating = new SimpleIntegerProperty();
    totalGames = new SimpleIntegerProperty();
    wonGames = new SimpleIntegerProperty();
    leaderboard = new SimpleObjectProperty<>();
    position = new SimpleIntegerProperty();
  }

  public float getWinLossRatio() {
    return (float) getWonGames() / (float) getTotalGames();
  }

  public String getPlayerName() {
    return playerName.get();
  }

  public void setPlayerName(String playerName) {
    this.playerName.set(playerName);
  }

  public StringProperty playerNameProperty() {
    return playerName;
  }

  public int getPosition() {
    return position.get();
  }

  public void setPosition(int position) {
    this.position.set(position);
  }

  public IntegerProperty positionProperty() {
    return position;
  }

  public int getRating() {
    return rating.get();
  }

  public void setRating(int rating) {
    this.rating.set(rating);
  }

  public IntegerProperty ratingProperty() {
    return rating;
  }

  public int getTotalGames() {
    return totalGames.get();
  }

  public void setTotalGames(int totalGames) {
    this.totalGames.set(totalGames);
  }

  public IntegerProperty totalGamesProperty() {
    return totalGames;
  }

  public int getWonGames() {
    return wonGames.get();
  }

  public void setWonGames(int wonGames) {
    this.wonGames.set(wonGames);
  }

  public IntegerProperty wonGamesProperty() {
    return wonGames;
  }

  @Override
  public int hashCode() {
    return playerName.get() != null ? playerName.get().hashCode() : 0;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    LeaderboardEntry that = (LeaderboardEntry) o;

    return !(playerName.get() != null ? !playerName.get().equalsIgnoreCase(that.playerName.get()) : that.playerName.get() != null);

  }

  @Override
  public String toString() {
    return "Ranked1v1EntryBean{" +
      "displayName=" + playerName.get() +
        '}';
  }
}
