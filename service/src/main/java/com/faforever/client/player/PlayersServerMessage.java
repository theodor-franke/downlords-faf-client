package com.faforever.client.player;

import com.faforever.client.remote.ServerMessage;
import lombok.Data;

import java.util.List;

/** Holds player information sent by the server. */
@Data
public class PlayersServerMessage implements ServerMessage {

  List<PlayerServerMessage> players;
}
