package com.faforever.client.achievements;

import com.faforever.client.player.Player;
import com.faforever.client.player.PlayerService;
import com.faforever.client.remote.FafService;
import com.faforever.client.remote.UpdatedAchievement;
import com.faforever.client.remote.UpdatedAchievementsServerMessage;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


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
    when(playerService.getCurrentPlayer()).thenReturn(Optional.of(player));
  }

  @Test
  public void getPlayerAchievementsForCurrentUser() throws ExecutionException, InterruptedException {
    List<PlayerAchievement> playerAchievements = Arrays.asList(
      createPlayerAchievement("1"), createPlayerAchievement("2")
    );
    when(fafService.getPlayerAchievements(PLAYER_ID)).thenReturn(CompletableFuture.completedFuture(playerAchievements));

    List<PlayerAchievement> result = instance.getPlayerAchievements(PLAYER_ID).get();

    verify(fafService).getPlayerAchievements(PLAYER_ID);
    assertThat(result, is(playerAchievements));
    verifyNoMoreInteractions(fafService);
  }

  @Test
  public void getPlayerAchievementsForAnotherUser() throws Exception {
    List<PlayerAchievement> achievements = Arrays.asList(createPlayerAchievement("1"), createPlayerAchievement("2"));
    when(fafService.getPlayerAchievements(PLAYER_ID)).thenReturn(CompletableFuture.completedFuture(achievements));

    List<PlayerAchievement> playerAchievements = instance.getPlayerAchievements(PLAYER_ID).get();

    assertThat(playerAchievements, hasSize(2));
    assertThat(playerAchievements, is(achievements));
    verify(fafService).getPlayerAchievements(PLAYER_ID);
  }

  @Test
  public void getAchievements() {
    instance.getAchievements();
    verify(fafService).getAchievements();
  }

  @Test
  public void getAchievement() {
    instance.getAchievement("123");
    verify(fafService).getAchievement("123");
  }

  @Test
  public void onAchievementsUpdated_achievementsNotYetLoaded_loadsAchievements() throws ExecutionException, InterruptedException {
    when(fafService.getPlayerAchievements(PLAYER_ID)).thenReturn(CompletableFuture.completedFuture(Arrays.asList(
      createPlayerAchievement("1"), createPlayerAchievement("2")
    )));

    instance.onAchievementsUpdated(new UpdatedAchievementsServerMessage(Arrays.asList(
      createUpdatedAchievement("1", 3),
      createUpdatedAchievement("2", 1)
    )));

    verify(fafService).getPlayerAchievements(PLAYER_ID);

    List<PlayerAchievement> playerAchievements = instance.getPlayerAchievements(PLAYER_ID).get();
    assertThat(playerAchievements.get(0).getCurrentSteps(), is(3));
    assertThat(playerAchievements.get(1).getCurrentSteps(), is(1));
  }

  @NotNull
  private PlayerAchievement createPlayerAchievement(String id) {
    Achievement achievement = new Achievement();
    achievement.setId(id);

    PlayerAchievement playerAchievement = new PlayerAchievement();
    playerAchievement.setAchievement(achievement);
    playerAchievement.setState(AchievementState.REVEALED);
    playerAchievement.setCurrentSteps(0);
    return playerAchievement;
  }

  private UpdatedAchievement createUpdatedAchievement(String id, Integer currentSteps) {
    UpdatedAchievement updatedAchievement = new UpdatedAchievement();
    updatedAchievement.setAchievementId(id);
    updatedAchievement.setCurrentSteps(currentSteps);
    updatedAchievement.setCurrentState(AchievementState.REVEALED);
    return updatedAchievement;
  }
}
