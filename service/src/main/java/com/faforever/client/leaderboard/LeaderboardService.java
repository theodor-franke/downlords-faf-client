package com.faforever.client.leaderboard;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface LeaderboardService {
  int MINIMUM_GAMES_PLAYED_TO_BE_SHOWN = 10;

  CompletableFuture<List<RatingStat>> getLeaderboardStats(String leaderboardName);

  CompletableFuture<LeaderboardEntry> getEntryForPlayer(int playerId, String leaderboardName);

  CompletableFuture<List<LeaderboardEntry>> getEntries(String leaderboardName);
}
