package io.github.column01.furnacexp.commands;

import io.github.column01.furnacexp.helpers.ExperienceCalculation;
import io.github.column01.furnacexp.helpers.CommandResponses;
import io.github.column01.furnacexp.helpers.NBTHelpers;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import de.tr7zw.nbtapi.NBTTileEntity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class FurnaceXpCommand implements CommandExecutor {
    public static Multimap<Player, Location> blocks = ArrayListMultimap.create();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdlabel, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            // Get the location, material and make a block position of the furnace forgetting the tile entity
            Location targetBlockLocation = player.getTargetBlock(null, 10).getLocation();
            Material targetBlockType = targetBlockLocation.getBlock().getType();
            // Check if its a compatible block
                if(args.length < 1) {
                    if(isValidBlock(targetBlockType)) {
                        if (player.hasPermission("furnacexp.fxp")) {
                            // Gather the tile entity from the CraftWorld
                            NBTTileEntity furnace = new NBTTileEntity(targetBlockLocation.getBlock().getState());
                            // Get the recipes from the furnace
                            HashMap<String, Integer> recipesUsed = NBTHelpers.getRecipesUsed(furnace);
                            // Calculate the furnace's stored XP, get the player's total experience and calculate their new level
                            int furnaceXp = (int) (ExperienceCalculation.getFurnaceXp(recipesUsed));
                            int playerXP = player.getTotalExperience();
                            int newLevels = (int) (ExperienceCalculation.getNewLevel(playerXP, furnaceXp));
                            // Message the gathered data to the player
                            player.sendMessage(CommandResponses.xpStored(furnaceXp));
                            player.sendMessage(CommandResponses.playerXp(playerXP));
                            player.sendMessage(CommandResponses.levelsAfter(newLevels));
                            return true;
                        }
                    } else {
                        // If its not a compatible block tell the user.
                        player.sendMessage(CommandResponses.INVALID_BLOCK);
                    }
                }
                if(args.length == 1) {
                    // Get all the players in the cache, no duplicates
                    Set<Player> players = blocks.keySet();
                    if(args[0].equalsIgnoreCase("clear")) {
                        if(player.hasPermission("furnacexp.fxp.clear")) {
                            for(Player internalPlayer: players) {
                                if(internalPlayer == player) {
                                    // If the player is in the array, Remove all the blocks the player has cached and break the loop
                                    blocks.removeAll(internalPlayer);
                                    break;
                                }
                            }
                            // Even if we didn't clear their cache (only happens when it's already empty) tell them we did.
                            player.sendMessage(CommandResponses.EMPTIED_USER_CACHE);
                        } else {
                            // Player doesn't have permission
                            player.sendMessage(CommandResponses.NO_PERMS);
                        }
                    }
                    if(args[0].equalsIgnoreCase("clearall")) {
                        if(player.hasPermission("furnacexp.fxp.clearall")) {
                            // Empty all the blocks from the cache
                            blocks.clear();
                            player.sendMessage(CommandResponses.EMPTIED_ALL_CACHE);
                        } else {
                            // Player doesn't have permission
                            player.sendMessage(CommandResponses.NO_PERMS);
                        }
                    }
                    if(args[0].equalsIgnoreCase("calculate") || args[0].equalsIgnoreCase("calc")) {
                        if(player.hasPermission("furnacexp.fxp.calculate")) {
                            // If the player has no blocks in the cache
                            if(blocks.get(player).isEmpty()) {
                                player.sendMessage(CommandResponses.calcEmptyCache(cmdlabel));
                            } else {
                                for(Player internalPlayer: players) {
                                    if(internalPlayer == player) {
                                        // Get all the block locations the player added
                                        ArrayList<NBTTileEntity> furnaces = new ArrayList<>();
                                        for(Location block: blocks.get(player)) {
                                            // Gather the tile entity from the CraftWorld and add it to a furnace array
                                            NBTTileEntity furnace = new NBTTileEntity(block.getBlock().getState());
                                            furnaces.add(furnace);
                                        }
                                        // Calculate the total stored XP in the furnaces, get the player's total experience and calculate their new level
                                        int furnacesXp = (int) (ExperienceCalculation.getFurnaceXpArray(furnaces));
                                        int playerXP = player.getTotalExperience();
                                        int newLevels = (int) (ExperienceCalculation.getNewLevel(playerXP, furnacesXp));
                                        // Message the gathered data to the player
                                        player.sendMessage(CommandResponses.GETTING_XP);
                                        player.sendMessage(CommandResponses.xpStored(furnacesXp));
                                        player.sendMessage(CommandResponses.playerXp(playerXP));
                                        player.sendMessage(CommandResponses.levelsAfter(newLevels));
                                        // Empty the user's block cache
                                        blocks.removeAll(player);
                                    }
                                }
                            }
                        } else {
                            // Player does not have permission
                            player.sendMessage(CommandResponses.NO_PERMS);
                        }
                    }
                    if(args[0].equalsIgnoreCase("add")) {
                        if(isValidBlock(targetBlockType)) {
                            if(player.hasPermission("furnacexp.fxp.add")) {
                                // If they ran the add command, add the player and the block location to the blocks cache with the player who added them.
                                if(!blocks.containsEntry(player, targetBlockLocation)) {
                                    blocks.put(player, targetBlockLocation);
                                    player.sendMessage(CommandResponses.addedBlock(targetBlockLocation));
                                } else {
                                    // Block is already in the queue
                                    player.sendMessage(CommandResponses.BLOCK_EXISTS);
                                }
                            } else {
                                // Player doesn't have permission
                                player.sendMessage(CommandResponses.NO_PERMS);
                            }
                        } else {
                            // Not a valid block
                            player.sendMessage(CommandResponses.INVALID_BLOCK);
                        }
                    }
                } else {
                    // If it has too many arguments
                    player.sendMessage(CommandResponses.tooManyArguments(args));
                }
        } else {
            // If the sender is not a player (I.E. Server console)
            sender.sendMessage(CommandResponses.NOT_PLAYER);
        }
        return true;
    }

    private boolean isValidBlock(Material block) {
        return block == Material.FURNACE || block == Material.BLAST_FURNACE || block == Material.SMOKER;
    }
}
