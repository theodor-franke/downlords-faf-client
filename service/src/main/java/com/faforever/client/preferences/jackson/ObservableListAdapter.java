package com.faforever.client.preferences.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.List;

public class ObservableListAdapter {

  public static final JsonDeserializer<? extends ObservableList<?>> DESERIALIZER = new JsonDeserializer<>() {
    @Override
    public ObservableList<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      //noinspection unchecked
      return FXCollections.observableList(ctxt.readValue(p, List.class));
    }
  };
}
