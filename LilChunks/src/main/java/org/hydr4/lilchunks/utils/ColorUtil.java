package org.hydr4.lilchunks.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class ColorUtil {
    // Color codes
    public static final String BLACK = "&0";
    public static final String DARK_BLUE = "&1";
    public static final String DARK_GREEN = "&2";
    public static final String DARK_AQUA = "&3";
    public static final String DARK_RED = "&4";
    public static final String DARK_PURPLE = "&5";
    public static final String GOLD = "&6";
    public static final String GRAY = "&7";
    public static final String DARK_GRAY = "&8";
    public static final String BLUE = "&9";
    public static final String GREEN = "&a";
    public static final String AQUA = "&b";
    public static final String RED = "&c";
    public static final String LIGHT_PURPLE = "&d";
    public static final String YELLOW = "&e";
    public static final String WHITE = "&f";

    // Format codes
    public static final String OBFUSCATED = "&k";
    public static final String BOLD = "&l";
    public static final String STRIKETHROUGH = "&m";
    public static final String UNDERLINE = "&n";
    public static final String ITALIC = "&o";
    public static final String RESET = "&r";

    // ANSI color codes
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_DARK_RED = "\u001B[31m";
    public static final String ANSI_DARK_GREEN = "\u001B[32m";
    public static final String ANSI_DARK_YELLOW = "\u001B[33m";
    public static final String ANSI_DARK_BLUE = "\u001B[34m";
    public static final String ANSI_DARK_PURPLE = "\u001B[35m";
    public static final String ANSI_DARK_CYAN = "\u001B[36m";
    public static final String ANSI_GRAY = "\u001B[37m";
    public static final String ANSI_DARK_GRAY = "\u001B[90m";
    public static final String ANSI_RED = "\u001B[91m";
    public static final String ANSI_GREEN = "\u001B[92m";
    public static final String ANSI_YELLOW = "\u001B[93m";
    public static final String ANSI_BLUE = "\u001B[94m";
    public static final String ANSI_PURPLE = "\u001B[95m";
    public static final String ANSI_CYAN = "\u001B[96m";
    public static final String ANSI_WHITE = "\u001B[97m";
    public static final String ANSI_BOLD = "\u001B[1m";
    public static final String ANSI_ITALIC = "\u001B[3m";
    public static final String ANSI_UNDERLINE = "\u001B[4m";
    public static final String ANSI_STRIKETHROUGH = "\u001B[9m";

    private static final int BOX_WIDTH = 40;

    /**
     * Translates color codes in a string from & to §
     * @param text The text to translate
     * @return The translated text
     */
    public static String translate(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    /**
     * Translates color codes in a string from & to § and adds a prefix
     * @param prefix The prefix to add
     * @param text The text to translate
     * @return The translated text with prefix
     */
    public static String translate(String prefix, String text) {
        return translate(prefix + text);
    }

    /**
     * Creates a box-style message with the given title and color
     * @param title The title to display
     * @param color The color to use
     * @return Array of strings representing the box
     */
    public static String[] createBox(String title, String color) {
        String[] box = new String[3];
        box[0] = color + "╔" + "═".repeat(BOX_WIDTH - 2) + "╗";
        box[1] = color + "║ " + padCenter(title, BOX_WIDTH - 4) + " ║";
        box[2] = color + "╚" + "═".repeat(BOX_WIDTH - 2) + "╝";
        return box;
    }

    /**
     * Creates a box-style message with multiple lines
     * @param color The color to use
     * @param lines The lines to display
     * @return Array of strings representing the box
     */
    public static String[] createBox(String color, String... lines) {
        String[] box = new String[lines.length + 2];
        box[0] = color + "╔" + "═".repeat(BOX_WIDTH - 2) + "╗";
        
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            // Calculate padding needed
            int visibleLength = getVisibleLength(line);
            int padding = BOX_WIDTH - 4 - visibleLength;
            box[i + 1] = color + "║ " + line + " ".repeat(Math.max(0, padding)) + " ║";
        }
        
        box[box.length - 1] = color + "╚" + "═".repeat(BOX_WIDTH - 2) + "╝";
        return box;
    }

    /**
     * Gets the visible length of a string (ignoring color codes)
     * @param text The text to measure
     * @return The visible length
     */
    private static int getVisibleLength(String text) {
        return text.replaceAll("&[0-9a-fk-or]", "").length();
    }

    /**
     * Converts Minecraft color codes to ANSI color codes
     * @param text The text to convert
     * @return The converted text
     */
    public static String convertToAnsi(String text) {
        return text
            .replace("&0", ANSI_BLACK)
            .replace("&1", ANSI_DARK_BLUE)
            .replace("&2", ANSI_DARK_GREEN)
            .replace("&3", ANSI_DARK_CYAN)
            .replace("&4", ANSI_DARK_RED)
            .replace("&5", ANSI_DARK_PURPLE)
            .replace("&6", ANSI_DARK_YELLOW)
            .replace("&7", ANSI_GRAY)
            .replace("&8", ANSI_DARK_GRAY)
            .replace("&9", ANSI_BLUE)
            .replace("&a", ANSI_GREEN)
            .replace("&b", ANSI_CYAN)
            .replace("&c", ANSI_RED)
            .replace("&d", ANSI_PURPLE)
            .replace("&e", ANSI_YELLOW)
            .replace("&f", ANSI_WHITE)
            .replace("&k", ANSI_RESET) // Obfuscated not supported in ANSI
            .replace("&l", ANSI_BOLD)
            .replace("&m", ANSI_STRIKETHROUGH)
            .replace("&n", ANSI_UNDERLINE)
            .replace("&o", ANSI_ITALIC)
            .replace("&r", ANSI_RESET)
            // Also handle § color codes
            .replace("§0", ANSI_BLACK)
            .replace("§1", ANSI_DARK_BLUE)
            .replace("§2", ANSI_DARK_GREEN)
            .replace("§3", ANSI_DARK_CYAN)
            .replace("§4", ANSI_DARK_RED)
            .replace("§5", ANSI_DARK_PURPLE)
            .replace("§6", ANSI_DARK_YELLOW)
            .replace("§7", ANSI_GRAY)
            .replace("§8", ANSI_DARK_GRAY)
            .replace("§9", ANSI_BLUE)
            .replace("§a", ANSI_GREEN)
            .replace("§b", ANSI_CYAN)
            .replace("§c", ANSI_RED)
            .replace("§d", ANSI_PURPLE)
            .replace("§e", ANSI_YELLOW)
            .replace("§f", ANSI_WHITE)
            .replace("§k", ANSI_RESET) // Obfuscated not supported in ANSI
            .replace("§l", ANSI_BOLD)
            .replace("§m", ANSI_STRIKETHROUGH)
            .replace("§n", ANSI_UNDERLINE)
            .replace("§o", ANSI_ITALIC)
            .replace("§r", ANSI_RESET);
    }

    /**
     * Pads a string to the right with spaces
     * @param text The text to pad
     * @param length The desired length
     * @return The padded string
     */
    private static String padRight(String text, int length) {
        if (text.length() >= length) {
            return text.substring(0, length);
        }
        return text + " ".repeat(length - text.length());
    }

    /**
     * Centers a string with spaces
     * @param text The text to center
     * @param length The desired length
     * @return The centered string
     */
    private static String padCenter(String text, int length) {
        if (text.length() >= length) {
            return text.substring(0, length);
        }
        int padding = length - text.length();
        int leftPadding = padding / 2;
        int rightPadding = padding - leftPadding;
        return " ".repeat(leftPadding) + text + " ".repeat(rightPadding);
    }
} 