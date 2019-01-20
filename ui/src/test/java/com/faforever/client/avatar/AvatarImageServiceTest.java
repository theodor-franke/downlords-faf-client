package com.faforever.client.avatar;

import com.faforever.client.asset.AssetService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.URL;
import java.nio.file.Paths;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AvatarImageServiceTest {

  private AvatarImageService instance;

  @Mock
  private AssetService assetService;

  @Before
  public void setUp() throws Exception {
    instance = new AvatarImageService(assetService);
  }

  @Test
  public void testLoadAvatar() throws Exception {
    URL url = getClass().getResource("/theme/images/close.png").toURI().toURL();
    instance.loadAvatar(url);
    verify(assetService).loadAndCacheImage(url, Paths.get("avatars"), null);
  }
}
