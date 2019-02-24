package com.faforever.client.achievements;

import com.faforever.client.i18n.I18n;
import com.faforever.client.test.AbstractPlainJavaFxTest;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;

import static com.faforever.client.theme.UiService.DEFAULT_ACHIEVEMENT_IMAGE;
import static org.hamcrest.CoreMatchers.is;

public class AchievementItemControllerTest extends AbstractPlainJavaFxTest {

  private AchievementItemController instance;

  @Mock
  private I18n i18n;
  @Mock
  private AchievementImageService achievementImageService;

  @Before
  public void setUp() throws Exception {
    instance = new AchievementItemController(i18n, achievementImageService);
    Mockito.when(i18n.number(ArgumentMatchers.anyInt())).thenAnswer(invocation -> String.format("%d", (int) invocation.getArgument(0)));

    loadFxml("theme/achievement_item.fxml", clazz -> instance);
  }

  @Test
  public void testGetRoot() throws Exception {
    Assert.assertThat(instance.getRoot(), is(instance.achievementItemRoot));
    Assert.assertThat(instance.getRoot().getParent(), CoreMatchers.is(CoreMatchers.nullValue()));
  }

  @Test
  public void testSetAchievement() throws Exception {
    Achievement achievement = new Achievement();
    Mockito.when(achievementImageService.getImage(achievement, AchievementState.REVEALED)).thenReturn(new Image(getThemeFile(DEFAULT_ACHIEVEMENT_IMAGE)));

    instance.setAchievement(achievement);

    Assert.assertThat(instance.nameLabel.getText(), is(achievement.getName()));
    Assert.assertThat(instance.descriptionLabel.getText(), is(achievement.getDescription()));
    Assert.assertThat(instance.pointsLabel.getText(), is(String.format("%d", achievement.getExperiencePoints())));
    Assert.assertThat(instance.imageView.getImage(), CoreMatchers.notNullValue());
    Assert.assertThat(instance.imageView.getEffect(), CoreMatchers.is(CoreMatchers.instanceOf(ColorAdjust.class)));
    Assert.assertThat(instance.imageView.getOpacity(), CoreMatchers.is(0.5));
    Assert.assertThat(instance.progressBar.isVisible(), CoreMatchers.is(true));
  }

  @Test
  public void testSetAchievementStandardHasNoProgress() throws Exception {
    Achievement achievement = new Achievement();
    achievement.setType(AchievementType.STANDARD);
    instance.setAchievement(achievement);

    Assert.assertThat(instance.progressBar.isVisible(), CoreMatchers.is(false));
    Assert.assertThat(instance.progressLabel.isVisible(), CoreMatchers.is(false));
  }

  @Test
  public void testSetPlayerAchievementStandardDoesntUpdateProgress() throws Exception {
    Achievement achievement = new Achievement();
    achievement.setId("123");
    achievement.setType(AchievementType.STANDARD);
    instance.setAchievement(achievement);

    PlayerAchievement playerAchievement = new PlayerAchievement();
    playerAchievement.setState(AchievementState.UNLOCKED);
    playerAchievement.setCurrentSteps(50);
    playerAchievement.setAchievement(achievement);

    instance.setPlayerAchievement(playerAchievement);

    Assert.assertThat(instance.progressBar.getProgress(), CoreMatchers.is(0.0));
  }

  @Test(expected = IllegalStateException.class)
  public void testSetPlayerAchievementWithUnsetAchievementThrowsIse() throws Exception {
    instance.setPlayerAchievement(new PlayerAchievement());
  }

  @Test(expected = IllegalStateException.class)
  public void testSetPlayerAchievementIdDoesntMatch() throws Exception {
    Achievement thisAchievement = new Achievement();
    thisAchievement.setId("this");
    instance.setAchievement(thisAchievement);

    PlayerAchievement playerAchievement = new PlayerAchievement();
    Achievement otherAchievement = new Achievement();
    otherAchievement.setId("other");
    playerAchievement.setAchievement(otherAchievement);

    instance.setPlayerAchievement(playerAchievement);
  }

  @Test
  public void testSetPlayerAchievementRevealed() throws Exception {
    Achievement achievement = new Achievement();
    achievement.setId("123");
    instance.setAchievement(achievement);

    PlayerAchievement playerAchievement = new PlayerAchievement();
    playerAchievement.setState(AchievementState.REVEALED);
    playerAchievement.setAchievement(achievement);

    instance.setPlayerAchievement(playerAchievement);
    Assert.assertThat(instance.imageView.getEffect(), CoreMatchers.is(CoreMatchers.instanceOf(ColorAdjust.class)));
    Assert.assertThat(instance.imageView.getOpacity(), CoreMatchers.is(0.5));
  }

  @Test
  public void testSetPlayerAchievementUnlocked() throws Exception {
    Achievement achievement = new Achievement();
    achievement.setType(AchievementType.INCREMENTAL);
    achievement.setTotalSteps(100);
    achievement.setId("123");
    instance.setAchievement(achievement);

    PlayerAchievement playerAchievement = new PlayerAchievement();
    playerAchievement.setState(AchievementState.UNLOCKED);
    playerAchievement.setCurrentSteps(50);
    playerAchievement.setAchievement(achievement);

    instance.setPlayerAchievement(playerAchievement);
    Assert.assertThat(instance.imageView.getEffect(), CoreMatchers.is(CoreMatchers.nullValue()));
    Assert.assertThat(instance.imageView.getOpacity(), CoreMatchers.is(1.0));
    Assert.assertThat(instance.progressBar.isVisible(), CoreMatchers.is(true));
    Assert.assertThat(instance.progressBar.getProgress(), CoreMatchers.is(0.5));
  }
}
