package com.faforever.client.map;

public class MapBuilder {

  private final FaMap faMap;

  private MapBuilder() {
    faMap = new FaMap();
  }

  public static MapBuilder create() {
    return new MapBuilder();
  }

  public MapBuilder defaultValues() {
    return displayName("Map name")
        .folderName("map_name.v001")
        .mapSize(MapSize.valueOf(512, 512));
  }

  public MapBuilder mapSize(MapSize mapSize) {
    faMap.setSize(mapSize);
    return this;
  }

  public MapBuilder folderName(String technicalName) {
    faMap.setFolderName(technicalName);
    return this;
  }

  public MapBuilder displayName(String displayName) {
    faMap.setDisplayName(displayName);
    return this;
  }

  public FaMap get() {
    return faMap;
  }
}
