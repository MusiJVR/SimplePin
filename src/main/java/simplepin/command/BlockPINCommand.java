package simplepin.command;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplepin.SimplePin;
import simplepin.utils.GeneralUtils;
import simplepin.utils.LocalizationUtils;
import simplepin.utils.DatabaseDriver;

import java.util.HashMap;
import java.util.Map;

public class BlockPINCommand implements CommandExecutor {
    private final DatabaseDriver dbDriver;

    public BlockPINCommand(DatabaseDriver dbDriver) {
        this.dbDriver = dbDriver;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;

        String language = SimplePin.getInstance().getConfig().getString("lang");

        if (args.length != 0) {
            return GeneralUtils.msgSend(sender, language, "INVALID_COMMAND", false);
        }

        Player playerSender = (Player) sender;

        Map<String, Object> updateMapPins= new HashMap<>();
        updateMapPins.put("session_logged", 0);
        dbDriver.updateData("pins", updateMapPins, "uuid = ?", playerSender.getUniqueId());

        playerSender.sendMessage(LocalizationUtils.langCheck(language, "BLOCK_PIN_SUCCESSFULLY"));
        playerSender.playSound(playerSender.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);

        return true;
    }
}
