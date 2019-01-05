package com.faforever.client.game;

import java.util.Arrays;

public class GameLaunchMessageBuilder {

  private final StartGameProcessMessage startGameProcessMessage;

  public GameLaunchMessageBuilder() {
    startGameProcessMessage = new StartGameProcessMessage();
  }

  public static GameLaunchMessageBuilder create() {
    return new GameLaunchMessageBuilder();
  }

  public GameLaunchMessageBuilder defaultValues() {
    startGameProcessMessage.setUid(1);
    startGameProcessMessage.setMod(KnownFeaturedMod.DEFAULT.getTechnicalName());
    startGameProcessMessage.setArgs(Arrays.asList("/ratingcolor red", "/clan foo"));
    return this;
  }

  public StartGameProcessMessage get() {
    return startGameProcessMessage;
  }
}
