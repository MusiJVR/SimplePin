package simplepin.utils;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import simplepin.SimplePin;

import java.lang.reflect.Field;
import java.util.*;

public class PinMenuUtils {
    private static final List<String> HEAD_VALUES = Arrays.asList(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmY2MTI2OTczNWYxZTQ0NmJlY2ZmMjVmOWNiM2M4MjM2Nzk3MTlhMTVmN2YwZmJjOWEwMzkxMWE2OTJiZGQifX19",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2Q4MWEzMmQ5NzhmOTMzZGViN2VhMjZhYTMyNmU0MTc0Njk3NTk1YTQyNmVhYTlmMmFlNWY5YzJlNjYxMjkwIn19fQ==",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2VhZGFkZWQ4MTU2M2YxYzg3NzY5ZDZjMDQ2ODlkY2RiOWU4Y2EwMWRhMzUyODFjZDhmZTI1MTcyOGQyZCJ9fX0=",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmM2MDhjMmRiNTI1ZDZkNzdmN2RlNGI5NjFkNjdlNTNlOWQ3YmFjZGFmZjMxZDRjYTEwZmJiZjkyZDY2In19fQ==",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTE0NGM1MTkzNDM1MTk5YzEzNWJkNDdkMTY2ZWYxYjRlMmQzMjE4MzgzZGY5ZDM0ZTNiYjIwZDlmOGU1OTMifX19",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjYxZjdlMzg1NTY4NTZlYWU1NTY2ZWYxYzQ0YThjYzY0YWY4ZjNhNTgxNjJiMWRkODAxNmE4Nzc4YzcxYyJ9fX0=",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmUxY2YzMWM0OWEyNGE4ZjM3ODQ5ZmMzYzU0NjNhYjY0Y2M5YmNlYjZmMjc2YTVjNDRhZWRkMzRmZGY1MjAifX19",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjFjOWMwOWQ1MmRlYmM0NjVjMzI1NDJjNjhiZTQyYmRhNmY2NzUzZmUxZGViYTI1NzMyN2FjNWEwYzNhZCJ9fX0=",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmRjZjM5ZjRiY2Q5ODQ4NGIwYjQ3OWE3OTkyZDkyNzBmZTNhNTliOWIxYTgwNmQ3YTY0ZmZiNWI1NTFhZCJ9fX0="
    );

    public static void openPinMenu(Player player) {
        String language = SimplePin.getInstance().getConfig().getString("lang");
        Inventory pinMenuGUI = Bukkit.createInventory(player, InventoryType.WORKBENCH, ChatColor.BOLD + LocalizationUtils.langCheck(language, "PIN_MENU"));

        digitPanel(pinMenuGUI);

        ItemStack confirmPin = new ItemStack(Material.NAME_TAG, 1);
        ItemMeta confirmPin_meta = confirmPin.getItemMeta();
        confirmPin_meta.setDisplayName(ChatColor.GREEN + (ChatColor.BOLD + LocalizationUtils.langCheck(language, "CONFIRM_PIN")));
        confirmPin.setItemMeta(confirmPin_meta);
        pinMenuGUI.setItem(0, confirmPin);

        player.openInventory(pinMenuGUI);

        for (int i = 9; i < 36; i++) {
            if (i == 20) {
                ItemStack resetPin = new ItemStack(Material.NAME_TAG, 1);
                ItemMeta resetPin_meta = resetPin.getItemMeta();
                resetPin_meta.setDisplayName(ChatColor.RED + (ChatColor.BOLD + LocalizationUtils.langCheck(language, "RESET_PIN")));
                resetPin.setItemMeta(resetPin_meta);
                player.getInventory().setItem(i, resetPin);
                continue;
            }

            if (i > 21 && i < 26) {
                pinPanel(player, i);
                continue;
            }

            ItemStack voidStack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
            ItemMeta voidStack_meta = voidStack.getItemMeta();
            voidStack_meta.setDisplayName(" ");
            voidStack.setItemMeta(voidStack_meta);
            player.getInventory().setItem(i, voidStack);
        }
    }

    public static void digitPanel(Inventory inventoryMenuGUI) {
        for (int i = 0; i < HEAD_VALUES.size(); i++) {
            String value = HEAD_VALUES.get(i);
            String displayName = ChatColor.RESET + String.valueOf(i + 1);
            ItemStack digit = createHead(value, displayName);
            inventoryMenuGUI.setItem(i + 1, digit);
        }
    }

    public static void pinPanel(Player player, int index) {
        String pinTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmU1Mjg2YzQ3MGY2NmZmYTFhMTgzMzFjYmZmYjlhM2MyYTQ0MjRhOGM3MjU5YzQ0MzZmZDJlMzU1ODJhNTIyIn19fQ==";
        ItemStack digitPin = createHead(pinTexture, " ");
        player.getInventory().setItem(index, digitPin);
    }

    private static ItemStack createHead(String texture, String displayName) {
        ItemStack head = getHeadFromValue(texture);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(displayName);
            head.setItemMeta(meta);
        }
        return head;
    }

    private static ItemStack getHeadFromValue(String value) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) head.getItemMeta();

        try {
            Object gameProfile = createGameProfile(value);

            Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, gameProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        head.setItemMeta(meta);
        return head;
    }

    private static Object createGameProfile(String value) throws Exception {
        Class<?> gameProfileClass = Class.forName("com.mojang.authlib.GameProfile");
        Class<?> propertyClass = Class.forName("com.mojang.authlib.properties.Property");

        Object gameProfile = gameProfileClass.getConstructor(UUID.class, String.class).newInstance(UUID.randomUUID(), "DefaultName");

        Object property = propertyClass.getConstructor(String.class, String.class).newInstance("textures", value);

        Field propertiesField = gameProfileClass.getDeclaredField("properties");
        propertiesField.setAccessible(true);
        Object properties = propertiesField.get(gameProfile);

        properties.getClass().getMethod("put", Object.class, Object.class).invoke(properties, "textures", property);

        return gameProfile;
    }
}
