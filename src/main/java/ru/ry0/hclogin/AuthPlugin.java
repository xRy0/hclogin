package ru.ry0.hclogin;

import java.nio.file.Path;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import ru.ry0.hclogin.config.ConfigManager;
import ru.ry0.hclogin.events.ServerPostConnectListener;
import ru.ry0.hclogin.pulsar.PulsarManager;

@Plugin(id = "authplugin", name = "HinaCraft Login", version = "0.8", authors = {"Ry0"})
public class AuthPlugin {
    private final ProxyServer server;
    private final ConfigManager configManager;
    private final PulsarManager pulsarManager;
    

    @Inject
    public AuthPlugin(ProxyServer server, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.configManager = new ConfigManager(server, dataDirectory);
        this.pulsarManager = new PulsarManager(configManager);
    }

    @Subscribe
    public void onProxyInitializeEvent(ProxyInitializeEvent event) {
        server.getEventManager().register(this, new ServerPostConnectListener(server, configManager, pulsarManager, this));
    }
}
