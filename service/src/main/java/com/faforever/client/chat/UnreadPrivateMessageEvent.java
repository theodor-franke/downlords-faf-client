package com.faforever.client.chat;

import lombok.Data;

@Data
public class UnreadPrivateMessageEvent {
  private final ChatMessage message;
}
