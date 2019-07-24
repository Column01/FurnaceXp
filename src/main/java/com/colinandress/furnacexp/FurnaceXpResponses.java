package com.colinandress.furnacexp;

import org.bukkit.ChatColor;
import org.bukkit.Location;

class FurnaceXpResponses {
    private static String prefix = ChatColor.GRAY + "["+ ChatColor.GOLD + "Furnace XP" + ChatColor.GRAY + "] "+ ChatColor.RESET;
    static String noperms = prefix + ChatColor.RED + "You do not have permission for this command";
    static String invalidblock = prefix + ChatColor.RED + "You need to be looking at a furnace, blast furnace or smoker!";
    static String notplayer = "[Furnace XP] You must be a player to use this command.";
    static String blockexists = prefix + ChatColor.GRAY + "That block is already added";
    static String gettingxp = prefix + ChatColor.GRAY + "Getting the experience from the selected blocks...";
    static String emptyusercache = prefix + ChatColor.GRAY + "Removed all blocks you have cached.";
    static String emptyallcache = prefix + ChatColor.GRAY + "Cleared Block cache";

    static String tooManyArguments(String[] args) {
        return String.format(ChatColor.RED + "Invalid or too many arguments: %s", String.join(" ",args));
    }

    static String calcEmptyCache(String label) {
        return String.format(FurnaceXpResponses.prefix + ChatColor.GRAY +"You have not added any blocks to your cache. Please use " + ChatColor.GREEN + "/%s add", label);
    }

    static String addedBlock(Location block) {
        return String.format(FurnaceXpResponses.prefix + ChatColor.GRAY + "Added block at: " + ChatColor.GREEN + "%s, %s, %s", block.getX(), block.getY(), block.getZ());
    }

    static String xpStored(int BlockXp) {
        return String.format(FurnaceXpResponses.prefix + ChatColor.GRAY + "Experience stored in block(s): " + ChatColor.GREEN + "%s", BlockXp);
    }

    static String playerXp(int PlayerXp) {
        return String.format(FurnaceXpResponses.prefix + ChatColor.GRAY + "Current experience points: " + ChatColor.GREEN + "%s", PlayerXp);
    }

    static String levelsAfter(int Levels) {
        return String.format(FurnaceXpResponses.prefix + ChatColor.GRAY + "Levels after collection (estimated): " + ChatColor.GREEN + "%s", Levels);
    }
}
