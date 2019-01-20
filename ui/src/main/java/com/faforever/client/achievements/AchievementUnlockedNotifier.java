package com.faforever.client.achievements;

import com.faforever.client.audio.AudioService;
import com.faforever.client.i18n.I18n;
import com.faforever.client.notification.NotificationService;
import com.faforever.client.notification.TransientNotification;
import com.faforever.client.remote.FafService;
import com.faforever.client.remote.UpdatedAchievement;
import com.faforever.client.remote.UpdatedAchievementsServerMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;

@Lazy
@Component
public class AchievementUnlockedNotifier {

  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final NotificationService notificationService;
  private final I18n i18n;
  private final AchievementService achievementService;
  private final AchievementImageService achievementImageService;
  private final FafService fafService;
  private final AudioService audioService;

  private long lastSoundPlayed;


  public AchievementUnlockedNotifier(NotificationService notificationService, I18n i18n, AchievementService achievementService, AchievementImageService achievementImageService, FafService fafService, AudioService audioService) {
    this.notificationService = notificationService;
    this.i18n = i18n;
    this.achievementService = achievementService;
    this.achievementImageService = achievementImageService;
    this.fafService = fafService;
    this.audioService = audioService;
  }

  @PostConstruct
  void postConstruct() {
    fafService.addOnMessageListener(UpdatedAchievementsServerMessage.class, this::onUpdatedAchievementsMessage);
  }

  private void onUpdatedAchievementsMessage(UpdatedAchievementsServerMessage message) {
    message.getUpdatedAchievements().stream()
        .filter(UpdatedAchievement::getNewlyUnlocked)
        .forEachOrdered(updatedAchievement -> achievementService.getAchievement(updatedAchievement.getAchievementId())
            .thenAccept(this::notifyAboutUnlockedAchievement)
            .exceptionally(throwable -> {
              logger.warn("Could not valueOf achievement definition for achievement: {}", updatedAchievement.getAchievementId(), throwable);
              return null;
            })
        );
  }

  private void notifyAboutUnlockedAchievement(Achievement achievement) {
    if (lastSoundPlayed < System.currentTimeMillis() - 1000) {
      audioService.playAchievementUnlockedSound();
      lastSoundPlayed = System.currentTimeMillis();
    }
    notificationService.addNotification(new TransientNotification(
            i18n.get("achievement.unlockedTitle"),
            achievement.getName(),
        achievementImageService.getImage(achievement, AchievementState.UNLOCKED)
        )
    );
  }
}
