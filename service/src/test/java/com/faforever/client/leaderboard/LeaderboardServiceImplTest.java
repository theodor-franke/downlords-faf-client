package com.faforever.client.leaderboard;


import com.faforever.client.remote.FafService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LeaderboardServiceImplTest {

  private static final int PLAYER_ID = 123;
  @Mock
  private FafService fafService;

  private LeaderboardServiceImpl instance;
  private static final String LEADERBOARD_NAME = "ladder1v1";

  @Before
  public void setUp() throws Exception {
    instance = new LeaderboardServiceImpl(fafService);
  }

  @Test
  public void testGetLeaderboardEntries() throws Exception {
    List<LeaderboardEntry> ladder1V1Entries = Collections.emptyList();
    when(fafService.getLeaderboard(LEADERBOARD_NAME)).thenReturn(CompletableFuture.completedFuture(ladder1V1Entries));

    List<LeaderboardEntry> result = instance.getEntries(LEADERBOARD_NAME).toCompletableFuture().get(2, TimeUnit.SECONDS);

    verify(fafService).getLeaderboard(LEADERBOARD_NAME);
    assertThat(result, is(ladder1V1Entries));
  }

  @Test
  public void testGetLadder1v1Stats() throws Exception {
    LeaderboardEntry leaderboardEntry1 = new LeaderboardEntry();
    leaderboardEntry1.setRating(1);
    leaderboardEntry1.setTotalGames(LeaderboardService.MINIMUM_GAMES_PLAYED_TO_BE_SHOWN);

    LeaderboardEntry leaderboardEntry2 = new LeaderboardEntry();
    leaderboardEntry2.setRating(1);
    leaderboardEntry2.setTotalGames(LeaderboardService.MINIMUM_GAMES_PLAYED_TO_BE_SHOWN + 42);

    LeaderboardEntry leaderboardEntry3 = new LeaderboardEntry();
    leaderboardEntry3.setRating(3);
    leaderboardEntry3.setTotalGames(LeaderboardService.MINIMUM_GAMES_PLAYED_TO_BE_SHOWN);

    when(fafService.getLeaderboard(LEADERBOARD_NAME)).thenReturn(CompletableFuture.completedFuture(Arrays.asList(
      leaderboardEntry1, leaderboardEntry2, leaderboardEntry3
    )));

    List<RatingStat> result = instance.getLeaderboardStats(LEADERBOARD_NAME).toCompletableFuture().get(2, TimeUnit.SECONDS);
    verify(fafService).getLeaderboard(LEADERBOARD_NAME);

    result.sort(Comparator.comparingInt(RatingStat::getRating));

    assertThat(result, hasSize(2));
    assertThat(result.get(0).getTotalCount(), is(2));
    assertThat(result.get(0).getCountWithEnoughGamesPlayed(), is(2));
    assertThat(result.get(0).getRating(), is(1));

    assertThat(result.get(1).getTotalCount(), is(1));
    assertThat(result.get(1).getCountWithEnoughGamesPlayed(), is(1));
    assertThat(result.get(1).getRating(), is(3));
  }

  @Test
  public void testStatsOnlyShowsPlayersWithEnoughGamesPlayed() throws Exception {
    LeaderboardEntry leaderboardEntry1 = new LeaderboardEntry();
    leaderboardEntry1.setRating(1);
    leaderboardEntry1.setTotalGames(LeaderboardService.MINIMUM_GAMES_PLAYED_TO_BE_SHOWN);

    LeaderboardEntry leaderboardEntry2 = new LeaderboardEntry();
    leaderboardEntry2.setRating(1);
    leaderboardEntry2.setTotalGames(LeaderboardService.MINIMUM_GAMES_PLAYED_TO_BE_SHOWN - 1);

    LeaderboardEntry leaderboardEntry3 = new LeaderboardEntry();
    leaderboardEntry3.setRating(3);
    leaderboardEntry3.setTotalGames(LeaderboardService.MINIMUM_GAMES_PLAYED_TO_BE_SHOWN - 1);

    when(fafService.getLeaderboard(LEADERBOARD_NAME)).thenReturn(CompletableFuture.completedFuture(Arrays.asList(
      leaderboardEntry1, leaderboardEntry2, leaderboardEntry3
    )));

    List<RatingStat> result = instance.getLeaderboardStats(LEADERBOARD_NAME).toCompletableFuture().get(2, TimeUnit.SECONDS);
    verify(fafService).getLeaderboard(LEADERBOARD_NAME);

    assertThat(result, hasSize(2));
    assertThat(result.get(0).getTotalCount(), is(2));
    assertThat(result.get(0).getCountWithEnoughGamesPlayed(), is(1));
    assertThat(result.get(0).getRating(), is(1));

    assertThat(result.get(1).getTotalCount(), is(1));
    assertThat(result.get(1).getCountWithEnoughGamesPlayed(), is(0));
    assertThat(result.get(1).getRating(), is(3));
  }

  @Test
  public void testGetEntryForPlayer() throws Exception {
    LeaderboardEntry entry = new LeaderboardEntry();
    when(fafService.getLeaderboardEntryForPlayer(PLAYER_ID, LEADERBOARD_NAME)).thenReturn(CompletableFuture.completedFuture(entry));

    LeaderboardEntry result = instance.getEntryForPlayer(PLAYER_ID, LEADERBOARD_NAME).toCompletableFuture().get(2, TimeUnit.SECONDS);
    verify(fafService).getLeaderboardEntryForPlayer(PLAYER_ID, LEADERBOARD_NAME);
    assertThat(result, is(entry));
  }
}
