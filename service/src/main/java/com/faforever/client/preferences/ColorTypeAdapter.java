package com.faforever.client.preferences;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import javafx.scene.paint.Color;
import lombok.experimental.UtilityClass;

import java.io.IOException;

@UtilityClass
public class ColorTypeAdapter {

  public static final JsonSerializer<? super Color> SERIALIZER = new JsonSerializer<>() {
    @Override
    public void serialize(Color value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
      if (value == null) {
        gen.writeNull();
      } else {
        gen.writeString(value.toString());
      }
    }
  };

  public final JsonDeserializer<Color> DESERIALIZER = new JsonDeserializer<>() {
    @Override
    public Color deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      return Color.web(p.getValueAsString());
    }
  };
}
