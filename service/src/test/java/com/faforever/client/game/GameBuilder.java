package com.faforever.client.game;

import javafx.collections.FXCollections;

import java.time.Instant;

public class GameBuilder {

  private final Game game;

  public GameBuilder() {
    game = new Game();
  }

  public static GameBuilder create() {
    return new GameBuilder();
  }

  public GameBuilder defaultValues() {
    game.setFeaturedMod(KnownFeaturedMod.DEFAULT.getTechnicalName());
    game.setHostName("Host");
    game.setMapName("mapName");
    game.setMaxPlayers(2);
    game.setSimMods(FXCollections.emptyObservableMap());
    game.setState(GameState.OPEN);
    game.setTitle("Title");
    game.setTeams(FXCollections.emptyObservableMap());
    game.setId(1);
    game.setMaxRank(800);
    game.setMaxRank(1300);
    game.setStartTime(Instant.now());
    return this;
  }

  public Game get() {
    return game;
  }

  public GameBuilder title(String title) {
    game.setTitle(title);
    return this;
  }

  public GameBuilder featuredMod(String featuredMod) {
    game.setFeaturedMod(featuredMod);
    return this;
  }

  public GameBuilder state(GameState state) {
    game.setState(state);
    return this;
  }

  public GameBuilder host(String host) {
    game.setHostName(host);
    return this;
  }
}
