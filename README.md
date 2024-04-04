# SimplePin
**SimplePin** is a plugin for Minecraft servers based on Paper, Spigot and Bukkit cores.

**SimplePin** adds a registration system for servers in the form of a GUI interface with PIN codes!

All technical settings and features presented here are for the latest version of the plugin and will not always work on older versions.

## Commands
Here are all the commands that can be used in the plugin:
* `/helppin` - This command allows you to display all possible plugin commands
* `/blockpin` - This command allows you to activate the PIN the next time you log in, regardless of the time of relogin
* `/resetpin <player>` - This command allows you to reset the player's PIN
* `/setpin <player> <PIN>` - This command allows you to set a PIN for the player

## Config
When the server starts, the config file will be automatically created in this path: `plugins/SimplePin/config.yml`

```yml
lang: en_en
login-attempts: '3'
relogin-time: '300'
```

* `lang` - This parameter is responsible for the localization of the plugin (`en_en` or `ru_ru`)
* `login-attempts` - This parameter is intended to indicate the number of attempts to enter PIN codes (the value must not be less than `1` and greater than `100`)
* `relogin-time` - This parameter is intended to specify the number of seconds after which the server will again request a PIN code (the value must not be less than `1` and greater than `3600`)

## Issues
Please leave messages about any errors you find [here](https://github.com/MusiJVR/SimplePin/issues) or on the [Discord](https://discord.gg/xY8WJt7VGr)

## Social Media

- Page on [Modrinth](https://modrinth.com/plugin/simplepin)
- Page on [GitHub](https://github.com/MusiJVR/SimplePin)
- Page on [Discord](https://discord.gg/xY8WJt7VGr)