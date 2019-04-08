package com.faforever.client.game.relay.ice;

import com.faforever.client.game.relay.GpgGameMessage;
import com.faforever.client.game.relay.ice.event.GpgGameMessageEvent;
import com.faforever.client.game.relay.ice.event.IceAdapterStateChanged;
import com.faforever.client.remote.FafService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Dispatches all methods that the ICE adapter can call on its client.
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class IceAdapterCallbacks {

  private final ApplicationEventPublisher eventPublisher;
  private final FafService fafService;


  public IceAdapterCallbacks(ApplicationEventPublisher eventPublisher, FafService fafService) {
    this.eventPublisher = eventPublisher;
    this.fafService = fafService;
  }

  public void onConnectionStateChanged(String newState) {
    log.debug("ICE adapter connection state changed to: {}", newState);
    eventPublisher.publishEvent(new IceAdapterStateChanged(newState));
  }

  public void onGpgNetMessageReceived(String header, List<Object> chunks) {
    log.debug("Message from game: '{}' '{}'", header, chunks);
    eventPublisher.publishEvent(new GpgGameMessageEvent(new GpgGameMessage(header, chunks)));
  }

  public void onIceMsg(long localPlayerId, long remotePlayerId, Object message) {
    log.debug("ICE message for connection '{}/{}': {}", localPlayerId, remotePlayerId, message);
    fafService.sendIceMessage((int) remotePlayerId, message);
  }

  public void onIceConnectionStateChanged(long localPlayerId, long remotePlayerId, String state) {
    log.debug("ICE connection state for peer '{}' changed to: {}", remotePlayerId, state);
  }

  public void onConnected(long localPlayerId, long remotePlayerId, boolean connected) {
    if (connected) {
      log.debug("Connection between '{}' and '{}' has been established", localPlayerId, remotePlayerId);
    } else {
      log.debug("Connection between '{}' and '{}' has been lost", localPlayerId, remotePlayerId);
    }
  }
}
