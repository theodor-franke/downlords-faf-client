package com.faforever.client.game;


import com.faforever.client.config.ClientProperties;
import com.faforever.client.fx.PlatformService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class GameFocuser {
  private final GameService gameService;
  private final PlatformService platformService;
  private final ClientProperties clientProperties;

  @EventListener
  public void onGamePlaying(GamePlayingEvent event) {
    String faWindowTitle = clientProperties.getForgedAlliance().getWindowTitle();

    if (gameService.isCurrentGame(event.getGame()) && !platformService.isWindowFocused(faWindowTitle)) {
      platformService.focusWindow(faWindowTitle);
    }
  }
}
