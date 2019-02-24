package com.faforever.client.main;

import com.faforever.client.game.CurrentGameEndedEvent;
import com.faforever.client.game.Game;
import com.faforever.client.game.GameState;
import com.faforever.client.i18n.I18n;
import com.faforever.client.notification.ImmediateNotification;
import com.faforever.client.notification.NotificationService;
import com.faforever.client.notification.PersistentNotification;
import com.faforever.client.replay.ReplayService;
import com.faforever.client.test.AbstractPlainJavaFxTest;
import javafx.event.Event;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.testfx.util.WaitForAsyncUtils;

import java.util.Optional;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RateGameNotifierTest extends AbstractPlainJavaFxTest {

  private RateGameNotifier instance;

  @Mock
  private NotificationService notificationService;
  @Mock
  private I18n i18n;
  @Mock
  private ReplayService replayService;
  @Mock
  private ApplicationEventPublisher eventPublisher;

  @Before
  public void setUp() throws Exception {
    instance = new RateGameNotifier(notificationService, i18n, replayService, eventPublisher);
  }

  @Test
  public void onCurrentGameEnded() {
    Game game = new Game();
    game.setId(123);
    game.setState(GameState.PLAYING);

    when(replayService.findById(123)).thenReturn(completedFuture(Optional.empty()));

    instance.onCurrentGameEnded(new CurrentGameEndedEvent(game));

    ArgumentCaptor<PersistentNotification> notificationCaptor = ArgumentCaptor.forClass(PersistentNotification.class);
    verify(notificationService).addNotification(notificationCaptor.capture());

    PersistentNotification notification = notificationCaptor.getValue();
    notification.getActions().get(0).call(new Event(Event.ANY));

    WaitForAsyncUtils.waitForFxEvents();

    verify(notificationService).addNotification(any(ImmediateNotification.class));
  }
}
