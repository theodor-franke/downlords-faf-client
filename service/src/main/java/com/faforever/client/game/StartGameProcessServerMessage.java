package com.faforever.client.game;

import com.faforever.client.remote.ServerMessage;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

@Data
public class StartGameProcessServerMessage implements ServerMessage {

  /** The technical name of the mod, e.g. "faf". */
  private String mod;

  /** The ID of the game that will be played. */
  private int gameId;

  /**
   * The folder name of the map (e.g. {@code SCMP_001}) to play. Only set in case of server-initiated matches, like
   * leaderboard games.
   */
  @Nullable
  private String map;

  /** In which lobby mode to start the game in. */
  @Nullable
  private LobbyMode lobbyMode;

  /**
   * Selected faction of the player.
   */
  private Faction faction;

  /**
   * Displayed name in the game (this is usually the FAF display name).
   */
  private String name;

  /**
   * The number of connected players required, before the game starts from automatch waiting state. Mandatory for
   * automatch games, otherwise {@code null}.
   */
  @Nullable
  private Integer expectedPlayers;

  /**
   * The players team number. Note: Offset by 1, since team 1 is free-for-all team.
   */
  private int team;

  /**
   * The players position on the map. If {@code null}, the game will use the map default positioning.
   */
  @Nullable
  private Integer mapPosition;

  /**
   * See values for description.
   */
  public enum LobbyMode {

    /**
     * Default lobby where players can select their faction, teams and so on.
     */
    DEFAULT,

    /**
     * The lobby is skipped; the game starts straight away.
     */
    NONE
  }
}
