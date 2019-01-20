package com.faforever.client.matchmaking;

import com.faforever.client.remote.ServerMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchMakerInfoServerMessage implements ServerMessage {
  private Map<String, Integer> playersByPool;
}
