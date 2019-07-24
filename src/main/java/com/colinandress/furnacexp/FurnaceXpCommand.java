package com.colinandress.furnacexp;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.server.v1_14_R1.BlockPosition;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import net.minecraft.server.v1_14_R1.TileEntity;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class FurnaceXpCommand implements CommandExecutor {
    // Plugin Prefix
    public static Multimap<Player, Location> blocks = ArrayListMultimap.create();
    private static String prefix = ChatColor.GRAY + "["+ ChatColor.GOLD + "Furnace XP" + ChatColor.GRAY + "] "+ ChatColor.RESET;
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            // Get player's world and the location of the block they are looking at
            Player player = (Player) sender;
            CraftWorld cw = (CraftWorld) player.getWorld();
            // Get location and block material
            Location TargetBlockLocation = player.getTargetBlock(null, 10).getLocation();
            Material TargetBlockType = TargetBlockLocation.getBlock().getType();
            BlockPosition FurnaceLocation = HandleNBT.GetFurnacePosition(TargetBlockLocation);
            // Check if its a compatible block
                if(args.length < 1) {
                    if(isValidBlock(TargetBlockType)) {
                        if (player.hasPermission("furnacexp.fxp")) {
                            // Get the block location and cast it to new BlockPosition
                            // Gather the tile entity from the CraftWorld
                            TileEntity Furnace = cw.getHandle().getTileEntity(FurnaceLocation);
                            if (Furnace != null) {
                                // Handle the NBT of the furnace
                                NBTTagCompound FurnaceNBT = HandleNBT.getNBTOfFurnace(Furnace);
                                // Get Recipe names and Amounts from the furnace NBT
                                ArrayList<String> RecipeArray = HandleNBT.getRecipeNames(FurnaceNBT);
                                ArrayList<String> AmountArray = HandleNBT.getRecipeAmounts(FurnaceNBT);
                                // Get the furnace XP and the player's total experience
                                double FurnaceXp = FurnaceXpCalculation.GetFurnaceXp(RecipeArray, AmountArray);
                                double playerXP = player.getTotalExperience();
                                // Calculate the new level for the player
                                int TotalXp = (int) (FurnaceXpCalculation.getNewLevel(playerXP, FurnaceXp));
                                // Message the gathered data to the player with some fancy formatting
                                player.sendMessage(String.format(prefix + ChatColor.GRAY + "Experience stored in block: " + ChatColor.GREEN + "%.2f", FurnaceXp));
                                player.sendMessage(String.format(prefix + ChatColor.GRAY + "Current experience points: " + ChatColor.GREEN + "%s", ((int) playerXP)));
                                player.sendMessage(String.format(prefix + ChatColor.GRAY + "Levels after collection (estimated): " + ChatColor.GREEN + "%s", TotalXp));
                                return true;
                            }
                        }
                    }
                    else {
                        // If its not a compatible block, error
                        player.sendMessage(prefix + ChatColor.RED + "You need to be looking at a furnace, blast furnace or smoker!");
                    }
                }
                if(args.length == 1) {
                    // Get all the players in the cache, no duplicates
                    Set<Player> players = blocks.keySet();
                    if(args[0].equalsIgnoreCase("clear")) {
                        if(player.hasPermission("furnacexp.fxp.clear")) {
                            for(Player internalPlayer: players) {
                                if(internalPlayer == player) {
                                    // Remove all the blocks the player has cached
                                    blocks.removeAll(internalPlayer);
                                    player.sendMessage(prefix + ChatColor.GRAY +"Removed all blocks you have cached.");
                                }
                            }
                        }
                    }
                    if(args[0].equalsIgnoreCase("clearall")) {
                        if(player.hasPermission("furnacexp.fxp.clearall")) {
                            // Empty all the blocks from the cache
                            blocks.clear();
                            player.sendMessage(prefix + ChatColor.GRAY +"Cleared Block cache");
                        }
                    }
                    if(args[0].equalsIgnoreCase("calculate") || args[0].equalsIgnoreCase("calc")) {
                        if(player.hasPermission("furnacexp.fxp.calculate")) {
                            if(blocks.get(player).isEmpty()) {
                                player.sendMessage(String.format(prefix + ChatColor.GRAY +"You have not added any blocks to your cache. Please use " + ChatColor.GREEN + "/%s add", label));
                            }
                            else {
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
                                        // Calculate and get Various XP amounts and levels
                                        int FurnacesXp = (int) (FurnaceXpCalculation.GetFurnaceXpArray(Furnaces));
                                        int playerXP = player.getTotalExperience();
                                        int TotalXp = (int) (FurnaceXpCalculation.getNewLevel(playerXP, FurnacesXp));
                                        player.sendMessage(prefix + ChatColor.GRAY + "Getting the experience from the selected blocks...");
                                        player.sendMessage(String.format(prefix + ChatColor.GRAY + "Experience stored in blocks: " + ChatColor.GREEN + "%s", FurnacesXp));
                                        player.sendMessage(String.format(prefix + ChatColor.GRAY + "Current experience points: " + ChatColor.GREEN + "%s", ((int) playerXP)));
                                        player.sendMessage(String.format(prefix + ChatColor.GRAY + "Levels after collection (estimated): " + ChatColor.GREEN + "%s", TotalXp));
                                        blocks.removeAll(player);
                                    }
                                }
                            }
                        }
                        else {
                            player.sendMessage(prefix + ChatColor.RED + "You do not have permission for this command");
                        }
                    }
                    if(args[0].equalsIgnoreCase("add")) {
                        if(isValidBlock(TargetBlockType)) {
                            if(player.hasPermission("furnacexp.fxp.add")) {
                                // If they ran the add command, add the player and the block location to the blocks cache with the player who added them.
                                if(!blocks.containsEntry(player, TargetBlockLocation)) {
                                    blocks.put(player, TargetBlockLocation);
                                    player.sendMessage(String.format(prefix + ChatColor.GRAY + "Added block at: " + ChatColor.GREEN + "%s, %s, %s", FurnaceLocation.getX(), FurnaceLocation.getY(), FurnaceLocation.getZ()));
                                }
                                else {
                                    player.sendMessage(prefix + ChatColor.GRAY + "That block is already added");
                                }
                            }
                            else {
                                player.sendMessage(prefix + ChatColor.RED + "You do not have permission for this command");
                            }
                        }
                        else {
                            player.sendMessage(prefix + ChatColor.RED + "You need to be looking at a furnace, blast furnace or smoker!");
                        }
                    }
                }
                else {
                    player.sendMessage(String.format(ChatColor.RED + "Invalid or too many arguments: %s", String.join(" ",args)));
                }
            }
        else {
            // If the sender is not a player (I.E. Server console)
            sender.sendMessage("You must be a player to use this command.");
        }
        return true;
    }

    public boolean isValidBlock(Material block) {
        return block == Material.FURNACE || block == Material.BLAST_FURNACE || block == Material.SMOKER;
    }
}
