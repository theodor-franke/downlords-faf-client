package com.faforever.client.audio;

import com.faforever.client.preferences.NotificationsPrefs;
import com.faforever.client.preferences.Preferences;
import com.faforever.client.preferences.PreferencesService;
import com.faforever.client.test.AbstractPlainJavaFxTest;
import com.faforever.client.theme.UiService;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;


public class AudioServiceTest extends AbstractPlainJavaFxTest {

  private AudioService instance;
  private NotificationsPrefs notificationsPrefs;

  @Mock
  private PreferencesService preferencesService;
  @Mock
  private AudioClipPlayer audioClipPlayer;
  @Mock
  private UiService uiService;

  @Override
  public void start(Stage stage) throws Exception {
    instance = new AudioService(preferencesService, audioClipPlayer, uiService);

    Preferences preferences = new Preferences();
    notificationsPrefs = preferences.getNotification();

    Mockito.when(preferencesService.getPreferences()).thenReturn(preferences);
    Mockito.when(uiService.getThemeFileUrl(ArgumentMatchers.any())).thenReturn(getThemeFileUrl(UiService.MENTION_SOUND));

    instance.afterPropertiesSet();

    super.start(stage);
  }

  @Test
  public void testPlayChatMentionSound() {
    notificationsPrefs.setSoundsEnabled(true);
    notificationsPrefs.setMentionSoundEnabled(true);

    instance.playChatMentionSound();

    Mockito.verify(audioClipPlayer).playSound(ArgumentMatchers.any(AudioClip.class));
  }

  @Test
  public void testPlayPrivateMessageSound() {
    notificationsPrefs.setSoundsEnabled(true);
    notificationsPrefs.setPrivateMessageSoundEnabled(true);

    instance.playPrivateMessageSound();

    Mockito.verify(audioClipPlayer).playSound(ArgumentMatchers.any(AudioClip.class));
  }

  @Test
  public void testPlayInfoNotificationSound() {
    notificationsPrefs.setSoundsEnabled(true);
    notificationsPrefs.setInfoSoundEnabled(true);

    instance.playInfoNotificationSound();

    Mockito.verify(audioClipPlayer).playSound(ArgumentMatchers.any(AudioClip.class));
  }

  @Test
  public void testPlayWarnNotificationSound() {
    notificationsPrefs.setSoundsEnabled(true);
    notificationsPrefs.setWarnSoundEnabled(true);

    instance.playWarnNotificationSound();

    Mockito.verify(audioClipPlayer).playSound(ArgumentMatchers.any(AudioClip.class));
  }

  @Test
  public void testPlayErrorNotificationSound() {
    notificationsPrefs.setSoundsEnabled(true);
    notificationsPrefs.setErrorSoundEnabled(true);

    instance.playErrorNotificationSound();

    Mockito.verify(audioClipPlayer).playSound(ArgumentMatchers.any(AudioClip.class));
  }
}
