package ru.ry0.hclogin.pulsar;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import org.apache.pulsar.client.api.*;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import com.google.gson.Gson;
import ru.ry0.hclogin.config.ConfigManager;
import ru.ry0.hclogin.models.AuthData;

public class PulsarManager {
    private final PulsarClient pulsarClient;
    private final ConfigManager configManager;

    public PulsarManager(ConfigManager configManager) {
        this.configManager = configManager;
        try {
            this.pulsarClient = PulsarClient.builder()
                    .serviceUrl(configManager.getPulsarServiceUrl())
                    .listenerName(configManager.getPulsarListenerName())
                    .build();
        } catch (PulsarClientException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create Pulsar client", e);
        }
    }

    public void listenForAuth(Player player, ProxyServer server, String sessionId) {
        try {
            Consumer<byte[]> consumer = pulsarClient.newConsumer()
                    .topic(configManager.getPulsarTopic())
                    .subscriptionName(sessionId)
                    .subscriptionType(SubscriptionType.Exclusive)
                    .subscribe();

            while (player.isActive() && player.getCurrentServer().isPresent()) {
                Message<byte[]> msg = consumer.receive(1, TimeUnit.SECONDS);
                if (msg != null) {
                    String jsonString = new String(msg.getData(), StandardCharsets.UTF_8);
                    AuthData authData = parseJson(jsonString);

                    if (authData != null && authData.getSession().equals(sessionId)) {
                        processToken(player, server, authData.getToken(), authData.getUser().getName());
                        consumer.acknowledge(msg);
                        break;
                    }
                }
            }

            consumer.close();
        } catch (PulsarClientException e) {
            e.printStackTrace();
        }
    }

    private AuthData parseJson(String jsonString) {
        return new Gson().fromJson(jsonString, AuthData.class);
    }

    private void processToken(Player player, ProxyServer server, String token, String newName) {
        server.getCommandManager().executeImmediatelyAsync(player, "nickname set " + newName);
        server.getServer(configManager.getRedirectServerName()).ifPresent(serverInfo -> player.createConnectionRequest(serverInfo).fireAndForget());
    }
}
