package com.faforever.client.preferences.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.IOException;

public class StringPropertyAdapter {
  public static final JsonSerializer<? super StringProperty> SERIALIZER = new JsonSerializer<>() {
    @Override
    public void serialize(StringProperty value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
      if (value.getValue() == null) {
        gen.writeNull();
        return;
      }
      gen.writeString(value.get());
    }
  };
  public static final JsonDeserializer<? extends StringProperty> DESERIALIZER = new JsonDeserializer<>() {
    @Override
    public StringProperty deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      return new SimpleStringProperty(p.getValueAsString());
    }
  };
}
