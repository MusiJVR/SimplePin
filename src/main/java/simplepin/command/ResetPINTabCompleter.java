package simplepin.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import simplepin.utils.DatabaseDriver;

import java.util.*;

public class ResetPINTabCompleter implements TabCompleter {
    private final DatabaseDriver dbDriver;

    public ResetPINTabCompleter(DatabaseDriver dbDriver) {
        this.dbDriver = dbDriver;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("simplepin.extendedresetpin")) {
            return new ArrayList<>();
        }

        if (args.length == 1) {
            String inputColor = args[0].toLowerCase();

            ArrayList<String> playersCollection = new ArrayList<>();

            List<Map<String, Object>> rsPlayers = dbDriver.selectData("player_name", "pins", null);
            for (Map<String, Object> i : rsPlayers) {
                playersCollection.add((String) i.get("player_name"));
            }

            List<String> playerNames = null;
            for (String player : playersCollection) {
                if (player.startsWith(inputColor)) {
                    if (playerNames == null) {
                        playerNames = new ArrayList<>();
                    }
                    playerNames.add(player);
                }
            }
            if (playerNames != null) {
                Collections.sort(playerNames);
            }
            return playerNames;
        }
        return new ArrayList<>();
    }
}
