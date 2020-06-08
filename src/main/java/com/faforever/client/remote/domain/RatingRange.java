package com.faforever.client.remote.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.NotNull;

public class RatingRange implements Comparable<RatingRange> {

  private final Integer min;
  private final Integer max;

  public RatingRange(Integer min, Integer max) {
    this.min = min;
    this.max = max;
  }

  @Override
  public int compareTo(@NotNull RatingRange o) {
    return Integer.compare(getMin(), o.getMin());
  }

  public Integer getMin() {
    return min;
  }

  public Integer getMax() {
    return max;
  }

  @JsonValue
  public int[] toArray() {
    return new int[]{min, max};
  }

  @JsonCreator
  public static RatingRange fromArray(int[] array) {
    return new RatingRange(array[0], array[1]);
  }
}
