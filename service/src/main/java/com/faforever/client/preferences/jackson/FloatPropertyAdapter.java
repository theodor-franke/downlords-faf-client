package com.faforever.client.preferences.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;

import java.io.IOException;

public class FloatPropertyAdapter {
  public static final JsonSerializer<? super FloatProperty> SERIALIZER = new JsonSerializer<>() {
    @Override
    public void serialize(FloatProperty value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
      if (value.getValue() == null) {
        gen.writeNull();
        return;
      }
      gen.writeNumber(value.get());
    }
  };
  public static final JsonDeserializer<? extends FloatProperty> DESERIALIZER = new JsonDeserializer<>() {
    @Override
    public FloatProperty deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      return new SimpleFloatProperty(p.getFloatValue());
    }
  };
}
