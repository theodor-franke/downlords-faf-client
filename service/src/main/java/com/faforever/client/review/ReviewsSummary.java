package com.faforever.client.review;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public class ReviewsSummary {
  private final StringProperty id;
  private final FloatProperty positive;
  private final FloatProperty negative;
  private final IntegerProperty score;
  private final IntegerProperty reviews;
  private final FloatProperty lowerBound;
  private final MapProperty<Byte, Integer> scoreCounts;

  public ReviewsSummary() {
    id = new SimpleStringProperty();
    positive = new SimpleFloatProperty();
    negative = new SimpleFloatProperty();
    score = new SimpleIntegerProperty();
    reviews = new SimpleIntegerProperty();
    lowerBound = new SimpleFloatProperty();
    scoreCounts = new SimpleMapProperty<>(FXCollections.observableHashMap());
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

  public float getPositive() {
    return positive.get();
  }

  public void setPositive(float positive) {
    this.positive.set(positive);
  }

  public FloatProperty positiveProperty() {
    return positive;
  }

  public float getNegative() {
    return negative.get();
  }

  public void setNegative(float negative) {
    this.negative.set(negative);
  }

  public FloatProperty negativeProperty() {
    return negative;
  }

  public int getScore() {
    return score.get();
  }

  public void setScore(int score) {
    this.score.set(score);
  }

  public IntegerProperty scoreProperty() {
    return score;
  }

  public int getReviews() {
    return reviews.get();
  }

  public void setReviews(int reviews) {
    this.reviews.set(reviews);
  }

  public IntegerProperty reviewsProperty() {
    return reviews;
  }

  public float getLowerBound() {
    return lowerBound.get();
  }

  public void setLowerBound(float lowerBound) {
    this.lowerBound.set(lowerBound);
  }

  public FloatProperty lowerBoundProperty() {
    return lowerBound;
  }

  public ObservableMap<Byte, Integer> getScoreCounts() {
    return scoreCounts.get();
  }

  public MapProperty<Byte, Integer> scoreCountsProperty() {
    return scoreCounts;
  }

  public void setScoreCounts(ObservableMap<Byte, Integer> scoreCounts) {
    this.scoreCounts.set(scoreCounts);
  }
}
