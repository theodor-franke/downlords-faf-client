package com.faforever.client.preferences.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;

import java.io.IOException;
import java.util.Set;

public class SetPropertyAdapter {
  public static final JsonSerializer<? super SetProperty> SERIALIZER = new JsonSerializer<>() {
    @Override
    public void serialize(SetProperty value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
      if (value.get() == null) {
        gen.writeNull();
      }
      gen.writeObject(value.get());
    }
  };

  public static final JsonDeserializer<? extends SetProperty<?>> DESERIALIZER = new JsonDeserializer<>() {
    @Override
    public SetProperty<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      //noinspection unchecked
      return new SimpleSetProperty<>(FXCollections.observableSet(ctxt.readValue(p, Set.class)));
    }
  };
}
