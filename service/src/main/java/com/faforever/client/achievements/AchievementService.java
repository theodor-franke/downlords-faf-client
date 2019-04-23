package com.faforever.client.achievements;

import com.faforever.client.player.PlayerService;
import com.faforever.client.remote.FafService;
import com.faforever.client.remote.UpdatedAchievementsServerMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;


@Lazy
@Service
public class AchievementService {

  private final ObservableList<PlayerAchievement> playerAchievements;

  private final FafService fafService;
  private final PlayerService playerService;

  public AchievementService(FafService fafService, PlayerService playerService) {
    this.fafService = fafService;
    this.playerService = playerService;

    playerAchievements = FXCollections.observableArrayList();
  }

  public CompletableFuture<List<PlayerAchievement>> getPlayerAchievements(Integer playerId) {
    return playerService.getCurrentPlayer()
      .filter(currentPlayer -> Objects.equals(playerId, currentPlayer.getId()) && !playerAchievements.isEmpty())
      .map(player -> CompletableFuture.completedFuture((List<PlayerAchievement>) playerAchievements))
      .orElseGet(() -> fafService.getPlayerAchievements(playerId));
  }

  public CompletableFuture<List<Achievement>> getAchievements() {
    return fafService.getAchievements();
  }

  public CompletableFuture<Achievement> getAchievement(String achievementId) {
    return fafService.getAchievement(achievementId);
  }

  @EventListener
  public void onAchievementsUpdated(UpdatedAchievementsServerMessage message) {
    if (playerAchievements.isEmpty()) {
      loadAchievementsOfCurrentPlayer().thenRun(() -> updateAchievementsOfCurrentPlayer(message));
    } else {
      updateAchievementsOfCurrentPlayer(message);
    }
  }

  private void updateAchievementsOfCurrentPlayer(UpdatedAchievementsServerMessage message) {
    Map<String, PlayerAchievement> achievementsById = playerAchievements.stream()
      .collect(Collectors.toMap(playerAchievement -> playerAchievement.getAchievement().getId(), Function.identity()));

    message.getUpdatedAchievements().forEach(updatedAchievement -> {
      PlayerAchievement playerAchievement = achievementsById.get(updatedAchievement.getAchievementId());
      Optional.ofNullable(updatedAchievement.getCurrentSteps()).ifPresent(playerAchievement::setCurrentSteps);
      playerAchievement.setState(updatedAchievement.getCurrentState());
      playerAchievement.setUpdateTime(OffsetDateTime.now());
    });
  }

  private CompletableFuture<Void> loadAchievementsOfCurrentPlayer() {
    int playerId = playerService.getCurrentPlayer().orElseThrow(() -> new IllegalStateException("Player has to be set")).getId();
    return fafService.getPlayerAchievements(playerId).thenAccept(playerAchievements::setAll);
  }
}
