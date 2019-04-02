package com.faforever.client.preferences.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.type.CollectionType;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;

import java.io.IOException;

public class ListPropertyDeserializer extends StdDeserializer<ListProperty<?>> implements ContextualDeserializer {

  public static final ListPropertyDeserializer INSTANCE = new ListPropertyDeserializer();

  private ListPropertyDeserializer() {
    super(ObservableSet.class);
  }

  @Override
  public ListProperty<?> deserialize(JsonParser p, DeserializationContext ctxt) {
    throw new UnsupportedOperationException("Not sure how to implement this");
  }

  @Override
  public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
    JavaType targetType = ctxt.getContextualType();

    return new StdDeserializer<>(targetType) {
      @Override
      public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        CollectionType type = ctxt.getTypeFactory().constructCollectionType(ObservableSet.class, targetType.getContentType());
        return new SimpleListProperty<>((ObservableList<?>) p.getCodec().readValue(p, type));
      }
    };
  }
}
