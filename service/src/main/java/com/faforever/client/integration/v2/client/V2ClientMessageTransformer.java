package com.faforever.client.integration.v2.client;

import com.faforever.client.remote.ClientMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.stereotype.Component;
import org.supcomhub.server.protocol.v2.dto.client.V2ClientMessage;
import org.supcomhub.server.protocol.v2.dto.client.V2ClientMessageWrapper;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Transforms messages from the v2 protocol to internal client message objects.
 */
@Slf4j
@Component
public class V2ClientMessageTransformer implements GenericTransformer<String, ClientMessage> {

  private final ObjectMapper objectMapper;
  private final V2ClientMessageMapper v2ClientMessageMapper;
  private final Map<Class<?>, Method> mapperMethods;

  public V2ClientMessageTransformer(ObjectMapper objectMapper, V2ClientMessageMapper v2ClientMessageMapper) {
    this.objectMapper = objectMapper;
    this.v2ClientMessageMapper = v2ClientMessageMapper;

    mapperMethods = Stream.of(V2ClientMessageMapper.class.getDeclaredMethods())
      .filter(method -> method.getParameterCount() == 1)
      .collect(Collectors.toMap(method -> method.getParameterTypes()[0], Function.identity()));
  }

  @Override
  public ClientMessage transform(String source) {
    try {
      V2ClientMessageWrapper wrapper = objectMapper.readValue(source, V2ClientMessageWrapper.class);
      V2ClientMessage message = wrapper.getData();

      Method mappingMethod = mapperMethods.get(message.getClass());

      return (ClientMessage) mappingMethod.invoke(v2ClientMessageMapper, message);
    } catch (IOException | IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }
}
