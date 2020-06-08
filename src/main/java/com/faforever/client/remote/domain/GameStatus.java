package com.faforever.client.remote.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum GameStatus {

  UNKNOWN("unknown"),
  PLAYING("playing"),
  OPEN("open"),
  CLOSED("closed");

  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private static final Map<String, GameStatus> fromString;

  static {
    fromString = new HashMap<>();
    for (GameStatus gameStatus : values()) {
      fromString.put(gameStatus.string, gameStatus);
    }
  }

  private final String string;

  GameStatus(String string) {
    this.string = string;
  }

  @JsonCreator
  public static GameStatus fromString(String string) {
    GameStatus gameStatus = fromString.get(string != null ? string.toLowerCase(Locale.US) : null);
    if (gameStatus == null) {
      logger.warn("Unknown game state: {}", string);
      return UNKNOWN;
    }
    return gameStatus;
  }

  @JsonValue
  public String getString() {
    return string;
  }
}
