package com.faforever.client.remote;

import com.faforever.client.game.Faction;
import com.faforever.client.remote.domain.ClientMessage;
import com.faforever.client.remote.gson.FactionTypeAdapter;

public class ClientMessageSerializer extends JsonMessageSerializer<ClientMessage> {

  @Override
  protected void addTypeAdapters(GsonBuilder gsonBuilder) {
        .registerTypeAdapter(Faction.class, FactionTypeAdapter.INSTANCE)
  }
}
