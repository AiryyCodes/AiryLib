package dev.airyy.airylib.plugin.commands;

import dev.airyy.airylib.command.annotations.Command;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TeleportCommand {

    @Command("teleport {float} {float} {float}")
    public void onTeleport(Player player, float x, float y, float z) {
        player.teleport(new Location(player.getWorld(), x, y, z));
    }

    @Command("teleport {float} {float} {float} {player}")
    public void onTeleportOther(Player player, float x, float y, float z, Player target) {
        target.sendMessage("Teleporting " + target.getName() + " to " + x + " " + y + " " + z);
        target.teleport(new Location(player.getWorld(), x, y, z));
    }
}
