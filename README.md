# FurnaceXp
A simple spigot plugin to get the XP stored in a furnace.

Designed for `1.14` but it is theoritcally Compatible with `1.13` but not tested (not sure what changes they made to recipes when they added the new blocks. Some might break but would be easy to fix)

# Usage
- Drop into plugins folder on server and restart server (or use `reload confirm` if you like to live on the edge).
- Look at a furnace, blast furnace or smoker
- Run `/furnacexp` or one of the aliases of the command (`/fxp` and `/furnaceexperience`)
- Enjoy :D

# Contributing
1. Download the repository
2. Run [BuildTools](https://www.spigotmc.org/wiki/buildtools/) from Spigot and pick `--rev 1.14.3` (Required to build. Adds the Spigot stuff to localMaven() so we can build the dependencies)
3. `gradle build`

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
Plugins do sometimes have issues with each other. 99.99% of the time it is not either developers intent to cause this. Please be patient and we will try and sort out any conflicts that arise. With that being said, this is **NOT** and API. Please do not try to use it like one as it can cause unforseen issues and if you do, I will not help you with issues.

## Author
- Column01