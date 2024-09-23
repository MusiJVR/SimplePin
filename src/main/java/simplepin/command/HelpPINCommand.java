package simplepin.command;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import simplepin.SimplePin;
import simplepin.utils.GeneralUtils;

public class HelpPINCommand implements CommandExecutor {
    private final JavaPlugin plugin;

    public HelpPINCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            if (args.length != 0) {
                return true;
            }

            plugin.getServer().getLogger().info("\n[SimplePin] ⏴-------SimplePin-------⏵ \n" +
                    "[SimplePin] Command: helppin \n" +
                    "[SimplePin] Description: This command allows you to display all possible plugin commands \n" +
                    "[SimplePin] Usage: /<command> \n[SimplePin] \n" +
                    "[SimplePin] Command: blockpin \n" +
                    "[SimplePin] Description: This command allows you to activate the PIN the next time you log in, regardless of the time of relogin \n" +
                    "[SimplePin] Usage: /<command> \n[SimplePin] \n" +
                    "[SimplePin] Command: resetpin \n" +
                    "[SimplePin] Description: This command allows you to reset the player's PIN \n" +
                    "[SimplePin] Usage: /<command> <player> \n[SimplePin] \n" +
                    "[SimplePin] Command: setpin \n" +
                    "[SimplePin] Description: This command allows you to set a PIN for the player \n" +
                    "[SimplePin] Usage: /<command> <player> <PIN> \n" +
                    "[SimplePin] ⏴-----------------------⏵");
        } else {
            String language = SimplePin.getInstance().getConfig().getString("lang");
            Player playerSender = (Player) sender;

            if (args.length != 0) {
                return GeneralUtils.checkUtil(playerSender, language, "INVALID_COMMAND", false);
            }

            if (language.equals("en_en")) {
                sender.sendMessage("§b§l⏴-------§r§e§lSimplePin§r§b§l-------⏵§r \n" +
                        "§bCommand:§r §ehelppin§r \n" +
                        "§bDescription:§r §eThis command allows you to display all possible plugin commands§r \n" +
                        "§bUsage:§r §e/<command>§r \n \n" +
                        "§bCommand:§r §eblockpin§r \n" +
                        "§bDescription:§r §eThis command allows you to activate the PIN the next time you log in, regardless of the time of relogin§r \n" +
                        "§bUsage:§r §e/<command>§r \n \n" +
                        "§bCommand:§r §eresetpin§r \n" +
                        "§bDescription:§r §eThis command allows you to reset the player's PIN§r \n" +
                        "§bUsage:§r §e/<command> <player>§r \n \n" +
                        "§bCommand:§r §esetpin§r \n" +
                        "§bDescription:§r §eThis command allows you to set a PIN for the player§r \n" +
                        "§bUsage:§r §e/<command> <player> <PIN>§r \n" +
                        "§b§l⏴-----------------------⏵§r");
            } else if (language.equals("ru_ru")) {
                sender.sendMessage("§b§l⏴-------§r§e§lSimplePin§r§b§l-------⏵§r \n" +
                        "§bКоманда:§r §ehelppin§r \n" +
                        "§bОписание:§r §eЭта команда позволяет отображать все возможные команды плагина§r \n" +
                        "§bИспользование:§r §e/<command>§r \n \n" +
                        "§bКоманда:§r §eblockpin§r \n" +
                        "§bОписание:§r §eЭта команда позволяет активировать PIN при следующем входе в систему, независимо от времени повторного входа§r \n" +
                        "§bИспользование:§r §e/<command>§r \n \n" +
                        "§bКоманда:§r §eresetpin§r \n" +
                        "§bОписание:§r §eЭта команда позволяет сбросить PIN игрока§r \n" +
                        "§bИспользование:§r §e/<command> <player>§r \n \n" +
                        "§bКоманда:§r §esetpin§r \n" +
                        "§bОписание:§r §eЭта команда позволяет установить PIN игроку§r \n" +
                        "§bИспользование:§r §e/<command> <player> <PIN>§r \n" +
                        "§b§l⏴-----------------------⏵§r");
            } else {
                sender.sendMessage("§b§l⏴-------§r§e§lSimplePin§r§b§l-------⏵§r \n" +
                        "§bCommand:§r §ehelppin§r \n" +
                        "§bDescription:§r §eThis command allows you to display all possible plugin commands§r \n" +
                        "§bUsage:§r §e/<command>§r \n \n" +
                        "§bCommand:§r §eblockpin§r \n" +
                        "§bDescription:§r §eThis command allows you to activate the PIN the next time you log in, regardless of the time of relogin§r \n" +
                        "§bUsage:§r §e/<command>§r \n \n" +
                        "§bCommand:§r §eresetpin§r \n" +
                        "§bDescription:§r §eThis command allows you to reset the player's PIN§r \n" +
                        "§bUsage:§r §e/<command> <player>§r \n \n" +
                        "§bCommand:§r §esetpin§r \n" +
                        "§bDescription:§r §eThis command allows you to set a PIN for the player§r \n" +
                        "§bUsage:§r §e/<command> <player> <PIN>§r \n" +
                        "§b§l⏴-----------------------⏵§r");
            }
            playerSender.playSound(playerSender.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        }
        return true;
    }
}
