package com.faforever.client.game;

import com.faforever.client.remote.ServerMessage;
import lombok.Value;

import java.util.Map;

@Value
public class MatchAvailableNotification implements ServerMessage {
  Map<String, Integer> poolNameToSearchers;
}
