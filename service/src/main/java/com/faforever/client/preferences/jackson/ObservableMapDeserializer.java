package com.faforever.client.preferences.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.type.MapType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.io.IOException;
import java.util.Map;

public class ObservableMapDeserializer extends StdDeserializer<ObservableMap<?, ?>> implements ContextualDeserializer {

  public static final ObservableMapDeserializer INSTANCE = new ObservableMapDeserializer();

  private ObservableMapDeserializer() {
    super(ObservableMap.class);
  }

  @Override
  public ObservableMap<?, ?> deserialize(JsonParser p, DeserializationContext ctxt) {
    throw new UnsupportedOperationException("Not sure how to implement this");
  }

  @Override
  public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
    JavaType targetType = ctxt.getContextualType();

    return new StdDeserializer<>(targetType) {
      @Override
      public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        MapType type = ctxt.getTypeFactory().constructMapType(Map.class, targetType.getKeyType(), targetType.getContentType());
        return FXCollections.observableMap((Map<?, ?>) p.getCodec().readValue(p, type));
      }
    };
  }
}
