package com.faforever.client.chat;

import com.faforever.client.remote.ServerMessage;
import lombok.Data;

import java.util.Set;

@Data
public class ChatChannelsServerMessage implements ServerMessage {
  private Set<String> channels;
}
