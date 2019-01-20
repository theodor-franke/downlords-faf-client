package com.faforever.client.replay;

import com.faforever.client.game.Validity;
import com.faforever.client.map.FaMap;
import com.faforever.client.mod.FeaturedMod;
import com.faforever.client.review.Review;
import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import lombok.Data;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.time.Duration;
import java.time.temporal.Temporal;
import java.util.List;

public class Replay {

  private final IntegerProperty id;
  private final StringProperty title;
  private final MapProperty<String, List<String>> teams;
  private final MapProperty<Integer, List<PlayerStats>> teamPlayerStats;
  private final ObjectProperty<Temporal> startTime;
  private final ObjectProperty<Temporal> endTime;
  private final ObjectProperty<FeaturedMod> featuredMod;
  private final ObjectProperty<FaMap> map;
  private final ObjectProperty<Path> replayFile;
  private final IntegerProperty views;
  private final ListProperty<ChatMessage> chatMessages;
  private final ListProperty<GameOption> gameOptions;
  private final ListProperty<Review> reviews;
  private final ObjectProperty<Validity> validity;

  public Replay(String title) {
    this();
    this.title.set(title);
  }

  public Replay() {
    id = new SimpleIntegerProperty();
    title = new SimpleStringProperty();
    teams = new SimpleMapProperty<>(FXCollections.observableHashMap());
    teamPlayerStats = new SimpleMapProperty<>(FXCollections.observableHashMap());
    startTime = new SimpleObjectProperty<>();
    endTime = new SimpleObjectProperty<>();
    featuredMod = new SimpleObjectProperty<>();
    map = new SimpleObjectProperty<>();
    replayFile = new SimpleObjectProperty<>();
    views = new SimpleIntegerProperty();
    chatMessages = new SimpleListProperty<>(FXCollections.observableArrayList());
    gameOptions = new SimpleListProperty<>(FXCollections.observableArrayList());
    reviews = new SimpleListProperty<>(FXCollections.observableArrayList(param
      -> new Observable[]{param.scoreProperty(), param.textProperty()}));
    validity = new SimpleObjectProperty<>();
  }

  public Replay(LocalReplayInfo replayInfo, Path replayFile, FeaturedMod featuredMod, FaMap faMap) {
    this();
    id.set(replayInfo.getId());
    title.set(StringEscapeUtils.unescapeHtml4(replayInfo.getTitle()));
    startTime.set(replayInfo.getStartTime());
    endTime.set(replayInfo.getStartTime().plus(replayInfo.getDuration()));
    this.featuredMod.set(featuredMod);
    map.set(faMap);
    this.replayFile.set(replayFile);
    if (replayInfo.getTeams() != null) {
      teams.putAll(replayInfo.getTeams());
    }
  }

  public static String getReplayUrl(int replayId, String baseUrlFormat) {
    return String.format(baseUrlFormat, replayId);
  }

  public Validity getValidity() {
    return validity.get();
  }

  public void setValidity(Validity validity) {
    this.validity.set(validity);
  }

  public ObjectProperty<Validity> validityProperty() {
    return validity;
  }

  public Path getReplayFile() {
    return replayFile.get();
  }

  public void setReplayFile(Path replayFile) {
    this.replayFile.set(replayFile);
  }

  public ObjectProperty<Path> replayFileProperty() {
    return replayFile;
  }

  public String getTitle() {
    return title.get();
  }

  public void setTitle(String title) {
    this.title.set(title);
  }

  public StringProperty titleProperty() {
    return title;
  }

  public ObservableMap<String, List<String>> getTeams() {
    return teams.get();
  }

  public void setTeams(ObservableMap<String, List<String>> teams) {
    this.teams.set(teams);
  }

  public MapProperty<String, List<String>> teamsProperty() {
    return teams;
  }

  public int getId() {
    return id.get();
  }

  public void setId(int id) {
    this.id.set(id);
  }

  public IntegerProperty idProperty() {
    return id;
  }

  public Temporal getStartTime() {
    return startTime.get();
  }

  public void setStartTime(Temporal startTime) {
    this.startTime.set(startTime);
  }

  public ObjectProperty<Temporal> startTimeProperty() {
    return startTime;
  }

  @Nullable
  public Temporal getEndTime() {
    return endTime.get();
  }

  public void setEndTime(Temporal endTime) {
    this.endTime.set(endTime);
  }

  public ObjectProperty<Temporal> endTimeProperty() {
    return endTime;
  }

  public FeaturedMod getFeaturedMod() {
    return featuredMod.get();
  }

  public void setFeaturedMod(FeaturedMod featuredMod) {
    this.featuredMod.set(featuredMod);
  }

  public ObjectProperty<FeaturedMod> featuredModProperty() {
    return featuredMod;
  }

  @Nullable
  public FaMap getMap() {
    return map.get();
  }

  public void setMap(FaMap map) {
    this.map.set(map);
  }

  public ObjectProperty<FaMap> mapProperty() {
    return map;
  }

  public int getViews() {
    return views.get();
  }

  public void setViews(int views) {
    this.views.set(views);
  }

  public IntegerProperty viewsProperty() {
    return views;
  }

  public ObservableList<ChatMessage> getChatMessages() {
    return chatMessages.get();
  }

  public void setChatMessages(ObservableList<ChatMessage> chatMessages) {
    this.chatMessages.set(chatMessages);
  }

  public ListProperty<ChatMessage> chatMessagesProperty() {
    return chatMessages;
  }

  public ObservableList<GameOption> getGameOptions() {
    return gameOptions.get();
  }

  public void setGameOptions(ObservableList<GameOption> gameOptions) {
    this.gameOptions.set(gameOptions);
  }

  public ListProperty<GameOption> gameOptionsProperty() {
    return gameOptions;
  }

  public ObservableMap<Integer, List<PlayerStats>> getTeamPlayerStats() {
    return teamPlayerStats.get();
  }

  public void setTeamPlayerStats(ObservableMap<Integer, List<PlayerStats>> teamPlayerStats) {
    this.teamPlayerStats.set(teamPlayerStats);
  }

  public MapProperty<Integer, List<PlayerStats>> teamPlayerStatsProperty() {
    return teamPlayerStats;
  }

  public ObservableList<Review> getReviews() {
    return reviews.get();
  }

  public static class ChatMessage {
    private final ObjectProperty<Duration> time;
    private final StringProperty sender;
    private final StringProperty message;

    public ChatMessage(Duration time, String sender, String message) {
      this.time = new SimpleObjectProperty<>(time);
      this.sender = new SimpleStringProperty(sender);
      this.message = new SimpleStringProperty(message);
    }

    public Duration getTime() {
      return time.get();
    }

    public void setTime(Duration time) {
      this.time.set(time);
    }

    public ObjectProperty<Duration> timeProperty() {
      return time;
    }

    public String getSender() {
      return sender.get();
    }

    public void setSender(String sender) {
      this.sender.set(sender);
    }

    public StringProperty senderProperty() {
      return sender;
    }

    public String getMessage() {
      return message.get();
    }

    public void setMessage(String message) {
      this.message.set(message);
    }

    public StringProperty messageProperty() {
      return message;
    }
  }

  public static class GameOption {
    private final StringProperty key;
    private final StringProperty value;

    public GameOption(String key, Object value) {
      this.key = new SimpleStringProperty(key);
      this.value = new SimpleStringProperty(String.valueOf(value));
    }

    public String getKey() {
      return key.get();
    }

    public void setKey(String key) {
      this.key.set(key);
    }

    public StringProperty keyProperty() {
      return key;
    }

    public String getValue() {
      return value.get();
    }

    public void setValue(String value) {
      this.value.set(value);
    }

    public StringProperty valueProperty() {
      return value;
    }
  }

  @Data
  public static class PlayerStats {
    private final int playerId;
    private final int rankBefore;
    private final int rankAfter;
  }
}
