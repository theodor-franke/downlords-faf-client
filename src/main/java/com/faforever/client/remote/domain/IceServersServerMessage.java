package com.faforever.client.remote.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Message sent from the server to the client containing a list of ICE servers to use.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class IceServersServerMessage extends FafServerMessage {

  @JsonProperty("ice_servers")
  private List<IceServer> iceServers;

  @Data
  public static class IceServer {
    private String url;
    private String[] urls;
    private String credential;
    private String credentialType;
    private String username;
  }
}
