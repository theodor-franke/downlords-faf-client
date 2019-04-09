package com.faforever.client.integration;

import lombok.experimental.UtilityClass;

/**
 * Holds the names of all channel. A channel's name is also the name of its bean. Channels can be configured in {@link
 * com.faforever.client.config.integration.ChannelConfiguration}.
 */
@UtilityClass
public class ChannelNames {

  /**
   * Channel for messages sent from the client to the server. The payload of messages in this channel is {@link
   * com.faforever.client.remote.ClientMessage}.
   */
  public static final String SERVER_OUTBOUND = "serverOutbound";

  /**
   * Channel for messages sent from the server to the client. The payload of messages in this channel is {@link
   * com.faforever.client.remote.ServerMessage}.
   */
  public static final String SERVER_INBOUND = "serverInbound";

  /**
   * Channel for all messages that need to be processed, no matter whether they are coming from an external system or
   * from the server itself. This is required to make sure that all actions are processed in a serial order no matter
   * where they originate from.
   */
  public static final String INBOUND_DISPATCH = "inboundDispatch";
}
