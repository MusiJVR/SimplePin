package simplepin.command;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import simplepin.SimplePin;
import simplepin.utils.GeneralUtils;
import simplepin.utils.LocalizationUtils;
import simplepin.utils.PinMenuUtils;
import simplepin.utils.SqliteDriver;

import java.util.*;


public class SetPINCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private SqliteDriver sql;
    public SetPINCommand(JavaPlugin plugin, SqliteDriver sql) {
        this.plugin = plugin;
        this.sql = sql;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            String language = SimplePin.getInstance().getConfig().getString("lang");

            if (args.length != 1 && args.length != 2) {
                return GeneralUtils.msgSend(sender, language, "INVALID_COMMAND", false);
            }

            if (sender instanceof Player && !sender.isOp()) {
                return GeneralUtils.msgSend(sender, language, "NO_RIGHTS_USE_COMMAND", true);
            }

            String inputPlayerName = args[0];

            if (sender instanceof Player && sender.getName().equals(inputPlayerName)) {
                return GeneralUtils.msgSend(sender, language, "WRONG_PLAYER_NAME", true);
            }

            if (args.length == 2) {
                String newPIN = args[1];

                List<Map<String, Object>> rsPlayer = sql.sqlSelectData("PlayerName", "PINS", "PlayerName = '" + inputPlayerName + "'");

                if (rsPlayer.isEmpty()) {
                    return GeneralUtils.msgSend(sender, language, "WRONG_PLAYER_NAME", true);
                } else {
                    if (newPIN.length() != 4 || !GeneralUtils.checkDigits(newPIN)) {
                        return GeneralUtils.msgSend(sender, language, "WRONG_PIN", true);
                    }

                    sql.sqlUpdateData("PINS", "Pin = '" + newPIN + "', SessionLogged = " + 0, "PlayerName = '" + inputPlayerName + "'");

                    if (sender instanceof Player) {
                        Player playerSender = (Player) sender;
                        String msgResetPINSuccessfully = LocalizationUtils.langCheck(language, "SET_PIN_SUCCESSFULLY");
                        playerSender.sendMessage(msgResetPINSuccessfully.replace("%player%", inputPlayerName));
                        playerSender.playSound(playerSender.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                    }

                    Player playerSetPIN = plugin.getServer().getPlayer(inputPlayerName);
                    if (playerSetPIN != null) {
                        String msgYourPINSet = LocalizationUtils.langCheck(language, "YOUR_PIN_SET");
                        playerSetPIN.sendMessage(msgYourPINSet.replace("%pin%", newPIN));
                        playerSetPIN.playSound(playerSetPIN.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                    }

                    plugin.getServer().getLogger().info("[SimplePin] " + inputPlayerName + " has a new PIN set");
                }
            } else {
                if (!(sender instanceof Player)) {
                    return GeneralUtils.msgSend(sender, language, "INVALID_COMMAND", false);
                }

                Player player = (Player) sender;

                sql.sqlUpdateData("PINS", "Pin = NULL", "PlayerName = '" + inputPlayerName + "'");

                SimplePin.getInstance().playerInventories.put(player.getName(), player.getInventory().getContents());
                player.getInventory().clear();

                SimplePin.getInstance().playerMode.put(player.getName(), player.getGameMode().toString());
                player.setGameMode(GameMode.SPECTATOR);

                SimplePin.getInstance().pinCodes.put(player.getName(), "");

                SimplePin.getInstance().pinPlayer.put(player.getName(), inputPlayerName);

                SimplePin.getInstance().playerLocations.put(player.getName(), player.getLocation());

                SimplePin.getInstance().attemptsLogin.put(player.getName(), 1);

                PinMenuUtils.openPinMenu(player);
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return true;
    }
}