package org.hydr4.lilchunks;

import org.bukkit.plugin.java.JavaPlugin;
import org.hydr4.lilchunks.commands.MainCommand;
import org.hydr4.lilchunks.generator.GenerationManager;
import org.hydr4.lilchunks.utils.ColorUtil;
import org.hydr4.lilchunks.utils.Logger;
import org.hydr4.lilchunks.utils.VersionCompatibility;

public final class LilChunks extends JavaPlugin {
    private static LilChunks instance;
    private GenerationManager generationManager;
    private Logger logger;
    private boolean debug;

    // Minecraft color codes
    private static final char COLOR_CHAR = '\u00A7'; // § character
    private static final String AQUA = COLOR_CHAR + "b";
    private static final String GREEN = COLOR_CHAR + "a";
    private static final String RED = COLOR_CHAR + "c";
    private static final String BOLD = COLOR_CHAR + "l";

    @Override
    public void onLoad() {
        instance = this;
        logger = new Logger(this);
        
        // Pre-initialization
        String[] preInitBox = ColorUtil.createBox("LilChunks Pre-Init", ColorUtil.AQUA);
        for (String line : preInitBox) {
            logger.info(ColorUtil.convertToAnsi(line) + ColorUtil.ANSI_RESET);
        }
    }

    @Override
    public void onEnable() {
        // Initialize generation manager
        generationManager = new GenerationManager(this);
        
        // Register commands
        getCommand("lilchunks").setExecutor(new MainCommand(this));
        
        // Save default config
        saveDefaultConfig();
        
        // Log startup
        String[] startupBox = ColorUtil.createBox(
            ColorUtil.AQUA,
            "LilChunks v" + getDescription().getVersion(),
            "",
            ColorUtil.WHITE + "» " + ColorUtil.GREEN + "Status: " + ColorUtil.BOLD + "ENABLED",
            ColorUtil.WHITE + "» " + ColorUtil.AQUA + "Server: " + ColorUtil.BOLD + getServer().getBukkitVersion(),
            ColorUtil.WHITE + "» " + ColorUtil.YELLOW + "Author: " + ColorUtil.BOLD + "Hydr4"
        );
        
        for (String line : startupBox) {
            logger.info(ColorUtil.convertToAnsi(line) + ColorUtil.ANSI_RESET);
        }
        
        // Check version compatibility
        if (!checkVersionCompatibility()) {
            logger.severe("This version of Minecraft is not supported! Please use version 1.16.5 or higher.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        // Initialize components
        initializeComponents();
    }

    @Override
    public void onDisable() {
        // Stop all generation tasks
        if (generationManager != null) {
            generationManager.stopAll();
        }
        
        // Log shutdown
        String[] shutdownBox = ColorUtil.createBox(
            ColorUtil.RED,
            "LilChunks v" + getDescription().getVersion(),
            "",
            ColorUtil.WHITE + "» " + ColorUtil.RED + "Status: " + ColorUtil.BOLD + "DISABLED",
            ColorUtil.WHITE + "» " + ColorUtil.GRAY + "All tasks stopped"
        );
        
        for (String line : shutdownBox) {
            logger.info(ColorUtil.convertToAnsi(line) + ColorUtil.ANSI_RESET);
        }
    }

    private boolean checkVersionCompatibility() {
        String version = VersionCompatibility.getVersionString();
        if (version.startsWith("1.16") || version.startsWith("1.17") || 
            version.startsWith("1.18") || version.startsWith("1.19") || 
            version.startsWith("1.20") || version.startsWith("1.21")) {
            return true;
        }
        return false;
    }

    private void initializeComponents() {
        // TODO: Initialize configuration, managers, and other components
        this.debug = getConfig().getBoolean("debug", false);
    }

    public static LilChunks getInstance() {
        return instance;
    }

    public GenerationManager getGenerationManager() {
        return generationManager;
    }

    public Logger getCustomLogger() {
        return logger;
    }

    public boolean isDebug() {
        return debug;
    }
} 