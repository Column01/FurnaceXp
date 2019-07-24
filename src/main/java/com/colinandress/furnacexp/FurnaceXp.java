package com.colinandress.furnacexp;

import org.bukkit.plugin.java.JavaPlugin;

public class FurnaceXp extends JavaPlugin {

    @Override
    public void onEnable(){
        // Registering the commands and event listener
        this.getServer().getPluginManager().registerEvents(new FurnaceXpListeners(), this);
        this.getCommand("furnacexp").setExecutor(new FurnaceXpCommand());
    }

    @Override
    public void onDisable() {
        FurnaceXpCommand.blocks.clear();
    }
}
