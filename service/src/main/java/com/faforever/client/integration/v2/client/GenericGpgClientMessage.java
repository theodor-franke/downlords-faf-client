package com.faforever.client.integration.v2.client;

import lombok.Data;
import org.supcomhub.server.protocol.v2.dto.client.V2GpgClientMessage;

/**
 * Avoids the client from knowing all GPG sent by the game.
 */
@Data
public class GenericGpgClientMessage extends V2GpgClientMessage {

  private Object[] args;

}
