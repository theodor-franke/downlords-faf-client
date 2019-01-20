package com.faforever.client.vault;

import lombok.Getter;

@Getter
public enum SortOrder {
  DESC("-", "search.sort.descending"),
  ASC("", "search.sort.ascending");

  private final String query;
  private final String i18nKey;

  SortOrder(String query, String i18nKey) {
    this.query = query;
    this.i18nKey = i18nKey;
  }
}
