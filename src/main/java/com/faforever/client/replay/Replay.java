package com.faforever.client.replay;

import com.faforever.client.map.MapBean;
import com.faforever.client.mod.FeaturedMod;
import com.faforever.client.vault.review.Review;
import com.faforever.client.vault.review.ReviewsSummary;
import com.faforever.commons.api.dto.Faction;
import com.faforever.commons.api.dto.Game;
import com.faforever.commons.api.dto.GamePlayerStats;
import com.faforever.commons.api.dto.LeaderboardRatingJournal;
import com.faforever.commons.api.dto.Validity;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.maven.artifact.versioning.ComparableVersion;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.faforever.client.util.TimeUtil.fromPythonTime;

public class Replay {

  private final IntegerProperty id;
  private final StringProperty title;
  private final BooleanProperty replayAvailable;
  private final MapProperty<String, List<String>> teams;
  private final MapProperty<String, List<PlayerStats>> teamPlayerStats;
  private final ObjectProperty<OffsetDateTime> startTime;
  private final ObjectProperty<OffsetDateTime> endTime;
  private final ObjectProperty<FeaturedMod> featuredMod;
  private final ObjectProperty<MapBean> map;
  private final ObjectProperty<Path> replayFile;
  private final ObjectProperty<Integer> replayTicks;
  private final IntegerProperty views;
  private final ListProperty<ChatMessage> chatMessages;
  private final ListProperty<GameOption> gameOptions;
  private final ListProperty<Review> reviews;
  private final ObjectProperty<Validity> validity;
  private final ObjectProperty<ReviewsSummary> reviewsSummary;

  public Replay(String title) {
    this();
    this.title.set(title);
  }

  public Replay() {
    id = new SimpleIntegerProperty();
    title = new SimpleStringProperty();
    replayAvailable = new SimpleBooleanProperty(false);
    teams = new SimpleMapProperty<>(FXCollections.observableHashMap());
    teamPlayerStats = new SimpleMapProperty<>(FXCollections.observableHashMap());
    startTime = new SimpleObjectProperty<>();
    endTime = new SimpleObjectProperty<>();
    featuredMod = new SimpleObjectProperty<>();
    map = new SimpleObjectProperty<>();
    replayFile = new SimpleObjectProperty<>();
    replayTicks = new SimpleObjectProperty<>();
    views = new SimpleIntegerProperty();
    chatMessages = new SimpleListProperty<>(FXCollections.observableArrayList());
    gameOptions = new SimpleListProperty<>(FXCollections.observableArrayList());
    reviews = new SimpleListProperty<>(FXCollections.observableArrayList(param
        -> new Observable[]{param.scoreProperty(), param.textProperty()}));
    validity = new SimpleObjectProperty<>();
    reviewsSummary = new SimpleObjectProperty<>();
  }

  public Replay(LocalReplayInfo replayInfo, Path replayFile, FeaturedMod featuredMod, MapBean mapBean) {
    this();
    id.set(replayInfo.getUid());
    title.set(StringEscapeUtils.unescapeHtml4(replayInfo.getTitle()));
    replayAvailable.set(true);
    startTime.set(fromPythonTime(replayInfo.getGameTime() > 0 ? replayInfo.getGameTime() : replayInfo.getLaunchedAt()));
    endTime.set(fromPythonTime(replayInfo.getGameEnd()));
    this.featuredMod.set(featuredMod);
    map.set(mapBean);
    this.replayFile.set(replayFile);
    if (replayInfo.getTeams() != null) {
      teams.putAll(replayInfo.getTeams());
    }
  }

  public static Replay fromDto(Game dto) {
    Replay replay = new Replay();
    replay.setReplayAvailable(dto.getReplayAvailable());
    replay.setId(Integer.parseInt(dto.getId()));
    replay.setFeaturedMod(FeaturedMod.fromFeaturedMod(dto.getFeaturedMod()));
    replay.setTitle(dto.getName());
    replay.setStartTime(dto.getStartTime());
    Optional.ofNullable(dto.getEndTime()).ifPresent(replay::setEndTime);
    Optional.ofNullable(dto.getMapVersion()).ifPresent(mapVersion -> replay.setMap(MapBean.fromMapVersionDto(dto.getMapVersion())));
    replay.setReplayTicks(dto.getReplayTicks());
    replay.setTeams(teams(dto));
    replay.setTeamPlayerStats(teamPlayerStats(dto));
    replay.getReviews().setAll(reviews(dto));
    replay.setValidity(dto.getValidity());
    replay.setReviewsSummary(ReviewsSummary.fromDto(dto.getGameReviewsSummary()));
    return replay;
  }

  private static ObservableList<Review> reviews(Game dto) {
    return FXCollections.observableList(dto.getReviews().stream()
        .map(gameReview -> {
          Review review = Review.fromDto(gameReview);
          review.setVersion(new ComparableVersion(""));
          return review;
        })
        .collect(Collectors.toList()));
  }

  private static ObservableMap<String, List<String>> teams(Game dto) {
    ObservableMap<String, List<String>> teams = FXCollections.observableHashMap();
    dto.getPlayerStats()
        .forEach(gamePlayerStats -> teams.computeIfAbsent(
            String.valueOf(gamePlayerStats.getTeam()),
            s -> new LinkedList<>()
        ).add(gamePlayerStats.getPlayer().getLogin()));
    return teams;
  }

  private static ObservableMap<String, List<PlayerStats>> teamPlayerStats(Game dto) {
    ObservableMap<String, List<PlayerStats>> teams = FXCollections.observableHashMap();
    dto.getPlayerStats()
        .forEach(gamePlayerStats -> teams.computeIfAbsent(
            String.valueOf(gamePlayerStats.getTeam()),
            s -> new LinkedList<>()
        ).add(PlayerStats.fromDto(gamePlayerStats)));
    return teams;
  }

  public static String getReplayUrl(int replayId, String baseUrlFormat) {
    return String.format(baseUrlFormat, replayId);
  }

  public boolean getReplayAvailable() {
    return replayAvailable.get();
  }

  public void setReplayAvailable(boolean replayAvailable) {
    this.replayAvailable.set(replayAvailable);
  }

  public BooleanProperty replayAvailableProperty() {
    return replayAvailable;
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

  public OffsetDateTime getStartTime() {
    return startTime.get();
  }

  public void setStartTime(OffsetDateTime startTime) {
    this.startTime.set(startTime);
  }

  public ObjectProperty<OffsetDateTime> startTimeProperty() {
    return startTime;
  }

  @Nullable
  public OffsetDateTime getEndTime() {
    return endTime.get();
  }

  public void setEndTime(OffsetDateTime endTime) {
    this.endTime.set(endTime);
  }

  public ObjectProperty<OffsetDateTime> endTimeProperty() {
    return endTime;
  }

  @Nullable
  public Integer getReplayTicks() {
    return replayTicks.get();
  }

  public void setReplayTicks(Integer replayTicks) {
    this.replayTicks.set(replayTicks);
  }

  public ObjectProperty<Integer> replayTicksProperty() {
    return replayTicks;
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
  public MapBean getMap() {
    return map.get();
  }

  public void setMap(MapBean map) {
    this.map.set(map);
  }

  public ObjectProperty<MapBean> mapProperty() {
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

  public ObservableMap<String, List<PlayerStats>> getTeamPlayerStats() {
    return teamPlayerStats.get();
  }

  public void setTeamPlayerStats(ObservableMap<String, List<PlayerStats>> teamPlayerStats) {
    this.teamPlayerStats.set(teamPlayerStats);
  }

  public MapProperty<String, List<PlayerStats>> teamPlayerStatsProperty() {
    return teamPlayerStats;
  }

  public ObservableList<Review> getReviews() {
    return reviews.get();
  }

  public ReviewsSummary getReviewsSummary() {
    return reviewsSummary.get();
  }

  public void setReviewsSummary(ReviewsSummary reviewsSummary) {
    this.reviewsSummary.set(reviewsSummary);
  }

  public ObjectProperty<ReviewsSummary> reviewsSummaryProperty() {
    return reviewsSummary;
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
  @Builder
  public static class PlayerStats {
    private final int playerId;
    private final Double beforeMean;
    private final Double beforeDeviation;
    private final Double afterMean;
    private final Double afterDeviation;
    private final int score;
    private final Faction faction;

    public static PlayerStats fromDto(GamePlayerStats gamePlayerStats) {
      Optional<LeaderboardRatingJournal> ratingJournal = gamePlayerStats.getLeaderboardRatingJournals().stream().findFirst();
      Double beforeMean = ratingJournal.map(LeaderboardRatingJournal::getMeanBefore).orElse(null);
      Double beforeDeviation = ratingJournal.map(LeaderboardRatingJournal::getDeviationBefore).orElse(null);
      Double afterMean = ratingJournal.map(LeaderboardRatingJournal::getMeanAfter).orElse(null);
      Double afterDeviation = ratingJournal.map(LeaderboardRatingJournal::getDeviationAfter).orElse(null);
      return new PlayerStats(
          Integer.parseInt(gamePlayerStats.getPlayer().getId()),
          beforeMean,
          beforeDeviation,
          afterMean,
          afterDeviation,
          gamePlayerStats.getScore(),
          gamePlayerStats.getFaction()
      );
    }
  }
}
