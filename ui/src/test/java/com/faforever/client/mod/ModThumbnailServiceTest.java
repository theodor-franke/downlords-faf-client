package com.faforever.client.mod;

import com.faforever.client.asset.AssetService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ModThumbnailServiceTest {

  private ModThumbnailService instance;

  @Mock
  private AssetService assetService;

  @Before
  public void setUp() throws Exception {
    instance = new ModThumbnailService(assetService);
  }

  @Test
  public void testLoadThumbnail() throws MalformedURLException {
    ModVersion modVersion = new ModVersion();
    modVersion.setThumbnailUrl(new URL("http://127.0.0.1:65534/thumbnail.png"));

    instance.loadThumbnail(modVersion);
    verify(assetService).loadAndCacheImage(eq(modVersion.getThumbnailUrl()), eq(Paths.get("mods")), any());
  }

}
