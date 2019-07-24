package com.colinandress.furnacexp;

import net.minecraft.server.v1_14_R1.BlockPosition;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import net.minecraft.server.v1_14_R1.TileEntity;
import org.bukkit.Location;

import java.util.ArrayList;

class HandleNBT {

    // Saves the furnace's NBT to a new NBTTagCompound
    static NBTTagCompound getNBTOfFurnace(TileEntity Furnace) {
        NBTTagCompound nbt = new NBTTagCompound();
        return Furnace.save(nbt);
    }

    // Returns an array of strings that contains all items that were smelted by the furnace
    static ArrayList<String> getRecipeNames(NBTTagCompound TileEntity) {
        // Getting how many recipes are in the furnace and initializing two arrays for their values
        short recipesusedsize = TileEntity.getShort("RecipesUsedSize");
        ArrayList<String> RecipeLocationArr = new ArrayList<>();
        for(int i=0; i<recipesusedsize; i++) {
            String LocationInternal = "RecipeLocation" + i;
            String recipelocationN = TileEntity.getString(LocationInternal);
            RecipeLocationArr.add(recipelocationN);
        }
        return RecipeLocationArr;
    }

    // Returns and array of strings that contains all the amounts of smelted items. Lines up with the recipe names for later use.
    static ArrayList<String> getRecipeAmounts(NBTTagCompound TileEntity) {
        short recipesusedsize = TileEntity.getShort("RecipesUsedSize");
        ArrayList<String> RecipeAmountArr = new ArrayList<>();
        for(int i=0; i<recipesusedsize; i++) {
            String AmountInternal = "RecipeAmount" + i;
            String recipeamountN = Integer.toString(TileEntity.getInt(AmountInternal));
            RecipeAmountArr.add(recipeamountN);
        }
        return RecipeAmountArr;
    }

    // Returns a new BlockPosition provided a Location
    static BlockPosition GetFurnacePosition(Location BlockLocation) {
        return new BlockPosition(BlockLocation.getX(), BlockLocation.getY(), BlockLocation.getZ());
    }
}
