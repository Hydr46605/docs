package org.hydr4.lilchunks.generator;

import org.bukkit.configuration.ConfigurationSection;
import org.hydr4.lilchunks.LilChunks;

public class GenerationSettings {
    private final int chunksPerTick;
    private final boolean useAsync;
    private final boolean generateStructures;
    private final boolean generateCaves;
    private final boolean generateFeatures;
    private final boolean logProgress;
    private final int progressInterval;
    private final boolean detailedLogs;

    public GenerationSettings(LilChunks plugin) {
        ConfigurationSection config = plugin.getConfig();
        ConfigurationSection genSection = config.getConfigurationSection("chunk-generation");
        ConfigurationSection logSection = config.getConfigurationSection("logging");

        this.chunksPerTick = genSection.getInt("chunks-per-tick", 4);
        this.useAsync = genSection.getBoolean("use-async", true);
        this.generateStructures = genSection.getBoolean("generate-structures", true);
        this.generateCaves = genSection.getBoolean("generate-caves", true);
        this.generateFeatures = genSection.getBoolean("generate-features", true);
        
        this.logProgress = logSection.getBoolean("log-progress", true);
        this.progressInterval = logSection.getInt("progress-interval", 5);
        this.detailedLogs = logSection.getBoolean("detailed-logs", false);
    }

    public int getChunksPerTick() {
        return chunksPerTick;
    }

    public boolean isUseAsync() {
        return useAsync;
    }

    public boolean isGenerateStructures() {
        return generateStructures;
    }

    public boolean isGenerateCaves() {
        return generateCaves;
    }

    public boolean isGenerateFeatures() {
        return generateFeatures;
    }

    public boolean isLogProgress() {
        return logProgress;
    }

    public int getProgressInterval() {
        return progressInterval;
    }

    public boolean isDetailedLogs() {
        return detailedLogs;
    }
} 