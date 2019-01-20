package com.faforever.client.player;

import com.faforever.client.remote.ClientMessage;
import lombok.Value;

/**
 * Message sent from the client to the server requesting to create, update or remove a social relation to another
 * account.
 */
@Value(staticConstructor = "of")
public class UpdateSocialRelationClientMessage implements ClientMessage {

  /** The ID of the "other" account. */
  private int accountId;

  /** The operation to perform on this social relation. */
  private Operation operation;

  /** The type of the relation. */
  private RelationType relationType;

  /** The type of the relation. */
  public enum RelationType {
    /** Modify a 'friend' relation. */
    FRIEND,

    /** Modify a 'foe' relation. */
    FOE
  }

  /** The operation to perform on this social relation. */
  public enum Operation {
    /** Add this social relation. */
    ADD,
    /** Remove this social relation. */
    REMOVE
  }
}
