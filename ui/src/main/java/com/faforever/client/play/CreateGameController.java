package com.faforever.client.play;

import com.faforever.client.fx.Controller;
import com.faforever.client.fx.JavaFxUtil;
import com.faforever.client.fx.StringListCell;
import com.faforever.client.game.FaStrings;
import com.faforever.client.game.GameService;
import com.faforever.client.game.GameVisibility;
import com.faforever.client.game.HostGameRequest;
import com.faforever.client.game.KnownFeaturedMod;
import com.faforever.client.i18n.I18n;
import com.faforever.client.map.FaMap;
import com.faforever.client.map.MapPreviewService;
import com.faforever.client.map.MapService;
import com.faforever.client.map.MapService.PreviewSize;
import com.faforever.client.map.MapSize;
import com.faforever.client.map.generator.MapGeneratorService;
import com.faforever.client.mod.FeaturedMod;
import com.faforever.client.mod.ModService;
import com.faforever.client.mod.ModVersion;
import com.faforever.client.net.ConnectionState;
import com.faforever.client.notification.ImmediateErrorNotification;
import com.faforever.client.notification.NotificationService;
import com.faforever.client.preferences.PreferenceUpdateListener;
import com.faforever.client.preferences.PreferencesService;
import com.faforever.client.remote.FafService;
import com.faforever.client.reporting.ReportingService;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import org.apache.maven.artifact.versioning.ComparableVersion;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static javafx.scene.layout.BackgroundPosition.CENTER;
import static javafx.scene.layout.BackgroundRepeat.NO_REPEAT;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CreateGameController implements Controller<Pane> {

  private static final int MAX_RATING_LENGTH = 4;
  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private final MapService mapService;
  private final MapPreviewService mapPreviewService;
  private final ModService modService;
  private final GameService gameService;
  private final PreferencesService preferencesService;
  private final I18n i18n;
  private final NotificationService notificationService;
  private final ReportingService reportingService;
  private final FafService fafService;
  private final MapGeneratorService mapGeneratorService;
  public Label mapSizeLabel;
  public Label mapPlayersLabel;
  public Label mapDescriptionLabel;
  public Label mapNameLabel;
  public TextField mapSearchTextField;
  public TextField titleTextField;
  public ListView<ModVersion> modListView;
  public TextField passwordTextField;
  public TextField minRankTextField;
  public TextField maxRankTextField;
  public ListView<FeaturedMod> featuredModListView;
  public ListView<FaMap> mapListView;
  public Pane createGameRoot;
  public Button createGameButton;
  public Pane mapPreviewPane;
  public Label versionLabel;
  public CheckBox onlyForFriendsCheckBox;
  @VisibleForTesting
  FilteredList<FaMap> filteredFaMaps;
  private Runnable onCloseButtonClickedListener;
  private PreferenceUpdateListener preferenceUpdateListener;
  /**
   * Remembers if the controller's init method was called, to avoid memory leaks by adding several listeners
   */
  private boolean initialized;


  public CreateGameController(
    FafService fafService,
    MapService mapService,
    MapPreviewService mapPreviewService,
    ModService modService,
    GameService gameService,
    PreferencesService preferencesService,
    I18n i18n,
    NotificationService notificationService,
    ReportingService reportingService,
    MapGeneratorService mapGeneratorService
  ) {
    this.mapService = mapService;
    this.mapPreviewService = mapPreviewService;
    this.modService = modService;
    this.gameService = gameService;
    this.preferencesService = preferencesService;
    this.i18n = i18n;
    this.notificationService = notificationService;
    this.reportingService = reportingService;
    this.fafService = fafService;
    this.mapGeneratorService = mapGeneratorService;
  }

  public void initialize() {
    versionLabel.managedProperty().bind(versionLabel.visibleProperty());

    mapPreviewPane.prefHeightProperty().bind(mapPreviewPane.widthProperty());
    mapSearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue.isEmpty()) {
        filteredFaMaps.setPredicate(mapInfoBean -> true);
      } else {
        filteredFaMaps.setPredicate(mapInfoBean -> mapInfoBean.getDisplayName().toLowerCase().contains(newValue.toLowerCase())
          || mapInfoBean.getFolderName().toLowerCase().contains(newValue.toLowerCase()));
      }
      if (!filteredFaMaps.isEmpty()) {
        mapListView.getSelectionModel().select(0);
      }
    });
    mapSearchTextField.setOnKeyPressed(event -> {
      MultipleSelectionModel<FaMap> selectionModel = mapListView.getSelectionModel();
      int currentMapIndex = selectionModel.getSelectedIndex();
      int newMapIndex = currentMapIndex;
      if (KeyCode.DOWN == event.getCode()) {
        if (filteredFaMaps.size() > currentMapIndex + 1) {
          newMapIndex++;
        }
        event.consume();
      } else if (KeyCode.UP == event.getCode()) {
        if (currentMapIndex > 0) {
          newMapIndex--;
        }
        event.consume();
      }
      selectionModel.select(newMapIndex);
      mapListView.scrollTo(newMapIndex);
    });

    featuredModListView.setCellFactory(param -> new StringListCell<>(FeaturedMod::getDisplayName));

    JavaFxUtil.makeNumericTextField(minRankTextField, MAX_RATING_LENGTH);
    JavaFxUtil.makeNumericTextField(maxRankTextField, MAX_RATING_LENGTH);

    modService.getFeaturedMods().thenAccept(featuredModBeans -> Platform.runLater(() -> {
      featuredModListView.setItems(FXCollections.observableList(featuredModBeans).filtered(FeaturedMod::isVisible));
      selectLastOrDefaultGameType();
    }));

    if (preferencesService.getPreferences().getForgedAlliance().getPath() == null) {
      preferenceUpdateListener = preferences -> {
        if (!initialized && preferencesService.getPreferences().getForgedAlliance().getPath() != null) {
          initialized = true;

          Platform.runLater(this::init);
        }
      };
      preferencesService.addUpdateListener(new WeakReference<>(preferenceUpdateListener));
    } else {
      init();
    }
  }

  public void onCloseButtonClicked() {
    onCloseButtonClickedListener.run();
  }


  private void init() {
    bindGameVisibility();
    initModList();
    initMapSelection();
    initFeaturedModList();
    initRatingBoundaries();
    selectLastMap();
    setLastGameTitle();
    titleTextField.textProperty().addListener((observable, oldValue, newValue) -> {
      preferencesService.getPreferences().setLastGameTitle(newValue);
      preferencesService.storeInBackground();
    });

    createGameButton.textProperty().bind(Bindings.createStringBinding(() -> {
      switch (fafService.connectionStateProperty().get()) {
        case DISCONNECTED:
          return i18n.get("game.create.disconnected");
        case CONNECTING:
          return i18n.get("game.create.connecting");
        default:
          break;
      }
      if (Strings.isNullOrEmpty(titleTextField.getText())) {
        return i18n.get("game.create.titleMissing");
      } else if (featuredModListView.getSelectionModel().getSelectedItem() == null) {
        return i18n.get("game.create.featuredModMissing");
      }
      return i18n.get("game.create.create");
    }, titleTextField.textProperty(), featuredModListView.getSelectionModel().selectedItemProperty(), fafService.connectionStateProperty()));

    createGameButton.disableProperty().bind(
      titleTextField.textProperty().isEmpty()
        .or(featuredModListView.getSelectionModel().selectedItemProperty().isNull().or(fafService.connectionStateProperty().isNotEqualTo(ConnectionState.CONNECTED)))
    );
  }

  private void bindGameVisibility() {
    preferencesService.getPreferences()
      .lastGameOnlyFriendsProperty()
      .bindBidirectional(onlyForFriendsCheckBox.selectedProperty());
    onlyForFriendsCheckBox.selectedProperty().addListener(observable -> preferencesService.storeInBackground());
  }

  private void initModList() {
    modListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    modListView.setCellFactory(modListCellFactory());
    modListView.getItems().setAll(modService.getInstalledModVersions());
    try {
      modService.getActivatedSimAndUIMods().forEach(mod -> modListView.getSelectionModel().select(mod));
    } catch (IOException e) {
      logger.error("Activated mods could not be loaded", e);
    }
    modListView.scrollTo(modListView.getSelectionModel().getSelectedItem());
  }

  private void initMapSelection() {
    filteredFaMaps = new FilteredList<>(
      mapService.getInstalledMaps().sorted((o1, o2) -> o1.getDisplayName().compareToIgnoreCase(o2.getDisplayName()))
    );

    mapListView.setItems(filteredFaMaps);
    mapListView.setCellFactory(param -> new StringListCell<>(FaMap::getDisplayName));
    mapListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(() -> setSelectedMap(newValue)));
  }

  private void setSelectedMap(FaMap newValue) {
    JavaFxUtil.assertApplicationThread();

    if (newValue == null) {
      mapNameLabel.setText("");
      return;
    }

    preferencesService.getPreferences().setLastMap(newValue.getFolderName());
    preferencesService.storeInBackground();

    Image largePreview = mapPreviewService.loadPreview(newValue.getFolderName(), PreviewSize.LARGE);
    mapPreviewPane.setBackground(new Background(new BackgroundImage(largePreview, NO_REPEAT, NO_REPEAT, CENTER,
      new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false))));

    MapSize mapSize = newValue.getSize();
    mapSizeLabel.setText(i18n.get("mapPreview.size", mapSize.getWidthInKm(), mapSize.getHeightInKm()));
    mapNameLabel.setText(newValue.getDisplayName());
    mapPlayersLabel.setText(i18n.number(newValue.getPlayers()));
    mapDescriptionLabel.setText(Optional.ofNullable(newValue.getDescription())
      .map(Strings::emptyToNull)
      .map(FaStrings::removeLocalizationTag)
      .orElseGet(() -> i18n.get("map.noDescriptionAvailable")));

    ComparableVersion mapVersion = newValue.getVersion();
    if (mapVersion == null) {
      versionLabel.setVisible(false);
    } else {
      versionLabel.setText(i18n.get("map.versionFormat", mapVersion));
    }
  }

  private void initFeaturedModList() {
    featuredModListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      preferencesService.getPreferences().setLastGameType(newValue.getTechnicalName());
      preferencesService.storeInBackground();
    });
  }

  private void initRatingBoundaries() {
    int lastGameMinRating = preferencesService.getPreferences().getLastGameMinRank();
    int lastGameMaxRating = preferencesService.getPreferences().getLastGameMaxRank();

    minRankTextField.setText(i18n.number(lastGameMinRating));
    maxRankTextField.setText(i18n.number(lastGameMaxRating));

    minRankTextField.textProperty().addListener((observable, oldValue, newValue) -> {
      preferencesService.getPreferences().setLastGameMinRank(Integer.parseInt(newValue));
      preferencesService.storeInBackground();
    });
    maxRankTextField.textProperty().addListener((observable, oldValue, newValue) -> {
      preferencesService.getPreferences().setLastGameMaxRank(Integer.parseInt(newValue));
      preferencesService.storeInBackground();
    });
  }

  private void selectLastMap() {
    String lastMap = preferencesService.getPreferences().getLastMap();
    for (FaMap faMap : mapListView.getItems()) {
      if (faMap.getFolderName().equalsIgnoreCase(lastMap)) {
        mapListView.getSelectionModel().select(faMap);
        mapListView.scrollTo(faMap);
        return;
      }
    }
    if (mapListView.getSelectionModel().isEmpty()) {
      mapListView.getSelectionModel().selectFirst();
    }
  }

  private void setLastGameTitle() {
    titleTextField.setText(Strings.nullToEmpty(preferencesService.getPreferences().getLastGameTitle()));
  }

  @NotNull
  private Callback<ListView<ModVersion>, ListCell<ModVersion>> modListCellFactory() {
    return param -> {
      ListCell<ModVersion> cell = new StringListCell<>(ModVersion::getDisplayName);
      cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
        modListView.requestFocus();
        MultipleSelectionModel<ModVersion> selectionModel = modListView.getSelectionModel();
        if (!cell.isEmpty()) {
          int index = cell.getIndex();
          if (selectionModel.getSelectedIndices().contains(index)) {
            selectionModel.clearSelection(index);
          } else {
            selectionModel.select(index);
          }
          event.consume();
        }
      });
      return cell;
    };
  }

  private void selectLastOrDefaultGameType() {
    String lastGameMod = preferencesService.getPreferences().getLastGameType();
    if (lastGameMod == null) {
      lastGameMod = KnownFeaturedMod.DEFAULT.getTechnicalName();
    }

    for (FeaturedMod mod : featuredModListView.getItems()) {
      if (Objects.equals(mod.getTechnicalName(), lastGameMod)) {
        featuredModListView.getSelectionModel().select(mod);
        featuredModListView.scrollTo(mod);
        break;
      }
    }
  }

  public void onRandomMapButtonClicked() {
    int mapIndex = (int) (Math.random() * filteredFaMaps.size());
    mapListView.getSelectionModel().select(mapIndex);
    mapListView.scrollTo(mapIndex);
  }

  public void onGenerateMapButtonClicked() {
    mapGeneratorService.generateMap().thenAccept(mapName -> {
      Platform.runLater(() -> {
        initMapSelection();
        mapListView.getItems().stream()
            .filter(mapBean -> mapBean.getFolderName().equalsIgnoreCase(mapName))
            .findAny().ifPresent(mapBean -> {
          mapListView.getSelectionModel().select(mapBean);
          mapListView.scrollTo(mapBean);
          setSelectedMap(mapBean);
        });
      });
    });
  }

  public void onCreateButtonClicked() {
    ObservableList<ModVersion> selectedModVersions = modListView.getSelectionModel().getSelectedItems();

    try {
      modService.overrideActivatedMods(modListView.getSelectionModel().getSelectedItems());
    } catch (IOException e) {
      logger.warn("Activated mods could not be updated", e);
    }

    Set<UUID> simMods = selectedModVersions.stream()
      .map(ModVersion::getUuid)
      .collect(Collectors.toSet());

    HostGameRequest hostGameRequest = new HostGameRequest(
      titleTextField.getText(),
      Strings.emptyToNull(passwordTextField.getText()),
      featuredModListView.getSelectionModel().getSelectedItem(),
      mapListView.getSelectionModel().getSelectedItem().getFolderName(),
      simMods,
      onlyForFriendsCheckBox.isSelected() ? GameVisibility.PRIVATE : GameVisibility.PUBLIC,
      Integer.parseInt(minRankTextField.getText()),
      Integer.parseInt(maxRankTextField.getText())
    );

    gameService.hostGame(hostGameRequest).exceptionally(throwable -> {
      logger.warn("Game could not be hosted", throwable);
      notificationService.addNotification(
        new ImmediateErrorNotification(
          i18n.get("errorTitle"),
          i18n.get("game.create.failed"),
          throwable,
          i18n, reportingService
        ));
      return null;
    });

    onCloseButtonClicked();
  }

  public Pane getRoot() {
    return createGameRoot;
  }

  public void onSelectDefaultGameTypeButtonClicked() {
    featuredModListView.getSelectionModel().select(0);
  }

  public void onDeselectModsButtonClicked() {
    modListView.getSelectionModel().clearSelection();
  }

  public void onReloadModsButtonClicked() {
    modService.loadInstalledMods();
    initModList();
  }

  /**
   * @return returns true of the map was found and false if not
   */
  boolean selectMap(String mapFolderName) {
    Optional<FaMap> mapBeanOptional = mapListView.getItems().stream().filter(mapBean -> mapBean.getFolderName().equalsIgnoreCase(mapFolderName)).findAny();
    if (!mapBeanOptional.isPresent()) {
      return false;
    }
    mapListView.getSelectionModel().select(mapBeanOptional.get());
    mapListView.scrollTo(mapBeanOptional.get());
    return true;
  }

  void setOnCloseButtonClickedListener(Runnable onCloseButtonClickedListener) {
    this.onCloseButtonClickedListener = onCloseButtonClickedListener;
  }
}
