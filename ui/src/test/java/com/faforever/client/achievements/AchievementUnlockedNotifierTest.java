package com.faforever.client.achievements;

import com.faforever.client.audio.AudioService;
import com.faforever.client.i18n.I18n;
import com.faforever.client.notification.NotificationService;
import com.faforever.client.notification.TransientNotification;
import com.faforever.client.remote.FafService;
import com.faforever.client.remote.UpdatedAchievement;
import com.faforever.client.remote.UpdatedAchievementsServerMessage;
import javafx.scene.image.Image;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static org.mockito.Mockito.verifyZeroInteractions;

public class AchievementUnlockedNotifierTest {
  @Mock
  private AchievementUnlockedNotifier instance;
  @Mock
  private NotificationService notificationService;
  @Mock
  private I18n i18n;
  @Mock
  private AchievementService achievementService;
  @Mock
  private AchievementImageService achievementImageService;
  @Mock
  private FafService fafService;
  @Mock
  private AudioService audioService;

  @Captor
  private ArgumentCaptor<Consumer<UpdatedAchievementsServerMessage>> listenerCaptor;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    instance = new AchievementUnlockedNotifier(notificationService, i18n, achievementService, achievementImageService, fafService, audioService);
    instance.postConstruct();

    Mockito.verify(fafService).addOnMessageListener(ArgumentMatchers.eq(UpdatedAchievementsServerMessage.class), listenerCaptor.capture());
  }

  @Test
  public void newlyUnlocked() throws Exception {
    Achievement achievement = new Achievement();
    achievement.setType(AchievementType.STANDARD);
    achievement.setName("Test Achievement");
    Mockito.when(achievementImageService.getImage(achievement, AchievementState.UNLOCKED)).thenReturn(Mockito.mock(Image.class));

    triggerUpdatedAchievementsMessage(achievement, true);

    Mockito.verify(audioService).playAchievementUnlockedSound();

    ArgumentCaptor<TransientNotification> notificationCaptor = ArgumentCaptor.forClass(TransientNotification.class);
    Mockito.verify(notificationService).addNotification(notificationCaptor.capture());

    TransientNotification notification = notificationCaptor.getValue();

    Assert.assertThat(notification.getImage(), CoreMatchers.notNullValue());
    Assert.assertThat(notification.getTitle(), CoreMatchers.is("Achievement unlocked"));
    Assert.assertThat(notification.getText(), CoreMatchers.is("Test Achievement"));
  }

  @Test
  public void alreadyUnlocked() {
    Achievement achievement = new Achievement();
    achievement.setType(AchievementType.STANDARD);
    achievement.setName("Test Achievement");
    triggerUpdatedAchievementsMessage(achievement, false);

    verifyZeroInteractions(audioService);
    Mockito.verifyZeroInteractions(notificationService);
  }

  private void triggerUpdatedAchievementsMessage(Achievement achievement, boolean newlyUnlocked) {
    Mockito.when(achievementService.getAchievement("1234")).thenReturn(CompletableFuture.completedFuture(achievement));

    Mockito.when(i18n.get("achievement.unlockedTitle")).thenReturn("Achievement unlocked");
    Mockito.when(achievementImageService.getImage(achievement, AchievementState.REVEALED)).thenReturn(Mockito.mock(Image.class));

    UpdatedAchievementsServerMessage message = new UpdatedAchievementsServerMessage();
    UpdatedAchievement updatedAchievement = new UpdatedAchievement();
    updatedAchievement.setNewlyUnlocked(newlyUnlocked);
    updatedAchievement.setAchievementId("1234");
    message.setUpdatedAchievements(Collections.singletonList(updatedAchievement));

    listenerCaptor.getValue().accept(message);
  }
}
