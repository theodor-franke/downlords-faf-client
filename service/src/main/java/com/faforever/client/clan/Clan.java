package com.faforever.client.clan;

import com.faforever.client.player.Player;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;

public class Clan {

  private final StringProperty id;
  private final StringProperty description;
  private final ObjectProperty<Player> founder;
  private final ObjectProperty<Player> leader;
  private final StringProperty name;
  private final StringProperty tag;
  private final StringProperty websiteUrl;
  private final ListProperty<Player> members;
  private final ObjectProperty<LocalDateTime> createTime;

  public Clan() {
    id = new SimpleStringProperty();
    description = new SimpleStringProperty();
    founder = new SimpleObjectProperty<>();
    leader = new SimpleObjectProperty<>();
    name = new SimpleStringProperty();
    tag = new SimpleStringProperty();
    websiteUrl = new SimpleStringProperty();
    members = new SimpleListProperty<>(FXCollections.observableArrayList());
    createTime = new SimpleObjectProperty<>();
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

  public void setDescription(String description) {
    this.description.set(description);
  }

  public StringProperty descriptionProperty() {
    return description;
  }

  public Player getFounder() {
    return founder.get();
  }

  public void setFounder(Player founder) {
    this.founder.set(founder);
  }

  public ObjectProperty<Player> founderProperty() {
    return founder;
  }

  public Player getLeader() {
    return leader.get();
  }

  public void setLeader(Player leader) {
    this.leader.set(leader);
  }

  public ObjectProperty<Player> leaderProperty() {
    return leader;
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

  public String getTag() {
    return tag.get();
  }

  public void setTag(String tag) {
    this.tag.set(tag);
  }

  public StringProperty tagProperty() {
    return tag;
  }

  public LocalDateTime getCreateTime() {
    return createTime.get();
  }

  public void setCreateTime(LocalDateTime createTime) {
    this.createTime.set(createTime);
  }

  public ObjectProperty<LocalDateTime> createTimeProperty() {
    return createTime;
  }

  public ObservableList<Player> getMembers() {
    return members.get();
  }

  public ListProperty<Player> membersProperty() {
    return members;
  }

  public String getWebsiteUrl() {
    return websiteUrl.get();
  }

  public void setWebsiteUrl(String websiteUrl) {
    this.websiteUrl.set(websiteUrl);
  }

  public StringProperty websiteUrlProperty() {
    return websiteUrl;
  }
}
