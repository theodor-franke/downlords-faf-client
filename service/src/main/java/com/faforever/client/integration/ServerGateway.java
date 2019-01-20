package com.faforever.client.integration;

import com.faforever.client.remote.ClientMessage;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

/**
 * A Spring Integration gateway to send messages to the server.
 */
@MessagingGateway
public interface ServerGateway {

  /**
   * Sends the specified message to the specified client connection.
   */
  @Gateway(requestChannel = ChannelNames.SERVER_OUTBOUND)
  void send(ClientMessage clientMessage);
}
