package io.github.column01.furnacexp.helpers;

import de.tr7zw.nbtapi.NBTTileEntity;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.inventory.CookingRecipe;
import org.bukkit.inventory.Recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ExperienceCalculation {
    public static double getFurnaceXpArray(ArrayList<NBTTileEntity> furnaces) {
        double totalFurnaceXP = 0d;
        // Loops over the furnaces to calculate the total furnace xp
        for(NBTTileEntity furnace: furnaces) {
            // Get Recipe names and Amounts from the furnace
            HashMap<String, Integer> recipesUsed = NBTHelpers.getRecipesUsed(furnace);
            // Get the furnace XP and the player's total experience
            double furnaceXp = getFurnaceXp(recipesUsed);
            totalFurnaceXP = totalFurnaceXP + furnaceXp;
        }
        return totalFurnaceXP;
    }

    public static double getFurnaceXp(HashMap<String, Integer> recipesUsed) {
        double furnaceXp = 0d;
        for(String recipe: recipesUsed.keySet()) {
            double exp = getRecipeExp(recipe);
            int amount = recipesUsed.get(recipe);
            furnaceXp = furnaceXp + (exp * amount);
        }
        return furnaceXp;
    }

    public static double getRecipeExp(String key) {
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
    public static double getNewLevel(double playerXp, double furnaceXp) {
        double newXp = playerXp + furnaceXp;
        return getLvlForXP(newXp);
    }

    // Takes an experience value and converts it to the corresponding level. Comments above each one is to convert levels to XP.
    // This is an estimation due to how Minecraft experience works, but is good enough for our purposes.
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
