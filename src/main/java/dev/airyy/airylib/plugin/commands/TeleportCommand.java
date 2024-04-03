package dev.airyy.airylib.plugin.commands;

import dev.airyy.airylib.command.annotations.Command;
import dev.airyy.airylib.command.annotations.Permission;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TeleportCommand {

    @Command("teleport {float} {float} {float}")
    @Permission("airyy.teleport")
    public void onTeleport(Player player, float x, float y, float z) {
        player.teleport(new Location(player.getWorld(), x, y, z));
    }

    @Command("teleport {float} {float} {float} {player}")
    @Permission("airyy.teleport.other")
    public void onTeleportOther(Player player, float x, float y, float z, Player target) {
        target.sendMessage("Teleporting " + target.getName() + " to " + x + " " + y + " " + z);
        target.teleport(new Location(player.getWorld(), x, y, z));
    }
}
