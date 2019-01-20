package com.faforever.client.game;

import com.faforever.client.remote.ServerMessage;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
public class GameInfoServerMessage implements ServerMessage {
  private int id;
  private String title;
  private GameVisibility gameVisibility;
  private boolean passwordProtected;
  private GameState state;
  private String mod;
  private String leaderboardName;
  private List<SimMod> simMods;
  private String map;
  private Player host;
  private List<Player> players;
  private int maxPlayers;
  private Instant startTime;
  private Integer minRank;
  private Integer maxRank;
  private int modVersion;

  @Data
  public static class Player {
    int id;
    int team;
    String name;
  }

  @Data
  public static class SimMod {
    private UUID uuid;
    private String displayName;
  }
}
