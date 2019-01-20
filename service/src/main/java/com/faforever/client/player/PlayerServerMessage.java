package com.faforever.client.player;

import com.faforever.client.avatar.Avatar;
import com.faforever.client.game.Game;
import com.faforever.client.remote.ServerMessage;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.TimeZone;

/** Holds player information sent by the server. */
@Data
public class PlayerServerMessage implements ServerMessage {

  private int id;
  @NotNull
  private String displayName;
  private String country;
  private TimeZone timeZone;
  private Map<String, Integer> ranks;
  private int numberOfGames;
  private Avatar avatar;
  private String clanTag;
  private Game game;
}
