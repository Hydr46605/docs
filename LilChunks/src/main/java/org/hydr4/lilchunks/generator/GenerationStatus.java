package org.hydr4.lilchunks.generator;

public class GenerationStatus {
    private final boolean running;
    private final int chunksGenerated;
    private final int chunksTotal;
    private final long elapsedTime;

    public GenerationStatus(boolean running, int chunksGenerated, int chunksTotal, long elapsedTime) {
        this.running = running;
        this.chunksGenerated = chunksGenerated;
        this.chunksTotal = chunksTotal;
        this.elapsedTime = elapsedTime;
    }

    public boolean isRunning() {
        return running;
    }

    public int getChunksGenerated() {
        return chunksGenerated;
    }

    public int getChunksTotal() {
        return chunksTotal;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public double getProgress() {
        return chunksTotal > 0 ? (double) chunksGenerated / chunksTotal * 100 : 0;
    }

    public String getFormattedElapsedTime() {
        long seconds = elapsedTime / 1000;
        long minutes = seconds / 60;
        seconds %= 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
} 