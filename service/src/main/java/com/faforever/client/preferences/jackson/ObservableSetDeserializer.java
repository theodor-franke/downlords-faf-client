package com.faforever.client.preferences.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.type.CollectionType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

import java.io.IOException;
import java.util.Set;

public class ObservableSetDeserializer extends StdDeserializer<ObservableSet<?>> implements ContextualDeserializer {

  public static final ObservableSetDeserializer INSTANCE = new ObservableSetDeserializer();

  private ObservableSetDeserializer() {
    super(ObservableSet.class);
  }

  @Override
  public ObservableSet<?> deserialize(JsonParser p, DeserializationContext ctxt) {
    throw new UnsupportedOperationException("Not sure how to implement this");
  }

  @Override
  public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
    JavaType targetType = ctxt.getContextualType();

    return new StdDeserializer<>(targetType) {
      @Override
      public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        CollectionType type = ctxt.getTypeFactory().constructCollectionType(Set.class, targetType.getContentType());
        return FXCollections.observableSet((Set<?>) p.getCodec().readValue(p, type));
      }
    };
  }
}
