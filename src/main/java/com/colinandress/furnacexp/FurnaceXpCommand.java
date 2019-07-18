package com.colinandress.furnacexp;

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

public class FurnaceXpCommand implements CommandExecutor {
    // Plugin Prefix
    private static String prefix = ChatColor.GRAY + "["+ ChatColor.GOLD + "Furnace XP" + ChatColor.GRAY + "] "+ ChatColor.RESET;
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player){
            // Get player's world and the location of the block they are looking at
            Player player = (Player) sender;
            if(player.hasPermission("furnacexp.fxp")) {
                CraftWorld cw = (CraftWorld) player.getWorld();
                Location TargetLocation = player.getTargetBlock(null, 10).getLocation();
                // Get the material of the block
                Material TargetBlockType = TargetLocation.getBlock().getType();
                // Check if its a compatible block
                if (TargetBlockType == Material.FURNACE || TargetBlockType == Material.SMOKER || TargetBlockType == Material.BLAST_FURNACE) {
                    // Get the block location and cast it to new BlockPosition
                    double TargetX = TargetLocation.getX();
                    double TargetY = TargetLocation.getY();
                    double TargetZ = TargetLocation.getZ();
                    BlockPosition FurnaceLocation = new BlockPosition(TargetX, TargetY, TargetZ);
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
                else {
                    // If its not a compatible block, error
                    player.sendMessage(prefix + ChatColor.RED + String.format("Looking at: " + "%s", TargetBlockType.getKey()));
                    player.sendMessage(prefix + ChatColor.RED + "You need to be looking at a furnace, blast furnace or smoker!");
                }
            }
        }
        else {
            // If the sender is not a player (I.E. Server console)
            sender.sendMessage("You must be a player to use this command.");
            return false;
        }
        return true;
    }
}
