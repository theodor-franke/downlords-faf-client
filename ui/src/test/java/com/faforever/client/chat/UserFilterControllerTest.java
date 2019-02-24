package com.faforever.client.chat;

import com.faforever.client.game.Game;
import com.faforever.client.game.GameState;
import com.faforever.client.game.PlayerStatus;
import com.faforever.client.i18n.I18n;
import com.faforever.client.player.Player;
import com.faforever.client.test.AbstractPlainJavaFxTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class UserFilterControllerTest extends AbstractPlainJavaFxTest {

  @Mock
  private ChannelTabController channelTabController;
  @Mock
  private I18n i18n;

  private ChatChannelUser chatChannelUser;
  private UserFilterController instance;
  private Player player;

  @Before
  public void setUp() throws Exception {
    instance = new UserFilterController(i18n);
    instance.channelTabController = channelTabController;

    player = new Player("junit");
    chatChannelUser = new ChatChannelUser("junit", null, false)
      .setPlayer(player);

    loadFxml("theme/chat/user_filter.fxml", clazz -> instance);
  }

  @Test
  public void setChannelTabControllerTest() {
    instance.setChannelController(channelTabController);
    assertEquals(channelTabController, instance.channelTabController);
  }

  @Test
  public void testIsInClan() {
    String testClan = "testClan";
    player.setClanTag(testClan);
    instance.clanField.setText(testClan);

    assertTrue(instance.isInClan(chatChannelUser));
  }

  @Test
  public void testIsBoundedByRatingWithinBounds() {
    player.getRating().put("global", 7);

    instance.minRankField.setText("3");
    instance.maxRankField.setText("7");

    assertTrue(instance.isBoundByRating(chatChannelUser));
  }

  @Test
  public void testIsBoundedByRatingNotWithinBounds() {
    player.getRating().put("global", 5);

    instance.minRankField.setText("2");
    instance.maxRankField.setText("4");

    assertFalse(instance.isBoundByRating(chatChannelUser));
  }

  @Test
  public void testIsGameStatusMatchPlaying() {
    player.setGame(new Game().setState(GameState.PLAYING));
    instance.playerStatusFilter = PlayerStatus.PLAYING;

    assertTrue(instance.isGameStatusMatch(chatChannelUser));
  }

  @Test
  public void testIsGameStatusMatchLobby() {
    player.setGame(new Game().setState(GameState.OPEN).setHostName(player.getDisplayName()));
    instance.playerStatusFilter = PlayerStatus.HOSTING;

    assertTrue(instance.isGameStatusMatch(chatChannelUser));

    player.setGame(new Game().setState(GameState.OPEN));
    instance.playerStatusFilter = PlayerStatus.LOBBYING;

    assertTrue(instance.isGameStatusMatch(chatChannelUser));
  }

  @Test
  public void testOnGameStatusPlaying() {
    when(i18n.get("game.gameStatus.playing")).thenReturn("playing");

    instance.onGameStatusPlaying();
    assertEquals(PlayerStatus.PLAYING, instance.playerStatusFilter);
    assertEquals(i18n.get("game.gameStatus.playing"), instance.gameStatusMenu.getText());
  }

  @Test
  public void testOnGameStatusLobby() {
    when(i18n.get("game.gameStatus.lobby")).thenReturn("lobby");

    instance.onGameStatusLobby();
    assertEquals(PlayerStatus.LOBBYING, instance.playerStatusFilter);
    assertEquals(i18n.get("game.gameStatus.lobby"), instance.gameStatusMenu.getText());
  }

  @Test
  public void testOnGameStatusNone() {
    when(i18n.get("game.gameStatus.none")).thenReturn("none");

    instance.onGameStatusNone();
    assertEquals(PlayerStatus.IDLE, instance.playerStatusFilter);
    assertEquals(i18n.get("game.gameStatus.none"), instance.gameStatusMenu.getText());
  }
}
