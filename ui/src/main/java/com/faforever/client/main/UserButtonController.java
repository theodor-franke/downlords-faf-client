package com.faforever.client.main;

import com.faforever.client.chat.UserInfoWindowController;
import com.faforever.client.fx.Controller;
import com.faforever.client.player.PlayerService;
import com.faforever.client.theme.UiService;
import com.faforever.client.user.LogOutRequestEvent;
import com.faforever.client.user.LoginSuccessEvent;
import com.faforever.client.util.IdenticonUtil;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.MenuButton;
import javafx.scene.image.ImageView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserButtonController implements Controller<Node> {

  private final PlayerService playerService;
  private final UiService uiService;
  private final ApplicationEventPublisher eventPublisher;

  public MenuButton userButtonRoot;
  public ImageView userImageView;
  /** To protect against application events when the FXML component has not yet been initialized. */
  private boolean initialized;

  public UserButtonController(PlayerService playerService, UiService uiService, ApplicationEventPublisher eventPublisher) {
    this.playerService = playerService;
    this.uiService = uiService;
    this.eventPublisher = eventPublisher;
  }

  @Override
  public void initialize() {
    initialized = true;
  }

  @EventListener
  public void onLoginSuccessEvent(LoginSuccessEvent event) {
    if (!initialized) {
      log.warn("Ignoring player online event as this component has not yet been initialized: {}", this);
      return;
    }

    Platform.runLater(() -> {
      userButtonRoot.setText(event.getDisplayName());
      userImageView.setImage(IdenticonUtil.createIdenticon(event.getUserId()));
    });
  }

  @Override
  public Node getRoot() {
    return userButtonRoot;
  }

  public void onShowProfile() {
    UserInfoWindowController userInfoWindowController = uiService.loadFxml("theme/user_info_window.fxml");
    userInfoWindowController.setPlayer(playerService.getCurrentPlayer().orElseThrow(() -> new IllegalStateException("Player has not been set")));
    userInfoWindowController.setOwnerWindow(userButtonRoot.getScene().getWindow());
    userInfoWindowController.show();
  }

  public void onLogOut() {
    eventPublisher.publishEvent(new LogOutRequestEvent());
  }
}
