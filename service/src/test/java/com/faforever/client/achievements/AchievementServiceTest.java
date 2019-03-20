package com.faforever.client.achievements;

import com.faforever.client.player.Player;
import com.faforever.client.player.PlayerService;
import com.faforever.client.remote.FafService;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.verifyNoMoreInteractions;


@RunWith(MockitoJUnitRunner.class)
public class AchievementServiceTest {

  private static final int PLAYER_ID = 123;

  @Rule
  public TemporaryFolder preferencesDirectory = new TemporaryFolder();
  @Rule
  public TemporaryFolder cacheDirectory = new TemporaryFolder();
  @Rule
  public ExpectedException expectedException = ExpectedException.none();
  @Mock
  private PlayerService playerService;
  @Mock
  private AchievementService instance;
  @Mock
  private FafService fafService;

  @Before
  public void setUp() throws Exception {
    instance = new AchievementService(fafService, playerService);
    Player player = new Player("abc");
    player.setId(PLAYER_ID);
    Mockito.when(playerService.getCurrentPlayer()).thenReturn(Optional.of(player));

    instance.afterPropertiesSet();
  }

  @Test
  public void testGetPlayerAchievementsForCurrentUser() {
    instance.playerAchievements.add(new PlayerAchievement());
    instance.getPlayerAchievements(PLAYER_ID);
    Mockito.verify(fafService).addOnMessageListener(ArgumentMatchers.any(), ArgumentMatchers.any());
    verifyNoMoreInteractions(fafService);
  }

  @Test
  public void testGetPlayerAchievementsForAnotherUser() throws Exception {
    List<PlayerAchievement> achievements = Arrays.asList(new PlayerAchievement(), new PlayerAchievement());
    Mockito.when(fafService.getPlayerAchievements(PLAYER_ID)).thenReturn(CompletableFuture.completedFuture(achievements));

    List<PlayerAchievement> playerAchievements = instance.getPlayerAchievements(PLAYER_ID).toCompletableFuture().get(5, TimeUnit.SECONDS);

    Assert.assertThat(playerAchievements, Matchers.hasSize(2));
    Assert.assertThat(playerAchievements, CoreMatchers.is(achievements));
    Mockito.verify(fafService).getPlayerAchievements(PLAYER_ID);
  }

  @Test
  public void testGetAchievements() {
    instance.getAchievements();
    Mockito.verify(fafService).getAchievements();
  }
}
