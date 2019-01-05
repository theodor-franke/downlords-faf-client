package com.faforever.client.remote;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

public class V2ProtocolWebSocketHandler implements org.springframework.web.socket.WebSocketHandler {

  @Override
  public void afterConnectionEstablished(WebSocketSession session) {

  }

  @Override
  public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {

  }

  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception) {

  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {

  }

  @Override
  public boolean supportsPartialMessages() {
    return false;
  }
}
