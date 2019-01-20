package com.faforever.client.avatar.event;

import com.faforever.client.avatar.Avatar;
import org.jetbrains.annotations.Nullable;

public class AvatarChangedEvent {
  private final Avatar avatar;

  public AvatarChangedEvent(@Nullable Avatar avatar) {
    this.avatar = avatar;
  }

  @Nullable
  public Avatar getAvatar() {
    return avatar;
  }
}
