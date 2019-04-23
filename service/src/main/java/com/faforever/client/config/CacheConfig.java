package com.faforever.client.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {

  @Bean
  @Override
  public CacheManager cacheManager() {
    SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
    simpleCacheManager.setCaches(Arrays.asList(
        new CaffeineCache(CacheNames.STATISTICS, Caffeine.newBuilder().maximumSize(10).expireAfterWrite(20, MINUTES).build()),
        new CaffeineCache(CacheNames.ACHIEVEMENTS, Caffeine.newBuilder().expireAfterWrite(30, MINUTES).build()),
        new CaffeineCache(CacheNames.PLAYER_ACHIEVEMENTS, Caffeine.newBuilder().expireAfterWrite(10, MINUTES).build()),
        new CaffeineCache(CacheNames.MODS, Caffeine.newBuilder().expireAfterWrite(10, MINUTES).build()),
        new CaffeineCache(CacheNames.MAPS, Caffeine.newBuilder().expireAfterWrite(10, MINUTES).build()),
        new CaffeineCache(CacheNames.LEADERBOARD, Caffeine.newBuilder().maximumSize(1).expireAfterAccess(5, MINUTES).build()),
        new CaffeineCache(CacheNames.AVAILABLE_AVATARS, Caffeine.newBuilder().expireAfterAccess(30, SECONDS).build()),
        new CaffeineCache(CacheNames.COOP_MAPS, Caffeine.newBuilder().expireAfterAccess(10, SECONDS).build()),
        new CaffeineCache(CacheNames.NEWS, Caffeine.newBuilder().expireAfterWrite(1, MINUTES).build()),
        new CaffeineCache(CacheNames.RATING_HISTORY, Caffeine.newBuilder().expireAfterWrite(1, MINUTES).build()),
        new CaffeineCache(CacheNames.COOP_LEADERBOARD, Caffeine.newBuilder().expireAfterWrite(1, MINUTES).build()),
        new CaffeineCache(CacheNames.CLAN, Caffeine.newBuilder().expireAfterWrite(1, MINUTES).build()),
        new CaffeineCache(CacheNames.FEATURED_MODS, Caffeine.newBuilder().build()),
        new CaffeineCache(CacheNames.FEATURED_MOD_FILES, Caffeine.newBuilder().expireAfterWrite(10, MINUTES).build()),

        // Images should only be cached as long as they are in use. This avoids loading an image multiple times, while
        // at the same time it doesn't prevent unused images from being garbage collected.
        new CaffeineCache(CacheNames.ACHIEVEMENT_IMAGES, Caffeine.newBuilder().weakValues().build()),
        new CaffeineCache(CacheNames.AVATARS, Caffeine.newBuilder().weakValues().build()),
        new CaffeineCache(CacheNames.URL_PREVIEW, Caffeine.newBuilder().weakValues().expireAfterAccess(30, MINUTES).build()),
        new CaffeineCache(CacheNames.MAP_PREVIEW, Caffeine.newBuilder().weakValues().build()),
        new CaffeineCache(CacheNames.COUNTRY_FLAGS, Caffeine.newBuilder().weakValues().build()),
        new CaffeineCache(CacheNames.THEME_IMAGES, Caffeine.newBuilder().weakValues().build()),
        new CaffeineCache(CacheNames.MOD_THUMBNAIL, Caffeine.newBuilder().weakValues().build()
        )));
    return simpleCacheManager;
  }

  @Override
  public CacheResolver cacheResolver() {
    return null;
  }

  @Bean
  @Override
  public KeyGenerator keyGenerator() {
    return new SimpleKeyGenerator();
  }

  @Override
  public CacheErrorHandler errorHandler() {
    return new SimpleCacheErrorHandler();
  }
}
