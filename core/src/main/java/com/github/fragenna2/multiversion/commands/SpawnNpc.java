package com.github.fragenna2.multiversion.commands;

import com.github.fragenna2.multiversion.api.NMSHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnNpc implements CommandExecutor {

    private final NMSHandler nmsHandler;

    public SpawnNpc(NMSHandler nmsHandler) {
        this.nmsHandler = nmsHandler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You can't execute this command");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "You must provide the name of the npc");
            return true;
        }

        if (args[0] == null) {
            sender.sendMessage(ChatColor.RED + "You must provide the name of the npc");
            return true;
        }

        final Player player = (Player) sender;
        nmsHandler.getNpcHandler().spawnNpc(args[0], player.getLocation());
        return true;
    }
}
