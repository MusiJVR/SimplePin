package simplepin.command;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplepin.SimplePin;
import simplepin.utils.GeneralUtils;
import simplepin.utils.LocalizationUtils;
import simplepin.utils.SqliteDriver;


public class BlockPINCommand implements CommandExecutor {
    private SqliteDriver sql;
    public BlockPINCommand(SqliteDriver sql) {
        this.sql = sql;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;

        try {
            String language = SimplePin.getInstance().getConfig().getString("lang");

            if (args.length != 0) {
                return GeneralUtils.msgSend(sender, language, "INVALID_COMMAND", false);
            }

            Player playerSender = (Player) sender;

            sql.sqlUpdateData("PINS", "SessionLogged = " + 0, "UUID = '" + playerSender.getUniqueId() + "'");

            playerSender.sendMessage(LocalizationUtils.langCheck(language, "BLOCK_PIN_SUCCESSFULLY"));
            playerSender.playSound(playerSender.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return true;
    }
}