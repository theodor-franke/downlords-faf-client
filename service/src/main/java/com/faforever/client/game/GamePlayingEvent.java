package com.faforever.client.game;

import lombok.Value;

/** Published whenever a game changed its state to {@link com.faforever.client.game.GameState#PLAYING}. */
@Value
public class GamePlayingEvent {
  Game game;
}
