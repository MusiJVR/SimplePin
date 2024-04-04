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
import simplepin.utils.SqliteDriver;

import java.util.*;


public class ResetPINCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private SqliteDriver sql;
    public ResetPINCommand(JavaPlugin plugin, SqliteDriver sql) {
        this.plugin = plugin;
        this.sql = sql;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
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

            List<Map<String, Object>> rsPlayer = sql.sqlSelectData("PlayerName", "PINS", "PlayerName = '" + inputPlayerName + "'");

            if (rsPlayer.isEmpty()) {
                return GeneralUtils.msgSend(sender, language, "WRONG_PLAYER_NAME", true);
            } else {
                Player playerReset = plugin.getServer().getPlayer(inputPlayerName);

                if (playerReset != null) {
                    playerReset.kickPlayer(LocalizationUtils.langCheck(language, "RESET_PIN_TITLE"));
                }

                sql.sqlUpdateData("PINS", "Pin = NULL, SessionLogged = " + 0, "PlayerName = '" + inputPlayerName + "'");

                if (sender instanceof Player) {
                    Player playerSender = (Player) sender;
                    String msgResetPINSuccessfully = LocalizationUtils.langCheck(language, "RESET_PIN_SUCCESSFULLY");
                    playerSender.sendMessage(msgResetPINSuccessfully.replace("%player%", inputPlayerName));
                    playerSender.playSound(playerSender.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                }

                plugin.getServer().getLogger().info("[SimplePin] " + inputPlayerName + "'s PIN was reset");
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return true;
    }
}