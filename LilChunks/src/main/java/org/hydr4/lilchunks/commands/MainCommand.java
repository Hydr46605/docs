package org.hydr4.lilchunks.commands;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.hydr4.lilchunks.LilChunks;
import org.hydr4.lilchunks.generator.GenerationSettings;
import org.hydr4.lilchunks.utils.ColorUtil;
import org.hydr4.lilchunks.utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class MainCommand implements CommandExecutor, TabCompleter {
    private final LilChunks plugin;
    private final Logger logger;

    public MainCommand(LilChunks plugin) {
        this.plugin = plugin;
        this.logger = plugin.getCustomLogger();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "start" -> handleStart(sender, args);
            case "stop" -> handleStop(sender, args);
            case "status" -> handleStatus(sender, args);
            case "reload" -> handleReload(sender);
            case "help" -> sendHelp(sender);
            default -> {
                sender.sendMessage(ColorUtil.translate(ColorUtil.RED + "Unknown command. Use /lilchunks help for help."));
                return false;
            }
        }
        return true;
    }

    private void handleStart(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ColorUtil.translate(ColorUtil.RED + "This command can only be used by players."));
            return;
        }

        if (!sender.hasPermission("lilchunks.start")) {
            sender.sendMessage(ColorUtil.translate(ColorUtil.RED + "You don't have permission to use this command."));
            return;
        }

        World world = player.getWorld();
        int radius = args.length > 1 ? parseRadius(args[1]) : 5;

        if (radius <= 0) {
            sender.sendMessage(ColorUtil.translate(ColorUtil.RED + "Invalid radius. Please use a positive number."));
            return;
        }

        if (plugin.getGenerationManager().isGenerating(world)) {
            sender.sendMessage(ColorUtil.translate(ColorUtil.YELLOW + "Chunk generation is already in progress for this world."));
            return;
        }

        GenerationSettings settings = new GenerationSettings(plugin);
        if (plugin.getGenerationManager().startGeneration(world, player.getLocation(), radius)) {
            sender.sendMessage(ColorUtil.translate(ColorUtil.GREEN + "Started chunk generation in a " + radius + " chunk radius."));
        } else {
            sender.sendMessage(ColorUtil.translate(ColorUtil.RED + "Failed to start chunk generation."));
        }
    }

    private void handleStop(CommandSender sender, String[] args) {
        if (!sender.hasPermission("lilchunks.stop")) {
            sender.sendMessage(ColorUtil.translate(ColorUtil.RED + "You don't have permission to use this command."));
            return;
        }

        World world = null;
        if (sender instanceof Player player) {
            world = player.getWorld();
        } else if (args.length > 1) {
            world = plugin.getServer().getWorld(args[1]);
        }

        if (world == null) {
            sender.sendMessage(ColorUtil.translate(ColorUtil.RED + "Please specify a valid world."));
            return;
        }

        if (plugin.getGenerationManager().stopGeneration(world)) {
            sender.sendMessage(ColorUtil.translate(ColorUtil.GREEN + "Stopped chunk generation for world: " + world.getName()));
        } else {
            sender.sendMessage(ColorUtil.translate(ColorUtil.YELLOW + "No active generation found for world: " + world.getName()));
        }
    }

    private void handleStatus(CommandSender sender, String[] args) {
        if (!sender.hasPermission("lilchunks.status")) {
            sender.sendMessage(ColorUtil.translate(ColorUtil.RED + "You don't have permission to use this command."));
            return;
        }

        World world = null;
        if (sender instanceof Player player) {
            world = player.getWorld();
        } else if (args.length > 1) {
            world = plugin.getServer().getWorld(args[1]);
        }

        if (world == null) {
            sender.sendMessage(ColorUtil.translate(ColorUtil.RED + "Please specify a valid world."));
            return;
        }

        var status = plugin.getGenerationManager().getStatus(world);
        if (status == null) {
            sender.sendMessage(ColorUtil.translate(ColorUtil.YELLOW + "No active generation for world: " + world.getName()));
            return;
        }

        sender.sendMessage(ColorUtil.translate(ColorUtil.AQUA + "Generation Status for " + world.getName() + ":"));
        sender.sendMessage(ColorUtil.translate(ColorUtil.WHITE + "» Status: " + (status.isRunning() ? ColorUtil.GREEN + "Running" : ColorUtil.RED + "Stopped")));
        sender.sendMessage(ColorUtil.translate(ColorUtil.WHITE + "» Progress: " + status.getChunksGenerated() + "/" + status.getChunksTotal() + " chunks"));
    }

    private void handleReload(CommandSender sender) {
        if (!sender.hasPermission("lilchunks.reload")) {
            sender.sendMessage(ColorUtil.translate(ColorUtil.RED + "You don't have permission to use this command."));
            return;
        }

        plugin.reloadConfig();
        sender.sendMessage(ColorUtil.translate(ColorUtil.GREEN + "Configuration reloaded successfully."));
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(ColorUtil.translate(ColorUtil.AQUA + "LilChunks Help:"));
        sender.sendMessage(ColorUtil.translate(ColorUtil.WHITE + "» /lilchunks start [radius] - Start chunk generation"));
        sender.sendMessage(ColorUtil.translate(ColorUtil.WHITE + "» /lilchunks stop [world] - Stop chunk generation"));
        sender.sendMessage(ColorUtil.translate(ColorUtil.WHITE + "» /lilchunks status [world] - Check generation status"));
        sender.sendMessage(ColorUtil.translate(ColorUtil.WHITE + "» /lilchunks reload - Reload configuration"));
        sender.sendMessage(ColorUtil.translate(ColorUtil.WHITE + "» /lilchunks help - Show this help message"));
    }

    private int parseRadius(String arg) {
        try {
            return Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("start");
            completions.add("stop");
            completions.add("status");
            completions.add("reload");
            completions.add("help");
        } else if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "stop", "status" -> {
                    for (World world : plugin.getServer().getWorlds()) {
                        completions.add(world.getName());
                    }
                }
                case "start" -> {
                    completions.add("5");
                    completions.add("10");
                    completions.add("20");
                    completions.add("50");
                }
            }
        }

        return completions;
    }
} 