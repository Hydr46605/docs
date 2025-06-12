package org.hydr4.lilchunks.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.hydr4.lilchunks.LilChunks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class CommandBase implements CommandExecutor, TabCompleter {
    protected final LilChunks plugin;
    protected final String permission;
    protected final String usage;

    public CommandBase(LilChunks plugin, String permission, String usage) {
        this.plugin = plugin;
        this.permission = permission;
        this.usage = usage;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(Component.text("You don't have permission to use this command!", NamedTextColor.RED));
            return true;
        }

        try {
            execute(sender, args);
        } catch (Exception e) {
            sender.sendMessage(Component.text("An error occurred while executing the command: " + e.getMessage(), NamedTextColor.RED));
            plugin.getCustomLogger().error("Error executing command: " + e.getMessage());
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (!sender.hasPermission(permission)) {
            return new ArrayList<>();
        }

        try {
            return tabComplete(sender, args);
        } catch (Exception e) {
            plugin.getCustomLogger().error("Error in tab completion: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    protected abstract void execute(CommandSender sender, String[] args);
    protected abstract List<String> tabComplete(CommandSender sender, String[] args);

    protected void sendUsage(CommandSender sender) {
        sender.sendMessage(Component.text("Usage: " + usage, NamedTextColor.RED));
    }
} 