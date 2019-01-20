package com.faforever.client.api;

import com.faforever.client.mapstruct.MapStructConfig;
import com.faforever.client.player.Player;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.supcomhub.api.dto.ClanMembership;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(config = MapStructConfig.class, uses = PlayerMapper.class)
public interface ClanMembershipsMapper {

  default List<Player> map(List<ClanMembership> memberships) {
    return memberships.stream()
      .map(ClanMembership::getMember)
      .map(account -> Mappers.getMapper(PlayerMapper.class).map(account))
      .collect(Collectors.toList());
  }
}
