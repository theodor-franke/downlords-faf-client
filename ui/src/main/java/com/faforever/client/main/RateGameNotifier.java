package com.faforever.client.main;

import com.faforever.client.game.CurrentGameEndedEvent;
import com.faforever.client.game.Game;
import com.faforever.client.i18n.I18n;
import com.faforever.client.notification.Action;
import com.faforever.client.notification.ImmediateNotification;
import com.faforever.client.notification.NotificationService;
import com.faforever.client.notification.PersistentNotification;
import com.faforever.client.notification.Severity;
import com.faforever.client.replay.ReplayService;
import com.faforever.client.replay.ShowReplayEvent;
import javafx.application.Platform;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

import static java.util.Collections.singletonList;

// TODO unit test
@Component
public class RateGameNotifier {

  private final NotificationService notificationService;
  private final I18n i18n;
  private final ReplayService replayService;
  private final ApplicationEventPublisher eventPublisher;

  public RateGameNotifier(NotificationService notificationService, I18n i18n, ReplayService replayService, ApplicationEventPublisher eventPublisher) {
    this.notificationService = notificationService;
    this.i18n = i18n;
    this.replayService = replayService;
    this.eventPublisher = eventPublisher;
  }

  @EventListener
  public void onCurrentGameEnded(CurrentGameEndedEvent event) {
    Game game = event.getGame();
    int id = game.getId();
    notificationService.addNotification(
      new PersistentNotification(
        i18n.get("game.ended", game.getTitle()),
        Severity.INFO,
        singletonList(new Action(
          i18n.get("game.rate"),
          actionEvent -> onRateGame(id)
        ))
      )
    );
  }

  private CompletableFuture<Void> onRateGame(int gameId) {
    return replayService.findById(gameId)
      .thenAccept(replay -> Platform.runLater(() -> {
          if (replay.isPresent()) {
            eventPublisher.publishEvent(new ShowReplayEvent(replay.get()));
          } else {
            notificationService.addNotification(new ImmediateNotification(i18n.get("replay.notFoundTitle"), i18n.get("replay.replayNotFoundText", gameId), Severity.WARN));
          }
        })
      );
  }
}
