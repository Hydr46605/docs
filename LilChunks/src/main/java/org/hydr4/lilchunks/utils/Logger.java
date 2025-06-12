package org.hydr4.lilchunks.utils;

import org.bukkit.plugin.Plugin;
import org.hydr4.lilchunks.LilChunks;

public class Logger {
    private final Plugin plugin;
    private final boolean debug;
    private final boolean detailed;

    public Logger(Plugin plugin) {
        this.plugin = plugin;
        this.debug = plugin.getConfig().getBoolean("debug", false);
        this.detailed = plugin.getConfig().getBoolean("detailed-logging", false);
    }

    private String getTimestamp() {
        return java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    private String formatMessage(String message) {
        return ColorUtil.convertToAnsi("&7[&b" + getTimestamp() + "&7] &r" + message) + ColorUtil.ANSI_RESET;
    }

    public void info(String message) {
        plugin.getLogger().info(formatMessage(message));
    }

    public void success(String message) {
        plugin.getLogger().info(formatMessage(ColorUtil.GREEN + "✓ " + message));
    }

    public void warning(String message) {
        plugin.getLogger().warning(formatMessage(ColorUtil.YELLOW + "⚠ " + message));
    }

    public void error(String message) {
        plugin.getLogger().severe(formatMessage(ColorUtil.RED + "✗ " + message));
    }

    public void severe(String message) {
        plugin.getLogger().severe(formatMessage(ColorUtil.RED + "✗ " + message));
    }

    public void debug(String message) {
        if (debug) {
            plugin.getLogger().info(formatMessage(ColorUtil.GRAY + "[DEBUG] " + message));
        }
    }

    public void detailed(String message) {
        if (detailed) {
            plugin.getLogger().info(formatMessage(ColorUtil.DARK_GRAY + "[DETAILED] " + message));
        }
    }

    public void performance(String message) {
        if (debug) {
            plugin.getLogger().info(formatMessage(ColorUtil.GOLD + "[PERF] " + message));
        }
    }

    public void security(String message) {
        plugin.getLogger().info(formatMessage(ColorUtil.DARK_RED + "[SECURITY] " + message));
    }

    public void exception(String message, Throwable e) {
        plugin.getLogger().severe(formatMessage(ColorUtil.RED + "✗ " + message));
        if (debug) {
            e.printStackTrace();
        }
    }

    public void chunkOperation(String world, int x, int z, String operation) {
        if (detailed) {
            plugin.getLogger().info(formatMessage(
                ColorUtil.AQUA + "[CHUNK] " + 
                ColorUtil.WHITE + world + " " +
                ColorUtil.GRAY + "(" + x + ", " + z + ") " +
                ColorUtil.WHITE + operation
            ));
        }
    }

    public void generationProgress(String world, int current, int total, double percentage) {
        if (detailed) {
            plugin.getLogger().info(formatMessage(
                ColorUtil.GREEN + "[GEN] " +
                ColorUtil.WHITE + world + " " +
                ColorUtil.GRAY + current + "/" + total + " " +
                ColorUtil.WHITE + String.format("%.1f%%", percentage)
            ));
        }
    }

    public void section(String title) {
        String[] box = ColorUtil.createBox(
            ColorUtil.AQUA,
            ColorUtil.WHITE + "» " + ColorUtil.GREEN + "Status: " + ColorUtil.BOLD + "ENABLED",
            ColorUtil.WHITE + "» " + ColorUtil.AQUA + "Server: " + ColorUtil.BOLD + plugin.getServer().getBukkitVersion(),
            ColorUtil.WHITE + "» " + ColorUtil.YELLOW + "Author: " + ColorUtil.BOLD + "Hydr4"
        );
        for (String line : box) {
            plugin.getLogger().info(ColorUtil.convertToAnsi(line) + ColorUtil.ANSI_RESET);
        }
    }
} 