package com.faforever.client.avatar;

import com.faforever.client.asset.AssetService;
import javafx.scene.image.Image;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.nio.file.Paths;

import static com.faforever.client.config.CacheNames.AVATARS;

@Lazy
@Service
public class AvatarImageService {
  private final AssetService assetService;

  public AvatarImageService(AssetService assetService) {
    this.assetService = assetService;
  }

  @Cacheable(AVATARS)
  public Image loadAvatar(URL avatarUrl) {
    return assetService.loadAndCacheImage(avatarUrl, Paths.get("avatars"), null);
  }
}
