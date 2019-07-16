package com.colinandress.furnacexp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FurnaceExperience {
    public static double GetFurnaceXp(ArrayList<String> Recipe, ArrayList<String> Amount){
        double totalxp = 0.0;
        Map<String, String> FurnaceRecipes = new HashMap<>();
        assert Recipe.size() == Amount.size();
        for(int i=0; i<Recipe.size(); i++){
            FurnaceRecipes.put(Recipe.get(i), Amount.get(i));
        }
        Map<String, Double> xpMap = GetRecipesExperienceIndex();
        for(Map.Entry<String, String> recipe : FurnaceRecipes.entrySet()){
            for (Map.Entry<String, Double> xpMultiplier : xpMap.entrySet()) {
                if(recipe.getKey().equals(xpMultiplier.getKey())){
                    totalxp = totalxp + Double.parseDouble(recipe.getValue()) * xpMultiplier.getValue();
                }
            }
        }
        return totalxp;
    }

    public static Map<String, Double> GetRecipesExperienceIndex(){
        Map<String, Double> RecipesExperienceMap = new HashMap<>();
        // Don't judge me. I wanted to save time and effort plus I is stupid.
        // If you ever decide to fix this, you have a stronger will than I do.
        // If you are stuck with this after a while, consider a bullet in the head.
        // Items that give 0.1 experience
        RecipesExperienceMap.put("minecraft:dried_kelp_from_smelting", 0.1);
        RecipesExperienceMap.put("minecraft:dried_kelp_from_smoking", 0.1);
        RecipesExperienceMap.put("minecraft:popped_chorus_fruit", 0.1);
        RecipesExperienceMap.put("minecraft:gold_nugget_from_smelting", 0.1);
        RecipesExperienceMap.put("minecraft:iron_nugget_from_smelting", 0.1);
        RecipesExperienceMap.put("minecraft:coal_from_smelting", 0.1);
        RecipesExperienceMap.put("minecraft:coal_from_blasting", 0.1);
        RecipesExperienceMap.put("minecraft:netherbrick", 0.1);
        RecipesExperienceMap.put("minecraft:smooth_quartz", 0.1);
        RecipesExperienceMap.put("minecraft:smooth_stone", 0.1);
        RecipesExperienceMap.put("minecraft:stone", 0.1);
        RecipesExperienceMap.put("minecraft:glass", 0.1);

        // Items that give 0.15 experience
        RecipesExperienceMap.put("minecraft:charcoal", 0.15);
        RecipesExperienceMap.put("minecraft:sponge", 0.15);

        // Items that give 0.2 experience
        RecipesExperienceMap.put("minecraft:lime_dye_from_smelting", 0.2);
        RecipesExperienceMap.put("minecraft:green_dye", 0.2);
        RecipesExperienceMap.put("minecraft:quartz", 0.2);
        RecipesExperienceMap.put("minecraft:quartz_from_blasting", 0.2);
        RecipesExperienceMap.put("minecraft:lapis_from_smelting", 0.2);
        RecipesExperienceMap.put("minecraft:lapis_from_blasting", 0.2);

        // Items that give 0.35 experience
        RecipesExperienceMap.put("minecraft:cooked_porkchop", 0.35);
        RecipesExperienceMap.put("minecraft:cooked_porkchop_from_smoking", 0.35);
        RecipesExperienceMap.put("minecraft:cooked_beef", 0.35);
        RecipesExperienceMap.put("minecraft:cooked_beef_from_smoking", 0.35);
        RecipesExperienceMap.put("minecraft:cooked_chicken", 0.35);
        RecipesExperienceMap.put("minecraft:cooked_chicken_from_smoking", 0.35);
        RecipesExperienceMap.put("minecraft:cooked_cod", 0.35);
        RecipesExperienceMap.put("minecraft:cooked_cod_from_smoking", 0.35);
        RecipesExperienceMap.put("minecraft:cooked_salmon", 0.35);
        RecipesExperienceMap.put("minecraft:cooked_salmon_from_smoking", 0.35);
        RecipesExperienceMap.put("minecraft:cooked_mutton", 0.35);
        RecipesExperienceMap.put("minecraft:cooked_mutton_from_smoking", 0.35);
        RecipesExperienceMap.put("minecraft:cooked_rabbit", 0.35);
        RecipesExperienceMap.put("minecraft:cooked_rabbit_from_smoking", 0.35);
        RecipesExperienceMap.put("minecraft:baked_potato", 0.35);
        RecipesExperienceMap.put("minecraft:baked_potato_from_smoking", 0.35);
        RecipesExperienceMap.put("minecraft:terracotta", 0.35);

        // Items that give 0.7 experience
        RecipesExperienceMap.put("minecraft:redstone_from_smelting", 0.7);
        RecipesExperienceMap.put("minecraft:redstone_from_blasting", 0.7);
        RecipesExperienceMap.put("minecraft:iron_ingot", 0.7);
        RecipesExperienceMap.put("minecraft:iron_ingot_from_blasting", 0.7);

        // Items that give 1.0 experience
        RecipesExperienceMap.put("minecraft:diamond_from_smelting", 1.0);
        RecipesExperienceMap.put("minecraft:diamond_from_blasting", 1.0);
        RecipesExperienceMap.put("minecraft:emerald_from_smelting", 1.0);
        RecipesExperienceMap.put("minecraft:emerald_from_blasting", 1.0);
        RecipesExperienceMap.put("minecraft:gold_ingot", 1.0);
        RecipesExperienceMap.put("minecraft:gold_ingot_from_blasting", 1.0);

        return RecipesExperienceMap;
    }
}
