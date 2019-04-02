package com.faforever.client.preferences.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import javafx.beans.property.ListProperty;

import java.io.IOException;

public class ListPropertyAdapter {
  public static final JsonSerializer<? super ListProperty> SERIALIZER = new JsonSerializer<>() {
    @Override
    public void serialize(ListProperty value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
      if (value.get() == null) {
        gen.writeNull();
        return;
      }
      gen.writeObject(value.get());
    }
  };
}
