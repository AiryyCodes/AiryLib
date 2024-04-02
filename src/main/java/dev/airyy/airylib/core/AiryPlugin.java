package dev.airyy.airylib.core;

import dev.airyy.airylib.command.CommandManager;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AiryPlugin extends JavaPlugin {

    private CommandManager commandManager;

    public CommandManager getCommandManager() {
        if (commandManager == null) {
            commandManager = new CommandManager(this);
        }
        return commandManager;
    }
}
