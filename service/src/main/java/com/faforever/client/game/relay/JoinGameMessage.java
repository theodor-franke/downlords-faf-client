package com.faforever.client.game.relay;

public class JoinGameMessage extends GpgMessage implements Cloneable {

  private static final int USERNAME_INDEX = 0;
  private static final int PEER_UID_INDEX = 1;

  public JoinGameMessage() {
    super("JoinGame", 2);
  }

  public String getUsername() {
    return getString(USERNAME_INDEX);
  }

  public void setUsername(String username) {
    setValue(USERNAME_INDEX, username);
  }

  public int getPeerUid() {
    return getInt(PEER_UID_INDEX);
  }
}
