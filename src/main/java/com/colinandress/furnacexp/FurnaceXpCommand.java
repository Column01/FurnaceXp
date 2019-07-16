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

import java.text.DecimalFormat;
import java.util.ArrayList;

public class FurnaceXpCommand implements CommandExecutor {
    private static String prefix = ChatColor.GRAY + "["+ ChatColor.GOLD + "Furnace XP" + ChatColor.GRAY + "] "+ ChatColor.RESET;
    public static DecimalFormat df = new DecimalFormat("0.00");
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            CraftWorld cw = (CraftWorld)player.getWorld();

            Location TargetLocation = player.getTargetBlock(null, 10).getLocation();
            Material TargetBlockType = TargetLocation.getBlock().getType();
            // TargetBlock is grabbed form the TargetLocation
            if(TargetBlockType == Material.FURNACE || TargetBlockType == Material.SMOKER || TargetBlockType == Material.BLAST_FURNACE){
                // TargetLocation is declared above from a getTargetBlock() from the player who ran the command
                double TargetX = TargetLocation.getX();
                double TargetY = TargetLocation.getY();
                double TargetZ = TargetLocation.getZ();
                BlockPosition FurnaceLocation = new BlockPosition(TargetX, TargetY, TargetZ);
                TileEntity Furnace = cw.getHandle().getTileEntity(FurnaceLocation);
                // Gets the TileEntity NBT
                if(Furnace != null){
                    NBTTagCompound FurnaceNBT = this.getNBTOfFurnace(Furnace);
                    ArrayList<String> RecipeArray = this.getRecipeNames(FurnaceNBT);
                    ArrayList<String> AmountArray = this.getRecipeAmounts(FurnaceNBT);
                    double FurnaceXp = FurnaceExperience.GetFurnaceXp(RecipeArray, AmountArray);
                    double playerXP = player.getTotalExperience();
                    int TotalXp = (int) (getNewLevel(playerXP, FurnaceXp));
                    player.sendMessage(String.format(prefix + ChatColor.GRAY + "Experience Stored in Furnace: " + ChatColor.GREEN + "%.2f", FurnaceXp));
                    player.sendMessage(String.format(prefix + ChatColor.GRAY + "Your current total experience points: " + ChatColor.GREEN + "%s", ((int) playerXP)));
                    player.sendMessage(String.format(prefix + ChatColor.GRAY + "Your levels after collection (estimated): " + ChatColor.GREEN + "%s", TotalXp));
                    return true;
                }
            }
            else {
                player.sendMessage(prefix + ChatColor.RED + String.format("Looking at: %s", TargetBlockType));
                player.sendMessage(prefix + ChatColor.RED + "You need to be looking at a furnace!");
            }
        }
        else{
            sender.sendMessage("You must be a player to use this command!");
            return false;
        }
        return true;
    }

    private NBTTagCompound getNBTOfFurnace(TileEntity Furnace){
        NBTTagCompound nbt = new NBTTagCompound();
        return Furnace.save(nbt);
    }

    private ArrayList<String> getRecipeNames(NBTTagCompound TileEntity){
        // Getting how many recipes are in the furnace and initializing two arrays for their values
        short recipesusedsize = TileEntity.getShort("RecipesUsedSize");
        ArrayList<String> RecipeLocationArr = new ArrayList<>();
        for(int i=0; i<recipesusedsize; i++){
            String LocationInternal = "RecipeLocation" + i;
            String recipelocationN = TileEntity.getString(LocationInternal);
            RecipeLocationArr.add(recipelocationN);
        }
        return RecipeLocationArr;
    }

    private ArrayList<String> getRecipeAmounts(NBTTagCompound TileEntity){
        short recipesusedsize = TileEntity.getShort("RecipesUsedSize");
        ArrayList<String> RecipeAmountArr = new ArrayList<>();
        for(int i=0; i<recipesusedsize; i++){
            String AmountInternal = "RecipeAmount" + i;
            String recipeamountN = Integer.toString(TileEntity.getInt(AmountInternal));
            RecipeAmountArr.add(recipeamountN);
        }
        return RecipeAmountArr;
    }

    public double getNewLevel(double PlayerXp, double FurnaceXp){
        double newXp = PlayerXp + FurnaceXp;
        return getLvlForXP(newXp);
    }

    public static double getLvlForXP(double xp) {
        if (xp <= 393) {
            // xp = level ^ 2 + 6 * level (0-16 levels)
            return Math.sqrt(xp + 9) - 3;
        }else if (xp > 394 && xp <= 1627) {
            // xp = 2.5 * level ^ 2 - 40.5 * level + 360 (17-31 levels)
            return 1.0 / 10.0 * (Math.sqrt(40 * xp - 7839) + 81);
        }else if (xp > 1628) {
            // xp = 4.5 * level ^ 2 - 162.5 * level + 2220 (32+ levels)
            return 1.0 / 18.0 * (Math.sqrt(72 * xp - 54215) + 325);
        }
        return 0;
    }
}
