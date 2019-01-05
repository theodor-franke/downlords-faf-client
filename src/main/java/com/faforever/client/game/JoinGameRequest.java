package com.faforever.client.game;

import com.faforever.client.remote.ClientMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinGameRequest implements ClientMessage {
  private int id;
  private String password;
}
