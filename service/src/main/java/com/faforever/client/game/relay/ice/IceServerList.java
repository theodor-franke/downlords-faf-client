package com.faforever.client.game.relay.ice;

import com.faforever.client.remote.ServerMessage;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class IceServerList implements ServerMessage {
  private final int ttlSeconds;
  private final Instant createdAt;
  private final List<IceServer> servers;
}
