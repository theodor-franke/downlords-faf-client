package com.faforever.client.remote;

import com.faforever.client.game.Faction;
import com.faforever.client.game.HostGameRequest;
import com.faforever.client.game.relay.GpgGameMessage;
import com.faforever.client.game.relay.ice.IceServerList;
import com.faforever.client.net.ConnectionState;
import com.faforever.client.user.AccountDetailsServerMessage;
import javafx.beans.property.ReadOnlyObjectProperty;

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

  CompletableFuture<AccountDetailsServerMessage> connectAndLogIn(String username, String password);

  void requestHostGame(HostGameRequest hostGameRequest);

  void requestJoinGame(int gameId, String password);

  void disconnect();

  void reconnect();

  void addFriend(int playerId);

  void addFoe(int playerId);

  void startSearchLadder1v1(Faction faction);

  void stopSearchingRanked();

  void sendGpgMessage(GpgGameMessage message);

  void removeFriend(int playerId);

  void removeFoe(int playerId);

  void selectAvatar(Integer avatarId);

  CompletableFuture<IceServerList> getIceServers();

  void restoreGameSession(int gameId);

}
