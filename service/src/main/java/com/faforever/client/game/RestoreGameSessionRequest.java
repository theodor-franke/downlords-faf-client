package com.faforever.client.game;

import com.faforever.client.remote.ClientMessage;
import lombok.Value;

@Value
public class RestoreGameSessionRequest implements ClientMessage {
  int gameId;
}
