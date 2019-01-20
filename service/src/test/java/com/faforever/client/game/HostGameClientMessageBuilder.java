package com.faforever.client.game;

import com.faforever.client.mod.FeaturedMod;

import java.util.Set;
import java.util.UUID;

public class HostGameClientMessageBuilder {

  private static String title;
  private static String password;
  private static FeaturedMod featuredMod;
  private static String map;
  private static Set<UUID> simMods;
  private static GameVisibility gameVisibility;

  private HostGameClientMessageBuilder() {
  }

  public static HostGameClientMessageBuilder create() {
    return new HostGameClientMessageBuilder();
  }

  public HostGameClientMessageBuilder defaultValues() {
    map = "map";
    featuredMod = FeaturedModBeanBuilder.create().defaultValues().get();
    password = "password";
    title = "title";
    simMods = Set.of(
      UUID.fromString("000000000000-0000-0000-0000-00000111"),
      UUID.fromString("000000000000-0000-0000-0000-00000222"),
      UUID.fromString("000000000000-0000-0000-0000-00000333")
    );
    gameVisibility = GameVisibility.PUBLIC;

    return this;
  }

  public HostGameRequest get() {
    return new HostGameRequest(
      title,
      password,
      featuredMod,
      map,
      simMods,
      gameVisibility
    );
  }
}
