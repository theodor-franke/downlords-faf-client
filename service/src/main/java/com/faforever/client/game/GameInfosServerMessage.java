package com.faforever.client.game;

import com.faforever.client.remote.ServerMessage;
import lombok.Data;

import java.util.List;

@Data
public class GameInfosServerMessage implements ServerMessage {
  List<GameInfoServerMessage> games;
}
