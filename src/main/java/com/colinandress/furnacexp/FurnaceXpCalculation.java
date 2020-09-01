package com.colinandress.furnacexp;

import net.minecraft.server.v1_16_R2.NBTTagCompound;
import net.minecraft.server.v1_16_R2.TileEntity;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.inventory.CookingRecipe;
import org.bukkit.inventory.Recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class FurnaceXpCalculation {
    static double getFurnaceXpArray(ArrayList<TileEntity> furnaces) {
        double totalFurnaceXP = 0.0;
        // Loops over the furnaces to calculate the total furnace xp
        for(TileEntity Furnace: furnaces) {
            // Handle the NBT of the furnace
            NBTTagCompound furnaceNBT = HandleNBT.getNBTOfFurnace(Furnace);
            // Get Recipe names and Amounts from the furnace NBT
            ArrayList<String> RecipeArray = HandleNBT.getRecipeNames(furnaceNBT);
            ArrayList<String> AmountArray = HandleNBT.getRecipeAmounts(furnaceNBT);
            // Get the furnace XP and the player's total experience
            double FurnaceXp = getFurnaceXp(RecipeArray, AmountArray);
            totalFurnaceXP = totalFurnaceXP + FurnaceXp;
        }
        return totalFurnaceXP;
    }

    static double getFurnaceXp(ArrayList<String> recipes, ArrayList<String> amounts) {
        double furnaceXp = 0.0;
        Map<String, String> furnaceRecipes = new HashMap<>();
        assert recipes.size() == amounts.size();
        // For all the items in the two arrays, add them to a map as a Key: Value pair
        for(int i = 0; i < recipes.size(); i++) {
            furnaceRecipes.put(recipes.get(i), amounts.get(i));
        }
        // For every item in the furnaceRecipes, get the key and value of them and calculate the experience in the furnace
        for(Map.Entry<String, String> recipe : furnaceRecipes.entrySet()) {
            double exp = getRecipeExp(recipe.getKey());
            int amount = Integer.parseInt(recipe.getValue());
            furnaceXp = furnaceXp + (exp * amount);
        }
        return furnaceXp;
    }

    private static double getRecipeExp(String key) {
        // Iterates over the bukkit recipes and then returns the experience of said recipe. Called recursively for each recipe in the FurnaceRecipes array
        Iterator<Recipe> recipeIterator = Bukkit.recipeIterator();
        while (recipeIterator.hasNext()) {
            Recipe recipe = recipeIterator.next();
            if (!(recipe instanceof Keyed) || !((Keyed) recipe).getKey().toString().equals(key)) {
                continue;
            }
            if (recipe instanceof CookingRecipe) {
                return ((CookingRecipe) recipe).getExperience();
            }
            break;
        }
        return 0d;
    }

    // Takes the player's XP and the furnaces stored XP, and obtains the new Level for the player upon collection
    static double getNewLevel(double playerXp, double furnaceXp) {
        double newXp = playerXp + furnaceXp;
        return getLvlForXP(newXp);
    }

    // Takes an experience value and converts it to the corresponding level. Comments above each one is to convert levels to XP.
    // This is an estimation due to how Minecraft experience works, but is good enough for our purposes.
    private static double getLvlForXP(double xp) {
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
