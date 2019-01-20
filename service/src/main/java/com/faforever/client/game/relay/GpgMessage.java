package com.faforever.client.game.relay;


import com.faforever.client.remote.ClientMessage;
import com.faforever.client.remote.ServerMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a message received from either the game or the server.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GpgMessage implements ServerMessage, ClientMessage {

  private String command;
  private List<Object> args;

  protected GpgMessage(String command, int numberOfArgs) {
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
