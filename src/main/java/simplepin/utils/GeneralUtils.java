package simplepin.utils;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplepin.SimplePin;

import java.util.*;


public class GeneralUtils {
    public static boolean checkUtil(Player player, String language, String msg, Boolean value) {
        player.sendMessage(LocalizationUtils.langCheck(language, msg));
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_STRONG, 1.0f, 1.0f);
        return value;
    }

    public static boolean checkDigits(String string) {
        boolean digits = true;
        for(int i = 0; i < string.length() && digits; i++) {
            if(!Character.isDigit(string.charAt(i))) {
                digits = false;
            }
        }
        return digits;
    }

    public static int setDefaultValue(Integer value, String pathConfig, Integer minValue, Integer maxValue) {
        String valueDefaultConfig = SimplePin.getInstance().getConfig().getString(pathConfig);
        if (GeneralUtils.checkDigits(valueDefaultConfig)) {
            Integer valueDefault = new Integer(valueDefaultConfig);

            if (valueDefault >= minValue && valueDefault <= maxValue) {
                value = valueDefault;
            }
        }

        return value;
    }

    public static boolean msgSend(CommandSender sender, String language, String msgTitle, Boolean value) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("[SimplePin] " + LocalizationUtils.langCheck("en_en", msgTitle));
            return value;
        } else {
            return GeneralUtils.checkUtil((Player) sender, language, msgTitle, value);
        }
    }

    public static String getMapKey(Map<String, String> map, String value) {
        Set<Map.Entry<String, String>> entrySet = map.entrySet();

        for (Map.Entry<String, String> pair : entrySet) {
            if (value.equals(pair.getValue())) {
                return pair.getKey();
            }
        }

        return null;
    }
}