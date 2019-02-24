package com.faforever.client.play;


import com.faforever.client.fx.Controller;
import com.faforever.client.i18n.I18n;
import com.faforever.client.player.Player;
import com.faforever.client.theme.UiService;
import javafx.collections.ObservableMap;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TeamCardController implements Controller<Node> {
  private final UiService uiService;
  private final I18n i18n;

  public Pane teamPaneRoot;
  public VBox teamPane;
  public Label teamNameLabel;

  public TeamCardController(UiService uiService, I18n i18n) {
    this.uiService = uiService;
    this.i18n = i18n;
  }

  /**
   * Creates a new {@link TeamCardController} and adds its root to the specified {@code teamsPane}.
   *
   * @param teamsList a mapping of team name (e.g. "2") to a list of player names that are in that team
   */
  static void createAndAdd(ObservableMap<Integer, List<Player>> teamsList, UiService uiService, Pane teamsPane, String leaderboardName) {
    teamsList.forEach((key, players) -> {
      TeamCardController teamCardController = uiService.loadFxml("theme/team_card.fxml");
      teamCardController.setPlayersInTeam(key, players, leaderboardName);
      teamsPane.getChildren().add(teamCardController.getRoot());
    });
  }

  public void setPlayersInTeam(int team, List<Player> playerList, String leaderboardName) {
    int totalRank = 0;
    for (Player player : playerList) {
      // If the server wasn't bugged, this would never be the case.
      if (player == null) {
        continue;
      }
      PlayerCardTooltipController playerCardTooltipController = uiService.loadFxml("theme/player_card_tooltip.fxml");
      Integer rank = player.getRating().get(leaderboardName);
      if (rank != null) {
        totalRank += rank;
      }
      playerCardTooltipController.setPlayer(player, rank);

      teamPane.getChildren().add(playerCardTooltipController.getRoot());
    }

    String teamTitle;
    if (team == 1 || team == -1) {
      teamTitle = i18n.get("game.tooltip.teamTitleNoTeam");
    } else if (team == 0) {
      teamTitle = i18n.get("game.tooltip.observers");
    } else {
      // TODO display average rank instead
      teamTitle = i18n.get("game.tooltip.teamTitle", team - 1, totalRank);
    }
    teamNameLabel.setText(teamTitle);
  }

  public Node getRoot() {
    return teamPaneRoot;
  }
}
