package com.colinandress.furnacexp;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Set;

public class FurnaceXpListeners implements Listener {
    @EventHandler
    // Removes the player's block cache on disconnect
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Set<Player> players = FurnaceXpCommand.blocks.keySet();
        for(Player internalPlayer: players) {
            if(internalPlayer == player) {
                FurnaceXpCommand.blocks.removeAll(internalPlayer);
            }
        }
    }
}
