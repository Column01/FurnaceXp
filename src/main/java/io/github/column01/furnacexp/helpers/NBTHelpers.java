package io.github.column01.furnacexp.helpers;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTTileEntity;

import java.util.HashMap;

public class NBTHelpers {
    public static HashMap<String, Integer> getRecipesUsed(NBTTileEntity furnace) {
        NBTCompound recipesUsedTag = furnace.getCompound("RecipesUsed");
        HashMap<String, Integer> recipesUsed = new HashMap<>();
        for (String recipe: recipesUsedTag.getKeys()) {
            recipesUsed.put(recipe, recipesUsedTag.getInteger(recipe));
        }
        return recipesUsed;
    }
}
