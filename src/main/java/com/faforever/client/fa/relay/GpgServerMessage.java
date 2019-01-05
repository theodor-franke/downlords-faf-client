package com.faforever.client.fa.relay;


import com.faforever.client.remote.ServerMessage;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a message received from the relay server (deserialized from JSON).
 */
public class GpgServerMessage implements ServerMessage {

  @Getter
  private GpgServerMessageType command;
  private List<Object> args;

  protected GpgServerMessage(GpgServerMessageType command, int numberOfArgs) {
    this.command = command;
    this.args = new ArrayList<>(Collections.nCopies(numberOfArgs, null));
  }

  protected void setValue(int index, Object value) {
    args.set(index, value);
  }

  protected int getInt(int index) {
    return ((Number) args.get(index)).intValue();
  }

  protected boolean getBoolean(int index) {
    return (boolean) args.get(index);
  }

  protected String getString(int index) {
    return ((String) args.get(index));
  }

  @SuppressWarnings("unchecked")
  protected <T> T getObject(int index) {
    return (T) args.get(index);
  }
}
