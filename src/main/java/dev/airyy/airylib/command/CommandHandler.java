package dev.airyy.airylib.command;

import dev.airyy.airylib.command.arguments.Argument;
import dev.airyy.airylib.command.arguments.InvalidArgumentException;
import dev.airyy.airylib.misc.ChatUtils;
import dev.airyy.airylib.misc.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public final class CommandHandler<T> extends Command {

    private final Map<String, AbstractMap.SimpleEntry<CommandData, Method>> methods;
    private final T commandClass;
    private final List<String> availableArgs;
    private final CommandManager commandManager;


    public CommandHandler(Map<String, AbstractMap.SimpleEntry<CommandData, Method>> methods, T commandClass, List<String> availableArgs, CommandManager commandManager, String name) {
        super(name);

        this.methods = methods;
        this.commandClass = commandClass;
        this.availableArgs = availableArgs;
        this.commandManager = commandManager;
    }

    public CommandHandler(Map<String, AbstractMap.SimpleEntry<CommandData, Method>> method, T commandClass, List<String> availableArgs, CommandManager commandManager, String name, String description, String usageMessage, List<String> aliases) {
        super(name, description, usageMessage, aliases);

        this.methods = method;
        this.commandClass = commandClass;
        this.availableArgs = availableArgs;
        this.commandManager = commandManager;
    }

    @Override
    public boolean execute(CommandSender sender, String commandName, String[] args) {
        List<Object> params = new ArrayList<>();
        params.add(sender);

        Method method = null;
        String commandAnnotation = "";
        for (String key : methods.keySet()) {
            String[] split = key.split(" ");
            List<String> splitList = new ArrayList<>();
            for (String s : split) {
                if (s.equalsIgnoreCase(commandName))
                    continue;

                splitList.add(s);
            }

            if (key.equalsIgnoreCase(commandName) && args.length == 0) {
                method = methods.get(key).getValue();
                commandAnnotation = methods.get(key).getKey().commandAnnotation();
                break;
            }

            if (splitList.size() != args.length) {
                continue;
            }

            method = methods.get(key).getValue();
            commandAnnotation = methods.get(key).getKey().commandAnnotation();
        }

        if (method == null || commandAnnotation.isEmpty())
            return true;

        String permission = methods.get(commandAnnotation).getKey().permission();
        if (!permission.isEmpty() && !sender.hasPermission(permission)) {
            sender.sendMessage(ChatUtils.color(commandManager.getPermissionMessage()));
            return true;
        }

        List<String> argsList = StringUtils.splitFromIndex(commandAnnotation, " ", 1);

        for (int i = 0; i < args.length; i++) {
            Argument<?> argument = commandManager.getArgument(argsList.get(i));
            if (argument == null)
                continue;

            try {
                params.add(argument.convert(args[i]));
            } catch (InvalidArgumentException e) {
                return true;
            }
        }

        String[] newArgs = Arrays.copyOfRange(args, params.size() > 1 ? params.size() - 2 : 0, args.length);

        try {
            if (sender instanceof Player player) {
                handlePlayer(player, commandName, newArgs, method, params);
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    public void handlePlayer(Player player, String commandName, String[] args, Method method, List<Object> params) throws InvocationTargetException, IllegalAccessException {
        if (!commandName.equalsIgnoreCase(this.getName()))
            return;

        if (params.isEmpty()) {
            List<Object> paramsArray = new ArrayList<>();
            paramsArray.add(player);
            callMethod(method, paramsArray);
            return;
        }

        callMethod(method, params);
    }

    private void callMethod(Method method, List<Object> params) throws InvocationTargetException, IllegalAccessException {
        method.invoke(commandClass, params.toArray());
    }
}
