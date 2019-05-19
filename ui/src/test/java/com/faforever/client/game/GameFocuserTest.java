package com.faforever.client.game;

import com.faforever.client.config.ClientProperties;
import com.faforever.client.fx.PlatformService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GameFocuserTest {

  private GameFocuser instance;

  @Mock
  private GameService gameService;
  @Mock
  private PlatformService platformService;
  private String windowTitle;

  @Before
  public void setUp() throws Exception {
    ClientProperties clientProperties = new ClientProperties();
    windowTitle = clientProperties.getForgedAlliance().getWindowTitle();

    instance = new GameFocuser(gameService, platformService, clientProperties);
  }

  @Test
  public void currentGame() {
    Game game = new Game();

    when(gameService.isCurrentGame(game)).thenReturn(true);

    instance.onGamePlaying(new GamePlayingEvent(game));

    verify(gameService).isCurrentGame(game);
    verify(platformService).isWindowFocused(windowTitle);
    verify(platformService).focusWindow(windowTitle);
  }

  @Test
  public void notCurrentGame() {
    Game game = new Game();

    when(gameService.isCurrentGame(game)).thenReturn(false);

    instance.onGamePlaying(new GamePlayingEvent(game));

    verify(gameService).isCurrentGame(game);
    verifyZeroInteractions(platformService);
    verifyZeroInteractions(platformService);
  }
}
