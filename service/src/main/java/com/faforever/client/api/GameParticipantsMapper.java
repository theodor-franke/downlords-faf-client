package com.faforever.client.api;

import com.faforever.client.mapstruct.MapStructConfig;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import org.mapstruct.Mapper;
import org.supcomhub.api.dto.GameParticipant;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(config = MapStructConfig.class)
public interface GameParticipantsMapper {

  default ObservableMap<String, List<String>> map(List<GameParticipant> gameParticipants) {
    return FXCollections.observableMap(
      gameParticipants.stream()
        .collect(Collectors.groupingBy(
          gameParticipant -> String.valueOf(gameParticipant.getTeam()),
          Collectors.mapping(gameParticipant -> gameParticipant.getParticipant().getDisplayName(), Collectors.toList())))
    );
  }
}
