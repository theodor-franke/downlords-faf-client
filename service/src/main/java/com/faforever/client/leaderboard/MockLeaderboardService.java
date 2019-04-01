package com.faforever.client.leaderboard;

import com.faforever.client.SpringProfiles;
import com.faforever.client.task.CompletableTask;
import com.faforever.client.task.CompletableTask.Priority;
import com.faforever.client.task.TaskService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


@Lazy
@Service
@Profile(SpringProfiles.PROFILE_OFFLINE)
public class MockLeaderboardService implements LeaderboardService {

  private final TaskService taskService;


  public MockLeaderboardService(TaskService taskService) {
    this.taskService = taskService;
  }

  @Override
  public CompletableFuture<List<RatingStat>> getLeaderboardStats(String leaderboardName) {
    return CompletableFuture.completedFuture(Collections.emptyList());
  }

  @Override
  public CompletableFuture<Optional<LeaderboardEntry>> getEntryForPlayer(int playerId, String leaderboardName) {
    return CompletableFuture.completedFuture(
      Optional.of(createLeaderboardEntry("Player #" + playerId, 111, 222, 333, 192))
    );
  }

  @Override
  public CompletableFuture<List<LeaderboardEntry>> getEntries(String leaderboardName) {
    return taskService.submitTask(new CompletableTask<List<LeaderboardEntry>>(Priority.HIGH) {
      @Override
      protected List<LeaderboardEntry> call() {
        updateTitle("Reading ladder");

        List<LeaderboardEntry> list = new ArrayList<>();
        for (int i = 1; i <= 10000; i++) {
          String name = RandomStringUtils.random(10);
          int rating = (int) (Math.random() * 2500);
          int gamecount = (int) (Math.random() * 10000);
          float winloss = (float) (Math.random() * 100);

          list.add(createLeaderboardEntry(name, i, rating, gamecount, (int) (gamecount * winloss)));

        }
        return list;
      }
    }).getFuture();
  }

  private LeaderboardEntry createLeaderboardEntry(String name, int position, int rank, int totalGames, int wonGames) {
    LeaderboardEntry leaderboardEntry = new LeaderboardEntry();
    leaderboardEntry.setPlayerName(name);
    leaderboardEntry.setPosition(position);
    leaderboardEntry.setRating(rank);
    leaderboardEntry.setTotalGames(totalGames);
    leaderboardEntry.setWonGames(wonGames);

    return leaderboardEntry;
  }
}
