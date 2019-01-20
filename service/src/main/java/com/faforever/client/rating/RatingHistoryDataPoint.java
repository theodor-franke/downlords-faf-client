package com.faforever.client.rating;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class RatingHistoryDataPoint {
  private final OffsetDateTime instant;
  private final int rank;
}
