package com.faforever.client.news;

import com.faforever.client.preferences.PreferencesService;
import com.faforever.client.remote.FafService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Lazy
@Service
public class NewsService implements InitializingBean {

  /** The delay (in seconds) between polling for new news. */
  private static final long POLL_DELAY = Duration.ofMinutes(10).toMillis();

  private final PreferencesService preferencesService;
  private final ApplicationEventPublisher eventPublisher;
  private final TaskScheduler taskScheduler;
  private final FafService fafService;

  public NewsService(
    PreferencesService preferencesService,
    ApplicationEventPublisher eventPublisher,
    TaskScheduler taskScheduler,
    FafService fafService
  ) {
    this.preferencesService = preferencesService;
    this.eventPublisher = eventPublisher;
    this.taskScheduler = taskScheduler;
    this.fafService = fafService;
  }

  @Override
  public void afterPropertiesSet() {
    taskScheduler.scheduleWithFixedDelay(this::pollForNews, Date.from(Instant.now().plusSeconds(5)), POLL_DELAY);
  }

  public CompletableFuture<List<NewsItem>> getNews() {
    return fafService.getNews();
  }

  private void pollForNews() {
    fafService.getNews().thenAccept(newsItems -> newsItems.stream().findFirst()
      .ifPresent(newsItem -> {
        String lastReadNewsId = preferencesService.getPreferences().getNews().getLastReadNewsId();
        if (!Objects.equals(newsItem.getId(), lastReadNewsId)) {
          eventPublisher.publishEvent(new UnreadNewsEvent(true));
        }
      }));
  }
}
