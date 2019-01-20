package com.faforever.client.avatar;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.jetbrains.annotations.Nullable;

import java.net.URL;

public class Avatar {
  private final ObjectProperty<Integer> id;
  private final ObjectProperty<URL> url;
  private final StringProperty description;

  public Avatar() {
    this(null, null, null);
  }

  public Avatar(Integer id, @Nullable URL url, @Nullable String description) {
    this.id = new SimpleObjectProperty<>(id);
    this.url = new SimpleObjectProperty<>(url);
    this.description = new SimpleStringProperty(description);
  }

  public Integer getId() {
    return id.get();
  }

  public void setId(int id) {
    this.id.set(id);
  }

  public ObjectProperty<Integer> idProperty() {
    return id;
  }

  @Nullable
  public URL getUrl() {
    return url.get();
  }

  public void setUrl(URL url) {
    this.url.set(url);
  }

  public ObjectProperty<URL> urlProperty() {
    return url;
  }

  @Nullable
  public String getDescription() {
    return description.get();
  }

  public void setDescription(String description) {
    this.description.set(description);
  }

  public StringProperty descriptionProperty() {
    return description;
  }
}
