package com.faforever.client.preferences.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

import java.io.IOException;
import java.util.Set;

public class ObservableSetAdapter {

  public static final JsonDeserializer<? extends ObservableSet<?>> DESERIALIZER = new JsonDeserializer<>() {
    @Override
    public ObservableSet<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      //noinspection unchecked
      return FXCollections.observableSet(ctxt.readValue(p, Set.class));
    }
  };
}
