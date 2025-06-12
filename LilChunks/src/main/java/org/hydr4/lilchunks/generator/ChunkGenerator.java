package org.hydr4.lilchunks.generator;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitTask;
import org.hydr4.lilchunks.LilChunks;
import org.hydr4.lilchunks.utils.VersionCompatibility;
import org.hydr4.lilchunks.utils.Logger;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

public class ChunkGenerator {
    private final LilChunks plugin;
    private final World world;
    private final Queue<ChunkPosition> chunkQueue;
    private final AtomicInteger chunksGenerated;
    private final AtomicInteger chunksTotal;
    private final AtomicLong startTime;
    private final GenerationSettings settings;
    private final ReentrantLock lock;
    private boolean isRunning;
    private BukkitTask task;
    private long lastProgressUpdate;
    private int failedChunks;
    private static final int MAX_FAILED_CHUNKS = 100;
    private static final long PROGRESS_UPDATE_INTERVAL = 5000; // 5 seconds
    private final Logger logger;
    private Location center;
    private int radius;

    public ChunkGenerator(LilChunks plugin, World world, GenerationSettings settings) {
        this.plugin = plugin;
        this.world = world;
        this.settings = settings;
        this.chunkQueue = new ConcurrentLinkedQueue<>();
        this.chunksGenerated = new AtomicInteger(0);
        this.chunksTotal = new AtomicInteger(0);
        this.startTime = new AtomicLong(0);
        this.lock = new ReentrantLock();
        this.isRunning = false;
        this.failedChunks = 0;
        this.logger = plugin.getCustomLogger();
    }

    public void start(Location center, int radius) {
        if (!lock.tryLock()) {
            logger.warning("Generation already in progress for world: " + world.getName());
            return;
        }

        try {
            if (isRunning) {
                logger.warning("Generation already running for world: " + world.getName());
                return;
            }

            this.center = center;
            this.radius = radius;
            isRunning = true;
            startTime.set(System.currentTimeMillis());
            lastProgressUpdate = System.currentTimeMillis();
            chunksGenerated.set(0);
            failedChunks = 0;
            
            // Calculate total chunks
            int totalChunks = (radius * 2 + 1) * (radius * 2 + 1);
            chunksTotal.set(totalChunks);

            // Generate chunk positions
            int centerX = center.getBlockX() >> 4;
            int centerZ = center.getBlockZ() >> 4;
            
            // Use spiral pattern for better performance
            generateSpiralChunkPositions(centerX, centerZ, radius);

            // Start generation task
            if (VersionCompatibility.supportsAsyncChunks() && settings.isUseAsync()) {
                startAsyncGeneration();
            } else {
                startSyncGeneration();
            }

            logger.info("Started chunk generation in world: " + world.getName() + 
                " with radius: " + radius + " (Total chunks: " + totalChunks + ")");
        } finally {
            lock.unlock();
        }
    }

    private void generateSpiralChunkPositions(int centerX, int centerZ, int radius) {
        int x = 0;
        int z = 0;
        int dx = 0;
        int dz = -1;
        int size = radius * 2 + 1;
        int maxI = size * size;

        for (int i = 0; i < maxI; i++) {
            if ((-radius <= x) && (x <= radius) && (-radius <= z) && (z <= radius)) {
                chunkQueue.add(new ChunkPosition(centerX + x, centerZ + z));
            }

            if ((x == z) || ((x < 0) && (x == -z)) || ((x > 0) && (x == 1 - z))) {
                int temp = dx;
                dx = -dz;
                dz = temp;
            }
            x += dx;
            z += dz;
        }
    }

    private void startAsyncGeneration() {
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            if (!isRunning || chunkQueue.isEmpty()) {
                stop();
                return;
            }

            long startTick = System.currentTimeMillis();
            int chunksThisTick = 0;

            for (int i = 0; i < settings.getChunksPerTick(); i++) {
                if (chunkQueue.isEmpty()) {
                    break;
                }

                ChunkPosition pos = chunkQueue.poll();
                if (generateChunkAsync(pos)) {
                    chunksThisTick++;
                }
            }

            long tickTime = System.currentTimeMillis() - startTick;
            if (chunksThisTick > 0) {
                logger.performance(String.format(
                    "Generated %d chunks in %dms (%.2f chunks/ms)",
                    chunksThisTick, tickTime, (double) chunksThisTick / tickTime
                ));
            }

            updateProgress();
        }, 1L, 1L);
    }

    private void startSyncGeneration() {
        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (!isRunning || chunkQueue.isEmpty()) {
                stop();
                return;
            }

            long startTick = System.currentTimeMillis();
            int chunksThisTick = 0;

            for (int i = 0; i < settings.getChunksPerTick(); i++) {
                if (chunkQueue.isEmpty()) {
                    break;
                }

                ChunkPosition pos = chunkQueue.poll();
                if (generateChunkSync(pos)) {
                    chunksThisTick++;
                }
            }

            long tickTime = System.currentTimeMillis() - startTick;
            if (chunksThisTick > 0) {
                logger.performance(String.format(
                    "Generated %d chunks in %dms (%.2f chunks/ms)",
                    chunksThisTick, tickTime, (double) chunksThisTick / tickTime
                ));
            }

            updateProgress();
        }, 1L, 1L);
    }

    private boolean generateChunkAsync(ChunkPosition pos) {
        try {
            Bukkit.getScheduler().runTask(plugin, () -> {
                try {
                    Chunk chunk = world.getChunkAt(pos.x, pos.z);
                    if (settings.isGenerateStructures()) {
                        world.regenerateChunk(pos.x, pos.z);
                    }
                    chunksGenerated.incrementAndGet();
                    logger.chunkOperation("Generated", pos.x, pos.z, world.getName());
                } catch (Exception e) {
                    handleChunkGenerationError(pos, e);
                }
            });
            return true;
        } catch (Exception e) {
            handleChunkGenerationError(pos, e);
            return false;
        }
    }

    private boolean generateChunkSync(ChunkPosition pos) {
        try {
            Chunk chunk = world.getChunkAt(pos.x, pos.z);
            if (settings.isGenerateStructures()) {
                world.regenerateChunk(pos.x, pos.z);
            }
            chunksGenerated.incrementAndGet();
            logger.chunkOperation("Generated", pos.x, pos.z, world.getName());
            return true;
        } catch (Exception e) {
            handleChunkGenerationError(pos, e);
            return false;
        }
    }

    private void handleChunkGenerationError(ChunkPosition pos, Exception e) {
        failedChunks++;
        logger.exception(
            String.format("Failed to generate chunk at [%d, %d] in world '%s'", 
                pos.x, pos.z, world.getName()),
            e
        );

        if (failedChunks >= MAX_FAILED_CHUNKS) {
            logger.error("Too many failed chunks (" + failedChunks + 
                "), stopping generation for safety!");
            stop();
        }
    }

    private void updateProgress() {
        if (settings.isLogProgress()) {
            int generated = chunksGenerated.get();
            int total = chunksTotal.get();
            double percentage = (double) generated / total * 100;
            long elapsed = (System.currentTimeMillis() - startTime.get()) / 1000;
            
            logger.generationProgress(world.getName(), generated, total, percentage);
            logger.performance(String.format("Elapsed time: %ds", elapsed));
        }
    }

    public void stop() {
        if (!lock.tryLock()) {
            return;
        }

        try {
            if (!isRunning) {
                return;
            }

            isRunning = false;
            if (task != null) {
                task.cancel();
            }
            
            int generated = chunksGenerated.get();
            int total = chunksTotal.get();
            long elapsed = (System.currentTimeMillis() - startTime.get()) / 1000;
            
            logger.success(String.format(
                "Chunk generation completed! Generated %d/%d chunks in %d seconds (Failed: %d)",
                generated, total, elapsed, failedChunks
            ));
        } finally {
            lock.unlock();
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public int getChunksGenerated() {
        return chunksGenerated.get();
    }

    public int getChunksTotal() {
        return chunksTotal.get();
    }

    public int getFailedChunks() {
        return failedChunks;
    }

    public GenerationStatus getStatus() {
        return new GenerationStatus(
            isRunning(),
            getChunksGenerated(),
            getChunksTotal(),
            System.currentTimeMillis() - startTime.get()
        );
    }

    private static class ChunkPosition {
        final int x;
        final int z;

        ChunkPosition(int x, int z) {
            this.x = x;
            this.z = z;
        }
    }
} 