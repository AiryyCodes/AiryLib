package dev.airyy.airylib.plugin;

import dev.airyy.airylib.core.AiryPlugin;
import dev.airyy.airylib.plugin.commands.PingCommand;
import dev.airyy.airylib.plugin.commands.TeleportCommand;

public final class AiryLib extends AiryPlugin {

    @Override
    public void onEnable() {
        getCommandManager().registerCommand(new PingCommand());
        getCommandManager().registerCommand(new TeleportCommand());
    }

    @Override
    public void onDisable() {

    }
}
