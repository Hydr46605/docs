package org.hydr4.lilchunks.utils;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class VersionCompatibility {
    private static final String VERSION = Bukkit.getBukkitVersion();
    private static final int MAJOR_VERSION = getMajorVersion();
    private static final int MINOR_VERSION = getMinorVersion();

    public static boolean isPaper() {
        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static boolean isVersionAtLeast(int major, int minor) {
        return MAJOR_VERSION > major || (MAJOR_VERSION == major && MINOR_VERSION >= minor);
    }

    public static boolean isVersionAtMost(int major, int minor) {
        return MAJOR_VERSION < major || (MAJOR_VERSION == major && MINOR_VERSION <= minor);
    }

    private static int getMajorVersion() {
        String[] parts = VERSION.split("\\.");
        if (parts.length >= 2) {
            return Integer.parseInt(parts[1]);
        }
        return 0;
    }

    private static int getMinorVersion() {
        String[] parts = VERSION.split("\\.");
        if (parts.length >= 3) {
            return Integer.parseInt(parts[2].split("-")[0]);
        }
        return 0;
    }

    public static String getVersionString() {
        return VERSION;
    }

    public static boolean supportsAsyncChunks() {
        return isVersionAtLeast(1, 17);
    }

    public static boolean supportsWorldHeight() {
        return isVersionAtLeast(1, 17);
    }

    public static int getWorldMinHeight(World world) {
        if (supportsWorldHeight()) {
            return world.getMinHeight();
        }
        return 0;
    }

    public static int getWorldMaxHeight(World world) {
        if (supportsWorldHeight()) {
            return world.getMaxHeight();
        }
        return 256;
    }

    public static boolean supportsPlayerHeight() {
        return isVersionAtLeast(1, 20);
    }

    public static double getPlayerHeight(Player player) {
        if (supportsPlayerHeight()) {
            return player.getHeight();
        }
        return 1.8; // Default height for older versions
    }
} 