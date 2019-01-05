package com.faforever.client.remote;

import com.faforever.client.fa.relay.GpgGameMessage;
import com.faforever.client.fa.relay.ice.IceServer;
import com.faforever.client.game.Faction;
import com.faforever.client.game.HostGameRequest;
import com.faforever.client.game.StartGameProcessMessage;
import com.faforever.client.net.ConnectionState;
import com.faforever.client.player.PlayerServerMessage;
import javafx.beans.property.ReadOnlyObjectProperty;

import java.net.URL;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Entry class for all communication with the FAF server.
 */
public interface FafServerAccessor {

  @SuppressWarnings("unchecked")
  <T extends ServerMessage> void addOnMessageListener(Class<T> type, Consumer<T> listener);

  @SuppressWarnings("unchecked")
  <T extends ServerMessage> void removeOnMessageListener(Class<T> type, Consumer<T> listener);

  ReadOnlyObjectProperty<ConnectionState> connectionStateProperty();

  CompletableFuture<PlayerServerMessage> connectAndLogIn(String username, String password);

  void requestHostGame(HostGameRequest hostGameRequest);

  void requestJoinGame(int gameId, String password);

  void disconnect();

  void reconnect();

  void addFriend(int playerId);

  void addFoe(int playerId);

  CompletableFuture<StartGameProcessMessage> startSearchLadder1v1(Faction faction);

  void stopSearchingRanked();

  void sendGpgMessage(GpgGameMessage message);

  void removeFriend(int playerId);

  void removeFoe(int playerId);

  void selectAvatar(URL url);

  CompletableFuture<List<IceServer>> getIceServers();

  void restoreGameSession(int id);

  void ping();
}
