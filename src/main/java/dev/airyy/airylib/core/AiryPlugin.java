package dev.airyy.airylib.core;

import dev.airyy.airylib.command.CommandManager;
import dev.airyy.airylib.command.arguments.Argument;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AiryPlugin extends JavaPlugin {

    private CommandManager commandManager;

    public CommandManager getCommandManager() {
        if (commandManager == null) {
            commandManager = new CommandManager(this);
        }
        return commandManager;
    }

    public void registerCommand(Object command) {
        getCommandManager().registerCommand(command);
    }

    public void registerArgument(Argument<?> argument) {
        getCommandManager().registerArgument(argument);
    }

    public String getPermissionMessage() {
        return getCommandManager().getPermissionMessage();
    }

    public void setPermissionMessage(String message) {
        getCommandManager().setPermissionMessage(message);
    }
}
