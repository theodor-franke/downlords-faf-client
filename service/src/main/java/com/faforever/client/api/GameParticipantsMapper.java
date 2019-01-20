package com.faforever.client.api;

import com.faforever.client.mapstruct.MapStructConfig;
import org.mapstruct.Mapper;
import org.supcomhub.api.dto.GameParticipant;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(config = MapStructConfig.class)
public interface GameParticipantsMapper {

  default Map<String, List<String>> map(List<GameParticipant> gameParticipants) {
    return gameParticipants.stream()
      .collect(Collectors.groupingBy(
        gameParticipant -> String.valueOf(gameParticipant.getTeam()),
        Collectors.mapping(gameParticipant -> gameParticipant.getParticipant().getDisplayName(), Collectors.toList())));
  }
}
