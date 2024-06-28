package ru.ry0.hclogin.config;

import com.velocitypowered.api.proxy.ProxyServer;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class ConfigManager {
    private final ProxyServer server;
    private final Path configPath;
    private Map<String, Object> config;

    public ConfigManager(ProxyServer server, Path dataDirectory) {
        this.server = server;
        this.configPath = dataDirectory.resolve("config.yml");
        loadConfig();
    }

    private void loadConfig() {
        try {
            if (Files.notExists(configPath)) {
                Files.createDirectories(configPath.getParent());
                try (InputStream in = getClass().getResourceAsStream("/config.yml")) {
                    if (in != null) {
                        Files.copy(in, configPath);
                    } else {
                        throw new FileNotFoundException("Default config.yml not found in resources.");
                    }
                }
            }
            try (InputStream in = Files.newInputStream(configPath)) {
                Yaml yaml = new Yaml();
                this.config = yaml.load(in);
                validateConfig();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void validateConfig() {
        if (getAuthServerName() == null || getAuthServerName().isEmpty()) {
            throw new IllegalArgumentException("Auth server name must not be blank.");
        }
        if (getRedirectServerName() == null || getRedirectServerName().isEmpty()) {
            throw new IllegalArgumentException("Redirect server name must not be blank.");
        }
        if (getAuthUrl() == null || getAuthUrl().isEmpty()) {
            throw new IllegalArgumentException("Auth URL must not be blank.");
        }
        if (getPulsarServiceUrl() == null || getPulsarServiceUrl().isEmpty()) {
            throw new IllegalArgumentException("Pulsar service URL must not be blank.");
        }
        if (getPulsarListenerName() == null || getPulsarListenerName().isEmpty()) {
            throw new IllegalArgumentException("Pulsar listener name must not be blank.");
        }
        if (getPulsarTopic() == null || getPulsarTopic().isEmpty()) {
            throw new IllegalArgumentException("Pulsar topic must not be blank.");
        }
    }

    public String getAuthServerName() {
        return (String) ((Map<String, Object>) config.get("auth")).get("server_name");
    }

    public String getRedirectServerName() {
        return (String) ((Map<String, Object>) config.get("auth")).get("redirect_server");
    }

    public String getAuthUrl() {
        return (String) ((Map<String, Object>) config.get("auth")).get("auth_url");
    }

    public String getPulsarServiceUrl() {
        return (String) ((Map<String, Object>) config.get("pulsar")).get("service_url");
    }

    public String getPulsarListenerName() {
        return (String) ((Map<String, Object>) config.get("pulsar")).get("listener_name");
    }

    public String getPulsarTopic() {
        return (String) ((Map<String, Object>) config.get("pulsar")).get("topic");
    }
}
