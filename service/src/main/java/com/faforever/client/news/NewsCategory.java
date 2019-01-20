package com.faforever.client.news;


import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum NewsCategory {

  SERVER_UPDATE("server update"),
  TOURNAMENT("tournament"),
  FA_UPDATE("fa update"),
  LOBBY_UPDATE("lobby update"),
  BALANCE("balance"),
  WEBSITE("website"),
  CAST("cast"),
  PODCAST("podcast"),
  FEATURED_MOD("featured mods"),
  DEVELOPMENT("development update"),
  UNCATEGORIZED("uncategorized"),
  LADDER("ladder");

  private static final Map<String, NewsCategory> fromString;

  static {
    fromString = new HashMap<>();
    for (NewsCategory newsCategory : values()) {
      fromString.put(newsCategory.name, newsCategory);
    }
  }

  private final String name;

  NewsCategory(String name) {
    this.name = name;
  }

  public static NewsCategory fromString(String string) {
    if (string == null) {
      return null;
    }
    String toLower = string.toLowerCase(Locale.US);
    if (!fromString.containsKey(toLower)) {
      return NewsCategory.UNCATEGORIZED;
    }
    return fromString.get(toLower);
  }
}
