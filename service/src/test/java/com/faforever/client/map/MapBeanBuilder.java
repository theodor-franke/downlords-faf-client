package com.faforever.client.map;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class MapBeanBuilder {

  private final FaMap faMap;

  public MapBeanBuilder() {
    faMap = new FaMap();
  }

  public MapBeanBuilder defaultValues() {
    uid(UUID.randomUUID().toString())
        .displayName("Map Name");
    return this;
  }

  public MapBeanBuilder displayName(String name) {
    faMap.setDisplayName(name);
    return this;
  }

  public MapBeanBuilder uid(String uid) {
    faMap.setId(uid);
    return this;
  }

  public FaMap get() {
    return faMap;
  }

  public MapBeanBuilder downloadUrl(URL url) throws MalformedURLException {
    faMap.setDownloadUrl(url);
    return this;
  }

  public MapBeanBuilder author(String author) {
    faMap.setAuthor(author);
    return this;
  }

  public MapBeanBuilder smallThumbnailUrl(URL thumbnailUrl) {
    faMap.setSmallThumbnailUrl(thumbnailUrl);
    return this;
  }

  public MapBeanBuilder largeThumbnailUrl(URL thumbnailUrl) {
    faMap.setLargeThumbnailUrl(thumbnailUrl);
    return this;
  }

  public static MapBeanBuilder create() {
    return new MapBeanBuilder();
  }
}
