package com.faforever.client.remote.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RestoreGameSessionMessage extends ClientMessage {

  @JsonProperty("game_id")
  private final int gameId;

  public RestoreGameSessionMessage(int gameId) {
    super(ClientMessageType.RESTORE_GAME_SESSION);
    this.gameId = gameId;
  }
}
