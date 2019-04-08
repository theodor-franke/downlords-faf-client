package com.faforever.client.player;

import com.faforever.client.audio.AudioService;
import com.faforever.client.i18n.I18n;
import com.faforever.client.notification.NotificationService;
import com.faforever.client.notification.TransientNotification;
import com.faforever.client.util.IdenticonUtil;
import com.google.common.eventbus.EventBus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


/**
 * Displays a notification whenever a friend goes offline (if enabled in settings).
 */
@Component
public class FriendOfflineNotifier implements InitializingBean {

  private final NotificationService notificationService;
  private final I18n i18n;
  private final EventBus eventBus;
  private final AudioService audioService;
  private final PlayerService playerService;


  public FriendOfflineNotifier(NotificationService notificationService, I18n i18n, EventBus eventBus,
                               AudioService audioService, PlayerService playerService) {
    this.notificationService = notificationService;
    this.i18n = i18n;
    this.eventBus = eventBus;
    this.audioService = audioService;
    this.playerService = playerService;
  }

  @Override
  public void afterPropertiesSet() {
    eventBus.register(this);
  }

  @EventListener
  public void onUserOffline(PlayerOfflineMessage event) {
    int playerId = event.getPlayerId();
    playerService.getPlayerById(playerId).ifPresent(player -> {
      if (player.getSocialStatus() != SocialStatus.FRIEND) {
        return;
      }

      audioService.playFriendOfflineSound();
      notificationService.addNotification(
          new TransientNotification(
              i18n.get("friend.nowOfflineNotification.title", player.getDisplayName()), "",
              IdenticonUtil.createIdenticon(player.getId())
          ));
    });
  }
}
