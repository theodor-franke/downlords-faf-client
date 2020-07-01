package com.faforever.client.leaderboard;

import com.faforever.client.chat.InitiatePrivateChatEvent;
import com.faforever.client.chat.UserInfoWindowController;
import com.faforever.client.fx.Controller;
import com.faforever.client.main.event.NavigateEvent;
import com.faforever.client.main.event.ShowUserReplaysEvent;
import com.faforever.client.player.Player;
import com.faforever.client.player.PlayerService;
import com.faforever.client.theme.UiService;
import com.google.common.eventbus.EventBus;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.faforever.client.player.SocialStatus.FOE;
import static com.faforever.client.player.SocialStatus.FRIEND;
import static com.faforever.client.player.SocialStatus.SELF;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
public class LeaderboardUserContextMenuController implements Controller<ContextMenu> {
  private final UiService uiService;
  private Player player;
  private final EventBus eventBus;
  private final PlayerService playerService;
  public ContextMenu leaderboardUserContextMenuRoot;
  public MenuItem addFriendItem;
  public MenuItem removeFriendItem;
  public MenuItem addFoeItem;
  public MenuItem removeFoeItem;
  public MenuItem sendPrivateMessageItem;

  public LeaderboardUserContextMenuController (UiService uiService, EventBus eventBus, PlayerService playerService) {
    this.uiService = uiService;
    this.eventBus = eventBus;
    this.playerService = playerService;
  }

  /*This method doesn't just set the player: it also sets up the environment for the context menu*/
  public void setPlayer(Player player) {
    this.player = player;

    sendPrivateMessageItem.visibleProperty().bind(player.socialStatusProperty().isNotEqualTo(SELF));
    addFriendItem.visibleProperty().bind(
        player.socialStatusProperty().isNotEqualTo(FRIEND).and(player.socialStatusProperty().isNotEqualTo(SELF))
    );
    removeFriendItem.visibleProperty().bind(player.socialStatusProperty().isEqualTo(FRIEND));
    addFoeItem.visibleProperty().bind(player.socialStatusProperty().isNotEqualTo(FOE).and(player.socialStatusProperty().isNotEqualTo(SELF)));
    removeFoeItem.visibleProperty().bind(player.socialStatusProperty().isEqualTo(FOE));
  }

  public void onShowUserInfoSelected() {
    UserInfoWindowController userInfoWindowController = uiService.loadFxml("theme/user_info_window.fxml");
    userInfoWindowController.setPlayer(player);
    userInfoWindowController.setOwnerWindow(leaderboardUserContextMenuRoot.getOwnerWindow());
    userInfoWindowController.show();
  }

  public void onSendPrivateMessageSelected() {
    eventBus.post(new InitiatePrivateChatEvent(player.getUsername()));
  }

  public void onCopyUsernameSelected() {
    ClipboardContent clipboardContent = new ClipboardContent();
    clipboardContent.putString(player.getUsername());
    Clipboard.getSystemClipboard().setContent(clipboardContent);
  }

  public void onAddFriendSelected() {
    if (player.getSocialStatus() == FOE) {
      playerService.removeFoe(player);
    }
    playerService.addFriend(player);
  }

  public void onRemoveFriendSelected() {
    playerService.removeFriend(player);
  }

  public void onAddFoeSelected() {
    if (player.getSocialStatus() == FRIEND) {
      playerService.removeFriend(player);
    }
    playerService.addFoe(player);
  }

  public void onRemoveFoeSelected() {
    playerService.removeFoe(player);
  }

  public void onViewReplaysSelected() {
    eventBus.post(new ShowUserReplaysEvent(player.getId()));
  }

  ContextMenu getContextMenu() {
    return leaderboardUserContextMenuRoot;
  }

  @Override
  public ContextMenu getRoot() { return leaderboardUserContextMenuRoot; }

  @Override
  public void initialize() {

  }

}
