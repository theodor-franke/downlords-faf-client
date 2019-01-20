package com.faforever.client.game;

public class StartGameProcessMessageBuilder {

  private final StartGameProcessServerMessage startGameProcessMessage;

  public StartGameProcessMessageBuilder() {
    startGameProcessMessage = new StartGameProcessServerMessage();
  }

  public static StartGameProcessMessageBuilder create() {
    return new StartGameProcessMessageBuilder();
  }

  public StartGameProcessMessageBuilder defaultValues() {
    startGameProcessMessage.setGameId(1);
    startGameProcessMessage.setMod(KnownFeaturedMod.DEFAULT.getTechnicalName());
    startGameProcessMessage.setTeam(0);
    startGameProcessMessage.setExpectedPlayers(2);
    return this;
  }

  public StartGameProcessServerMessage get() {
    return startGameProcessMessage;
  }
}
