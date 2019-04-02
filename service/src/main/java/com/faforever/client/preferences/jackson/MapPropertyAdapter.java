package com.faforever.client.preferences.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import javafx.beans.property.MapProperty;

import java.io.IOException;

public class MapPropertyAdapter {
  public static final JsonSerializer<? super MapProperty> SERIALIZER = new JsonSerializer<>() {
    @Override
    public void serialize(MapProperty value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
      if (value.get() == null) {
        gen.writeNull();
        return;
      }
      gen.writeObject(value.get());
    }
  };
}
