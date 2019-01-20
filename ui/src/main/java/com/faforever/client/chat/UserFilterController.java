package com.faforever.client.chat;

import com.faforever.client.fx.Controller;
import com.faforever.client.game.PlayerStatus;
import com.faforever.client.i18n.I18n;
import com.faforever.client.player.Player;
import com.faforever.client.util.ProgrammingError;
import com.google.common.annotations.VisibleForTesting;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserFilterController implements Controller<Node> {

  private final I18n i18n;
  private final BooleanProperty filterApplied;
  public MenuButton gameStatusMenu;
  public GridPane filterUserRoot;
  public TextField clanField;
  public TextField minRankField;
  public TextField maxRankField;
  public ToggleGroup gameStatusToggleGroup;
  @VisibleForTesting
  ChannelTabController channelTabController;
  @VisibleForTesting
  PlayerStatus playerStatusFilter;

  public UserFilterController(I18n i18n) {
    this.i18n = i18n;
    this.filterApplied = new SimpleBooleanProperty(false);
  }

  void setChannelController(ChannelTabController channelTabController) {
    this.channelTabController = channelTabController;
  }

  public void initialize() {
    clanField.textProperty().addListener((observable, oldValue, newValue) -> filterUsers());
    minRankField.textProperty().addListener((observable, oldValue, newValue) -> filterUsers());
    maxRankField.textProperty().addListener((observable, oldValue, newValue) -> filterUsers());
  }

  private void filterUsers() {
    channelTabController.setUserFilter(this::filterUser);
    filterApplied.set(
        !maxRankField.getText().isEmpty()
            || !minRankField.getText().isEmpty()
            || !clanField.getText().isEmpty()
            || playerStatusFilter != null
    );
  }

  private boolean filterUser(CategoryOrChatUserListItem userListItem) {
    if (userListItem.getUser() == null) {
      return false;
    }
    ChatChannelUser user = userListItem.getUser();
    return channelTabController.isUsernameMatch(user)
        && isInClan(user)
        && isBoundByRating(user)
        && isGameStatusMatch(user);
  }

  public BooleanProperty filterAppliedProperty() {
    return filterApplied;
  }

  public boolean isFilterApplied() {
    return filterApplied.get();
  }

  @VisibleForTesting
  boolean isInClan(ChatChannelUser chatUser) {
    if (clanField.getText().isEmpty()) {
      return true;
    }

    Optional<Player> playerOptional = chatUser.getPlayer();

    if (!playerOptional.isPresent()) {
      return false;
    }

    Player player = playerOptional.get();
    String clan = player.getClanTag();
    if (clan == null) {
      return false;
    }

    String lowerCaseSearchString = clan.toLowerCase();
    return lowerCaseSearchString.contains(clanField.getText().toLowerCase());
  }

  @VisibleForTesting
  boolean isBoundByRating(ChatChannelUser chatUser) {
    if (minRankField.getText().isEmpty() && maxRankField.getText().isEmpty()) {
      return true;
    }

    Optional<Player> optionalPlayer = chatUser.getPlayer();

    if (!optionalPlayer.isPresent()) {
      return false;
    }

    Player player = optionalPlayer.get();

    // TODO support multiple ranks?
    Integer rank = player.getRanks().values().stream().findFirst().orElse(0);
    int minRating;
    int maxRating;

    try {
      minRating = Integer.parseInt(minRankField.getText());
    } catch (NumberFormatException e) {
      minRating = Integer.MIN_VALUE;
    }
    try {
      maxRating = Integer.parseInt(maxRankField.getText());
    } catch (NumberFormatException e) {
      maxRating = Integer.MAX_VALUE;
    }

    return rank >= minRating && rank <= maxRating;
  }

  @VisibleForTesting
  boolean isGameStatusMatch(ChatChannelUser chatUser) {
    if (playerStatusFilter == null) {
      return true;
    }

    Optional<Player> playerOptional = chatUser.getPlayer();

    if (!playerOptional.isPresent()) {
      return false;
    }

    Player player = playerOptional.get();
    PlayerStatus playerStatus = player.getStatus();
    if (playerStatusFilter == PlayerStatus.LOBBYING) {
      return PlayerStatus.LOBBYING == playerStatus || PlayerStatus.HOSTING == playerStatus;
    } else {
      return playerStatusFilter == playerStatus;
    }
  }

  public void onGameStatusPlaying() {
    updateGameStatusMenuText(playerStatusFilter == PlayerStatus.PLAYING ? null : PlayerStatus.PLAYING);
    filterUsers();
  }

  public void onGameStatusLobby() {
    updateGameStatusMenuText(playerStatusFilter == PlayerStatus.LOBBYING ? null : PlayerStatus.LOBBYING);
    filterUsers();
  }

  public void onGameStatusNone() {
    updateGameStatusMenuText(playerStatusFilter == PlayerStatus.IDLE ? null : PlayerStatus.IDLE);
    filterUsers();
  }

  private void updateGameStatusMenuText(PlayerStatus status) {
    playerStatusFilter = status;
    if (status == null) {
      gameStatusMenu.setText(i18n.get("game.gameStatus"));
      gameStatusToggleGroup.selectToggle(null);
      return;
    }

    switch (status) {
      case PLAYING:
        gameStatusMenu.setText(i18n.get("game.gameStatus.playing"));
        break;
      case LOBBYING:
        gameStatusMenu.setText(i18n.get("game.gameStatus.lobby"));
        break;
      case IDLE:
        gameStatusMenu.setText(i18n.get("game.gameStatus.none"));
        break;
      default:
        throw new ProgrammingError("Uncovered player status: " + status);
    }
  }

  public Node getRoot() {
    return filterUserRoot;
  }
}
