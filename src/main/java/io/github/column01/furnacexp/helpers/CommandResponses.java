package io.github.column01.furnacexp.helpers;

import org.bukkit.ChatColor;
import org.bukkit.Location;

public class CommandResponses {
    private static final String PREFIX = ChatColor.GRAY + "["+ ChatColor.GOLD + "Furnace XP" + ChatColor.GRAY + "] "+ ChatColor.RESET;
    public static final String NO_PERMS = PREFIX + ChatColor.RED + "You do not have permission for this command";
    public static final String INVALID_BLOCK = PREFIX + ChatColor.RED + "You need to be looking at a furnace, blast furnace or smoker!";
    public static final String NOT_PLAYER = "[Furnace XP] You must be a player to use this command.";
    public static final String BLOCK_EXISTS = PREFIX + ChatColor.GRAY + "That block is already added";
    public static final String GETTING_XP = PREFIX + ChatColor.GRAY + "Getting the experience from the selected blocks...";
    public static final String EMPTIED_USER_CACHE = PREFIX + ChatColor.GRAY + "Removed all blocks you have cached.";
    public static final String EMPTIED_ALL_CACHE = PREFIX + ChatColor.GRAY + "Cleared global block cache";

    public static String tooManyArguments(String[] args) {
        return String.format(ChatColor.RED + "Invalid or too many arguments: %s", String.join(" ",args));
    }

    public static String calcEmptyCache(String label) {
        return String.format(CommandResponses.PREFIX + ChatColor.GRAY +"You have not added any blocks to your cache. Please use " + ChatColor.GREEN + "/%s add", label);
    }

    public static String addedBlock(Location block) {
        return String.format(CommandResponses.PREFIX + ChatColor.GRAY + "Added block at: " + ChatColor.GREEN + "%s, %s, %s", block.getX(), block.getY(), block.getZ());
    }

    public static String xpStored(int BlockXp) {
        return String.format(CommandResponses.PREFIX + ChatColor.GRAY + "Experience stored in block(s): " + ChatColor.GREEN + "%s", BlockXp);
    }

    public static String playerXp(int PlayerXp) {
        return String.format(CommandResponses.PREFIX + ChatColor.GRAY + "Current experience points: " + ChatColor.GREEN + "%s", PlayerXp);
    }

    public static String levelsAfter(int Levels) {
        return String.format(CommandResponses.PREFIX + ChatColor.GRAY + "Levels after collection (estimated): " + ChatColor.GREEN + "%s", Levels);
    }
}
