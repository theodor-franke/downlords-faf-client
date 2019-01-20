package com.faforever.client.game;

import com.faforever.client.game.relay.GpgClientCommand;
import com.faforever.client.game.relay.GpgGameMessage;

import java.util.Collections;

public class GameEndedMessage extends GpgGameMessage {

  public GameEndedMessage() {
    super(GpgClientCommand.GAME_STATE, Collections.singletonList("Ended"));
  }
}
