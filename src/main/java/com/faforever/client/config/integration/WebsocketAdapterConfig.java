package com.faforever.client.config.integration;

import com.faforever.client.integration.ChannelNames;
import com.faforever.client.integration.Protocol;
import com.faforever.client.player.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.websocket.IntegrationWebSocketContainer;
import org.springframework.integration.websocket.inbound.WebSocketInboundChannelAdapter;
import org.springframework.integration.websocket.outbound.WebSocketOutboundMessageHandler;
import org.springframework.integration.websocket.support.PassThruSubProtocolHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import java.security.Principal;

@Configuration
@Slf4j
public class WebsocketAdapterConfig {

  /**
   * TCP inbound adapter that accepts connections and messages from clients.
   */
  @Bean
  public WebSocketOutboundMessageHandler webSocketOutboundMessageHandler(IntegrationWebSocketContainer serverWebSocketContainer) {
    return new WebSocketOutboundMessageHandler(serverWebSocketContainer);
  }

  /**
   * Integration flow that reads from the websocket adapter and transforms protocol messages into internal messages.
   */
  @Bean
  public IntegrationFlow webSocketAdapterInboundFlow(WebSocketInboundChannelAdapter webSocketInboundChannelAdapter, V2ClientMessageTransformer v2ClientMessageTransformer) {
    return IntegrationFlows
      .from(webSocketInboundChannelAdapter)
      .transform(v2ClientMessageTransformer)
      .channel(ChannelNames.SERVER_INBOUND)
      .get();
  }

  /**
   * Integration flow that converts an internal message into the legacy message format and sends it back to the original
   * client.
   */
  @Bean
  public IntegrationFlow webSocketAdapterOutboundFlow(WebSocketOutboundMessageHandler webSocketOutboundMessageHandler, V2ServerMessageTransformer v2ServerMessageTransformer) {
    return IntegrationFlows
      .from(ChannelNames.WEB_OUTBOUND)
      .transform(v2ServerMessageTransformer)
      // Handle each message in a single task so that one failing message does not prevent others from being sent.
      // A message may fail if the receiving client is no longer connected
      .channel(ChannelNames.WEB_SOCKET_OUTBOUND)
      .handle(webSocketOutboundMessageHandler)
      .get();
  }

  @EventListener
  public void onCloseConnection(CloseConnectionEvent event) {
    if (event.getClientConnection().getProtocol() == Protocol.V2_JSON_UTF_8) {
      // FIXME implement
      log.warn("Closing websocket connections has not yet been implemented");
    }
  }

  private static class FafSubProtocolHandler extends PassThruSubProtocolHandler {

    private final PlayerService playerService;

    private FafSubProtocolHandler(PlayerService playerService) {
      this.playerService = playerService;

      setSupportedProtocols(Protocol.V2_JSON_UTF_8.name());
    }

    @Override
    public void afterSessionStarted(WebSocketSession session, MessageChannel outputChannel) {
      Principal sessionPrincipal = session.getPrincipal();

      extractClientDetailsOrNull(sessionPrincipal)
        .ifPresent(clientDetails -> clientDetails.setClientConnection(clientConnection));
    }

    @Override
    public void afterSessionEnded(WebSocketSession session, CloseStatus closeStatus, MessageChannel outputChannel) {
      clientConnectionService.removeConnection(session.getId(), Protocol.V2_JSON_UTF_8);
    }
  }
}
