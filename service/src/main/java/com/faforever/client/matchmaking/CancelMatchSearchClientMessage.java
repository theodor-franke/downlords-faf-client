package com.faforever.client.matchmaking;

import com.faforever.client.remote.ClientMessage;
import lombok.Value;

@Value
public class CancelMatchSearchClientMessage implements ClientMessage {
  String pool;
}
