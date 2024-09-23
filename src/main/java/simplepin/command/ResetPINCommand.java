package simplepin.command;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import simplepin.SimplePin;
import simplepin.utils.GeneralUtils;
import simplepin.utils.LocalizationUtils;
import simplepin.utils.DatabaseDriver;

import java.util.*;

public class ResetPINCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private final DatabaseDriver dbDriver;

    public ResetPINCommand(JavaPlugin plugin, DatabaseDriver dbDriver) {
        this.plugin = plugin;
        this.dbDriver = dbDriver;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String language = SimplePin.getInstance().getConfig().getString("lang");

        if (args.length != 0 && args.length != 1) {
            return GeneralUtils.msgSend(sender, language, "INVALID_COMMAND", false);
        }

        String inputPlayerName;
        if (!(sender instanceof Player) && args.length != 1) {
            return GeneralUtils.msgSend(sender, language, "INVALID_COMMAND", false);
        } else {
            if (sender instanceof Player && args.length == 0) {
                inputPlayerName = sender.getName();
            } else {
                if (sender instanceof Player && !sender.isOp()) {
                    return GeneralUtils.msgSend(sender, language, "NO_RIGHTS_USE_COMMAND", true);
                }

                inputPlayerName = args[0];
            }
        }

        List<Map<String, Object>> rsPlayer = dbDriver.selectData("player_name", "pins", "WHERE player_name = ?", inputPlayerName);

        if (rsPlayer.isEmpty()) {
            return GeneralUtils.msgSend(sender, language, "WRONG_PLAYER_NAME", true);
        } else {
            Player playerReset = plugin.getServer().getPlayer(inputPlayerName);

            if (playerReset != null) {
                playerReset.kickPlayer(LocalizationUtils.langCheck(language, "RESET_PIN_TITLE"));
            }

            Map<String, Object> updateMapPins= new HashMap<>();
            updateMapPins.put("pin", null);
            updateMapPins.put("session_logged", 0);
            dbDriver.updateData("pins", updateMapPins, "player_name = ?", inputPlayerName);

            if (sender instanceof Player) {
                Player playerSender = (Player) sender;
                String msgResetPINSuccessfully = LocalizationUtils.langCheck(language, "RESET_PIN_SUCCESSFULLY");
                playerSender.sendMessage(msgResetPINSuccessfully.replace("%player%", inputPlayerName));
                playerSender.playSound(playerSender.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            }

            plugin.getServer().getLogger().info("[SimplePin] " + inputPlayerName + "'s PIN was reset");
        }

        return true;
    }
}
