package simplepin;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import simplepin.command.*;
import simplepin.handler.*;
import simplepin.langs.DefaultConfig;
import simplepin.utils.LocalizationUtils;
import simplepin.utils.DatabaseDriver;

import java.util.*;

public final class SimplePin extends JavaPlugin implements Listener {
    private static SimplePin instance;
    private DatabaseDriver dbDriver;
    public Map<String, String> pinPlayer = new HashMap<>();
    public Map<String, String> playerMode = new HashMap<>();
    public Map<String, String> pinCodes = new HashMap<>();
    public Map<String, ItemStack[]> playerInventories = new HashMap<>();
    public Map<String, Location> playerLocations = new HashMap<>();
    public Map<String, Integer> attemptsLogin = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        DefaultConfig.createDefaultConfigFile(this);
        LocalizationUtils.loadLang(this);

        dbDriver = new DatabaseDriver("jdbc:sqlite:" + getDataFolder() + "/spdatabase.db");
        dbDriver.createTable("pins", "uuid TEXT NOT NULL PRIMARY KEY", "player_name TEXT", "pin TEXT", "session_logged INTEGER");

        Map<String, Object> updateMapPins= new HashMap<>();
        updateMapPins.put("session_logged", 0);
        dbDriver.updateData("pins", updateMapPins, "session_logged = ?", 1);

        getServer().getLogger().info("[SimplePin] " + "\u001B[32m" + "╔═════╗╔═══╗╔═╗ ╔═╗" + "\u001B[0m");
        getServer().getLogger().info("[SimplePin] " + "\u001B[32m" + "║ ╔═╗ ║╚╗ ╔╝║ ╚╗║ ║" + "\u001B[0m");
        getServer().getLogger().info("[SimplePin] " + "\u001B[32m" + "║ ╚═╝ ║ ║ ║ ║  ╚╝ ║" + "\u001B[0m");
        getServer().getLogger().info("[SimplePin] " + "\u001B[32m" + "║ ╔═══╝ ║ ║ ║ ╔╗  ║" + "\u001B[0m");
        getServer().getLogger().info("[SimplePin] " + "\u001B[32m" + "║ ║    ╔╝ ╚╗║ ║╚╗ ║" + "\u001B[0m");
        getServer().getLogger().info("[SimplePin] " + "\u001B[32m" + "╚═╝    ╚═══╝╚═╝ ╚═╝" + "\u001B[0m");
        getServer().getLogger().info("[SimplePin] SimplePin is enabled");

        getServer().getPluginManager().registerEvents(new PlayerJoinEventHandler(dbDriver), this);
        getServer().getPluginManager().registerEvents(new PINMenuInventoryHandler(this, dbDriver), this);
        getServer().getPluginCommand("helppin").setExecutor(new HelpPINCommand(this));
        getServer().getPluginCommand("helppin").setTabCompleter(new HelpPINTabCompleter());
        getServer().getPluginCommand("reloadpin").setExecutor(new ReloadConfigCommand(this));
        getServer().getPluginCommand("reloadpin").setTabCompleter(new ReloadConfigTabCompleter());
        getServer().getPluginCommand("blockpin").setExecutor(new BlockPINCommand(dbDriver));
        getServer().getPluginCommand("blockpin").setTabCompleter(new BlockPINTabCompleter());
        getServer().getPluginCommand("resetpin").setExecutor(new ResetPINCommand(this, dbDriver));
        getServer().getPluginCommand("resetpin").setTabCompleter(new ResetPINTabCompleter(dbDriver));
        getServer().getPluginCommand("setpin").setExecutor(new SetPINCommand(this, dbDriver));
        getServer().getPluginCommand("setpin").setTabCompleter(new SetPINTabCompleter(dbDriver));
    }

    @Override
    public void onDisable() {
        for (Player player : getServer().getOnlinePlayers()) {
            PINMenuInventoryHandler.playerResetInventory(player);
        }

        getServer().getLogger().info("[SimplePin] SimplePin is disabled");
        dbDriver.closeConnection();
    }

    public static SimplePin getInstance() {
        return instance;
    }
}
