package com.faforever.client.player;

import com.faforever.client.audio.AudioService;
import com.faforever.client.game.Game;
import com.faforever.client.i18n.I18n;
import com.faforever.client.notification.NotificationService;
import com.faforever.client.notification.TransientNotification;
import com.faforever.client.play.JoinGameHelper;
import com.faforever.client.preferences.NotificationsPrefs;
import com.faforever.client.preferences.Preferences;
import com.faforever.client.preferences.PreferencesService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.event.EventListener;
import org.springframework.util.ReflectionUtils;

import static com.natpryce.hamcrest.reflection.HasAnnotationMatcher.hasAnnotation;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FriendJoinedGameNotifierTest {
  private FriendJoinedGameNotifier instance;
  @Mock
  private NotificationService notificationService;
  @Mock
  private I18n i18n;
  @Mock
  private JoinGameHelper joinGameHelper;
  @Mock
  private PreferencesService preferencesService;
  @Mock
  private Preferences preferences;
  @Mock
  private NotificationsPrefs notification;
  @Mock
  private AudioService audioService;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    instance = new FriendJoinedGameNotifier(notificationService, i18n, joinGameHelper, preferencesService, audioService);

    when(preferencesService.getPreferences()).thenReturn(preferences);
    when(preferences.getNotification()).thenReturn(notification);
  }

  @Test
  public void testSubscribeAnnotations() {
    assertThat(ReflectionUtils.findMethod(instance.getClass(), "onFriendJoinedGame", FriendJoinedGameEvent.class),
        hasAnnotation(EventListener.class));
  }

  @Test
  public void onFriendJoinedGame() {
    Game game = new Game().setTitle("My Game");
    Player player = new Player("junit");
    player.setGame(game);
    player.setId(1);

    when(notification.isFriendJoinsGameToastEnabled()).thenReturn(true);
    when(i18n.get("friend.joinedGameNotification.title", "junit", "My Game")).thenReturn("junit joined My Game");
    when(i18n.get("friend.joinedGameNotification.action")).thenReturn("Click to join");

    instance.onFriendJoinedGame(new FriendJoinedGameEvent(player, game));

    ArgumentCaptor<TransientNotification> captor = ArgumentCaptor.forClass(TransientNotification.class);
    verify(notificationService).addNotification(captor.capture());

    TransientNotification notification = captor.getValue();
    assertThat(notification.getTitle(), is("junit joined My Game"));
    assertThat(notification.getText(), is("Click to join"));
    assertThat(notification.getImage(), notNullValue());
  }

  @Test
  public void testNoNotificationIfDisabledInPreferences() {
    when(notification.isFriendJoinsGameToastEnabled()).thenReturn(false);

    instance.onFriendJoinedGame(new FriendJoinedGameEvent(new Player("junit"), new Game()));

    verify(notificationService, never()).addNotification(any(TransientNotification.class));
  }
}
