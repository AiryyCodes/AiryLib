package dev.airyy.airylib.plugin.commands;

import dev.airyy.airylib.command.annotations.Command;
import org.bukkit.entity.Player;

public class PingCommand {

    @Command("ping")
    public void onPing(Player player) {
        player.sendMessage("Pong!");
    }

    @Command("ping {int}")
    public void onPingNumber(Player player, int number) {
        player.sendMessage("Pong! " + number);
    }
}