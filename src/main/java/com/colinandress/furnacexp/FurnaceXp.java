package com.colinandress.furnacexp;

import org.bukkit.plugin.java.JavaPlugin;

public class FurnaceXp extends JavaPlugin {

    @Override
    public void onEnable(){
        // Registering the command for the plugin
        this.getCommand("furnacexp").setExecutor(new com.colinandress.furnacexp.FurnaceXpCommand());
    }

    @Override
    public void onDisable(){
    }
}
