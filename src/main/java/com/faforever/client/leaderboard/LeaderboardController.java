package com.faforever.client.leaderboard;

import com.faforever.client.fx.AbstractViewController;
import com.faforever.client.fx.StringCell;
import com.faforever.client.game.KnownFeaturedMod;
import com.faforever.client.i18n.I18n;
import com.faforever.client.main.event.NavigateEvent;
import com.faforever.client.notification.ImmediateErrorNotification;
import com.faforever.client.notification.NotificationService;
import com.faforever.client.reporting.ReportingService;
import com.faforever.client.theme.UiService;
import com.faforever.client.util.Assert;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.Pane;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.lang.ref.WeakReference;

import static javafx.collections.FXCollections.observableList;


@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class LeaderboardController extends AbstractViewController<Node> {

  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private final LeaderboardService leaderboardService;
  private final NotificationService notificationService;
  private final I18n i18n;
  private final ReportingService reportingService;
  public Pane leaderboardRoot;
  public TableColumn<LeaderboardEntry, Number> rankColumn;
  public TableColumn<LeaderboardEntry, String> nameColumn;
  public TableColumn<LeaderboardEntry, Number> winLossColumn;
  public TableColumn<LeaderboardEntry, Number> gamesPlayedColumn;
  public TableColumn<LeaderboardEntry, Number> ratingColumn;
  public TableRow<LeaderboardEntry> leaderboardEntryTableRow;
  public TableView<LeaderboardEntry> ratingTable;
  public TextField searchTextField;
  public Pane connectionProgressPane;
  public Pane contentPane;
  public JFXButton searchButton;
  public Pagination paginationControl;
  private KnownFeaturedMod ratingType;
  private final static int NUMBER_OF_PLAYERS_PER_PAGE = 100;
  private WeakReference<LeaderboardUserContextMenuController> contextMenuController;
  private final UiService uiService;
  public LeaderboardEntry selectedEntry;


  @Override
  public void initialize() {
    super.initialize();
    rankColumn.setCellValueFactory(param -> param.getValue().rankProperty());
    rankColumn.setCellFactory(param -> new StringCell<>(rank -> i18n.number(rank.intValue())));

    nameColumn.setCellValueFactory(param -> param.getValue().usernameProperty());
    nameColumn.setCellFactory(param -> new StringCell<>(name -> name));

    winLossColumn.setCellValueFactory(param -> param.getValue().winLossRatioProperty());
    winLossColumn.setCellFactory(param -> new StringCell<>(number -> i18n.get("percentage", number.floatValue() * 100)));

    gamesPlayedColumn.setCellValueFactory(param -> param.getValue().gamesPlayedProperty());
    gamesPlayedColumn.setCellFactory(param -> new StringCell<>(count -> i18n.number(count.intValue())));

    ratingColumn.setCellValueFactory(param -> param.getValue().ratingProperty());
    ratingColumn.setCellFactory(param -> new StringCell<>(rating -> i18n.number(rating.intValue())));

    contentPane.managedProperty().bind(contentPane.visibleProperty());
    connectionProgressPane.managedProperty().bind(connectionProgressPane.visibleProperty());
    connectionProgressPane.visibleProperty().bind(contentPane.visibleProperty().not());
    paginationControl.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> updateTable(newValue.intValue()));

    ratingTable.setRowFactory(param -> {
      leaderboardEntryTableRow = new TableRow<>();
      leaderboardEntryTableRow.setOnContextMenuRequested(this::onContextMenuRequested);
      return leaderboardEntryTableRow;
    });
  }


  @Override
  protected void onDisplay(NavigateEvent navigateEvent) {
    paginationControl.currentPageIndexProperty().setValue(0);//initialize table
    updateTable(0);
  }

  public Node getRoot() {
    return leaderboardRoot;
  }

  public void setRatingType(KnownFeaturedMod ratingType) {
    this.ratingType = ratingType;
  }

  public void handleSearchButtonClicked(ActionEvent event) {
    paginationControl.currentPageIndexProperty().setValue(0);
    updateTable(0);
  }

  private void  updateTable(int currentPage)
  {
    String searchTextFieldText = searchTextField.getText();
    Assert.checkNullIllegalState(ratingType, "ratingType must not be null");

    contentPane.setVisible(false);
    leaderboardService.getSearchResults(ratingType, searchTextFieldText,currentPage + 1, NUMBER_OF_PLAYERS_PER_PAGE).thenAccept(leaderboardEntryBeans -> {
      Platform.runLater(() -> {
        ratingTable.setItems(observableList(leaderboardEntryBeans));
        contentPane.setVisible(true);
      });
    }).exceptionally(throwable -> {
      Platform.runLater(() -> {
        contentPane.setVisible(false);
        logger.warn("Error while loading leaderboard entries", throwable);
        notificationService.addNotification(new ImmediateErrorNotification(
            i18n.get("errorTitle"), i18n.get("leaderboard.failedToLoad"),
            throwable, i18n, reportingService
        ));
      });
      return null;

    });
  }


  public void onContextMenuRequested(ContextMenuEvent contextMenuEvent) {

    if (contextMenuController != null) {
      LeaderboardUserContextMenuController controller = contextMenuController.get();
      if (controller != null) {
        controller.getContextMenu().show(leaderboardRoot.getScene().getWindow(), contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
        return;
      }
    }

    TableViewSelectionModel<LeaderboardEntry> leaderboardEntryTableViewSelectionModel = ratingTable.getSelectionModel();
    ObservableList<LeaderboardEntry> selectedEntryList = leaderboardEntryTableViewSelectionModel.getSelectedItems();
    selectedEntryList.addListener((ListChangeListener<LeaderboardEntry>) change -> {
      selectedEntry = change.getList().get(0);
    });

    LeaderboardUserContextMenuController controller = uiService.loadFxml("theme\\leaderboard\\leaderboard_user_context_menu.fxml");

    leaderboardService.getPlayerObjectsById(selectedEntryList.get(0).getId()).thenAccept(players -> Platform.runLater(() -> {
      controller.setPlayer(players.get(0));
      controller.getContextMenu().show(leaderboardRoot.getScene().getWindow(), contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
      contextMenuController = new WeakReference<>(controller);
    }));
  }
}
