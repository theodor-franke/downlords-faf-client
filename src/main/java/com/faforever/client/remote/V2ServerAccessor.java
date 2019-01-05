package com.faforever.client.remote;

import com.faforever.client.fa.relay.GpgGameMessage;
import com.faforever.client.fa.relay.ice.IceServer;
import com.faforever.client.game.Faction;
import com.faforever.client.game.HostGameRequest;
import com.faforever.client.game.JoinGameRequest;
import com.faforever.client.game.StartGameProcessMessage;
import com.faforever.client.integration.ChannelNames;
import com.faforever.client.integration.Protocol;
import com.faforever.client.integration.ServerGateway;
import com.faforever.client.net.ConnectionState;
import com.faforever.client.player.PlayerServerMessage;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.StandardIntegrationFlow;
import org.springframework.integration.dsl.context.IntegrationFlowContext;
import org.springframework.integration.websocket.ClientWebSocketContainer;
import org.springframework.integration.websocket.inbound.WebSocketInboundChannelAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class V2ServerAccessor implements FafServerAccessor {

  private final ServerGateway serverGateway;
  private final Map<Class<? extends ServerMessage>, Collection<Consumer<? extends ServerMessage>>> messageListeners;
  private final ObjectProperty<ConnectionState> connectionState;
  private final IntegrationFlowContext integrationFlowContext;

  private CompletableFuture<PlayerServerMessage> loginFuture;
  private WebSocketInboundChannelAdapter webSocketInboundChannelAdapter;
  private String inboundFlowId;
  private StandardWebSocketClient webSocketClient;
  private ClientWebSocketContainer webSocketContainer;

  public V2ServerAccessor(ServerGateway serverGateway, IntegrationFlowContext integrationFlowContext) {
    this.serverGateway = serverGateway;
    this.integrationFlowContext = integrationFlowContext;

    connectionState = new SimpleObjectProperty<>();
    messageListeners = new HashMap<>();
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
  public CompletableFuture<PlayerServerMessage> connectAndLogIn(String username, String password) {
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
    serverGateway.send(new JoinGameRequest());
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

  }

  @Override
  public void addFriend(int playerId) {

  }

  @Override
  public void addFoe(int playerId) {

  }

  @Override
  public CompletableFuture<StartGameProcessMessage> startSearchLadder1v1(Faction faction) {
    return null;
  }

  @Override
  public void stopSearchingRanked() {

  }

  @Override
  public void sendGpgMessage(GpgGameMessage message) {

  }

  @Override
  public void removeFriend(int playerId) {

  }

  @Override
  public void removeFoe(int playerId) {

  }

  @Override
  public void selectAvatar(URL url) {

  }

  @Override
  public CompletableFuture<List<IceServer>> getIceServers() {
    return null;
  }

  @Override
  public void restoreGameSession(int id) {

  }

  @Override
  public void ping() {

  }
}
