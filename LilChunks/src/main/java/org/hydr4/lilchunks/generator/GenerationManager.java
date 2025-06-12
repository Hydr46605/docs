package org.hydr4.lilchunks.generator;

import org.bukkit.Location;
import org.bukkit.World;
import org.hydr4.lilchunks.LilChunks;
import org.hydr4.lilchunks.utils.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GenerationManager {
    private final LilChunks plugin;
    private final Logger logger;
    private final Map<String, ChunkGenerator> generators;
    private final GenerationSettings settings;

    public GenerationManager(LilChunks plugin) {
        this.plugin = plugin;
        this.logger = plugin.getCustomLogger();
        this.generators = new ConcurrentHashMap<>();
        this.settings = new GenerationSettings(plugin);
    }

    public boolean startGeneration(World world, Location center, int radius) {
        String worldName = world.getName();
        if (generators.containsKey(worldName)) {
            logger.warning("Generation already in progress for world: " + worldName);
            return false;
        }

        ChunkGenerator generator = new ChunkGenerator(plugin, world, settings);
        generators.put(worldName, generator);
        generator.start(center, radius);
        logger.success("Started chunk generation for world: " + worldName);
        return true;
    }

    public boolean stopGeneration(World world) {
        String worldName = world.getName();
        ChunkGenerator generator = generators.get(worldName);
        if (generator != null) {
            generator.stop();
            generators.remove(worldName);
            logger.success("Stopped chunk generation for world: " + worldName);
            return true;
        } else {
            logger.warning("No active generation found for world: " + worldName);
            return false;
        }
    }

    public void stopAll() {
        for (Map.Entry<String, ChunkGenerator> entry : generators.entrySet()) {
            String worldName = entry.getKey();
            ChunkGenerator generator = entry.getValue();
            generator.stop();
            logger.info("Stopped chunk generation for world: " + worldName);
        }
        generators.clear();
        logger.success("Stopped all chunk generation tasks");
    }

    public boolean isGenerating(World world) {
        ChunkGenerator generator = generators.get(world.getName());
        return generator != null && generator.isRunning();
    }

    public ChunkGenerator getGenerator(World world) {
        return generators.get(world.getName());
    }

    public GenerationStatus getStatus(World world) {
        ChunkGenerator generator = generators.get(world.getName());
        if (generator == null) {
            return null;
        }
        return generator.getStatus();
    }
} 