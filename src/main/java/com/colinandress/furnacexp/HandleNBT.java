package com.colinandress.furnacexp;

import net.minecraft.server.v1_16_R2.BlockPosition;
import net.minecraft.server.v1_16_R2.NBTTagCompound;
import net.minecraft.server.v1_16_R2.TileEntity;
import org.bukkit.Location;

import java.util.HashMap;

class HandleNBT {

    // Saves the furnace's NBT to a new NBTTagCompound
    static NBTTagCompound getNBTOfFurnace(TileEntity furnace) {
        NBTTagCompound nbt = new NBTTagCompound();
        return furnace.save(nbt);
    }

    static HashMap<String, Integer> getRecipesUsed(NBTTagCompound furnace) {
        NBTTagCompound recipesUsedTag = furnace.getCompound("RecipesUsed");
        HashMap<String, Integer> recipesUsed = new HashMap<>();
        for (String recipe: recipesUsedTag.getKeys()) {
            recipesUsed.put(recipe, recipesUsedTag.getInt(recipe));
        }
        return recipesUsed;
    }

    // Returns a new BlockPosition provided a Location
    static BlockPosition getFurnacePosition(Location BlockLocation) {
        return new BlockPosition(BlockLocation.getX(), BlockLocation.getY(), BlockLocation.getZ());
    }
}
