package simplepin.handler;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import simplepin.SimplePin;
import simplepin.utils.GeneralUtils;
import simplepin.utils.LocalizationUtils;
import simplepin.utils.PinMenuUtils;
import simplepin.utils.DatabaseDriver;

import java.util.*;

public class PINMenuInventoryHandler implements Listener {
    private final JavaPlugin plugin;
    private final DatabaseDriver dbDriver;

    public PINMenuInventoryHandler(JavaPlugin plugin, DatabaseDriver dbDriver) {
        this.plugin = plugin;
        this.dbDriver = dbDriver;
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        String language = SimplePin.getInstance().getConfig().getString("lang");
        Player player = (Player) event.getWhoClicked();
        ItemStack nameTag = event.getInventory().getItem(0);

        if(event.getView().getTitle().equals(ChatColor.BOLD + LocalizationUtils.langCheck(language, "PIN_MENU")) || nameTag != null && nameTag.getType() == Material.NAME_TAG && nameTag.hasItemMeta() && nameTag.displayName().equals("§a§l" + LocalizationUtils.langCheck(language, "CONFIRM_PIN"))) {


            if (event.getCurrentItem() == null) {

            } else if (Objects.equals(event.getCurrentItem().getItemMeta().getDisplayName(), "1")) {
                recordPinDigit("1", player, event);
            } else if (Objects.equals(event.getCurrentItem().getItemMeta().getDisplayName(), "2")) {
                recordPinDigit("2", player, event);
            } else if (Objects.equals(event.getCurrentItem().getItemMeta().getDisplayName(), "3")) {
                recordPinDigit("3", player, event);
            } else if (Objects.equals(event.getCurrentItem().getItemMeta().getDisplayName(), "4")) {
                recordPinDigit("4", player, event);
            } else if (Objects.equals(event.getCurrentItem().getItemMeta().getDisplayName(), "5")) {
                recordPinDigit("5", player, event);
            } else if (Objects.equals(event.getCurrentItem().getItemMeta().getDisplayName(), "6")) {
                recordPinDigit("6", player, event);
            } else if (Objects.equals(event.getCurrentItem().getItemMeta().getDisplayName(), "7")) {
                recordPinDigit("7", player, event);
            } else if (Objects.equals(event.getCurrentItem().getItemMeta().getDisplayName(), "8")) {
                recordPinDigit("8", player, event);
            } else if (Objects.equals(event.getCurrentItem().getItemMeta().getDisplayName(), "9")) {
                recordPinDigit("9", player, event);
            } else if (Objects.equals(event.getCurrentItem().getItemMeta().getDisplayName(), "§c§l" + LocalizationUtils.langCheck(language, "RESET_PIN"))) {
                resetPinDigit(player);
                player.playSound(player.getLocation(), Sound.BLOCK_STONE_PLACE, 1.0f, 1.0f);
            } else if (Objects.equals(event.getCurrentItem().getItemMeta().getDisplayName(), "§a§l" + LocalizationUtils.langCheck(language, "CONFIRM_PIN"))) {
                confirmPinDigit(plugin, dbDriver, player, language);
            }

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMenuClose(InventoryCloseEvent event) {
        String language = SimplePin.getInstance().getConfig().getString("lang");
        Player player = (Player) event.getPlayer();
        ItemStack nameTag = event.getInventory().getItem(0);

        if(event.getView().getTitle().equals(ChatColor.BOLD + LocalizationUtils.langCheck(language, "PIN_MENU")) || nameTag != null && nameTag.getType() == Material.NAME_TAG && nameTag.hasItemMeta() && nameTag.displayName().equals("§a§l" + LocalizationUtils.langCheck(language, "CONFIRM_PIN"))) {
            if (SimplePin.getInstance().pinPlayer.containsKey(player.getName())) {
                if (SimplePin.getInstance().pinPlayer.get(player.getName()).equals(player.getName())) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            player.teleport(SimplePin.getInstance().playerLocations.get(player.getName()));
                            player.openInventory(event.getInventory());
                        }
                    });
                } else {
                    playerResetInventory(player);

                    plugin.getServer().dispatchCommand(player, "resetpin " + SimplePin.getInstance().pinPlayer.get(player.getName()));
                }
            }
        }
    }

    public static void resetPinDigit(Player player) {
        SimplePin.getInstance().pinCodes.put(player.getName(), "");

        for (int i = 22; i < 26; i++) {
            PinMenuUtils.pinPanel(player, i);
        }
    }

    public static void recordPinDigit(String digit, Player player, InventoryClickEvent event) {
        String pin = SimplePin.getInstance().pinCodes.get(player.getName()) + digit;

        if (pin.length() < 5) {
            SimplePin.getInstance().pinCodes.put(player.getName(), pin);
            ItemStack item = event.getCurrentItem().clone();
            SkullMeta item_meta = (SkullMeta) item.getItemMeta();
            item_meta.setDisplayName(" ");
            item.setItemMeta(item_meta);
            player.getInventory().setItem(pin.length() + 21, item);
        }

        player.playSound(player.getLocation(), Sound.BLOCK_STONE_PLACE, 1.0f, 1.0f);
    }

    public static void confirmPinDigit(JavaPlugin plugin, DatabaseDriver dbDriver, Player player, String language) {
        String pin = SimplePin.getInstance().pinCodes.get(player.getName());

        List<Map<String, Object>> rsPin = dbDriver.selectData("pin", "pins", "WHERE player_name = ?", SimplePin.getInstance().pinPlayer.get(player.getName()));
        String playerPin = (String) rsPin.get(0).get("pin");

        if (playerPin != null) {
            if (pin.equals(playerPin) && playerPin.length() == 4) {
                playerResetInventory(player);
                sessionLoggedStatus(plugin, dbDriver, player.getName());
                plugin.getServer().getLogger().info("[SimplePin] " + player.getName() + " successfully logged in");
            } else {
                if (pin.length() == 4) {
                    int allowedAttempts = GeneralUtils.setDefaultValue(3, "login-attempts", 1, 100);

                    SimplePin.getInstance().attemptsLogin.put(player.getName(), SimplePin.getInstance().attemptsLogin.get(player.getName()) + 1);
                    resetPinDigit(player);

                    if (SimplePin.getInstance().attemptsLogin.get(player.getName()) > allowedAttempts) {
                        attemptsAreOver(plugin, player, language);
                    }
                }
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_STRONG, 1.0f, 1.0f);
            }
        } else {
            if (pin.length() == 4) {
                Map<String, Object> updateMapFirstPins= new HashMap<>();
                updateMapFirstPins.put("pin", pin);
                dbDriver.updateData("pins", updateMapFirstPins, "player_name = ?", SimplePin.getInstance().pinPlayer.get(player.getName()));

                if (Objects.equals(GeneralUtils.getMapKey(SimplePin.getInstance().pinPlayer, SimplePin.getInstance().pinPlayer.get(player.getName())), SimplePin.getInstance().pinPlayer.get(player.getName()))) {
                    sessionLoggedStatus(plugin, dbDriver, player.getName());
                    plugin.getServer().getLogger().info("[SimplePin] " + player.getName() + " successfully logged in");
                } else {
                    String msgResetPINSuccessfully = LocalizationUtils.langCheck(language, "SET_PIN_SUCCESSFULLY");
                    player.sendMessage(msgResetPINSuccessfully.replace("%player%", SimplePin.getInstance().pinPlayer.get(player.getName())));

                    Player playerSetPIN = plugin.getServer().getPlayer(SimplePin.getInstance().pinPlayer.get(player.getName()));
                    if (playerSetPIN != null) {
                        String msgYourPINSet = LocalizationUtils.langCheck(language, "YOUR_PIN_SET");
                        playerSetPIN.sendMessage(msgYourPINSet.replace("%pin%", pin));
                        playerSetPIN.playSound(playerSetPIN.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                    }

                    Map<String, Object> updateMapSecondPins= new HashMap<>();
                    updateMapSecondPins.put("session_logged", 0);
                    dbDriver.updateData("pins", updateMapSecondPins, "player_name = ?", SimplePin.getInstance().pinPlayer.get(player.getName()));

                    plugin.getServer().getLogger().info("[SimplePin] " + SimplePin.getInstance().pinPlayer.get(player.getName()) + " has a new PIN set");
                }

                playerResetInventory(player);
            } else {
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_STRONG, 1.0f, 1.0f);
            }
        }
    }

    public static void playerResetInventory(Player player) {
        if(SimplePin.getInstance().pinPlayer.containsKey(player.getName())) {
            SimplePin.getInstance().pinPlayer.remove(player.getName());

            player.setGameMode(GameMode.valueOf(SimplePin.getInstance().playerMode.get(player.getName())));
            SimplePin.getInstance().playerMode.remove(player.getName());

            SimplePin.getInstance().pinCodes.remove(player.getName());
            player.closeInventory();

            player.getInventory().setContents(SimplePin.getInstance().playerInventories.get(player.getName()));
            SimplePin.getInstance().playerInventories.remove(player.getName());

            player.teleport(SimplePin.getInstance().playerLocations.get(player.getName()));
            SimplePin.getInstance().playerLocations.remove(player.getName());

            SimplePin.getInstance().attemptsLogin.remove(player.getName());

            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        }
    }

    public static void sessionLoggedStatus(JavaPlugin plugin, DatabaseDriver dbDriver, String playerName) {
        Map<String, Object> updateMapFirstPins= new HashMap<>();
        updateMapFirstPins.put("session_logged", 1);
        dbDriver.updateData("pins", updateMapFirstPins, "player_name = ?", playerName);

        int secondReloginTime = GeneralUtils.setDefaultValue(300, "relogin-time", 1, 3600);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                Map<String, Object> updateMapSecondPins= new HashMap<>();
                updateMapSecondPins.put("session_logged", 0);
                dbDriver.updateData("pins", updateMapSecondPins, "player_name = ?", playerName);
            }
        }, secondReloginTime * 20L);
    }

    public static void attemptsAreOver(JavaPlugin plugin, Player player, String language) {
        playerResetInventory(player);
        player.kickPlayer(LocalizationUtils.langCheck(language, "KICK_TITLE"));
        plugin.getServer().getLogger().info("[SimplePin] " + player.getName() + " entered PIN many times and was kicked");
    }
}
