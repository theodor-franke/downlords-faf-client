package com.faforever.client.api;

import com.faforever.client.leaderboard.LeaderboardEntry;

public class LeaderboardEntryBeanBuilder {

  private LeaderboardEntry leaderboardEntry;

  private LeaderboardEntryBeanBuilder() {
    leaderboardEntry = new LeaderboardEntry();
  }

  public static LeaderboardEntryBeanBuilder create() {
    return new LeaderboardEntryBeanBuilder();
  }

  LeaderboardEntryBeanBuilder username(String username) {
    leaderboardEntry.setPlayerName(username);
    return this;
  }

  public LeaderboardEntryBeanBuilder defaultValues() {
    return this;
  }

  public LeaderboardEntry get() {
    return leaderboardEntry;
  }
}
