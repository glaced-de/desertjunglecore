package net.glaced.desertjunglecore.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class SpeedCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage("§cUsage: /speed <1-50>");
            return true;
        }

        int value;
        try {
            value = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage("§cPlease enter a number between 1 and 50.");
            return true;
        }

        if (value < 1 || value > 50) {
            player.sendMessage("§cSpeed must be between 1 and 50.");
            return true;
        }

        if (player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR) {
            player.sendMessage("§cYou must be in creative or spectator mode to change fly speed.");
            return true;
        }

        float flySpeed = value / 50f;
        player.setFlySpeed(flySpeed);

        player.sendMessage("§aFly speed set to §e" + value + "§a.");
        return true;
    }
}

