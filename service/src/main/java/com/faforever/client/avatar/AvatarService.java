package com.faforever.client.avatar;

import com.faforever.client.remote.FafService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Lazy
@Service
public class AvatarService {

  private final FafService fafService;

  public AvatarService(FafService fafService) {
    this.fafService = fafService;
  }

  public CompletableFuture<List<Avatar>> getAvailableAvatars(int playerId) {
    return fafService.getAvailableAvatars(playerId);
  }

  public void changeAvatar(Avatar avatar) {
    fafService.selectAvatar(avatar);
  }
}
