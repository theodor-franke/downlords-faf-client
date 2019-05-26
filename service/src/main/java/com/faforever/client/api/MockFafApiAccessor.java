package com.faforever.client.api;

import com.faforever.client.SpringProfiles;
import com.faforever.client.game.KnownFeaturedMod;
import com.faforever.client.vault.SearchConfig;
import com.faforever.client.vault.SortConfig;
import com.faforever.commons.io.ByteCountListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.supcomhub.api.dto.Account;
import org.supcomhub.api.dto.Achievement;
import org.supcomhub.api.dto.AchievementType;
import org.supcomhub.api.dto.Avatar;
import org.supcomhub.api.dto.Clan;
import org.supcomhub.api.dto.CoopMission;
import org.supcomhub.api.dto.CoopResult;
import org.supcomhub.api.dto.FeaturedModFile;
import org.supcomhub.api.dto.Game;
import org.supcomhub.api.dto.GameParticipant;
import org.supcomhub.api.dto.GameReview;
import org.supcomhub.api.dto.GameReviewSummary;
import org.supcomhub.api.dto.LeaderboardEntry;
import org.supcomhub.api.dto.Map;
import org.supcomhub.api.dto.MapReview;
import org.supcomhub.api.dto.MapVersion;
import org.supcomhub.api.dto.MapVersionReview;
import org.supcomhub.api.dto.Mod;
import org.supcomhub.api.dto.ModReview;
import org.supcomhub.api.dto.ModReviewSummary;
import org.supcomhub.api.dto.ModVersion;
import org.supcomhub.api.dto.ModVersionReview;
import org.supcomhub.api.dto.NewsPost;
import org.supcomhub.api.dto.PlayerAchievement;
import org.supcomhub.api.dto.PlayerEvent;
import org.supcomhub.api.dto.challonge.Tournament;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Lazy
@Component
@Profile(SpringProfiles.PROFILE_OFFLINE)
public class MockFafApiAccessor implements FafApiAccessor {

  @Override
  public List<PlayerAchievement> getPlayerAchievements(int playerId) {
    return Collections.emptyList();
  }

  @Override
  public List<PlayerEvent> getPlayerEvents(int playerId) {
    return Collections.emptyList();
  }

  @Override
  public List<Achievement> getAchievements() {
    return Collections.emptyList();
  }

  @Override
  public Achievement getAchievement(String achievementId) {
    Achievement achievement = new Achievement();
    achievement.setName("Mock achievement");
    achievement.setDescription("Congratulations! You read this text.");
    achievement.setType(AchievementType.STANDARD);
    return achievement;
  }

  @Override
  public void authorize(int playerId, String username, String password) {

  }

  @Override
  public List<Mod> getMods() {
    Account uploader = new Account();
    return Arrays.asList(
      new org.supcomhub.api.dto.Mod("1", "Mod Number One", uploader, Collections.emptyList(), mod()),
      new org.supcomhub.api.dto.Mod("2", "Mod Number Two", uploader, Collections.emptyList(), mod()),
      new org.supcomhub.api.dto.Mod("3", "Mod Number Three", uploader, Collections.emptyList(), mod()),
      new org.supcomhub.api.dto.Mod("4", "Mod Number Four", uploader, Collections.emptyList(), mod()),
      new org.supcomhub.api.dto.Mod("5", "Mod Number Five", uploader, Collections.emptyList(), mod()),
      new org.supcomhub.api.dto.Mod("6", "Mod Number Six", uploader, Collections.emptyList(), mod()),
      new org.supcomhub.api.dto.Mod("7", "Mod Number Seven", uploader, Collections.emptyList(), mod()),
      new org.supcomhub.api.dto.Mod("8", "Mod Number Eight", uploader, Collections.emptyList(), mod())
    );
  }

  private ModVersion mod() {
    return new ModVersion();
  }

  @Override
  public List<org.supcomhub.api.dto.FeaturedMod> getFeaturedMods() {
    org.supcomhub.api.dto.FeaturedMod featuredMod = new org.supcomhub.api.dto.FeaturedMod();
    featuredMod.setDisplayName("Forged Alliance Forever");
    featuredMod.setTechnicalName("faf");
    featuredMod.setVisible(true);
    featuredMod.setDescription("Description");

    return Collections.singletonList(featuredMod);
  }

  @Override
  public List<LeaderboardEntry> getLeaderboard(String leaderboardName) {
    return Collections.emptyList();
  }

  @Override
  public Optional<LeaderboardEntry> getLeaderboardEntry(int playerId, String leaderboardName) {
    return Optional.empty();
  }

  @Override
  public List<FeaturedModFile> getFeaturedModFiles(String featuredModId, Integer version) {
    return Collections.emptyList();
  }

  @Override
  public List<Avatar> getAvailableAvatars(int playerId, int loadMoreCount, int page) {
    return Collections.emptyList();
  }

  @Override
  public List<MapReview> findNonEmptyMapReviews(String id, int count, int page) {
    return Collections.emptyList();
  }

  @Override
  public List<GameReview> findNonEmptyGameReviews(int id, int count, int page) {
    return Collections.emptyList();
  }

  @Override
  public GameReviewSummary findGameReviewSummary(int id) {
    return null;
  }

  @Override
  public MapReview findOwnMapReview(String mapId) {
    return null;
  }

  @Override
  public List<ModReview> findNonEmptyModReviews(String id, int count, int page) {
    return Collections.emptyList();
  }

  @Override
  public ModReviewSummary findModReviewSummary(String id) {
    return null;
  }

  @Override
  public Collection<NewsPost> getNews() {
    return Collections.emptyList();
  }

  @Override
  public List<GameParticipant> getGameParticipants(int playerId, KnownFeaturedMod knownFeaturedMod) {
    return Collections.emptyList();
  }

  @Override
  public List<MapVersion> getMostPlayedMaps(int count, int page) {
    return Collections.emptyList();
  }

  @Override
  public List<MapVersion> getHighestRatedMaps(int count, int page) {
    return Collections.emptyList();
  }

  @Override
  public List<MapVersion> getNewestMaps(int count, int page) {
    return Collections.emptyList();
  }

  @Override
  public List<Game> getLastGamesOnMap(int playerId, String mapVersionId, int count) {
    return Collections.emptyList();
  }

  @Override
  public void uploadMod(Path file, ByteCountListener listener) {

  }

  @Override
  public void uploadMap(Path file, boolean isRanked, ByteCountListener listener) {

  }

  @Override
  public List<CoopMission> getCoopMissions() {
    return Collections.emptyList();
  }

  @Override
  public ModVersion getModVersion(UUID uid) {
    return null;
  }

  @Override
  public List<Game> getNewestReplays(int count, int page) {
    return Collections.emptyList();
  }

  @Override
  public List<Game> getHighestRatedReplays(int count, int page) {
    return Collections.emptyList();
  }

  @Override
  public List<Game> findReplaysByQuery(String query, int maxResults, int page, SortConfig sortConfig) {
    return Collections.emptyList();
  }

  @Override
  public Optional<MapVersion> findMapByFolderName(String folderName) {
    return Optional.empty();
  }

  @Override
  public List<Account> getPlayersByIds(Collection<Integer> playerIds) {
    return Collections.emptyList();
  }

  @Override
  public GameReview createGameReview(GameReview review) {
    return null;
  }

  @Override
  public void updateGameReview(GameReview review) {

  }

  @Override
  public ModVersionReview createModVersionReview(ModVersionReview review) {
    return null;
  }

  @Override
  public void updateModVersionReview(ModVersionReview review) {

  }

  @Override
  public MapVersionReview createMapVersionReview(MapVersionReview review) {
    return null;
  }

  @Override
  public void updateMapVersionReview(MapVersionReview review) {

  }

  @Override
  public void deleteGameReview(String id) {

  }

  @Override
  public Optional<Clan> getClanByTag(String tag) {
    return Optional.empty();
  }

  @Override
  public List<Map> findMapsByQuery(SearchConfig searchConfig, int page, int count) {
    return Collections.emptyList();
  }

  @Override
  public Optional<MapVersion> findMapVersionById(String id) {
    return Optional.empty();
  }

  @Override
  public void deleteMapVersionReview(String id) {

  }

  @Override
  public void deleteModVersionReview(String id) {

  }

  @Override
  public Optional<Game> findReplayById(int id) {
    return Optional.empty();
  }

  @Override
  public List<Mod> findModsByQuery(SearchConfig query, int page, int maxResults) {
    return Collections.emptyList();
  }

  @Override
  public List<MapVersion> getLadder1v1Maps(int count, int page) {
    return Collections.emptyList();
  }

  @Override
  public List<Tournament> getAllTournaments() {
    return Collections.emptyList();
  }

  @Override
  public List<MapVersion> getOwnedMaps(int playerId, int loadMoreCount, int page) {
    return Collections.emptyList();
  }

  @Override
  public void updateMapVersion(String id, MapVersion mapVersion) {
  }

  @Override
  public void changePassword(String username, String currentPasswordHash, String newPasswordHash) {

  }

  @Override
  public List<CoopResult> getCoopLeaderboard(String missionId, int numberOfPlayers) {
    return Collections.emptyList();
  }
}
