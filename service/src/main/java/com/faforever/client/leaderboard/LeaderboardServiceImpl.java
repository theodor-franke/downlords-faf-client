package com.faforever.client.leaderboard;

import com.faforever.client.SpringProfiles;
import com.faforever.client.remote.FafService;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Lazy
@Service
@Profile("!" + SpringProfiles.PROFILE_OFFLINE)
public class LeaderboardServiceImpl implements LeaderboardService {

  private final FafService fafService;

  public LeaderboardServiceImpl(FafService fafService) {
    this.fafService = fafService;
  }

  @Override
  public CompletableFuture<List<RatingStat>> getLeaderboardStats(String leaderboardName) {
    return fafService.getLeaderboard(leaderboardName).thenApply(this::toRankStats);
  }

  private List<RatingStat> toRankStats(List<LeaderboardEntry> entries) {
    Map<Integer, Long> totalCount = countByRank(entries.stream());
    Map<Integer, Long> countWithoutFewGames = countByRank(entries.stream()
      .filter(entry -> entry.gamesPlayedProperty().get() >= MINIMUM_GAMES_PLAYED_TO_BE_SHOWN));

    return totalCount.entrySet().stream()
      .map(entry -> new RatingStat(
        entry.getKey(),
        entry.getValue().intValue(),
        countWithoutFewGames.getOrDefault(entry.getKey(), 0L).intValue()))
      .collect(Collectors.toList());
  }

  private Map<Integer, Long> countByRank(Stream<LeaderboardEntry> entries) {
    return entries.collect(Collectors.groupingBy(LeaderboardEntry::getRank, Collectors.counting()));
  }

  @Override
  public CompletableFuture<LeaderboardEntry> getEntryForPlayer(int playerId, String leaderboardName) {
    return fafService.getLeaderboardEntryForPlayer(playerId, leaderboardName);
  }

  @Override
  public CompletableFuture<List<LeaderboardEntry>> getEntries(String leaderboardName) {
    return fafService.getLeaderboard(leaderboardName);
  }
}
