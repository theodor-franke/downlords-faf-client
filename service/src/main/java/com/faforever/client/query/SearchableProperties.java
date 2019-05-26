package com.faforever.client.query;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import org.supcomhub.api.dto.Account;
import org.supcomhub.api.dto.FeaturedMod;
import org.supcomhub.api.dto.Map;
import org.supcomhub.api.dto.MapVersion.Fields;
import org.supcomhub.api.dto.Mod;
import org.supcomhub.api.dto.ModVersion;

import static org.supcomhub.api.dto.AbstractEntity.Fields.createTime;
import static org.supcomhub.api.dto.AbstractEntity.Fields.id;
import static org.supcomhub.api.dto.AbstractEntity.Fields.updateTime;
import static org.supcomhub.api.dto.FeaturedMod.Fields.technicalName;
import static org.supcomhub.api.dto.Game.Fields.endTime;
import static org.supcomhub.api.dto.Game.Fields.host;
import static org.supcomhub.api.dto.Game.Fields.name;
import static org.supcomhub.api.dto.Game.Fields.playerStats;
import static org.supcomhub.api.dto.Game.Fields.startTime;
import static org.supcomhub.api.dto.Game.Fields.validity;
import static org.supcomhub.api.dto.Game.Fields.victoryCondition;
import static org.supcomhub.api.dto.GameParticipant.Fields.faction;
import static org.supcomhub.api.dto.GameParticipant.Fields.participant;
import static org.supcomhub.api.dto.GameParticipant.Fields.rankBefore;
import static org.supcomhub.api.dto.GameParticipant.Fields.startSpot;
import static org.supcomhub.api.dto.GameParticipant.Fields.team;
import static org.supcomhub.api.dto.MapVersion.Fields.description;
import static org.supcomhub.api.dto.MapVersion.Fields.folderName;
import static org.supcomhub.api.dto.MapVersion.Fields.height;
import static org.supcomhub.api.dto.MapVersion.Fields.maxPlayers;
import static org.supcomhub.api.dto.MapVersion.Fields.ranked;
import static org.supcomhub.api.dto.MapVersion.Fields.version;
import static org.supcomhub.api.dto.MapVersion.Fields.width;
import static org.supcomhub.api.dto.ReviewSummary.Fields.lowerBound;

/**
 * Contains mappings of searchable properties (as the API expects it) to their respective i18n key. The reason the i18n
 * keys are not builz dynamically is that it makes it impossible for the IDE to detect which key is used where, breaks
 * its refactor capability, and the actual UI text might depend on the context it is used in. Also, this way i18n keys
 * and API keys are nicely decoupled and can therefore be changed independently.
 */
public class SearchableProperties {
  public static final java.util.Map<String, String> GAME_PROPERTIES = ImmutableMap.<String, String>builder()
    .put(field(playerStats, participant, Account.Fields.displayName), "game.player.username")
    .put(field(playerStats, rankBefore), "game.filter.player.rank")
    .put(field(technicalName), "featuredMod.technicalName")
    .put(field(Fields.map, Map.Fields.displayName), "game.map.displayName")
    .put(field(playerStats, faction), "game.player.faction")
    .put(field(playerStats, startSpot), "game.player.startSpot")
    .put(field(maxPlayers), "map.maxPlayers")
    .put(field(ranked), "game.map.isRanked")
    .put(field(id), "game.id")
    .put(field(playerStats, id), "game.player.id")
    .put(field(name), "game.title")
    .put(field(startTime), "game.startTime")
    .put(field(endTime), "game.endTime")
    .put(field(validity), "game.validity")
    .put(field(victoryCondition), "game.victoryCondition")
    .put(field(playerStats, team), "game.player.team")
    .put(field(host, Account.Fields.displayName), "game.host.username")
    .put(field(host, id), "game.host.username")
    .put(field(FeaturedMod.Fields.displayName), "featuredMod.displayName")
    .put(field(description), "map.description")
    .put(field(width), "game.map.width")
    .put(field(height), "game.map.height")
    .put(field(version), "game.map.version")
    .put(field(folderName), "game.map.folderName")

      .build();

  public static final java.util.Map<String, String> MAP_PROPERTIES = ImmutableMap.<String, String>builder()
    .put(field(Map.Fields.displayName), "map.name")
    .put(field(Map.Fields.uploader, Account.Fields.displayName), "map.uploader")

    // TODO continue here. Unfortunately, map statistics are not part of Map yet.
//      .put("statistics.plays", "map.playCount")
//      .put("statistics.downloads", "map.numberOfDownloads")

    .put(field(Map.Fields.latestVersion, createTime), "map.uploadedDateTime")
    .put(field(Map.Fields.latestVersion, updateTime), "map.updatedDateTime")
    .put(field(Map.Fields.latestVersion, description), "map.description")
    .put(field(Map.Fields.latestVersion, maxPlayers), "map.maxPlayers")
    .put(field(Map.Fields.latestVersion, width), "map.width")
    .put(field(Map.Fields.latestVersion, height), "map.height")
    .put(field(Map.Fields.latestVersion, version), "map.version")
    .put(field(Map.Fields.latestVersion, folderName), "map.folderName")
    .put(field(Map.Fields.latestVersion, ranked), "map.ranked")

      .build();

  public static final java.util.Map<String, String> MOD_PROPERTIES = ImmutableMap.<String, String>builder()
    .put(field(Mod.Fields.displayName), "mod.displayName")
    .put(field(Mod.Fields.uploader, Account.Fields.displayName), "mod.author")

    .put(field(Mod.Fields.latestVersion, createTime), "mod.uploadedDateTime")
    .put(field(Mod.Fields.latestVersion, updateTime), "mod.updatedDateTime")
    .put(field(Mod.Fields.latestVersion, ModVersion.Fields.description), "mod.description")
    .put(field(Mod.Fields.latestVersion, id), "mod.id")
    .put(field(Mod.Fields.latestVersion, ModVersion.Fields.type), "mod.type")
    .put(field(Mod.Fields.latestVersion, ModVersion.Fields.ranked), "mod.ranked")
    .put(field(Mod.Fields.latestVersion, ModVersion.Fields.version), "mod.version")
    .put(field(Mod.Fields.latestVersion, ModVersion.Fields.filename), "mod.filename")

      .build();

  public static final String NEWEST_MOD_KEY = field(Mod.Fields.latestVersion, createTime);

  public static final String HIGHEST_RATED_MOD_KEY = field(Mod.Fields.latestVersion, ModVersion.Fields.reviewSummary, lowerBound);

  private static String field(String... path) {
    return Joiner.on('.').join(path);
  }
}
