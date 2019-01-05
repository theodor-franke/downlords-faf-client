package com.faforever.client.game;

import com.faforever.client.mod.FeaturedMod;
import com.faforever.client.remote.ClientMessage;
import lombok.Value;

import java.util.Set;

@Value
public class HostGameRequest implements ClientMessage {
  String title;
  String password;
  FeaturedMod featuredMod;
  String map;
  Set<String> simMods;
  GameVisibility gameVisibility;
}
