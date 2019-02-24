package com.faforever.client.preferences.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;

import java.io.IOException;

public class LongPropertyAdapter {
  public static final JsonSerializer<? super LongProperty> SERIALIZER = new JsonSerializer<>() {
    @Override
    public void serialize(LongProperty value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
      if (value.getValue() == null) {
        gen.writeNull();
        return;
      }
      gen.writeNumber(value.get());
    }
  };
  public static final JsonDeserializer<? extends LongProperty> DESERIALIZER = new JsonDeserializer<>() {
    @Override
    public LongProperty deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      return new SimpleLongProperty(p.getLongValue());
    }
  };
}
