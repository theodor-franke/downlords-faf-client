package com.faforever.client.avatar;

import com.faforever.client.remote.ClientMessage;
import lombok.Value;

@Value
public class SelectAvatarClientMessage implements ClientMessage {
  Integer avatarId;
}
