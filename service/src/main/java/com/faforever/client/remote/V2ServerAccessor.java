package com.faforever.client.remote;

import com.faforever.client.SpringProfiles;
import com.faforever.client.avatar.SelectAvatarClientMessage;
import com.faforever.client.game.Faction;
import com.faforever.client.game.HostGameRequest;
import com.faforever.client.game.JoinGameClientMessage;
import com.faforever.client.game.RestoreGameSessionRequest;
import com.faforever.client.game.relay.GpgGameMessage;
import com.faforever.client.game.relay.ice.IceServerList;
import com.faforever.client.game.relay.ice.ListIceServersClientMessage;
import com.faforever.client.integration.ChannelNames;
import com.faforever.client.integration.Protocol;
import com.faforever.client.integration.ServerGateway;
import com.faforever.client.matchmaking.SearchMatchClientMessage;
import com.faforever.client.net.ConnectionState;
import com.faforever.client.player.UpdateSocialRelationClientMessage;
import com.faforever.client.player.UpdateSocialRelationClientMessage.Operation;
import com.faforever.client.player.UpdateSocialRelationClientMessage.RelationType;
import com.faforever.client.user.AccountDetailsServerMessage;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.StandardIntegrationFlow;
import org.springframework.integration.dsl.context.IntegrationFlowContext;
import org.springframework.integration.websocket.ClientWebSocketContainer;
import org.springframework.integration.websocket.inbound.WebSocketInboundChannelAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Component
@Profile("!" + SpringProfiles.PROFILE_OFFLINE)
public class V2ServerAccessor implements FafServerAccessor {

  private final ServerGateway serverGateway;
  private final Map<Class<? extends ServerMessage>, Collection<Consumer<? extends ServerMessage>>> messageListeners;
  private final ObjectProperty<ConnectionState> connectionState;
  private final IntegrationFlowContext integrationFlowContext;

  private CompletableFuture<AccountDetailsServerMessage> loginFuture;
  private WebSocketInboundChannelAdapter webSocketInboundChannelAdapter;
  private String inboundFlowId;
  private StandardWebSocketClient webSocketClient;
  private ClientWebSocketContainer webSocketContainer;
  private CompletableFuture<IceServerList> iceServersRequestFuture;

  public V2ServerAccessor(ServerGateway serverGateway, IntegrationFlowContext integrationFlowContext) {
    this.serverGateway = serverGateway;
    this.integrationFlowContext = integrationFlowContext;

    connectionState = new SimpleObjectProperty<>();
    messageListeners = new HashMap<>();

    this.addOnMessageListener(IceServerList.class, this::onIceServerList);
  }

  private void onIceServerList(IceServerList iceServerList) {
    iceServersRequestFuture.complete(iceServerList);
    iceServersRequestFuture = null;
  }

  @Override
  public <T extends ServerMessage> void addOnMessageListener(Class<T> type, Consumer<T> listener) {
    if (!messageListeners.containsKey(type)) {
      messageListeners.put(type, new LinkedList<>());
    }
    messageListeners.get(type).add(listener);
  }

  @Override
  public <T extends ServerMessage> void removeOnMessageListener(Class<T> type, Consumer<T> listener) {
    messageListeners.get(type).remove(listener);
  }

  @Override
  public ReadOnlyObjectProperty<ConnectionState> connectionStateProperty() {
    return connectionState;
  }

  @Override
  public CompletableFuture<AccountDetailsServerMessage> connectAndLogIn(String username, String password) {
    disconnect();

    String uriTemplate = "ws://localhost:8012/ws";
    webSocketClient = new StandardWebSocketClient();
    webSocketContainer = new ClientWebSocketContainer(webSocketClient, uriTemplate);
    webSocketContainer.addSupportedProtocols(Protocol.V2_JSON_UTF_8.name());

    webSocketInboundChannelAdapter = new WebSocketInboundChannelAdapter(webSocketContainer);
    StandardIntegrationFlow flow = IntegrationFlows.from(webSocketInboundChannelAdapter)
      .channel(ChannelNames.SERVER_OUTBOUND)
      .get();
    inboundFlowId = integrationFlowContext.registration(flow).register().getId();

    loginFuture = new CompletableFuture<>();
    return loginFuture;
  }

  @Override
  public void requestHostGame(HostGameRequest hostGameRequest) {
    serverGateway.send(hostGameRequest);
  }

  @Override
  public void requestJoinGame(int gameId, String password) {
    serverGateway.send(JoinGameClientMessage.of(gameId, password));
  }

  @Override
  public void disconnect() {
    if (inboundFlowId != null) {
      integrationFlowContext.remove(inboundFlowId);
      inboundFlowId = null;
    }
    if (webSocketContainer != null) {
      webSocketContainer.stop();
      webSocketContainer = null;
    }
  }

  @Override
  public void reconnect() {
    // FIXME implement
  }

  @Override
  public void addFriend(int accountId) {
    serverGateway.send(UpdateSocialRelationClientMessage.of(accountId, Operation.REMOVE, RelationType.FRIEND));
  }

  @Override
  public void addFoe(int playerId) {
    serverGateway.send(UpdateSocialRelationClientMessage.of(playerId, Operation.ADD, RelationType.FOE));
  }

  @Override
  public void startSearchLadder1v1(Faction faction) {
    serverGateway.send(SearchMatchClientMessage.of("ladder1v1", faction));
  }

  @Override
  public void stopSearchingRanked() {

  }

  @Override
  public void sendGpgMessage(GpgGameMessage message) {
    serverGateway.send(message);
  }

  @Override
  public void removeFriend(int playerId) {
    serverGateway.send(UpdateSocialRelationClientMessage.of(playerId, Operation.REMOVE, RelationType.FRIEND));
  }

  @Override
  public void removeFoe(int playerId) {
    serverGateway.send(UpdateSocialRelationClientMessage.of(playerId, Operation.REMOVE, RelationType.FOE));
  }

  @Override
  public void selectAvatar(Integer avatarId) {
    serverGateway.send(new SelectAvatarClientMessage(avatarId));
  }

  @Override
  public CompletableFuture<IceServerList> getIceServers() {
    if (iceServersRequestFuture != null) {
      iceServersRequestFuture.cancel(false);
    }
    iceServersRequestFuture = new CompletableFuture<>();
    serverGateway.send(new ListIceServersClientMessage());
    return iceServersRequestFuture;
  }

  @Override
  public void restoreGameSession(int gameId) {
    serverGateway.send(new RestoreGameSessionRequest(gameId));
  }
}
