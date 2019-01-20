package com.faforever.client.game;

import com.faforever.client.remote.ClientMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.supcomhub.server.protocol.v2.dto.client.PlayerGameState;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameStateReport implements ClientMessage {

  private PlayerGameState state;
}
