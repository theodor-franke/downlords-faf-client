package com.faforever.client.preferences;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class NewsPrefs {
  private final StringProperty lastReadNewsId;

  public NewsPrefs() {
    lastReadNewsId = new SimpleStringProperty();
  }

  public String getLastReadNewsId() {
    return lastReadNewsId.get();
  }

  public void setLastReadNewsId(String lastReadNewsId) {
    this.lastReadNewsId.set(lastReadNewsId);
  }

  public StringProperty lastReadNewsIdProperty() {
    return lastReadNewsId;
  }
}
