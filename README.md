# FurnaceXp

A simple spigot plugin to get the XP stored in a furnace.

## Version Info

- `3.x.x` for MC 1.16
- `1.x.x` for MC 1.14

## Usage

- Download the release for your version (info above) [on SpigotMC](https://www.spigotmc.org/resources/furnace-xp.69397/history)
- Download the NBT-API dependency from [here](https://www.spigotmc.org/resources/nbt-api.7939/)
- Drop both jars into your plugins folder on the server and restart server.
- Set the command permissions found on [SpigotMC](https://www.spigotmc.org/resources/furnace-xp-1-14.69397/)
- Look at a furnace, blast furnace or smoker
- Run `/furnacexp` or one of the aliases of the command (`/fxp` and `/furnaceexperience`)
- Enjoy :D

## Contributing

1. Download the repository
2. Run [BuildTools](https://www.spigotmc.org/wiki/buildtools/) from Spigot and use the option `--rev 1.16.2` (Required to build. Adds the Spigot stuff to localMaven() so we can get the dependencies)
3. `gradle idea` to initialize the dependencies for the project and finally `gradle build`

Make your changes and submit a pull request. Please comment your code and explain what changes you made and why. If it fixes an issue, please link the issue it fixes.

## Issues

### Server crashing or asking if the plugin is outdated?

Please Provide
- Spigot/PaperMC Version
- Crash Report (if server is crashing) or StackTrace (if plugin is not loading)
- List of other plugins installed on the server

## Author

- Column01