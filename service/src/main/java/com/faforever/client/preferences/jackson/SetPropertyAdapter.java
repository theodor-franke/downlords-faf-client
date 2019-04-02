package com.faforever.client.preferences.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import javafx.beans.property.SetProperty;

import java.io.IOException;

public class SetPropertyAdapter {

  public static final JsonSerializer<? super SetProperty> SERIALIZER = new JsonSerializer<>() {
    @Override
    public void serialize(SetProperty value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
      if (value.get() == null) {
        gen.writeNull();
      }
      gen.writeObject(value.get());
    }
  };
}
