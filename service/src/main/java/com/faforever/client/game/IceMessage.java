package com.faforever.client.game;

import com.faforever.client.game.relay.GpgClientCommand;
import com.faforever.client.game.relay.GpgGameMessage;

import java.util.Arrays;

public class IceMessage extends GpgGameMessage {
  public IceMessage(int remotePlayerId, Object message) {
    super(GpgClientCommand.ICE_MESSAGE, Arrays.asList(remotePlayerId, message));
  }

  public int getReceiverId() {
    return getInt(0);
  }

  public Object getContent() {
    return getArgs().get(1);
  }
}
