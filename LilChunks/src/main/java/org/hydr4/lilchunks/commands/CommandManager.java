package org.hydr4.lilchunks.commands;

import org.hydr4.lilchunks.LilChunks;

public class CommandManager {
    private final LilChunks plugin;

    public CommandManager(LilChunks plugin) {
        this.plugin = plugin;
        registerCommands();
    }

    private void registerCommands() {
        // Register the main command
        plugin.getCommand("lilchunks").setExecutor(new MainCommand(plugin));
    }
} 