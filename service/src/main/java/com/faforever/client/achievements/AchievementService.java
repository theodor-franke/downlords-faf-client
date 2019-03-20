package com.faforever.client.achievements;

import com.faforever.client.player.PlayerService;
import com.faforever.client.remote.FafService;
import com.faforever.client.remote.UpdatedAchievementsServerMessage;
import com.google.common.annotations.VisibleForTesting;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;


@Lazy
@Service
public class AchievementService implements InitializingBean {

  @VisibleForTesting
  final ObservableList<PlayerAchievement> playerAchievements;

  private final ObservableList<PlayerAchievement> readOnlyPlayerAchievements;
  private final FafService fafService;
  private final PlayerService playerService;

  public AchievementService(FafService fafService, PlayerService playerService) {
    this.fafService = fafService;
    this.playerService = playerService;

    playerAchievements = FXCollections.observableArrayList();
    readOnlyPlayerAchievements = FXCollections.unmodifiableObservableList(playerAchievements);
  }


  public CompletableFuture<List<PlayerAchievement>> getPlayerAchievements(Integer playerId) {
    int currentPlayerId = playerService.getCurrentPlayer().orElseThrow(() -> new IllegalStateException("Player has to be set")).getId();
    if (Objects.equals(currentPlayerId, playerId)) {
      if (readOnlyPlayerAchievements.isEmpty()) {

        return reloadAchievements();
      }
      return CompletableFuture.completedFuture(readOnlyPlayerAchievements);
    }

    return fafService.getPlayerAchievements(playerId);
  }

  public CompletableFuture<List<Achievement>> getAchievements() {
    return fafService.getAchievements();
  }


  public CompletableFuture<Achievement> getAchievement(String achievementId) {
    return fafService.getAchievement(achievementId);
  }

  private CompletableFuture<List<PlayerAchievement>> reloadAchievements() {
    CompletableFuture<List<PlayerAchievement>> achievementsLoadedFuture = new CompletableFuture<>();
    int playerId = playerService.getCurrentPlayer().orElseThrow(() -> new IllegalStateException("Player has to be set")).getId();
    fafService.getPlayerAchievements(playerId).thenAccept(achievements -> {
      playerAchievements.setAll(achievements);
      achievementsLoadedFuture.complete(readOnlyPlayerAchievements);
    });
    return achievementsLoadedFuture;
  }

  @Override
  public void afterPropertiesSet() {
    fafService.addOnMessageListener(UpdatedAchievementsServerMessage.class, updatedAchievementsMessage -> reloadAchievements());
  }
}
