package io.github.column01.furnacexp;

import io.github.column01.furnacexp.commands.FurnaceXpCommand;
import io.github.column01.furnacexp.helpers.PlayerQuitListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class FurnaceXp extends JavaPlugin {

    @Override
    public void onEnable() {
        // Registering the commands and event listener
        this.getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        Objects.requireNonNull(this.getCommand("furnacexp")).setExecutor(new FurnaceXpCommand());
    }

    @Override
    public void onDisable() {
        FurnaceXpCommand.blocks.clear();
    }

}
