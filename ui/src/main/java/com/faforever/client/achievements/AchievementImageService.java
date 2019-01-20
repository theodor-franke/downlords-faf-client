package com.faforever.client.achievements;

import com.faforever.client.asset.AssetService;
import com.faforever.client.config.CacheNames;
import javafx.scene.image.Image;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.nio.file.Paths;

import static com.github.nocatch.NoCatch.noCatch;

@Service
@Lazy
public class AchievementImageService {
  private static final int ACHIEVEMENT_IMAGE_SIZE = 128;

  private final AssetService assetService;

  public AchievementImageService(AssetService assetService) {
    this.assetService = assetService;
  }

  @Cacheable(CacheNames.ACHIEVEMENT_IMAGES)
  public Image getImage(Achievement achievement, AchievementState achievementState) {
    URL url;
    switch (achievementState) {
      case REVEALED:
        url = noCatch(() -> new URL(achievement.getRevealedIconUrl()));
        break;
      case UNLOCKED:
        url = noCatch(() -> new URL(achievement.getUnlockedIconUrl()));
        break;
      default:
        throw new UnsupportedOperationException("Not yet implemented");
    }
    return assetService.loadAndCacheImage(url, Paths.get("achievements").resolve(achievementState.name().toLowerCase()),
      null, ACHIEVEMENT_IMAGE_SIZE, ACHIEVEMENT_IMAGE_SIZE);
  }
}
