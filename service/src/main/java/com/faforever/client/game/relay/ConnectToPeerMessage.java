package com.faforever.client.game.relay;



public class ConnectToPeerMessage extends GpgMessage {

  private static final int USERNAME_INDEX = 0;
  private static final int PEER_UID_INDEX = 1;
  private static final int OFFER_INDEX = 2;

  public ConnectToPeerMessage() {
    super("ConnectToPeer", 3);
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

  public boolean isOffer() {
    return getBoolean(OFFER_INDEX);
  }
}
