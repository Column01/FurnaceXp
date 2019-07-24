# FurnaceXp
A simple spigot plugin to get the XP stored in a furnace.

Designed for `1.14` but it is theoretically Compatible with `1.13` with some minor changes to the code. If there are people who would like that, I can make and compile a version for `1.13` :D Just leave an issue on the github or drop me a message on SpigotMC

# Usage
- Drop into plugins folder on server and restart server (or use `reload confirm` if you like to live on the edge).
- Set the command permissions found on [SpigotMC](https://www.spigotmc.org/resources/furnace-xp-1-14.69397/)
- Look at a furnace, blast furnace or smoker
- Run `/furnacexp` or one of the aliases of the command (`/fxp` and `/furnaceexperience`)
- Enjoy :D

# Contributing
1. Download the repository
2. Run [BuildTools](https://www.spigotmc.org/wiki/buildtools/) from Spigot and use the option `--rev 1.14.4` (Required to build. Adds the Spigot stuff to localMaven() so we can get the dependencies)
3. `gradle idea` to initialize the dependencies for the project and finally `gradle build`

Make your changes and submit a pull request. Please comment your code and explain what changes you made and why. If it fixes an issue, please link the issue it fixes.

# Issues
### Find an item that doesn't show XP in the furnace?
Please provide:
- Spigot/PaperMC version
- Item you tried to smelt
- Block data (`/data get block <tab coordinates while looking at furnace>` copy contents or screenshot chat)

### Server crashing or asking if the plugin is outdated?
Please Provide
- Spigot/PaperMC Version
- Crash Report (if server is crashing) or StackTrace (if plugin is not loading)
- List of other plugins installed on the server

### Note
Plugins do sometimes have issues with each other. 99.99% of the time it is not either developers intent to cause this. Please be patient and we will try and sort out any conflicts that arise. With that being said, this is **NOT** and API. Please do not try to use it like one as it can cause unforeseen issues and if you do, I will not help you with issues.

## Author
- Column01