package com.faforever.client.replay;

import com.faforever.client.game.Game;
import com.faforever.client.game.GameState;
import com.faforever.client.player.Player;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * This class is meant to be serialized/deserialized from/to JSON.
 */
@Data
public class LocalReplayInfo {

  @JsonProperty("host")
  private String host;

  @JsonProperty("id")
  private Integer id;

  @JsonProperty("title")
  private String title;

  @JsonProperty("map")
  private String map;

  @JsonProperty("state")
  private GameState state;

  @JsonProperty("options")
  private Boolean[] options;

  @JsonProperty("featuredMod")
  private FeaturedMod featuredMod;

  @JsonProperty("simMods")
  private Map<UUID, String> simMods;

  @JsonProperty("teams")
  private Map<String, List<String>> teams;

  @JsonProperty("recorder")
  private String recorder;

  @JsonProperty("startTime")
  private Instant startTime;

  @JsonProperty("duration")
  private Duration duration;

  @JsonProperty("ticks")
  private double ticks;

  public void updateFromGameInfoBean(Game game) {
    id = game.getId();
    title = game.getTitle();
    host = game.getHostName();
    map = game.getMapName();
    state = game.getState();
    featuredMod = new FeaturedMod(game.getFeaturedMod(), game.getFeaturedModVersion());
    startTime = game.getStartTime();
    duration = Duration.between(startTime, Instant.now());
    simMods = game.getSimMods();

    // FIXME this (and all others here) should do a deep copy
    this.teams = game.getTeams().entrySet().stream()
      .collect(Collectors.groupingBy(
        o -> String.valueOf(o.getKey()),
        HashMap::new,
        Collectors.flatMapping(
          entry -> entry.getValue().stream().map(Player::getDisplayName),
          Collectors.toList()
        )
      ));
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  static class FeaturedMod {
    private String name;
    private int version;
  }
}
