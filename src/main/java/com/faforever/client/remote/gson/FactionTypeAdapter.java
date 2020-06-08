package com.faforever.client.remote.gson;

import com.faforever.client.game.Faction;

import java.io.IOException;

public class FactionTypeAdapter extends TypeAdapter<Faction> {

  public static final FactionTypeAdapter INSTANCE = new FactionTypeAdapter();

  private FactionTypeAdapter() {
    // private
  }

  @Override
  public void write(JsonWriter out, Faction value) throws IOException {
    out.value(value.getString());
  }

  @Override
  public Faction read(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return null;
    }
    return Faction.fromString(in.nextString());
  }
}
