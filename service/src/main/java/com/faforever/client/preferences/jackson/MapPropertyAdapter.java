package com.faforever.client.preferences.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;

import java.io.IOException;
import java.util.Map;

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

  public static final JsonDeserializer<? extends MapProperty<?, ?>> DESERIALIZER = new JsonDeserializer<>() {
    @Override
    public MapProperty<?, ?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      //noinspection unchecked
      return new SimpleMapProperty<>(FXCollections.observableMap(ctxt.readValue(p, Map.class)));
    }
  };
}
