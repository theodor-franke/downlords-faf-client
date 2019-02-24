package com.faforever.client.preferences.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.io.IOException;
import java.util.Map;

public class ObservableMapAdapter {

  public static final JsonDeserializer<? extends ObservableMap<?, ?>> DESERIALIZER = new JsonDeserializer<>() {
    @Override
    public ObservableMap<?, ?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      //noinspection unchecked
      return FXCollections.observableMap(ctxt.readValue(p, Map.class));
    }
  };
}
