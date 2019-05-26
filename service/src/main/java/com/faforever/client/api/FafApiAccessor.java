package com.faforever.client.api;

import com.faforever.client.game.KnownFeaturedMod;
import com.faforever.client.vault.SearchConfig;
import com.faforever.client.vault.SortConfig;
import com.faforever.commons.io.ByteCountListener;
import org.supcomhub.api.dto.Account;
import org.supcomhub.api.dto.Achievement;
import org.supcomhub.api.dto.Avatar;
import org.supcomhub.api.dto.Clan;
import org.supcomhub.api.dto.CoopMission;
import org.supcomhub.api.dto.CoopResult;
import org.supcomhub.api.dto.FeaturedMod;
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

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Provides access to the FAF REST API. Services should not access this class directly, but use {@link
 * com.faforever.client.remote.FafService} instead.
 */
public interface FafApiAccessor {

  List<PlayerAchievement> getPlayerAchievements(int playerId);

  List<PlayerEvent> getPlayerEvents(int playerId);

  List<Achievement> getAchievements();

  Achievement getAchievement(String achievementId);

  void authorize(int playerId, String username, String password);

  List<Mod> getMods();

  List<FeaturedMod> getFeaturedMods();

  List<LeaderboardEntry> getLeaderboard(String leaderboardName);

  Optional<LeaderboardEntry> getLeaderboardEntry(int playerId, String leaderboardName);

  List<GameParticipant> getGameParticipants(int playerId, KnownFeaturedMod knownFeaturedMod);

  List<MapVersion> getMostPlayedMaps(int count, int page);

  List<MapVersion> getHighestRatedMaps(int count, int page);

  List<MapVersion> getNewestMaps(int count, int page);

  List<Game> getLastGamesOnMap(int playerId, String mapVersionId, int count);

  void uploadMod(Path file, ByteCountListener listener);

  void uploadMap(Path file, boolean isRanked, ByteCountListener listener) throws IOException;

  List<CoopMission> getCoopMissions();

  List<CoopResult> getCoopLeaderboard(String missionId, int numberOfPlayers);

  void changePassword(String username, String currentPasswordHash, String newPasswordHash) throws IOException;

  ModVersion getModVersion(UUID uid);

  List<FeaturedModFile> getFeaturedModFiles(String featuredModId, Integer version);

  List<Game> getNewestReplays(int count, int page);

  List<Game> getHighestRatedReplays(int count, int page);

  List<Game> findReplaysByQuery(String condition, int maxResults, int page, SortConfig sortConfig);

  Optional<MapVersion> findMapByFolderName(String folderName);

  List<Account> getPlayersByIds(Collection<Integer> playerIds);

  GameReview createGameReview(GameReview review);

  void updateGameReview(GameReview review);

  ModVersionReview createModVersionReview(ModVersionReview review);

  void updateModVersionReview(ModVersionReview review);

  MapVersionReview createMapVersionReview(MapVersionReview review);

  void updateMapVersionReview(MapVersionReview review);

  void deleteGameReview(String id);

  Optional<Clan> getClanByTag(String tag);

  List<Map> findMapsByQuery(SearchConfig searchConfig, int page, int count);

  Optional<MapVersion> findMapVersionById(String id);

  void deleteMapVersionReview(String id);

  void deleteModVersionReview(String id);

  Optional<Game> findReplayById(int id);

  List<Mod> findModsByQuery(SearchConfig query, int page, int maxResults);

  List<MapVersion> getLadder1v1Maps(int count, int page);

  List<Tournament> getAllTournaments();

  List<MapVersion> getOwnedMaps(int playerId, int loadMoreCount, int page);

  void updateMapVersion(String id, MapVersion mapVersion);

  List<Avatar> getAvailableAvatars(int playerId, int loadMoreCount, int page);

  List<MapReview> findNonEmptyMapReviews(String id, int count, int page);

  List<GameReview> findNonEmptyGameReviews(int id, int count, int page);

  GameReviewSummary findGameReviewSummary(int id);

  MapReview findOwnMapReview(String mapId);

  List<ModReview> findNonEmptyModReviews(String id, int count, int page);

  ModReviewSummary findModReviewSummary(String id);

  Collection<NewsPost> getNews();
}
