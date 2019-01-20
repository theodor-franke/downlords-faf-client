package com.faforever.client.player;

import com.faforever.client.remote.ServerMessage;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SocialRelationsServerMessage implements ServerMessage {

  @NotNull
  private List<SocialRelation> socialRelations;

  @Data
  public static class SocialRelation {

    int playerId;
    RelationType type;

    /** The type of the social relation. */
    public enum RelationType {
      /** The "current" player sees the "other" player as a friend. */
      FRIEND,
      /** The "current" player sees the "other" player as a foe. */
      FOE
    }
  }
}
