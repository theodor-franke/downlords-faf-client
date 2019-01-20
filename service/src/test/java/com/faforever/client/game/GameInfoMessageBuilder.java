package com.faforever.client.game;

import com.faforever.client.game.GameInfoServerMessage.Player;

import java.util.ArrayList;

public class GameInfoMessageBuilder {

  private GameInfoServerMessage gameInfoMessage;

  private GameInfoMessageBuilder(Integer id) {
    gameInfoMessage = new GameInfoServerMessage();
    gameInfoMessage.setId(id);
  }

  public static GameInfoMessageBuilder create(Integer uid) {
    return new GameInfoMessageBuilder(uid);
  }

  public GameInfoMessageBuilder defaultValues() {
    gameInfoMessage.setHost(new Player().setName("Some host"));
    gameInfoMessage.setMod(KnownFeaturedMod.FAF.getTechnicalName());
    gameInfoMessage.setMap("scmp_007");
    gameInfoMessage.setMaxPlayers(4);
    gameInfoMessage.setState(GameState.OPEN);
    gameInfoMessage.setTitle("Test preferences");
    gameInfoMessage.setPlayers(new ArrayList<>());
    gameInfoMessage.setPasswordProtected(false);
    return this;
  }

  public GameInfoServerMessage get() {
    return gameInfoMessage;
  }

  public GameInfoMessageBuilder addTeamMember(int team, int playerId, String playerName) {
    gameInfoMessage.getPlayers().add(new Player().setId(playerId).setTeam(team).setName(playerName));
    return this;
  }

  public GameInfoMessageBuilder host(String host) {
    gameInfoMessage.setHost(new Player().setName(host));
    return this;
  }

  public GameInfoMessageBuilder title(String title) {
    gameInfoMessage.setTitle(title);
    return this;
  }

  public GameInfoMessageBuilder mapName(String mapName) {
    gameInfoMessage.setMap(mapName);
    return this;
  }

  public GameInfoMessageBuilder featuredMod(String mod) {
    gameInfoMessage.setMod(mod);
    return this;
  }

  public GameInfoMessageBuilder maxPlayers(int maxPlayers) {
    gameInfoMessage.setMaxPlayers(maxPlayers);
    return this;
  }

  public GameInfoMessageBuilder state(GameState state) {
    gameInfoMessage.setState(state);
    return this;
  }

  public GameInfoMessageBuilder passwordProtected(boolean passwordProtected) {
    gameInfoMessage.setPasswordProtected(passwordProtected);
    return this;
  }
}
