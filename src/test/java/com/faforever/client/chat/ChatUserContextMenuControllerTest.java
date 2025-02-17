package com.faforever.client.chat;

import com.faforever.client.chat.avatar.AvatarBean;
import com.faforever.client.chat.avatar.AvatarService;
import com.faforever.client.game.Game;
import com.faforever.client.game.JoinGameHelper;
import com.faforever.client.game.KnownFeaturedMod;
import com.faforever.client.game.PlayerStatus;
import com.faforever.client.i18n.I18n;
import com.faforever.client.moderator.ModeratorService;
import com.faforever.client.notification.NotificationService;
import com.faforever.client.player.Player;
import com.faforever.client.player.PlayerBuilder;
import com.faforever.client.player.PlayerService;
import com.faforever.client.preferences.Preferences;
import com.faforever.client.preferences.PreferencesBuilder;
import com.faforever.client.preferences.PreferencesService;
import com.faforever.client.remote.domain.GameStatus;
import com.faforever.client.remote.domain.GameType;
import com.faforever.client.replay.ReplayService;
import com.faforever.client.reporting.ReportDialogController;
import com.faforever.client.teammatchmaking.TeamMatchmakingService;
import com.faforever.client.test.AbstractPlainJavaFxTest;
import com.faforever.client.theme.UiService;
import com.faforever.commons.api.dto.GroupPermission;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.testfx.util.WaitForAsyncUtils;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static com.faforever.client.player.SocialStatus.FOE;
import static com.faforever.client.player.SocialStatus.FRIEND;
import static com.faforever.client.player.SocialStatus.OTHER;
import static com.faforever.client.player.SocialStatus.SELF;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ChatUserContextMenuControllerTest extends AbstractPlainJavaFxTest {
  private static final String TEST_USER_NAME = "junit";

  @Mock
  private PreferencesService preferencesService;
  @Mock
  private UiService uiService;
  @Mock
  private PlayerService playerService;
  @Mock
  private ReplayService replayService;
  @Mock
  private NotificationService notificationService;
  @Mock
  private I18n i18n;
  @Mock
  private EventBus eventBus;
  @Mock
  private JoinGameHelper joinGameHelper;
  @Mock
  private AvatarService avatarService;
  @Mock
  private ModeratorService moderatorService;
  @Mock
  private TeamMatchmakingService teamMatchmakingService;
  @Mock
  private ReportDialogController reportDialogController;

  private ChatUserContextMenuController instance;
  private Player player;
  private ChatChannelUser chatUser;

  @Before
  public void setUp() throws Exception {
    instance = new ChatUserContextMenuController(preferencesService, playerService,
        replayService, notificationService, i18n, eventBus, joinGameHelper,
        avatarService, uiService, moderatorService, teamMatchmakingService);

    Preferences preferences = PreferencesBuilder.create().defaultValues().get();

    when(preferencesService.getPreferences()).thenReturn(preferences);
    when(avatarService.getAvailableAvatars()).thenReturn(CompletableFuture.completedFuture(Arrays.asList(
        new AvatarBean(new URL("http://www.example.com/avatar1.png"), "Avatar Number #1"),
        new AvatarBean(new URL("http://www.example.com/avatar2.png"), "Avatar Number #2"),
        new AvatarBean(new URL("http://www.example.com/avatar3.png"), "Avatar Number #3")
    )));
    when(moderatorService.getPermissions())
        .thenReturn(CompletableFuture.completedFuture(Collections.emptySet()));
    when(uiService.loadFxml("theme/reporting/report_dialog.fxml")).thenReturn(reportDialogController);


    loadFxml("theme/chat/chat_user_context_menu.fxml", clazz -> instance);

    player = PlayerBuilder.create(TEST_USER_NAME).socialStatus(SELF).avatar(null).game(new Game()).get();
    chatUser = ChatChannelUserBuilder.create(TEST_USER_NAME).defaultValues().player(player).get();
  }

  @Test
  public void testKickBanContextMenuNotShownForNormalUser() {
    when(moderatorService.getPermissions())
        .thenReturn(CompletableFuture.completedFuture(Collections.emptySet()));
    instance.setChatUser(chatUser);
    player.setSocialStatus(FOE);
    WaitForAsyncUtils.waitForFxEvents();

    assertFalse(instance.banItem.isVisible());
    assertFalse(instance.kickGameItem.isVisible());
    assertFalse(instance.kickLobbyItem.isVisible());
    assertFalse(instance.moderatorActionSeparator.isVisible());
  }

  @Test
  public void testKickBanContextMenuNotShownForSelf() {
    player.setSocialStatus(SELF);
    instance.setChatUser(chatUser);

    assertFalse(instance.banItem.isVisible());
    assertFalse(instance.kickGameItem.isVisible());
    assertFalse(instance.kickLobbyItem.isVisible());
    assertFalse(instance.moderatorActionSeparator.isVisible());
  }

  @Test
  public void testKickContextMenuShownForMod() {
    Set<String> permissions = Sets.newHashSet(GroupPermission.ADMIN_KICK_SERVER);
    when(moderatorService.getPermissions())
        .thenReturn(CompletableFuture.completedFuture(permissions));
    player.setSocialStatus(FOE);
    instance.setChatUser(chatUser);

    assertFalse(instance.banItem.isVisible());
    assertTrue(instance.kickGameItem.isVisible());
    assertTrue(instance.kickLobbyItem.isVisible());
    assertTrue(instance.moderatorActionSeparator.isVisible());
  }

  @Test
  public void testBanContextMenuShownForMod() {
    Set<String> permissions = Sets.newHashSet(GroupPermission.ROLE_ADMIN_ACCOUNT_BAN);
    when(moderatorService.getPermissions())
        .thenReturn(CompletableFuture.completedFuture(permissions));
    player.setSocialStatus(FOE);
    instance.setChatUser(chatUser);

    assertTrue(instance.banItem.isVisible());
    assertFalse(instance.kickGameItem.isVisible());
    assertFalse(instance.kickLobbyItem.isVisible());
    assertTrue(instance.moderatorActionSeparator.isVisible());
  }

  @Test
  public void testJoinGameContextMenuNotShownForIdleUser() {
    instance.setChatUser(chatUser);
    player.setSocialStatus(OTHER);

    assertFalse(instance.joinGameItem.isVisible());
  }

  @Test
  public void testJoinGameContextMenuShownForHostingUser() {
    Game game = new Game();
    game.setFeaturedMod(KnownFeaturedMod.FAF.getTechnicalName());
    game.setStatus(GameStatus.OPEN);
    game.setHost(player.getUsername());

    player.setSocialStatus(OTHER);
    player.setGame(game);
    instance.setChatUser(chatUser);

    assertEquals(player.getStatus(), PlayerStatus.HOSTING);
    assertTrue(instance.joinGameItem.isVisible());
  }

  @Test
  public void testJoinGameContextMenuNotShownForMatchmakerPlayer() {
    Game game = new Game();
    game.setGameType(GameType.MATCHMAKER);
    game.setStatus(GameStatus.OPEN);
    game.setHost(player.getUsername());

    player.setSocialStatus(OTHER);
    player.setGame(game);
    instance.setChatUser(chatUser);

    assertEquals(player.getStatus(), PlayerStatus.HOSTING);
    assertFalse(instance.joinGameItem.isVisible());
  }

  @Test
  public void testInviteContextMenuShownForIdleUser() {
    instance.setChatUser(chatUser);
    player.setGame(null);
    player.setSocialStatus(OTHER);

    assertTrue(instance.inviteItem.isVisible());
  }

  @Test
  public void testInviteContextMenuNotShownForHostingUser() {
    Game game = new Game();
    game.setFeaturedMod(KnownFeaturedMod.FAF.getTechnicalName());
    game.setStatus(GameStatus.OPEN);
    game.setHost(player.getUsername());

    player.setSocialStatus(OTHER);
    player.setGame(game);
    instance.setChatUser(chatUser);

    assertEquals(player.getStatus(), PlayerStatus.HOSTING);
    assertFalse(instance.inviteItem.isVisible());
  }

  @Test
  public void testInviteContextMenuNotShownForLobbyingUser() {
    Game game = new Game();
    game.setFeaturedMod(KnownFeaturedMod.FAF.getTechnicalName());
    game.setStatus(GameStatus.OPEN);
    game.setHost("otherPlayer");

    player.setSocialStatus(OTHER);
    player.setGame(game);
    instance.setChatUser(chatUser);

    assertEquals(player.getStatus(), PlayerStatus.LOBBYING);
    assertFalse(instance.inviteItem.isVisible());
  }

  @Test
  public void testInviteContextMenuNotShownForPlayingUser() {
    Game game = new Game();
    game.setFeaturedMod(KnownFeaturedMod.FAF.getTechnicalName());
    game.setStatus(GameStatus.PLAYING);
    game.setHost(player.getUsername());

    player.setSocialStatus(OTHER);
    player.setGame(game);
    instance.setChatUser(chatUser);

    assertEquals(player.getStatus(), PlayerStatus.PLAYING);
    assertFalse(instance.inviteItem.isVisible());
  }

  @Test
  public void testOnSendPrivateMessage() {
    instance.setChatUser(chatUser);

    instance.onSendPrivateMessageSelected();

    verify(eventBus).post(any(InitiatePrivateChatEvent.class));
  }

  @Test
  public void testOnAddFriendWithFoe() {
    instance.setChatUser(chatUser);

    player.setSocialStatus(FOE);

    instance.onAddFriendSelected();

    verify(playerService).removeFoe(player);
    verify(playerService).addFriend(player);
  }

  @Test
  public void testOnAddFriendWithNeutral() {
    player.setSocialStatus(OTHER);

    instance.setChatUser(chatUser);

    instance.onAddFriendSelected();

    verify(playerService, never()).removeFoe(player);
    verify(playerService).addFriend(player);
  }

  @Test
  public void testOnRemoveFriend() {
    instance.setChatUser(chatUser);

    instance.onRemoveFriendSelected();

    verify(playerService).removeFriend(player);
  }

  @Test
  public void testOnAddFoeWithFriend() {
    instance.setChatUser(chatUser);

    player.setSocialStatus(FRIEND);

    instance.onAddFoeSelected();

    verify(playerService).removeFriend(player);
    verify(playerService).addFoe(player);
  }

  @Test
  public void testOnAddFoeWithNeutral() {
    instance.setChatUser(chatUser);

    player.setSocialStatus(OTHER);

    instance.onAddFoeSelected();

    verify(playerService, never()).removeFriend(player);
    verify(playerService).addFoe(player);
  }

  @Test
  public void testOnRemoveFoe() {
    instance.setChatUser(chatUser);

    instance.onRemoveFoeSelected();

    verify(playerService).removeFoe(player);
  }

  @Test
  public void testOnWatchGame() {
    instance.setChatUser(chatUser);

    instance.onWatchGameSelected();

    verify(replayService).runLiveReplay(player.getGame().getId());
  }

  @Test
  public void testOnWatchGameThrowsIoExceptionTriggersNotification() {
    instance.setChatUser(chatUser);

    doThrow(new RuntimeException("Error in runLiveReplay")).when(replayService).runLiveReplay(anyInt());

    instance.onWatchGameSelected();

    verify(notificationService).addImmediateErrorNotification(any(RuntimeException.class), eq("replays.live.loadFailure.message"));
  }

  @Test
  public void testOnReportChatUser() {
    instance.setChatUser(chatUser);

    instance.onReport();

    verify(reportDialogController).setOffender(player);
    verify(reportDialogController).setOwnerWindow(instance.getRoot().getOwnerWindow());
    verify(reportDialogController).show();
  }

  @Test
  public void testOnReportPlayer() {
    chatUser.setPlayer(null);
    instance.setChatUser(chatUser);

    instance.onReport();

    verify(reportDialogController).setOffender(chatUser.getUsername());
    verify(reportDialogController).setOwnerWindow(instance.getRoot().getOwnerWindow());
    verify(reportDialogController).show();
  }

  @Test
  public void testOnJoinGame() {
    instance.setChatUser(chatUser);

    instance.onJoinGameSelected();

    verify(joinGameHelper).join(any());
  }

  @Test
  public void testOnInvite() {
    instance.setChatUser(chatUser);

    instance.onInviteToGameSelected();

    verify(teamMatchmakingService).invitePlayer(TEST_USER_NAME);
  }

  @Test
  public void testOnSelectAvatar() throws Exception {
    instance.setChatUser(chatUser);
    instance.avatarComboBox.show();

    WaitForAsyncUtils.waitForAsyncFx(100_000, () -> instance.avatarComboBox.getSelectionModel().select(2));

    ArgumentCaptor<AvatarBean> captor = ArgumentCaptor.forClass(AvatarBean.class);
    verify(avatarService).changeAvatar(captor.capture());

    AvatarBean avatarBean = captor.getValue();
    assertThat(avatarBean.getUrl(), equalTo(new URL("http://www.example.com/avatar2.png")));
    assertThat(avatarBean.getDescription(), is("Avatar Number #2"));
  }

  @Test
  public void testHideUserInfoIfNoPlayer() {
    chatUser.setPlayer(null);
    instance.setChatUser(chatUser);
    assertThat(instance.showUserInfo.isVisible(), is(false));
    assertThat(instance.viewReplaysItem.isVisible(), is(false));
  }
}
