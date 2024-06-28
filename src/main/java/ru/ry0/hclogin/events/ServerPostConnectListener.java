package ru.ry0.hclogin.events;

import com.google.zxing.WriterException;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import ru.ry0.hclogin.AuthPlugin;
import ru.ry0.hclogin.config.ConfigManager;
import ru.ry0.hclogin.pulsar.PulsarManager;
import ru.ry0.hclogin.util.QrCodeUtil;
import ru.ry0.hclogin.util.RandomIdUtil;

import java.io.IOException;

public class ServerPostConnectListener {
    private final ProxyServer server;
    private final ConfigManager configManager;
    private final PulsarManager pulsarManager;
    private final AuthPlugin plugin;

    public ServerPostConnectListener(ProxyServer server, ConfigManager configManager, PulsarManager pulsarManager, AuthPlugin plugin) {
        this.server = server;
        this.configManager = configManager;
        this.pulsarManager = pulsarManager;
        this.plugin = plugin;
    }

    @Subscribe
    public void serverPostConnectEvent(ServerPostConnectEvent event) {
        Player player = event.getPlayer();
        RegisteredServer psrv = player.getCurrentServer().get().getServer();

        String serverName = psrv.getServerInfo().getName();
        if (serverName.equals(configManager.getAuthServerName())) {
            String sessionId = RandomIdUtil.generateRandomId();
            String url = configManager.getAuthUrl() + sessionId;

            try {
                QrCodeUtil.sendQrCodeToPlayer(player, url);
            } catch (WriterException | IOException e) {
                e.printStackTrace();
            }

            server.getScheduler().buildTask(plugin, () -> pulsarManager.listenForAuth(player, server, sessionId)).schedule();
        }
    }
}
