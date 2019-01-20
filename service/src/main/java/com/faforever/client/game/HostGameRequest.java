package com.faforever.client.game;

import com.faforever.client.mod.FeaturedMod;
import com.faforever.client.remote.ClientMessage;
import lombok.Value;

import java.util.Set;
import java.util.UUID;

/** Sent from the client to the server to request hosting a game. */
@Value
public class HostGameRequest implements ClientMessage {
  String title;
  String password;
  FeaturedMod featuredMod;
  String mapName;
  Set<UUID> simMods;
  GameVisibility gameVisibility;
  Integer minRank;
  Integer maxRank;
}
