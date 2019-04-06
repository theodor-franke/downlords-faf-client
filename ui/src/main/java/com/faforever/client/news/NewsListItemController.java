package com.faforever.client.news;

import com.faforever.client.fx.Controller;
import com.faforever.client.i18n.I18n;
import com.faforever.client.theme.UiService;
import com.google.common.collect.ImmutableMap;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NewsListItemController implements Controller<Node> {

  private static final Map<NewsTag, String> CATEGORY_IMAGES = ImmutableMap.<NewsTag, String>builder()
    .put(NewsTag.SERVER_UPDATE, UiService.SERVER_UPDATE_NEWS_IMAGE)
    .put(NewsTag.TOURNAMENT, UiService.TOURNAMENT_NEWS_IMAGE)
    .put(NewsTag.FA_UPDATE, UiService.FA_UPDATE_NEWS_IMAGE)
    .put(NewsTag.LOBBY_UPDATE, UiService.LOBBY_UPDATE_NEWS_IMAGE)
    .put(NewsTag.BALANCE, UiService.BALANCE_NEWS_IMAGE)
    .put(NewsTag.WEBSITE, UiService.WEBSITE_NEWS_IMAGE)
    .put(NewsTag.CAST, UiService.CAST_NEWS_IMAGE)
    .put(NewsTag.PODCAST, UiService.PODCAST_NEWS_IMAGE)
    .put(NewsTag.FEATURED_MOD, UiService.FEATURED_MOD_NEWS_IMAGE)
    .put(NewsTag.DEVELOPMENT, UiService.DEVELOPMENT_NEWS_IMAGE)
    .put(NewsTag.LADDER, UiService.LADDER_NEWS_IMAGE)
    .build();

  private final I18n i18n;
  private final UiService uiService;

  public Node newsItemRoot;
  public ImageView imageView;
  public Label titleLabel;
  public Label authoredLabel;

  public NewsListItemController(I18n i18n, UiService uiService) {
    this.i18n = i18n;
    this.uiService = uiService;
  }

  @Override
  public Node getRoot() {
    return newsItemRoot;
  }

  public void setNewsItem(NewsItem newsItem) {
    // TODO only use this if there's no thumbnail. However, there's never a thumbnail ATM.
    String categoryImageUrl = newsItem.getTags().stream()
      .findFirst()
      .map(newsTag -> CATEGORY_IMAGES.getOrDefault(newsTag, UiService.DEFAULT_NEWS_IMAGE))
      .orElse(UiService.DEFAULT_NEWS_IMAGE);

    imageView.setImage(uiService.getThemeImage(categoryImageUrl));
    titleLabel.setText(newsItem.getTitle());
    authoredLabel.setText(i18n.get("news.authoredFormat", newsItem.getAuthor(), newsItem.getDate()));
  }
}
