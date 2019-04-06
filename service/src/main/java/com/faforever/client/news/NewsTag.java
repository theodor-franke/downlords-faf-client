package com.faforever.client.news;


import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum NewsTag {

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

  private static final Map<String, NewsTag> fromString;

  static {
    fromString = new HashMap<>();
    for (NewsTag newsTag : values()) {
      fromString.put(newsTag.name, newsTag);
    }
  }

  private final String name;

  NewsTag(String name) {
    this.name = name;
  }

  public static NewsTag fromString(String string) {
    if (string == null) {
      return null;
    }
    String toLower = string.toLowerCase(Locale.US);
    if (!fromString.containsKey(toLower)) {
      return NewsTag.UNCATEGORIZED;
    }
    return fromString.get(toLower);
  }
}
