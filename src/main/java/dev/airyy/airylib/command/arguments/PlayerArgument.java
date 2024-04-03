package dev.airyy.airylib.command.arguments;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerArgument implements Argument<Player> {

    @Override
    public String getPlaceholder() {
        return "{player}";
    }

    @Override
    public Player convert(String value) throws InvalidArgumentException {
        Player player = Bukkit.getPlayer(value);
        if (player == null)
            throw new InvalidArgumentException("Player not found");
        return player;
    }

    @Override
    public String getString(String value) throws InvalidArgumentException {
        return convert(value).getName();
    }
}
