package com.faforever.client.remote.gson;

import com.faforever.client.fa.relay.GpgServerMessageType;
import com.faforever.client.remote.domain.FafServerMessageType;
import com.faforever.client.remote.domain.MessageTarget;
import com.faforever.client.remote.domain.ServerMessage;
import com.faforever.client.remote.domain.ServerMessageType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class ServerMessageJsonComponent {

  @SuppressWarnings("unused")
  public static class Deserializer extends JsonDeserializer<ServerMessage> {

    @Override
    public ServerMessage deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      TreeNode treeNode = p.readValueAsTree();

      String command = ((JsonNode) treeNode.get("command")).textValue();
      JsonNode targetNode = (JsonNode) treeNode.get("target");

      String target = null;
      if (targetNode != null && targetNode.isNull()) {
        target = targetNode.textValue();
      }

      MessageTarget messageTarget = MessageTarget.fromString(target);

      ServerMessageType serverMessageType;
      switch (messageTarget) {
        case GAME:
        case CONNECTIVITY:
          serverMessageType = GpgServerMessageType.fromString(command);
          break;

        case CLIENT:
          serverMessageType = FafServerMessageType.fromString(command);
          break;

        default:
          return null;
      }

      if (serverMessageType == null) {
        return null;
      }

      return ctxt.readValue(p, serverMessageType.getType());
    }
  }
}
