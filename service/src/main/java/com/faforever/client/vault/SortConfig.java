package com.faforever.client.vault;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SortConfig {
  private String sortProperty;
  private SortOrder sortOrder;

  public String toQuery() {
    return sortOrder.getQuery() + sortProperty;
  }
}
