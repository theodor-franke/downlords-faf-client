package com.faforever.client.preferences.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.io.IOException;

public class DoublePropertyAdapter {
  public static final JsonSerializer<? super DoubleProperty> SERIALIZER = new JsonSerializer<>() {
    @Override
    public void serialize(DoubleProperty value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
      if (value.getValue() == null) {
        gen.writeNull();
        return;
      }
      gen.writeNumber(value.get());
    }
  };
  public static final JsonDeserializer<? extends DoubleProperty> DESERIALIZER = new JsonDeserializer<>() {
    @Override
    public DoubleProperty deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      return new SimpleDoubleProperty(p.getDoubleValue());
    }
  };
}
