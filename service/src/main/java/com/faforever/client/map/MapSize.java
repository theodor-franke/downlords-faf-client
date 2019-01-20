package com.faforever.client.map;

import lombok.Value;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Value
public class MapSize implements Comparable<MapSize> {

  private static final float MAP_SIZE_FACTOR = 51.2f;

  private static Map<String, MapSize> cache = new HashMap<>();

  /**
   * The map width in pixels. One kilometer equals 51.2 pixels.
   */
  private final int widthInPixels;

  /**
   * The map height in pixels. One kilometer equals 51.2 pixels.
   */
  private final int heightInPixels;

  public static MapSize valueOf(int widthInPixels, int heightInPixels) {
    String cacheKey = String.valueOf(widthInPixels) + heightInPixels;
    if (cache.containsKey(cacheKey)) {
      return cache.get(cacheKey);
    }

    MapSize mapSize = new MapSize(widthInPixels, heightInPixels);
    cache.put(cacheKey, mapSize);
    return mapSize;
  }

  @Override
  public int compareTo(@NotNull MapSize o) {
    int dimension = widthInPixels * heightInPixels;
    int otherDimension = o.widthInPixels * o.heightInPixels;

    if (dimension == otherDimension) {
      return Integer.compare(widthInPixels, o.widthInPixels);
    }

    return Integer.compare(dimension, otherDimension);
  }

  public int getWidthInKm() {
    return (int) (widthInPixels / MAP_SIZE_FACTOR);
  }

  public int getHeightInKm() {
    return (int) (heightInPixels / MAP_SIZE_FACTOR);
  }

  @Override
  public String toString() {
    return String.format("%dx%d", widthInPixels, heightInPixels);
  }
}
