package com.faforever.client.game;

import com.faforever.client.game.relay.GpgMessage;

public class HostGameMessage extends GpgMessage {

  public HostGameMessage() {
    super("HostGame", 1);
  }

  public String getMapName() {
    return getString(0);
  }
}
