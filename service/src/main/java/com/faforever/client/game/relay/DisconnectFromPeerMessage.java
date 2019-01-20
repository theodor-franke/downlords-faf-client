package com.faforever.client.game.relay;

public class DisconnectFromPeerMessage extends GpgMessage {

  private static final int UID_INDEX = 0;

  public DisconnectFromPeerMessage() {
    super("DisconnectFromPeer", 1);
  }

  public int getUid() {
    return getInt(UID_INDEX);
  }

  public void setUid(int uid) {
    setValue(UID_INDEX, uid);
  }
}
