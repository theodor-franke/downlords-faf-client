package com.faforever.client.api;

import com.faforever.client.achievements.Achievement;
import com.faforever.client.achievements.PlayerAchievement;
import com.faforever.client.avatar.Avatar;
import com.faforever.client.clan.Clan;
import com.faforever.client.coop.CoopMission;
import com.faforever.client.coop.CoopResult;
import com.faforever.client.event.PlayerEvent;
import com.faforever.client.leaderboard.LeaderboardEntry;
import com.faforever.client.map.FaMap;
import com.faforever.client.map.MapSize;
import com.faforever.client.mapstruct.MapStructConfig;
import com.faforever.client.mod.FeaturedMod;
import com.faforever.client.mod.FeaturedModFile;
import com.faforever.client.mod.Mod;
import com.faforever.client.mod.ModVersion;
import com.faforever.client.player.NameRecord;
import com.faforever.client.replay.Replay;
import com.faforever.client.review.Review;
import com.faforever.client.review.ReviewsSummary;
import com.faforever.client.tournament.Tournament;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import org.apache.maven.artifact.versioning.ComparableVersion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.supcomhub.api.dto.GameReview;
import org.supcomhub.api.dto.GameReviewSummary;
import org.supcomhub.api.dto.LadderMap;
import org.supcomhub.api.dto.Map;
import org.supcomhub.api.dto.MapReview;
import org.supcomhub.api.dto.MapVersion;
import org.supcomhub.api.dto.MapVersionReview;
import org.supcomhub.api.dto.ModReview;
import org.supcomhub.api.dto.ModReviewSummary;
import org.supcomhub.api.dto.ReviewScoreCount;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(config = MapStructConfig.class, uses = {
  GameParticipantsMapper.class,
  PlayerMapper.class,
  ClanMembershipsMapper.class
})
public interface ApiDtoMapper {
  Avatar map(org.supcomhub.api.dto.Avatar dto);

  Achievement map(org.supcomhub.api.dto.Achievement dto);

  @Mapping(target = "selected", ignore = true)
  @Mapping(target = "selectable", ignore = true)
  @Mapping(target = "likes", ignore = true)
  @Mapping(target = "played", ignore = true)
  @Mapping(target = "comments", ignore = true)
  @Mapping(target = "reviewsSummary", ignore = true)
  @Mapping(target = "modType", ignore = true)
  @Mapping(target = "mountInfos", ignore = true)
  @Mapping(target = "hookDirectories", ignore = true)
  @Mapping(target = "uploader", source = "mod.uploader.displayName")
  @Mapping(target = "imagePath", ignore = true)
  @Mapping(target = "displayName", source = "mod.displayName")
  ModVersion map(org.supcomhub.api.dto.ModVersion modVersion);

  @Mapping(target = "version", source = "map.version")
  @Mapping(target = "mapFolderName", source = "map.folderName")
  CoopMission map(org.supcomhub.api.dto.CoopMission coopMission);

  @Mapping(target = "gameId", source = "game.id")
  CoopResult map(org.supcomhub.api.dto.CoopResult coopResult);

  FeaturedModFile map(org.supcomhub.api.dto.FeaturedModFile featuredModFile);

  @Mapping(target = "title", source = "name")
  @Mapping(target = "replayFile", ignore = true)
  @Mapping(target = "teams", source = "playerStats")
  @Mapping(target = "chatMessages", ignore = true)
  @Mapping(target = "gameOptions", ignore = true)
  @Mapping(target = "teamPlayerStats", ignore = true)
  @Mapping(target = "views", ignore = true)
  @Mapping(target = "map", source = "mapVersion")
  Replay map(org.supcomhub.api.dto.Game game);

  PlayerAchievement map(org.supcomhub.api.dto.PlayerAchievement playerAchievement);

  @Mapping(target = "size", qualifiedByName = "mapSizeMapper")
  @Mapping(target = "smallThumbnailUrl", source = "thumbnailUrlSmall")
  @Mapping(target = "largeThumbnailUrl", source = "thumbnailUrlLarge")
  FaMap map(MapVersion mapVersion);

  @Named("mapSizeMapper")
  default MapSize mapMapSize(MapVersion mapVersion) {
    return MapSize.valueOf(mapVersion.getWidth(), mapVersion.getHeight());
  }

  FaMap map(LadderMap ladderMap);

  FaMap map(Map map);

  @Mapping(target = "username", source = "account.displayName")
  @Mapping(target = "position", source = "rank")
  @Mapping(target = "gamesPlayed", ignore = true)
  @Mapping(target = "winLossRatio", ignore = true)
  LeaderboardEntry map(org.supcomhub.api.dto.LeaderboardEntry leaderboardEntry);

  Mod map(org.supcomhub.api.dto.Mod mod);

  FeaturedMod map(org.supcomhub.api.dto.FeaturedMod featuredMod);

  @Mapping(target = "members", source = "memberships")
  Clan map(org.supcomhub.api.dto.Clan clan);

  Tournament map(org.supcomhub.api.dto.challonge.Tournament tournament);

  NameRecord map(NameRecord nameRecord);

  Review map(MapReview dto);

  Review map(MapVersionReview dto);

  Review map(GameReview dto);

  Review map(ModReview dto);

  ReviewsSummary map(GameReviewSummary gameReviewSummary);

  ReviewsSummary map(ModReviewSummary modReviewSummary);

  PlayerEvent map(org.supcomhub.api.dto.PlayerEvent playerEvent);

  default ComparableVersion map(short version) {
    return new ComparableVersion(String.valueOf(version));
  }

  default LocalDateTime map(OffsetDateTime dateTime) {
    return LocalDateTime.from(dateTime);
  }

  default ObservableMap<Byte, Integer> map(List<ReviewScoreCount> reviewScoreCounts) {
    return FXCollections.observableMap(reviewScoreCounts.stream()
      .collect(Collectors.toMap(ReviewScoreCount::getScore, ReviewScoreCount::getCount)));
  }
}
