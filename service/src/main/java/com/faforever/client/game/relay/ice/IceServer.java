package com.faforever.client.game.relay.ice;

import lombok.Data;

import java.net.URL;
import java.util.List;

@Data
public class IceServer {
  private final List<URL> urls;
  private final String username;
  private final String credential;
  private final String credentialType;
}
