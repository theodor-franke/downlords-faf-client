package com.faforever.client.news;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsItem {

  private String id;
  private String author;
  private String title;
  private String content;
  private OffsetDateTime date;
  private List<NewsTag> tags;
}
