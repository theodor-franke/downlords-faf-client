package com.faforever.client.preferences.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.io.IOException;
import java.util.List;

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

  public static final JsonDeserializer<? extends ListProperty<?>> DESERIALIZER = new JsonDeserializer<>() {
    @Override
    public ListProperty<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      //noinspection unchecked
      return new SimpleListProperty<>(FXCollections.observableList(ctxt.readValue(p, List.class)));
    }
  };
}
