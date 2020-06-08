package com.faforever.client.preferences.gson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import javafx.beans.property.*;
import javafx.beans.value.WritableObjectValue;
import javafx.collections.FXCollections;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@JsonComponent
public class PropertyTypeAdapter {

  @SuppressWarnings("unused")
  public static class Serializer extends JsonSerializer<Property<?>> {
    @Override
    public void serialize(Property<?> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
      if (value.getValue() == null) {
        gen.writeNull();
      }
      if (value instanceof StringProperty) {
        gen.writeString(((StringProperty) value).get());
      }
      if (value instanceof IntegerProperty) {
        gen.writeNumber(((IntegerProperty) value).get());
      }
      if (value instanceof DoubleProperty) {
        gen.writeNumber(((DoubleProperty) value).get());
      }
      if (value instanceof LongProperty) {
        gen.writeNumber(((LongProperty) value).get());
      }
      if (value instanceof FloatProperty) {
        gen.writeNumber(((FloatProperty) value).get());
      }
      if (value instanceof BooleanProperty) {
        gen.writeBoolean(((BooleanProperty) value).get());
      }
      if (value instanceof WritableObjectValue) {
        gen.writeObject(((WritableObjectValue<?>) value).get());
      }

      throw new IllegalStateException("Unhandled object type: " + value.getClass());
    }
  }

  @SuppressWarnings("unused")
  public static class Deserializer extends JsonDeserializer<Property<?>> {

    @Override
    public Property<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      JavaType contextualType = ctxt.getContextualType();

      Class<?> clazz = contextualType.getRawClass();
      if (StringProperty.class.isAssignableFrom(clazz)) {
        return new SimpleStringProperty(p.getValueAsString());
      }
      if (IntegerProperty.class.isAssignableFrom(clazz)) {
        return new SimpleIntegerProperty(p.getValueAsInt());
      }
      if (DoubleProperty.class.isAssignableFrom(clazz)) {
        return new SimpleDoubleProperty(p.getDoubleValue());
      }
      if (LongProperty.class.isAssignableFrom(clazz)) {
        return new SimpleLongProperty(p.getValueAsLong());
      }
      if (FloatProperty.class.isAssignableFrom(clazz)) {
        return new SimpleFloatProperty(p.getFloatValue());
      }
      if (BooleanProperty.class.isAssignableFrom(clazz)) {
        return new SimpleBooleanProperty(p.getBooleanValue());
      }

      if (contextualType.isCollectionLikeType()) {
        CollectionLikeType collectionLikeType = (CollectionLikeType) contextualType;
        JavaType contentType = collectionLikeType.getContentType();

        if (clazz == ObjectProperty.class) {
          return new SimpleObjectProperty<>(ctxt.readValue(p, contentType));
        } else if (clazz == ListProperty.class) {
          return new SimpleListProperty<>(FXCollections.observableList(
            ctxt.readValue(p, ctxt.getTypeFactory().constructCollectionType(List.class, contentType)))
          );
        } else if (clazz == SetProperty.class) {
          return new SimpleSetProperty<>(FXCollections.observableSet(
            ctxt.<Set<Object>>readValue(p, ctxt.getTypeFactory().constructCollectionType(Set.class, contentType))
          ));
        }
      }

      if (contextualType.isMapLikeType()) {
        MapLikeType mapType = (MapLikeType) contextualType;
        JavaType keyType = mapType.getKeyType();
        JavaType contentType = mapType.getContentType();

        if (clazz == MapProperty.class) {
          return new SimpleMapProperty<>(FXCollections.observableMap(
            ctxt.readValue(p, ctxt.getTypeFactory().constructMapType(Map.class, keyType, contentType)))
          );
        }
      }

      throw new IllegalStateException("Unhandled object type: " + contextualType);
    }
  }
}
