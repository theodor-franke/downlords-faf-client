package com.faforever.client.integration;

import com.faforever.client.remote.ClientMessage;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.stereotype.Component;

/**
 * A Spring Integration gateway to send messages to the server.
 */
@MessagingGateway
@IntegrationComponentScan
@Component
public interface ServerGateway {

  /**
   * Sends the specified message to the specified client connection.
   */
  @Gateway(requestChannel = ChannelNames.SERVER_OUTBOUND)
  void send(ClientMessage clientMessage);
}
