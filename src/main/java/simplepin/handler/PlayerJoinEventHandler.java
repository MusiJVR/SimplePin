package simplepin.handler;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import simplepin.SimplePin;
import simplepin.utils.PinMenuUtils;
import simplepin.utils.SqliteDriver;

import java.util.*;


public class PlayerJoinEventHandler implements Listener {
    private SqliteDriver sql;
    public PlayerJoinEventHandler(SqliteDriver sql) {
        this.sql = sql;
    }

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent event) {
        try {
            Player player = event.getPlayer();
            UUID uuid = player.getUniqueId();

            List<Map<String, Object>> rsRegistration = sql.sqlSelectData("Pin", "PINS", "UUID = '" + uuid + "'");
            if (rsRegistration.isEmpty()) {
                Map<String, Object> insertMap = new HashMap<>();
                insertMap.put("UUID", uuid);
                insertMap.put("PlayerName", event.getPlayer().getName());
                insertMap.put("SessionLogged", 0);
                sql.sqlInsertData("PINS", insertMap);
            }

            List<Map<String, Object>> rsSessionLogged = sql.sqlSelectData("SessionLogged", "PINS", "UUID = '" + uuid + "'");
            if ((Integer) rsSessionLogged.get(0).get("SessionLogged") == 0) {
                List<Map<String, Object>> rsPin = sql.sqlSelectData("Pin", "PINS", "UUID = '" + uuid + "'");
                String pin = (String) rsPin.get(0).get("Pin");

                if (pin == null || pin.length() != 4) {
                    sql.sqlUpdateData("PINS", "Pin = NULL", "UUID = '" + uuid + "'");
                }

                if (!SimplePin.getInstance().playerInventories.containsKey(player.getName())) {
                    SimplePin.getInstance().playerInventories.put(player.getName(), player.getInventory().getContents());
                    player.getInventory().clear();
                }

                SimplePin.getInstance().playerMode.put(player.getName(), player.getGameMode().toString());
                player.setGameMode(GameMode.SPECTATOR);

                SimplePin.getInstance().pinCodes.put(player.getName(), "");

                SimplePin.getInstance().pinPlayer.put(player.getName(), player.getName());

                SimplePin.getInstance().playerLocations.put(player.getName(), player.getLocation());

                SimplePin.getInstance().attemptsLogin.put(player.getName(), 1);

                PinMenuUtils.openPinMenu(player);
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
}