package com.faforever.client.game;

import com.faforever.client.fa.relay.GpgClientCommand;
import com.faforever.client.fa.relay.GpgGameMessage;

import java.util.Collections;

public class GameEndedMessage extends GpgGameMessage {

  public GameEndedMessage() {
    super(GpgClientCommand.GAME_STATE, Collections.singletonList("Ended"));
  }
}
