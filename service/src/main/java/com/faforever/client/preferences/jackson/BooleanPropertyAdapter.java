package com.faforever.client.preferences.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.IOException;

public class BooleanPropertyAdapter {
  public static final JsonSerializer<? super BooleanProperty> SERIALIZER = new JsonSerializer<>() {
    @Override
    public void serialize(BooleanProperty value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
      if (value.getValue() == null) {
        gen.writeNull();
        return;
      }
      gen.writeBoolean(value.get());
    }
  };
  public static final JsonDeserializer<? extends BooleanProperty> DESERIALIZER = new JsonDeserializer<>() {
    @Override
    public BooleanProperty deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      return new SimpleBooleanProperty(p.getBooleanValue());
    }
  };
}
