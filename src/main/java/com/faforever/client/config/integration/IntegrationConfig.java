package com.faforever.client.config.integration;

import com.faforever.client.integration.ChannelNames;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.IntegrationMessageHeaderAccessor;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.router.PayloadTypeRouter;

@Configuration
@IntegrationComponentScan("com.faforever.server.integration")
public class IntegrationConfig {

  /**
   * Reads messages from the standard server inbound channel. Since messages are resequenced, all messages must specify
   * the header {@link IntegrationMessageHeaderAccessor#SEQUENCE_NUMBER}.
   */
  @Bean
  public IntegrationFlow inboundFlow() {
    return IntegrationFlows
      .from(ChannelNames.SERVER_INBOUND)
      // FIXME websocket messages don't have a sequence number yet
//      .resequence(spec -> spec.releasePartialSequences(true))
      .channel(ChannelNames.INBOUND_DISPATCH)
      .get();
  }

  @Bean
  public IntegrationFlow dispatchFlow() {
    return IntegrationFlows
      .from(ChannelNames.INBOUND_DISPATCH)
      .route(inboundRouter())
      .get();
  }

  /**
   * Reads messages from the server outbound channel and sends it to the target adapter's channel.
   */
  @Bean
  public IntegrationFlow outboundFlow() {
    return IntegrationFlows
      .from(ChannelNames.SERVER_OUTBOUND)
      .channel(ChannelNames.WEB_OUTBOUND)
      .get();
  }

  /**
   * Routes request messages to their corresponding request channel.
   */
  private PayloadTypeRouter inboundRouter() {
    PayloadTypeRouter router = new PayloadTypeRouter();
    // FIXME implement
    return router;
  }
}
