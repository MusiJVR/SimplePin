package simplepin.langs;

import org.bukkit.configuration.file.YamlConfiguration;

public class Langs {
    public enum LangEN {
        SUCCESS_RELOAD_CONFIG("messages.success-reload-config", "§aThe plugin config was successfully reloaded"),
        INVALID_COMMAND("messages.invalid-command", "§cThe command was entered incorrectly "),
        NO_RIGHTS_USE_COMMAND("messages.no-rights-use-command", "§eYou do not have sufficient rights to use this command "),
        WRONG_PLAYER_NAME("messages.wrong-player-name", "§cInvalid player name "),
        WRONG_PIN("messages.wrong-pin", "§cIncorrect PIN value "),
        KICK_TITLE("messages.kick-title", "§c§lYou have entered PIN too many times "),
        RESET_PIN_TITLE("messages.reset-pin-title", "§c§lYour PIN has been reset "),
        RESET_PIN_SUCCESSFULLY("messages.reset-pin-successfully", "§bYou have successfully reset§r §e§l%player%'s§r §bPIN§r "),
        SET_PIN_SUCCESSFULLY("messages.set-pin-successfully", "§bYou have successfully set§r §e§l%player%'s§r §bnew PIN§r "),
        YOUR_PIN_SET("messages.your-pin-set", "§bYour PIN code has been set to§r §e§l%pin%§r "),
        BLOCK_PIN_SUCCESSFULLY("messages.block-pin-successfully", "§bThe next time you log into the server you will be asked for a PIN§r "),

        PIN_MENU("menu-gui.pin-menu.inventory-name", "Enter PIN "),
        CONFIRM_PIN("menu-gui.pin-menu.confirm-pin", "Confirm PIN "),
        RESET_PIN("menu-gui.pin-menu.reset-pin", "Reset PIN ");

        private String path;
        private String msg;
        private static YamlConfiguration LANG;

        LangEN(String path, String msg) {
            this.path = path;
            this.msg = msg;
        }

        public static void setFile(YamlConfiguration config) {
            LANG = config;
        }

        @Override
        public String toString() {
            return LANG.getString(path, msg);
        }

        public String getMsg() {
            return this.msg;
        }
        public String getPath() {
            return this.path;
        }
    }

    public enum LangRU {
        SUCCESS_RELOAD_CONFIG("messages.success-reload-config", "§aКонфигурация плагина была успешно перезагружена"),
        INVALID_COMMAND("messages.invalid-command", "§cНекорректная команда "),
        NO_RIGHTS_USE_COMMAND("messages.no-rights-use-command", "§eУ вас недостаточно прав на использование данной команды "),
        WRONG_PLAYER_NAME("messages.wrong-player-name", "§cНекорректное имя игрока "),
        WRONG_PIN("messages.wrong-pin", "§cНекорректное значение PIN "),
        KICK_TITLE("messages.kick-title", "§c§lВы слишком много раз вводили PIN "),
        RESET_PIN_TITLE("messages.reset-pin-title", "§c§lВаш PIN был сброшен "),
        RESET_PIN_SUCCESSFULLY("messages.reset-pin-successfully", "§bВы успешно сбросили PIN§r §e§l%player%§r "),
        SET_PIN_SUCCESSFULLY("messages.set-pin-successfully", "§bВы успешно установили новый PIN§r §e§l%player%§r "),
        YOUR_PIN_SET("messages.your-pin-set", "§bВам установили PIN§r §e§l%pin%§r "),
        BLOCK_PIN_SUCCESSFULLY("messages.block-pin-successfully", "§bПри следующем входе на сервер у вас потребуют PIN§r "),

        PIN_MENU("menu-gui.pin-menu.inventory-name", "Введите PIN "),
        CONFIRM_PIN("menu-gui.pin-menu.confirm-pin", "Подтвердить PIN "),
        RESET_PIN("menu-gui.pin-menu.reset-pin", "Сбросить PIN ");

        private String path;
        private String msg;
        private static YamlConfiguration LANG;

        LangRU(String path, String msg) {
            this.path = path;
            this.msg = msg;
        }

        public static void setFile(YamlConfiguration config) {
            LANG = config;
        }

        @Override
        public String toString() {
            return LANG.getString(path, msg);
        }

        public String getMsg() {
            return this.msg;
        }
        public String getPath() {
            return this.path;
        }
    }
}
