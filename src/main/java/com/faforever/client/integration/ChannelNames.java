package com.faforever.client.integration;

import com.faforever.client.remote.ClientMessage;
import com.faforever.client.remote.ServerMessage;

/**
 * Holds the names of all channel. A channel's name is also the name of its bean. Channels can be configured in {@link
 * com.faforever.client.config.integration.ChannelConfiguration}.
 */
public final class ChannelNames {

  /**
   * Channel for single-recipient outbound client messages. The payload of messages in this channel is {@link
   * ServerMessage}.
   */
  public static final String SERVER_OUTBOUND = "serverOutbound";

  /**
   * Channel for inbound server messages. The payload of messages in this channel is {@link ClientMessage}.
   */
  public static final String SERVER_INBOUND = "serverInbound";

  /**
   * Channel for outbound messages over the standard protocol.
   */
  public static final String WEB_OUTBOUND = "webOutbound";

  /**
   * Channel for all messages that need to be processed, no matter whether they are coming from an external system or
   * from the server itself. This is required to make sure that all actions are processed in a serial order no matter
   * where they originate from.
   */
  public static final String INBOUND_DISPATCH = "inboundDispatch";

  /**
   * Channel for {@link ServerDisconnectedEvent}.
   */
  public static final String SERVER_DISCONNECTED_EVENT = "serverDisconnectedEvent";

  /**
   * Channel for raw outbound messages to be written on the web socket.
   */
  public static final String WEB_SOCKET_OUTBOUND = "webSocketOutbound";

}
