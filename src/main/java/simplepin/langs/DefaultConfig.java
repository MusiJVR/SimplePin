package simplepin.langs;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;


public enum DefaultConfig {
    LANG("lang", "en_en"),
    LOGIN_ATTEMPTS("login-attempts", "3"),
    RELOGIN_TIME("relogin-time", "300");

    private String path;
    private String value;
    private static YamlConfiguration CONFIG;

    DefaultConfig(String path, String value) {
        this.path = path;
        this.value = value;
    }

    public static void setFile(YamlConfiguration config) {
        CONFIG = config;
    }

    @Override
    public String toString() {
        return CONFIG.getString(path, value);
    }

    public String getValue() {
        return this.value;
    }
    public String getPath() {
        return this.path;
    }

    public static void createDefaultConfigFile(JavaPlugin plugin) {
        File config = new File(plugin.getDataFolder(), "config.yml");
        YamlConfiguration configFile = YamlConfiguration.loadConfiguration(config);
        if (!config.exists()) {
            try {
                configFile.save(config);
            } catch (IOException e) {
                e.printStackTrace();
                plugin.getServer().getLogger().info("[SimplePin] Could not create config file");
                plugin.getServer().getLogger().info("[SimplePin] Disabling plugin");
                plugin.getServer().getPluginManager().disablePlugin(plugin);
            }
        }
        for (DefaultConfig item : DefaultConfig.values()) {
            if (configFile.getString(item.getPath()) == null) {
                configFile.set(item.getPath(), item.getValue());
            }
        }
        DefaultConfig.setFile(configFile);
        try {
            configFile.save(config);
        } catch (IOException e) {
            plugin.getServer().getLogger().info("[SimplePin] Could not save config file");
            plugin.getServer().getLogger().info("[SimplePin] Disabling plugin");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }
}