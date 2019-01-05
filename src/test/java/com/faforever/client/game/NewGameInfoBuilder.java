package com.faforever.client.game;

import java.util.HashSet;

public class NewGameInfoBuilder {

  private final HostGameRequest hostGameRequest;

  private NewGameInfoBuilder() {
    hostGameRequest = new HostGameRequest();
  }

  public static NewGameInfoBuilder create() {
    return new NewGameInfoBuilder();
  }

  public NewGameInfoBuilder defaultValues() {
    hostGameRequest.setMap("map");
    hostGameRequest.setFeaturedMod(FeaturedModBeanBuilder.create().defaultValues().get());
    hostGameRequest.setPassword("password");
    hostGameRequest.setTitle("title");
    hostGameRequest.setSimMods(new HashSet<String>() {{
      add("111-456-789");
      add("222-456-789");
      add("333-456-789");
    }});
    return this;
  }

  public HostGameRequest get() {
    return hostGameRequest;
  }


}
