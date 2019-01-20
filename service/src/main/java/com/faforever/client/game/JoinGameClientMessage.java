package com.faforever.client.game;

import com.faforever.client.remote.ClientMessage;
import lombok.Value;

@Value(staticConstructor = "of")
public class JoinGameClientMessage implements ClientMessage {
  private int id;
  private String password;
}
