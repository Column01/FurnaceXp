package com.colinandress.furnacexp;

import net.minecraft.server.v1_16_R2.BlockPosition;
import net.minecraft.server.v1_16_R2.NBTTagCompound;
import net.minecraft.server.v1_16_R2.TileEntity;
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
        short recipesUsedSize = TileEntity.getShort("RecipesUsedSize");
        ArrayList<String> recipeLocationArr = new ArrayList<>();
        for(int i=0; i<recipesUsedSize; i++) {
            String locationInternal = "RecipeLocation" + i;
            String recipeLocationN = TileEntity.getString(locationInternal);
            recipeLocationArr.add(recipeLocationN);
        }
        return recipeLocationArr;
    }

    // Returns and array of strings that contains all the amounts of smelted items. Lines up with the recipe names for later use.
    static ArrayList<String> getRecipeAmounts(NBTTagCompound TileEntity) {
        short recipesUsedSize = TileEntity.getShort("RecipesUsedSize");
        ArrayList<String> recipeAmountArr = new ArrayList<>();
        for(int i=0; i<recipesUsedSize; i++) {
            String AmountInternal = "RecipeAmount" + i;
            String recipeAmountN = Integer.toString(TileEntity.getInt(AmountInternal));
            recipeAmountArr.add(recipeAmountN);
        }
        return recipeAmountArr;
    }

    // Returns a new BlockPosition provided a Location
    static BlockPosition getFurnacePosition(Location BlockLocation) {
        return new BlockPosition(BlockLocation.getX(), BlockLocation.getY(), BlockLocation.getZ());
    }
}
