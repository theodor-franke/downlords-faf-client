package com.faforever.client.game;

import lombok.Data;

import java.util.List;

@Data
public class GameInfoMessage {
  List<GameInfoMessage> games;
}
