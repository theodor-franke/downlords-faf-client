package com.faforever.client.avatar;

import com.faforever.client.remote.FafService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AvatarServiceTest {

  @Rule
  public TemporaryFolder cacheFolder = new TemporaryFolder();

  @Mock
  private FafService fafService;

  private AvatarService instance;
  @Before
  public void setUp() throws Exception {
    instance = new AvatarService(fafService);
  }

  @Test
  public void getAvailableAvatars() throws Exception {
    instance.getAvailableAvatars(1);
    verify(fafService).getAvailableAvatars(1);
  }

  @Test
  public void changeAvatar() throws Exception {
    Avatar avatar = new Avatar(null, null, null);
    instance.changeAvatar(avatar);
    verify(fafService).selectAvatar(avatar);
  }
}
