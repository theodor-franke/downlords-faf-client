package com.faforever.client.api;

import com.faforever.client.mapstruct.MapStructConfig;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface FxMapper {

  default <T> ObservableList<T> createObservableList() {
    return FXCollections.observableArrayList();
  }
}
