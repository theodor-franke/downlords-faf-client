package com.faforever.client.api;

import com.faforever.client.mapstruct.MapStructConfig;
import com.faforever.client.player.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.supcomhub.api.dto.Account;

@Mapper(config = MapStructConfig.class)
public interface PlayerMapper {

  @Mapping(target = "socialStatus", ignore = true)
  @Mapping(target = "numberOfGames", ignore = true)
  @Mapping(target = "clanTag", ignore = true)
  @Mapping(target = "country", ignore = true)
  @Mapping(target = "game", ignore = true)
  @Mapping(target = "idleSince", ignore = true)
  @Mapping(target = "names", ignore = true)
  @Mapping(target = "chatChannelUsers", ignore = true)
  @Mapping(target = "ranks", ignore = true)
  @Mapping(target="avatarTooltip", ignore = true)
  @Mapping(target="avatarUrl", ignore = true)
  Player map(Account account);
}
