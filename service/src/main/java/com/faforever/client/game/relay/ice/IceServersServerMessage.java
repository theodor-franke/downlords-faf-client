package com.faforever.client.game.relay.ice;

import com.faforever.client.remote.ServerMessage;
import lombok.Data;

import java.net.URL;
import java.util.List;

@Data
public class IceServersServerMessage implements ServerMessage {

  private List<IceServer> iceServers;

  @Data
  public static class IceServer {
    private List<URL> urls;
    private String credential;
    private String credentialType;
    private String username;
  }
}
