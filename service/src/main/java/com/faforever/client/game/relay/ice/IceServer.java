package com.faforever.client.game.relay.ice;

import lombok.Data;

import java.net.URI;

@Data
public class IceServer {
  private final URI url;
  private final String username;
  private final String credential;
  private final String credentialType;
}
