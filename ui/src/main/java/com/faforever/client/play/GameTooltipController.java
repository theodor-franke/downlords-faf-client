package com.faforever.client.play;


import com.faforever.client.fx.Controller;
import com.faforever.client.fx.JavaFxUtil;
import com.faforever.client.game.Game;
import com.faforever.client.player.Player;
import com.faforever.client.player.PlayerService;
import com.faforever.client.theme.UiService;
import com.google.common.base.Joiner;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.collections.ObservableMap;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
public class GameTooltipController implements Controller<Node> {

  private final UiService uiService;
  private final PlayerService playerService;

  public TitledPane modsPane;
  public Pane teamsPane;
  public Label modsLabel;
  public VBox gameTooltipRoot;
  private ObservableMap<Integer, List<Player>> lastTeams;
  private ObservableMap<UUID, String> lastSimMods;
  @SuppressWarnings("FieldCanBeLocal")
  private InvalidationListener teamChangedListener;
  @SuppressWarnings("FieldCanBeLocal")
  private InvalidationListener simModsChangedListener;
  private WeakInvalidationListener weakTeamChangeListener;
  private WeakInvalidationListener weakModChangeListener;


  public GameTooltipController(UiService uiService, PlayerService playerService) {
    this.uiService = uiService;
    this.playerService = playerService;
  }

  public void initialize() {
    modsPane.managedProperty().bind(modsPane.visibleProperty());
  }

  public void setGame(Game game) {
    teamChangedListener = change -> createTeams(game);
    simModsChangedListener = change -> createModsList(game.getSimMods());

    if (lastTeams != null && weakTeamChangeListener != null) {
      lastTeams.removeListener(weakTeamChangeListener);
    }

    if (lastSimMods != null && weakModChangeListener != null) {
      game.getSimMods().removeListener(weakModChangeListener);
    }

    lastSimMods = game.getSimMods();
    lastTeams = game.getTeams();
    createTeams(game);
    createModsList(game.getSimMods());
    weakTeamChangeListener = new WeakInvalidationListener(teamChangedListener);
    JavaFxUtil.addListener(game.getTeams(),weakTeamChangeListener);
    weakModChangeListener = new WeakInvalidationListener(simModsChangedListener);
    JavaFxUtil.addListener(game.getSimMods(),weakModChangeListener);
  }

  private void createTeams(Game game) {
    Platform.runLater(() -> {
      ObservableMap<Integer, List<Player>> teams = game.getTeams();
      synchronized (teams) {
        teamsPane.getChildren().clear();
        TeamCardController.createAndAdd(teams, uiService, teamsPane, game.getFeaturedMod());
      }
    });
  }

  private void createModsList(ObservableMap<UUID, String> simMods) {
    String stringSimMods = Joiner.on(System.getProperty("line.separator")).join(simMods.values());
    Platform.runLater(() -> {
      if (simMods.isEmpty()) {
        modsPane.setVisible(false);
        return;
      }

      modsLabel.setText(stringSimMods);
      modsPane.setVisible(true);
    });
  }

  public Node getRoot() {
    return gameTooltipRoot;
  }
}
