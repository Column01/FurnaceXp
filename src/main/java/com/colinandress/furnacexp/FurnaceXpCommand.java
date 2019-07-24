package com.colinandress.furnacexp;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.server.v1_14_R1.BlockPosition;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import net.minecraft.server.v1_14_R1.TileEntity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Set;

public class FurnaceXpCommand implements CommandExecutor {
    // Plugin Prefix
    static Multimap<Player, Location> blocks = ArrayListMultimap.create();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdlabel, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            // Get the craft world, location, material and make a block position of the furnace forgetting the tile entity
            CraftWorld cw = (CraftWorld) player.getWorld();
            Location TargetBlockLocation = player.getTargetBlock(null, 10).getLocation();
            Material TargetBlockType = TargetBlockLocation.getBlock().getType();
            BlockPosition FurnacePosition = HandleNBT.GetFurnacePosition(TargetBlockLocation);
            // Check if its a compatible block
                if(args.length < 1) {
                    if(isValidBlock(TargetBlockType)) {
                        if (player.hasPermission("furnacexp.fxp")) {
                            // Gather the tile entity from the CraftWorld
                            TileEntity Furnace = cw.getHandle().getTileEntity(FurnacePosition);
                            if (Furnace != null) {
                                // Get the NBT from the furnace and then get the recipe names and amounts
                                NBTTagCompound FurnaceNBT = HandleNBT.getNBTOfFurnace(Furnace);
                                ArrayList<String> RecipeArray = HandleNBT.getRecipeNames(FurnaceNBT);
                                ArrayList<String> AmountArray = HandleNBT.getRecipeAmounts(FurnaceNBT);
                                // Calculate the furnace's stored XP, get the player's total experience and calculate their new level
                                int FurnaceXp = (int) (FurnaceXpCalculation.GetFurnaceXp(RecipeArray, AmountArray));
                                int playerXP = player.getTotalExperience();
                                int newLevels = (int) (FurnaceXpCalculation.getNewLevel(playerXP, FurnaceXp));
                                // Message the gathered data to the player
                                player.sendMessage(FurnaceXpResponses.xpStored(FurnaceXp));
                                player.sendMessage(FurnaceXpResponses.playerXp(playerXP));
                                player.sendMessage(FurnaceXpResponses.levelsAfter(newLevels));
                                return true;
                            }
                        }
                    } else {
                        // If its not a compatible block tell the user.
                        player.sendMessage(FurnaceXpResponses.invalidblock);
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
                            player.sendMessage(FurnaceXpResponses.emptyusercache);
                        } else {
                            // Player doesn't have permission
                            player.sendMessage(FurnaceXpResponses.noperms);
                        }
                    }
                    if(args[0].equalsIgnoreCase("clearall")) {
                        if(player.hasPermission("furnacexp.fxp.clearall")) {
                            // Empty all the blocks from the cache
                            blocks.clear();
                            player.sendMessage(FurnaceXpResponses.emptyallcache);
                        } else {
                            // Player doesn't have permission
                            player.sendMessage(FurnaceXpResponses.noperms);
                        }
                    }
                    if(args[0].equalsIgnoreCase("calculate") || args[0].equalsIgnoreCase("calc")) {
                        if(player.hasPermission("furnacexp.fxp.calculate")) {
                            // If the player has no blocks in the cache
                            if(blocks.get(player).isEmpty()) {
                                player.sendMessage(FurnaceXpResponses.calcEmptyCache(cmdlabel));
                            } else {
                                for(Player internalPlayer: players) {
                                    if(internalPlayer == player) {
                                        // Get all the block locations the player added
                                        ArrayList<TileEntity> Furnaces = new ArrayList<>();
                                        for(Location block: blocks.get(player)) {
                                            // Gather the tile entity from the CraftWorld and add it to a furnace array
                                            BlockPosition internalFurnacePosition = HandleNBT.GetFurnacePosition(block);
                                            TileEntity Furnace = cw.getHandle().getTileEntity(internalFurnacePosition);
                                            Furnaces.add(Furnace);
                                        }
                                        // Calculate the total stored XP in the furnaces, get the player's total experience and calculate their new level
                                        int FurnacesXp = (int) (FurnaceXpCalculation.GetFurnaceXpArray(Furnaces));
                                        int playerXP = player.getTotalExperience();
                                        int newLevels = (int) (FurnaceXpCalculation.getNewLevel(playerXP, FurnacesXp));
                                        // Message the gathered data to the player
                                        player.sendMessage(FurnaceXpResponses.gettingxp);
                                        player.sendMessage(FurnaceXpResponses.xpStored(FurnacesXp));
                                        player.sendMessage(FurnaceXpResponses.playerXp(playerXP));
                                        player.sendMessage(FurnaceXpResponses.levelsAfter(newLevels));
                                        // Empty the user's block cache
                                        blocks.removeAll(player);
                                    }
                                }
                            }
                        } else {
                            // Player does not have permission
                            player.sendMessage(FurnaceXpResponses.noperms);
                        }
                    }
                    if(args[0].equalsIgnoreCase("add")) {
                        if(isValidBlock(TargetBlockType)) {
                            if(player.hasPermission("furnacexp.fxp.add")) {
                                // If they ran the add command, add the player and the block location to the blocks cache with the player who added them.
                                if(!blocks.containsEntry(player, TargetBlockLocation)) {
                                    blocks.put(player, TargetBlockLocation);
                                    player.sendMessage(FurnaceXpResponses.addedBlock(TargetBlockLocation));
                                } else {
                                    // Block is already in the queue
                                    player.sendMessage(FurnaceXpResponses.blockexists);
                                }
                            } else {
                                // Player doesn't have permission
                                player.sendMessage(FurnaceXpResponses.noperms);
                            }
                        } else {
                            // Not a valid block
                            player.sendMessage(FurnaceXpResponses.invalidblock);
                        }
                    }
                } else {
                    // If it has too many arguments
                    player.sendMessage(FurnaceXpResponses.tooManyArguments(args));
                }
        } else {
            // If the sender is not a player (I.E. Server console)
            sender.sendMessage(FurnaceXpResponses.notplayer);
        }
        return true;
    }

    private boolean isValidBlock(Material block) {
        return block == Material.FURNACE || block == Material.BLAST_FURNACE || block == Material.SMOKER;
    }
}
