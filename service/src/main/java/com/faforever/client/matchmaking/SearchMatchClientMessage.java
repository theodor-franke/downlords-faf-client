package com.faforever.client.matchmaking;

import com.faforever.client.game.Faction;
import com.faforever.client.remote.ClientMessage;
import lombok.Value;

@Value(staticConstructor = "of")
public class SearchMatchClientMessage implements ClientMessage {
  String pool;
  Faction faction;
}
