package com.faforever.client.event;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import org.supcomhub.api.dto.Event;

public class PlayerEvent {

  private IntegerProperty count;
  private ObjectProperty<Event> event;

  public int getCount() {
    return count.get();
  }

  public void setCount(int count) {
    this.count.set(count);
  }

  public IntegerProperty countProperty() {
    return count;
  }

  public Event getEvent() {
    return event.get();
  }

  public void setEvent(Event event) {
    this.event.set(event);
  }

  public ObjectProperty<Event> eventProperty() {
    return event;
  }
}
