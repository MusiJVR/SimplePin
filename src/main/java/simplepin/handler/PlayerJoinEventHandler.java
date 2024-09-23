package simplepin.handler;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import simplepin.SimplePin;
import simplepin.utils.PinMenuUtils;
import simplepin.utils.DatabaseDriver;

import java.util.*;

public class PlayerJoinEventHandler implements Listener {
    private final DatabaseDriver dbDriver;

    public PlayerJoinEventHandler(DatabaseDriver dbDriver) {
        this.dbDriver = dbDriver;
    }

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        String playerIP = player.getAddress().getAddress().getHostAddress();

        List<Map<String, Object>> rsRegistration = dbDriver.selectData("pin", "pins", "WHERE uuid = ?", uuid);
        if (rsRegistration.isEmpty()) {
            Map<String, Object> insertMapPins = new HashMap<>();
            insertMapPins.put("uuid", uuid);
            insertMapPins.put("player_name", event.getPlayer().getName());
            insertMapPins.put("pin", null);
            insertMapPins.put("player_ip", playerIP);
            insertMapPins.put("session_logged", 0);
            dbDriver.insertData("pins", insertMapPins);
        }

        List<Map<String, Object>> rsSessionLogged = dbDriver.selectData("player_ip, session_logged", "pins", "WHERE uuid = ?", uuid);
        if (playerIP != null && !playerIP.equals(rsSessionLogged.get(0).get("player_ip")) || (int) rsSessionLogged.get(0).get("session_logged") == 0) {
            List<Map<String, Object>> rsPin = dbDriver.selectData("pin", "pins", "WHERE uuid = ?", uuid);
            String pin = (String) rsPin.get(0).get("pin");

            if (pin == null || pin.length() != 4) {
                Map<String, Object> updateMapPins= new HashMap<>();
                updateMapPins.put("pin", null);
                dbDriver.updateData("pins", updateMapPins, "uuid = ?", uuid);
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
    }
}
