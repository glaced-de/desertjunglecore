package net.glaced.desertjunglecore.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MuteCommand implements CommandExecutor {

    private final JavaPlugin plugin;
    private final Set<UUID> mutedPlayers = new HashSet<>();

    public MuteCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player admin)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (args.length == 0) {
            admin.sendMessage("§cUsage: /mute <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            admin.sendMessage("§cPlayer not found.");
            return true;
        }

        UUID id = target.getUniqueId();

        // UNMUTE
        if (mutedPlayers.contains(id)) {
            mutedPlayers.remove(id);

            admin.getScheduler().execute(
                    plugin,
                    () -> admin.sendActionBar("§aYou unmuted §e" + target.getName()),
                    null,
                    0L
            );

            admin.sendMessage("§aYou unmuted §e" + target.getName());
            return true;
        }

        // MUTE
        mutedPlayers.add(id);

        admin.getScheduler().execute(
                plugin,
                () -> admin.sendActionBar("§cYou muted §e" + target.getName()),
                null,
                0L
        );

        admin.sendMessage("§cYou muted §e" + target.getName());
        return true;
    }

    public boolean isMuted(Player player) {
        return mutedPlayers.contains(player.getUniqueId());
    }
}
