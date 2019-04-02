package com.faforever.client.preferences.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.type.CollectionType;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;

import java.io.IOException;

public class MapPropertyDeserializer extends StdDeserializer<MapProperty<?, ?>> implements ContextualDeserializer {

  public static final MapPropertyDeserializer INSTANCE = new MapPropertyDeserializer();

  private MapPropertyDeserializer() {
    super(ObservableSet.class);
  }

  @Override
  public MapProperty<?, ?> deserialize(JsonParser p, DeserializationContext ctxt) {
    throw new UnsupportedOperationException("Not sure how to implement this");
  }

  @Override
  public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
    JavaType targetType = ctxt.getContextualType();

    return new StdDeserializer<>(targetType) {
      @Override
      public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        CollectionType type = ctxt.getTypeFactory().constructCollectionType(ObservableSet.class, targetType.getContentType());
        return new SimpleMapProperty<>((ObservableMap<?, ?>) p.getCodec().readValue(p, type));
      }
    };
  }
}
