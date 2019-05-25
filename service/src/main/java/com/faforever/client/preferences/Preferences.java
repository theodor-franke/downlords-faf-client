package com.faforever.client.preferences;

import com.faforever.client.game.KnownFeaturedMod;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.TableColumn.SortType;
import javafx.util.Pair;
import lombok.Getter;

import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;

import static javafx.collections.FXCollections.observableArrayList;

// Remnants of the time when ratings instead of ranks were used
@JsonIgnoreProperties({"lastGameMinRating", "lastGameMaxRating"})
public class Preferences {

  public static final String DEFAULT_THEME_NAME = "default";

  private final WindowPrefs mainWindow;
  private final ForgedAlliancePrefs forgedAlliance;
  private final LoginPrefs login;
  private final ChatPrefs chat;
  private final NotificationsPrefs notification;
  private final StringProperty themeName;
  private final StringProperty lastGameType;
  private final LocalizationPrefs localization;
  private final StringProperty lastGameTitle;
  private final StringProperty lastMap;
  private final BooleanProperty rememberLastTab;
  private final BooleanProperty showPasswordProtectedGames;
  private final BooleanProperty showModdedGames;
  private final ListProperty<String> ignoredNotifications;
  private final IntegerProperty lastGameMinRank;
  private final IntegerProperty lastGameMaxRank;
  private final StringProperty gamesViewMode;
  private final Ladder1v1Prefs ladder1v1;
  private final NewsPrefs news;
  private final DeveloperPrefs developer;
  private final VaultPrefs vaultPrefs;
  private final ListProperty<Pair<String, SortType>> gameListSorting;
  private final ObjectProperty<TilesSortingOrder> gameTileSortingOrder;
  private final ObjectProperty<UnitDataBaseType> unitDataBaseType;
  private final MapProperty<URI, ArrayList<HttpCookie>> storedCookies;
  private final BooleanProperty lastGameOnlyFriends;
  private final BooleanProperty disallowJoinsViaDiscord;
  private final BooleanProperty showGameDetailsSidePane;

  public Preferences() {
    gameTileSortingOrder = new SimpleObjectProperty<>(TilesSortingOrder.PLAYER_DES);
    chat = new ChatPrefs();
    login = new LoginPrefs();

    localization = new LocalizationPrefs();
    mainWindow = new WindowPrefs();
    forgedAlliance = new ForgedAlliancePrefs();
    themeName = new SimpleStringProperty(DEFAULT_THEME_NAME);
    lastGameType = new SimpleStringProperty(KnownFeaturedMod.DEFAULT.getTechnicalName());
    ignoredNotifications = new SimpleListProperty<>(observableArrayList());
    notification = new NotificationsPrefs();
    rememberLastTab = new SimpleBooleanProperty(true);
    lastGameTitle = new SimpleStringProperty();
    lastMap = new SimpleStringProperty();
    lastGameMinRank = new SimpleIntegerProperty(800);
    lastGameMaxRank = new SimpleIntegerProperty(1300);
    ladder1v1 = new Ladder1v1Prefs();
    gamesViewMode = new SimpleStringProperty();
    news = new NewsPrefs();
    developer = new DeveloperPrefs();
    gameListSorting = new SimpleListProperty<>(observableArrayList());
    vaultPrefs = new VaultPrefs();
    unitDataBaseType = new SimpleObjectProperty<>(UnitDataBaseType.RACKOVER);
    storedCookies = new SimpleMapProperty<>(FXCollections.observableHashMap());
    showPasswordProtectedGames = new SimpleBooleanProperty(true);
    showModdedGames = new SimpleBooleanProperty(true);
    lastGameOnlyFriends = new SimpleBooleanProperty();
    disallowJoinsViaDiscord = new SimpleBooleanProperty();
    showGameDetailsSidePane = new SimpleBooleanProperty(false);
  }

  public WindowPrefs getMainWindow() {
    return mainWindow;
  }

  public ForgedAlliancePrefs getForgedAlliance() {
    return forgedAlliance;
  }

  public LoginPrefs getLogin() {
    return login;
  }

  public ChatPrefs getChat() {
    return chat;
  }

  public NotificationsPrefs getNotification() {
    return notification;
  }

  public String getThemeName() {
    return themeName.get();
  }

  public StringProperty themeNameProperty() {
    return themeName;
  }

  public void setThemeName(String themeName) {
    this.themeName.set(themeName);
  }

  public String getLastGameType() {
    return lastGameType.get();
  }

  public StringProperty lastGameTypeProperty() {
    return lastGameType;
  }

  public void setLastGameType(String lastGameType) {
    this.lastGameType.set(lastGameType);
  }

  public LocalizationPrefs getLocalization() {
    return localization;
  }

  public String getLastGameTitle() {
    return lastGameTitle.get();
  }

  public StringProperty lastGameTitleProperty() {
    return lastGameTitle;
  }

  public void setLastGameTitle(String lastGameTitle) {
    this.lastGameTitle.set(lastGameTitle);
  }

  public String getLastMap() {
    return lastMap.get();
  }

  public StringProperty lastMapProperty() {
    return lastMap;
  }

  public void setLastMap(String lastMap) {
    this.lastMap.set(lastMap);
  }

  public boolean isRememberLastTab() {
    return rememberLastTab.get();
  }

  public BooleanProperty rememberLastTabProperty() {
    return rememberLastTab;
  }

  public void setRememberLastTab(boolean rememberLastTab) {
    this.rememberLastTab.set(rememberLastTab);
  }

  public boolean isShowPasswordProtectedGames() {
    return showPasswordProtectedGames.get();
  }

  public void setShowPasswordProtectedGames(boolean showPasswordProtectedGames) {
    this.showPasswordProtectedGames.set(showPasswordProtectedGames);
  }

  public BooleanProperty showPasswordProtectedGamesProperty() {
    return showPasswordProtectedGames;
  }

  public boolean isShowModdedGames() {
    return showModdedGames.get();
  }

  public void setShowModdedGames(boolean showModdedGames) {
    this.showModdedGames.set(showModdedGames);
  }

  public BooleanProperty showModdedGamesProperty() {
    return showModdedGames;
  }

  public ObservableList<String> getIgnoredNotifications() {
    return ignoredNotifications.get();
  }

  public ListProperty<String> ignoredNotificationsProperty() {
    return ignoredNotifications;
  }

  public void setIgnoredNotifications(ObservableList<String> ignoredNotifications) {
    this.ignoredNotifications.set(ignoredNotifications);
  }

  public int getLastGameMinRank() {
    return lastGameMinRank.get();
  }

  public IntegerProperty lastGameMinRankProperty() {
    return lastGameMinRank;
  }

  public void setLastGameMinRank(int lastGameMinRank) {
    this.lastGameMinRank.set(lastGameMinRank);
  }

  public int getLastGameMaxRank() {
    return lastGameMaxRank.get();
  }

  public IntegerProperty lastGameMaxRankProperty() {
    return lastGameMaxRank;
  }

  public void setLastGameMaxRank(int lastGameMaxRank) {
    this.lastGameMaxRank.set(lastGameMaxRank);
  }

  public String getGamesViewMode() {
    return gamesViewMode.get();
  }

  public void setGamesViewMode(String gamesViewMode) {
    this.gamesViewMode.set(gamesViewMode);
  }

  public StringProperty gamesViewModeProperty() {
    return gamesViewMode;
  }

  public Ladder1v1Prefs getLadder1v1() {
    return ladder1v1;
  }

  public NewsPrefs getNews() {
    return news;
  }

  public DeveloperPrefs getDeveloper() {
    return developer;
  }

  public VaultPrefs getVaultPrefs() {
    return vaultPrefs;
  }

  public ObservableList<Pair<String, SortType>> getGameListSorting() {
    return gameListSorting.get();
  }

  public ListProperty<Pair<String, SortType>> gameListSortingProperty() {
    return gameListSorting;
  }

  public void setGameListSorting(ObservableList<Pair<String, SortType>> gameListSorting) {
    this.gameListSorting.set(gameListSorting);
  }

  public TilesSortingOrder getGameTileSortingOrder() {
    return gameTileSortingOrder.get();
  }

  public void setGameTileSortingOrder(TilesSortingOrder gameTileSortingOrder) {
    this.gameTileSortingOrder.set(gameTileSortingOrder);
  }

  public ObjectProperty<TilesSortingOrder> gameTileSortingOrderProperty() {
    return gameTileSortingOrder;
  }

  public UnitDataBaseType getUnitDataBaseType() {
    return unitDataBaseType.get();
  }

  public ObjectProperty<UnitDataBaseType> unitDataBaseTypeProperty() {
    return unitDataBaseType;
  }

  public void setUnitDataBaseType(UnitDataBaseType unitDataBaseType) {
    this.unitDataBaseType.set(unitDataBaseType);
  }

  public ObservableMap<URI, ArrayList<HttpCookie>> getStoredCookies() {
    return storedCookies.get();
  }

  public MapProperty<URI, ArrayList<HttpCookie>> storedCookiesProperty() {
    return storedCookies;
  }

  public void setStoredCookies(ObservableMap<URI, ArrayList<HttpCookie>> storedCookies) {
    this.storedCookies.set(storedCookies);
  }

  public boolean isLastGameOnlyFriends() {
    return lastGameOnlyFriends.get();
  }

  public BooleanProperty lastGameOnlyFriendsProperty() {
    return lastGameOnlyFriends;
  }

  public void setLastGameOnlyFriends(boolean lastGameOnlyFriends) {
    this.lastGameOnlyFriends.set(lastGameOnlyFriends);
  }

  public boolean isDisallowJoinsViaDiscord() {
    return disallowJoinsViaDiscord.get();
  }

  public void setDisallowJoinsViaDiscord(boolean disallowJoinsViaDiscord) {
    this.disallowJoinsViaDiscord.set(disallowJoinsViaDiscord);
  }

  public BooleanProperty disallowJoinsViaDiscordProperty() {
    return disallowJoinsViaDiscord;
  }

  public boolean isShowGameDetailsSidePane() {
    return showGameDetailsSidePane.get();
  }

  public void setShowGameDetailsSidePane(boolean showGameDetailsSidePane) {
    this.showGameDetailsSidePane.set(showGameDetailsSidePane);
  }

  public BooleanProperty showGameDetailsSidePaneProperty() {
    return showGameDetailsSidePane;
  }

  public enum UnitDataBaseType {
    SPOOKY("unitDatabase.spooky"),
    RACKOVER("unitDatabase.rackover");

    @Getter
    private final String i18nKey;

    UnitDataBaseType(String i18nKey) {
      this.i18nKey = i18nKey;
    }
  }
}
