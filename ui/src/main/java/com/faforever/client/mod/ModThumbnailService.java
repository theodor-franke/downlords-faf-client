package com.faforever.client.mod;

import com.faforever.client.asset.AssetService;
import com.faforever.client.util.IdenticonUtil;
import javafx.scene.image.Image;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.nio.file.Paths;

@Service
public class ModThumbnailService {
  private final AssetService assetService;

  public ModThumbnailService(AssetService assetService) {
    this.assetService = assetService;
  }

  public Image loadThumbnail(ModVersion modVersion) {
    // TODO reintroduce correct caching
    URL url = modVersion.getThumbnailUrl();
    return assetService.loadAndCacheImage(url, Paths.get("mods"), () -> IdenticonUtil.createIdenticon(modVersion.getDisplayName()));
  }
}
