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
import simplepin.utils.DatabaseDriver;

import java.util.*;

public class SetPINCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private final DatabaseDriver dbDriver;

    public SetPINCommand(JavaPlugin plugin, DatabaseDriver dbDriver) {
        this.plugin = plugin;
        this.dbDriver = dbDriver;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String language = SimplePin.getInstance().getConfig().getString("lang");

        if (args.length != 1 && args.length != 2) {
            return GeneralUtils.msgSend(sender, language, "INVALID_COMMAND", false);
        }

        if (sender instanceof Player && !sender.hasPermission("simplepin.setpin")) {
            return GeneralUtils.msgSend(sender, language, "NO_RIGHTS_USE_COMMAND", true);
        }

        String inputPlayerName = args[0];

        List<Map<String, Object>> rsPlayer = dbDriver.selectData("player_name", "pins", "WHERE player_name = ?", inputPlayerName);

        if (sender instanceof Player && sender.getName().equals(inputPlayerName) || rsPlayer.isEmpty()) {
            return GeneralUtils.msgSend(sender, language, "WRONG_PLAYER_NAME", true);
        }

        if (args.length == 2) {
            String newPIN = args[1];

            if (newPIN.length() != 4 || !GeneralUtils.checkDigits(newPIN)) {
                return GeneralUtils.msgSend(sender, language, "WRONG_PIN", true);
            }

            Map<String, Object> updateMapFirstPins= new HashMap<>();
            updateMapFirstPins.put("pin", newPIN);
            updateMapFirstPins.put("session_logged", 0);
            dbDriver.updateData("pins", updateMapFirstPins, "player_name = ?", inputPlayerName);

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
        } else {
            if (!(sender instanceof Player)) {
                return GeneralUtils.msgSend(sender, language, "INVALID_COMMAND", false);
            }

            Player player = (Player) sender;

            Map<String, Object> updateMapSecondPins= new HashMap<>();
            updateMapSecondPins.put("pin", null);
            dbDriver.updateData("pins", updateMapSecondPins, "player_name = ?", inputPlayerName);

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

        return true;
    }
}
