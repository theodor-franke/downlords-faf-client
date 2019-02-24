package com.faforever.client.preferences.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.IOException;

public class IntegerPropertyAdapter {
  public static final JsonSerializer<? super IntegerProperty> SERIALIZER = new JsonSerializer<>() {
    @Override
    public void serialize(IntegerProperty value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
      if (value.getValue() == null) {
        gen.writeNull();
        return;
      }
      gen.writeNumber(value.get());
    }
  };
  public static final JsonDeserializer<? extends IntegerProperty> DESERIALIZER = new JsonDeserializer<>() {
    @Override
    public IntegerProperty deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      return new SimpleIntegerProperty(p.getIntValue());
    }
  };
}
