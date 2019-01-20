package com.faforever.client.preferences;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.WritableObjectValue;
import javafx.collections.FXCollections;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

@UtilityClass
public class PropertyTypeAdapter {

  public final JsonSerializer<Property> SERIALIZER = new JsonSerializer<>() {
    @Override
    public void serialize(Property value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
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
        gen.writeObject(((WritableObjectValue) value).get());
      }

      throw new IllegalStateException("Unhandled object type: " + value.getClass());
    }
  };

  public static final JsonDeserializer<Property> DESERIALIZER = new JsonDeserializer<>() {
    @Override
    public Property deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      JavaType contextualType = ctxt.getContextualType();

      if (contextualType.isConcrete()) {
        Class<?> clazz = contextualType.getRawClass();
        if (StringProperty.class.isAssignableFrom(clazz)) {
          return new SimpleStringProperty(p.getValueAsString());
        }
        if (IntegerProperty.class.isAssignableFrom(clazz)) {
          return new SimpleIntegerProperty(p.getIntValue());
        }
        if (DoubleProperty.class.isAssignableFrom(clazz)) {
          return new SimpleDoubleProperty(p.getDoubleValue());
        }
        if (LongProperty.class.isAssignableFrom(clazz)) {
          return new SimpleLongProperty(p.getLongValue());
        }
        if (FloatProperty.class.isAssignableFrom(clazz)) {
          return new SimpleFloatProperty(p.getFloatValue());
        }
        if (BooleanProperty.class.isAssignableFrom(clazz)) {
          return new SimpleBooleanProperty(p.getBooleanValue());
        }
        if (SetProperty.class.isAssignableFrom(clazz)) {
          //noinspection unchecked
          return new SimpleSetProperty<>(FXCollections.observableSet(ctxt.readValue(p, Set.class)));
        }
        if (MapProperty.class.isAssignableFrom(clazz)) {
          //noinspection unchecked
          return new SimpleMapProperty<>(FXCollections.observableMap(ctxt.readValue(p, Map.class)));
        }
      }

      if (contextualType.hasGenericTypes()) {
        ParameterizedType parameterizedType = (ParameterizedType) contextualType;
        Type rawType = parameterizedType.getRawType();

        if (rawType == ObjectProperty.class) {
          return new SimpleObjectProperty<>(ctxt.readValue(p, contextualType.getContentType()));
        } else if (rawType == ListProperty.class) {
          CollectionType type = ctxt.getTypeFactory().constructCollectionType(List.class, contextualType.getContentType());
          return new SimpleListProperty<>(FXCollections.observableList(ctxt.readValue(p, type)));
        } else if (rawType == SetProperty.class) {
          CollectionType type = ctxt.getTypeFactory().constructCollectionType(Set.class, contextualType.getContentType());
          return new SimpleSetProperty<>(FXCollections.observableSet(ctxt.<Set<Object>>readValue(p, type)));
        } else if (rawType == MapProperty.class) {
          MapType type = ctxt.getTypeFactory().constructMapType(Map.class, contextualType.getKeyType(), contextualType.getContentType());
          return new SimpleMapProperty<>(FXCollections.observableMap(ctxt.readValue(p, type)));
        }
      }

      throw new IllegalStateException("Unhandled object type: " + contextualType);
    }
  };
}
