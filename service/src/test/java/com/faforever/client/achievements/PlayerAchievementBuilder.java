package com.faforever.client.achievements;

import org.supcomhub.api.dto.Achievement;
import org.supcomhub.api.dto.AchievementState;
import org.supcomhub.api.dto.PlayerAchievement;

import java.time.OffsetDateTime;

public class PlayerAchievementBuilder {

  private final PlayerAchievement playerAchievement;

  private PlayerAchievementBuilder() {
    playerAchievement = new PlayerAchievement();
  }

  public static PlayerAchievementBuilder create() {
    return new PlayerAchievementBuilder();
  }

  public PlayerAchievementBuilder defaultValues() {
    playerAchievement.setAchievement((Achievement) new Achievement().setId("1-2-3"));
    playerAchievement.setState(AchievementState.REVEALED);
    playerAchievement.setCreateTime(OffsetDateTime.now());
    playerAchievement.setUpdateTime(OffsetDateTime.now());
    return this;
  }

  public PlayerAchievementBuilder state(AchievementState state) {
    playerAchievement.setState(state);
    return this;
  }

  public PlayerAchievementBuilder currentSteps(int steps) {
    playerAchievement.setCurrentSteps(steps);
    return this;
  }

  public PlayerAchievementBuilder achievementId(String achievementId) {
    playerAchievement.setAchievement((Achievement) new Achievement().setId(achievementId));
    return this;
  }

  public PlayerAchievement get() {
    return playerAchievement;
  }
}
