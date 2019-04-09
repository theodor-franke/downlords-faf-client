package com.faforever.client.config.integration;

import com.faforever.client.integration.ChannelNames;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.messaging.MessageChannel;

import java.util.concurrent.Executors;

/**
 * Creates Spring Integration channels. Bean names must match their entry in {@link
 * com.faforever.client.integration.ChannelNames ChannelNames}. Channels that aren't explicitly configured here will be
 * implicitly instantiated as {@link DirectChannel} by Spring.
 */
@Configuration
public class ChannelConfiguration {

  /**
   * Channel that receives {@link com.faforever.client.remote.ServerMessage ServerMessages} created by server adapters.
   */
  @Bean(name = ChannelNames.SERVER_INBOUND)
  public MessageChannel serverInbound() {
    return MessageChannels.direct()
      .get();
  }

  /**
   * Takes all messages to be processed by the client (no matter whether produced by internal events or incoming
   * messages) and schedules them to be processed by a single thread.
   *
   * @see ChannelNames#INBOUND_DISPATCH
   */
  @Bean(name = ChannelNames.INBOUND_DISPATCH)
  public MessageChannel inboundDispatch() {
    return MessageChannels
      .executor(Executors.newSingleThreadExecutor(runnable -> new Thread(runnable, "inbound-dispatch")))
      .get();
  }

  /**
   * Takes all {@link com.faforever.client.remote.ServerMessage ServerMessages} to be received by the server and
   * schedules them to be sent by a single thread.
   */
  @Bean(name = ChannelNames.SERVER_OUTBOUND)
  public MessageChannel serverOutbound() {
    return MessageChannels
      .executor(Executors.newSingleThreadExecutor(runnable -> new Thread(runnable, "server-outbound")))
      .get();
  }
}
