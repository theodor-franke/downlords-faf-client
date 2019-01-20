package com.faforever.client.chat;

public class ChatMessageEvent {

  private final ChatMessage message;

  public ChatMessageEvent(ChatMessage message) {
    this.message = message;
  }

  public ChatMessage getMessage() {
    return message;
  }

}
