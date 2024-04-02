package dev.airyy.airylib.command.arguments;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerArgument implements Argument<Player> {

    @Override
    public String getPlaceholder() {
        return "{player}";
    }

    @Override
    public Player convert(String value) {
        return Bukkit.getPlayer(value);
    }

    @Override
    public String getString(String value) {
        return convert(value).getName();
    }
}
