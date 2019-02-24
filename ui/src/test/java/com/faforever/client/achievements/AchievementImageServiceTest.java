package com.faforever.client.achievements;

import com.faforever.client.asset.AssetService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.faforever.client.achievements.AchievementState.HIDDEN;
import static com.faforever.client.achievements.AchievementState.REVEALED;
import static com.faforever.client.achievements.AchievementState.UNLOCKED;

@RunWith(MockitoJUnitRunner.class)
public class AchievementImageServiceTest {

  private AchievementImageService instance;

  @Mock
  private AssetService assetService;

  @Before
  public void setUp() throws Exception {
    instance = new AchievementImageService(assetService);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetHiddenThrowsUnsupportedOperationException() throws Exception {
    instance.getImage(null, HIDDEN);
  }

  @Test
  public void testLoadAndCacheImageRevealed() throws Exception {
    Achievement achievement = new Achievement();;
    achievement.setRevealedIconUrl("http://example.com/revealed.png");
    Path cacheSubDir = Paths.get("achievements").resolve(REVEALED.name().toLowerCase());
    instance.getImage(achievement, REVEALED);
    Mockito.verify(assetService).loadAndCacheImage(new URL(achievement.getRevealedIconUrl()), cacheSubDir, null, 128, 128);
  }

  @Test
  public void testLoadAndCacheImageUnlocked() throws Exception {
    Achievement achievement = new Achievement();
    achievement.setUnlockedIconUrl("http://example.com/unlocked.png");
    Path cacheSubDir = Paths.get("achievements").resolve(UNLOCKED.name().toLowerCase());
    instance.getImage(achievement, UNLOCKED);
    Mockito.verify(assetService).loadAndCacheImage(new URL(achievement.getUnlockedIconUrl()), cacheSubDir, null, 128, 128);
  }
}
