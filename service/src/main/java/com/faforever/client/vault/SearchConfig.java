package com.faforever.client.vault;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchConfig {
  private SortConfig sortConfig;
  private String searchQuery;

  public boolean hasQuery() {
    return searchQuery != null && !searchQuery.isEmpty();
  }
}
