package com.colinandress.furnacexp;

import net.minecraft.server.v1_14_R1.NBTTagCompound;
import net.minecraft.server.v1_14_R1.TileEntity;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.inventory.CookingRecipe;
import org.bukkit.inventory.Recipe;

import java.util.*;

class FurnaceXpCalculation {
    static double GetFurnaceXpArray(ArrayList<TileEntity> furnaces) {
        double totalfurnacexp = 0.0;
        // Loops over the furnaces to calculate the total furnace xp
        for(TileEntity Furnace: furnaces) {
            // Handle the NBT of the furnace
            NBTTagCompound FurnaceNBT = HandleNBT.getNBTOfFurnace(Furnace);
            // Get Recipe names and Amounts from the furnace NBT
            ArrayList<String> RecipeArray = HandleNBT.getRecipeNames(FurnaceNBT);
            ArrayList<String> AmountArray = HandleNBT.getRecipeAmounts(FurnaceNBT);
            // Get the furnace XP and the player's total experience
            double FurnaceXp = GetFurnaceXp(RecipeArray, AmountArray);
            totalfurnacexp = totalfurnacexp + FurnaceXp;
        }
        return totalfurnacexp;
    }

    static double GetFurnaceXp(ArrayList<String> Recipe, ArrayList<String> Amount) {
        double furnacexp = 0.0;
        Map<String, String> FurnaceRecipes = new HashMap<>();
        assert Recipe.size() == Amount.size();
        // For all the items in the two arrays, add them to a map as a Key: Value pair
        for(int i=0; i<Recipe.size(); i++) {
            FurnaceRecipes.put(Recipe.get(i), Amount.get(i));
        }
        // For every item in the FurnaceRecipes, get the key and value of them and calculate the experience in the furnace
        for(Map.Entry<String, String> recipe : FurnaceRecipes.entrySet()) {
            double exp = getRecipeExp(recipe.getKey());
            int amount = Integer.valueOf(recipe.getValue());
            furnacexp = furnacexp + (exp * amount);
        }
        return furnacexp;
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
    static double getNewLevel(double PlayerXp, double FurnaceXp) {
        double newXp = PlayerXp + FurnaceXp;
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
