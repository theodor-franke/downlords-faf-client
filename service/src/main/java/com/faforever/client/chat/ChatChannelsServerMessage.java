package com.faforever.client.chat;

import com.faforever.client.remote.ServerMessage;
import lombok.Data;

import java.util.List;

@Data
public class ChatChannelsServerMessage implements ServerMessage {
  private List<String> channels;
}
