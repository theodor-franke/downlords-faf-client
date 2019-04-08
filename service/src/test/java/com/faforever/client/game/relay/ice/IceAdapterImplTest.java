package com.faforever.client.game.relay.ice;

import com.faforever.client.player.PlayerService;
import com.faforever.client.remote.FafService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;

// TODO implement tests
@Ignore
@RunWith(MockitoJUnitRunner.class)
public class IceAdapterImplTest {

  @Mock
  private IceAdapterImpl instance;
  @Mock
  private ApplicationContext applicationContext;
  @Mock
  private PlayerService playerService;
  @Mock
  private ApplicationEventPublisher eventPublisher;
  @Mock
  private FafService fafService;

  @Before
  public void setUp() throws Exception {
    instance = new IceAdapterImpl(applicationContext, playerService, fafService, eventPublisher);
  }

  @Test
  public void onIceAdapterStateChanged() throws Exception {
  }

  @Test
  public void onGpgGameMessage() throws Exception {
  }

  @Test
  public void start() throws Exception {
  }

  @Test
  public void stop() throws Exception {
  }

}
