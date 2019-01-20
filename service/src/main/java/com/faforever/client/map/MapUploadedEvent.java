package com.faforever.client.map;

public class MapUploadedEvent {
  private FaMap mapInfo;

  public MapUploadedEvent(FaMap faMap) {
    this.mapInfo = faMap;
  }

  public FaMap getMapInfo() {
    return mapInfo;
  }
}
