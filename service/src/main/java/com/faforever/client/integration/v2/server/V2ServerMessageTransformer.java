package com.faforever.client.integration.v2.server;

import com.faforever.client.remote.ServerMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.stereotype.Component;
import org.supcomhub.server.protocol.v2.dto.server.V2ServerMessage;
import org.supcomhub.server.protocol.v2.dto.server.V2ServerMessageWrapper;

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
public class V2ServerMessageTransformer implements GenericTransformer<String, ServerMessage> {

  private final ObjectMapper objectMapper;
  private final V2ServerMessageMapper v2ServerMessageMapper;
  private final Map<Class<?>, Method> mapperMethods;

  public V2ServerMessageTransformer(ObjectMapper objectMapper, V2ServerMessageMapper v2ServerMessageMapper) {
    this.objectMapper = objectMapper;
    this.v2ServerMessageMapper = v2ServerMessageMapper;

    mapperMethods = Stream.of(V2ServerMessageMapper.class.getDeclaredMethods())
      .filter(method -> method.getParameterCount() == 1)
      .collect(Collectors.toMap(method -> method.getParameterTypes()[0], Function.identity()));
  }

  @Override
  public ServerMessage transform(String source) {
    try {
      V2ServerMessageWrapper wrapper = objectMapper.readValue(source, V2ServerMessageWrapper.class);
      V2ServerMessage message = wrapper.getData();

      Method mappingMethod = mapperMethods.get(message.getClass());

      return (ServerMessage) mappingMethod.invoke(v2ServerMessageMapper, message);
    } catch (IOException | IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }
}
