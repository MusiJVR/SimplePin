package simplepin.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import simplepin.utils.SqliteDriver;

import java.util.*;


public class SetPINTabCompleter implements TabCompleter {
    private SqliteDriver sql;
    public SetPINTabCompleter(SqliteDriver sql) {
        this.sql = sql;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            return new ArrayList<>();
        }

        if (args.length == 1) {
            try {
                String inputColor = args[0].toLowerCase();

                ArrayList<String> playersCollection = new ArrayList<>();

                List<Map<String, Object>> rsPlayers = sql.sqlSelectData("PlayerName", "PINS");
                for (Map<String, Object> i : rsPlayers) {
                    playersCollection.add((String) i.get("PlayerName"));
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
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        } else if (args.length == 2) {
            return Arrays.asList("<PIN>");
        }
        return new ArrayList<>();
    }
}