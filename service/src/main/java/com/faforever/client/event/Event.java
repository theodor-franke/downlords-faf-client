package com.faforever.client.event;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Event {
  private StringProperty name;
  private StringProperty imageUrl;
  private ObjectProperty<Type> type;

  public Event() {
    name = new SimpleStringProperty();
    imageUrl = new SimpleStringProperty();
    type = new SimpleObjectProperty<>();
  }

  public String getName() {
    return name.get();
  }

  public void setName(String name) {
    this.name.set(name);
  }

  public StringProperty nameProperty() {
    return name;
  }

  public String getImageUrl() {
    return imageUrl.get();
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl.set(imageUrl);
  }

  public StringProperty imageUrlProperty() {
    return imageUrl;
  }

  public Type getType() {
    return type.get();
  }

  public void setType(Type type) {
    this.type.set(type);
  }

  public ObjectProperty<Type> typeProperty() {
    return type;
  }

  public static enum Type {
    NUMERIC,
    TIME
  }
}
