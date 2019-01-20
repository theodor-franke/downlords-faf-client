package com.faforever.client.review;

import com.faforever.client.player.Player;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Review {
  private final ObjectProperty<String> id;
  private final StringProperty text;
  private final ObjectProperty<Player> reviewer;
  private final ObjectProperty<Integer> score;

  public Review() {
    this(null, null, 0, null);
  }

  public Review(String id, String text, int score, Player reviewer) {
    this.id = new SimpleObjectProperty<>(id);
    this.text = new SimpleStringProperty(text);
    this.score = new SimpleObjectProperty<>(score);
    this.reviewer = new SimpleObjectProperty<>(reviewer);
  }

  public String getId() {
    return id.get();
  }

  public void setId(String id) {
    this.id.set(id);
  }

  public ObjectProperty<String> idProperty() {
    return id;
  }

  public String getText() {
    return text.get();
  }

  public void setText(String text) {
    this.text.set(text);
  }

  public StringProperty textProperty() {
    return text;
  }

  public Player getReviewer() {
    return reviewer.get();
  }

  public void setReviewer(Player reviewer) {
    this.reviewer.set(reviewer);
  }

  public ObjectProperty<Player> reviewerProperty() {
    return reviewer;
  }

  public Integer getScore() {
    return score.get();
  }

  public void setScore(Integer score) {
    this.score.set(score);
  }

  public ObjectProperty<Integer> scoreProperty() {
    return score;
  }
}
