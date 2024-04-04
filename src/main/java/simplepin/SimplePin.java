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
import simplepin.utils.SqliteDriver;

import java.util.*;


public final class SimplePin extends JavaPlugin implements Listener {
    private static SimplePin instance;
    private SqliteDriver sql;
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

        try {
            sql = new SqliteDriver(getDataFolder() + "/spdatabase.db");

            sql.sqlUpdateData("PINS", "SessionLogged = " + 0, "SessionLogged = " + 1);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        getServer().getLogger().info("[SimplePin] " + "\u001B[32m" + "╔═════╗╔═══╗╔═╗ ╔═╗" + "\u001B[0m");
        getServer().getLogger().info("[SimplePin] " + "\u001B[32m" + "║ ╔═╗ ║╚╗ ╔╝║ ╚╗║ ║" + "\u001B[0m");
        getServer().getLogger().info("[SimplePin] " + "\u001B[32m" + "║ ╚═╝ ║ ║ ║ ║  ╚╝ ║" + "\u001B[0m");
        getServer().getLogger().info("[SimplePin] " + "\u001B[32m" + "║ ╔═══╝ ║ ║ ║ ╔╗  ║" + "\u001B[0m");
        getServer().getLogger().info("[SimplePin] " + "\u001B[32m" + "║ ║    ╔╝ ╚╗║ ║╚╗ ║" + "\u001B[0m");
        getServer().getLogger().info("[SimplePin] " + "\u001B[32m" + "╚═╝    ╚═══╝╚═╝ ╚═╝" + "\u001B[0m");
        getServer().getLogger().info("[SimplePin] SimplePin is enabled");

        getServer().getPluginManager().registerEvents(new PlayerJoinEventHandler(sql), this);
        getServer().getPluginManager().registerEvents(new PINMenuInventoryHandler(this, sql), this);
        getServer().getPluginCommand("helppin").setExecutor(new HelpPINCommand(this));
        getServer().getPluginCommand("helppin").setTabCompleter(new HelpPINTabCompleter());
        getServer().getPluginCommand("blockpin").setExecutor(new BlockPINCommand(sql));
        getServer().getPluginCommand("blockpin").setTabCompleter(new BlockPINTabCompleter());
        getServer().getPluginCommand("resetpin").setExecutor(new ResetPINCommand(this, sql));
        getServer().getPluginCommand("resetpin").setTabCompleter(new ResetPINTabCompleter(sql));
        getServer().getPluginCommand("setpin").setExecutor(new SetPINCommand(this, sql));
        getServer().getPluginCommand("setpin").setTabCompleter(new SetPINTabCompleter(sql));
    }

    @Override
    public void onDisable() {
        for (Player player : getServer().getOnlinePlayers()) {
            PINMenuInventoryHandler.playerResetInventory(player);
        }

        getServer().getLogger().info("[SimplePin] SimplePin is disabled");
        try {
            sql.connection.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public static SimplePin getInstance() {return instance;}
}