package com.faforever.client.achievements;


import java.util.UUID;

public class AchievementBuilder {

  private final Achievement achievement;

  private AchievementBuilder() {
    achievement = new Achievement();
  }

  public static AchievementBuilder create() {
    return new AchievementBuilder();
  }

  public AchievementBuilder defaultValues() {
    achievement.setId(UUID.randomUUID().toString());
    achievement.setName("Name");
    achievement.setDescription("Description");
    achievement.setInitialState(AchievementState.REVEALED);
    achievement.setUnlockedIconUrl("http://127.0.0.1:65354/unlocked/1-2-3.png");
    achievement.setRevealedIconUrl("http://127.0.0.1:65354/revealed/1-2-3.png");
    achievement.setExperiencePoints(10);
    achievement.setTotalSteps(100);
    achievement.setType(AchievementType.INCREMENTAL);
    return this;
  }

  public AchievementBuilder id(String id) {
    achievement.setId(id);
    return this;
  }

  public AchievementBuilder type(AchievementType type) {
    achievement.setType(type);
    return this;
  }

  public Achievement get() {
    return achievement;
  }
}
