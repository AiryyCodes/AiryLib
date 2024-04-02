package dev.airyy.airylib.command;

import dev.airyy.airylib.command.arguments.Argument;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class CommandHandler<T> extends Command {

    private final Map<String, Method> methods;
    private final T commandClass;
    private final List<String> availableArgs;
    private final CommandManager commandManager;


    public CommandHandler(Map<String, Method> methods, T commandClass, List<String> availableArgs, CommandManager commandManager, String name) {
        super(name);

        this.methods = methods;
        this.commandClass = commandClass;
        this.availableArgs = availableArgs;
        this.commandManager = commandManager;
    }

    public CommandHandler(Map<String, Method> method, T commandClass, List<String> availableArgs, CommandManager commandManager, String name, String description, String usageMessage, List<String> aliases) {
        super(name, description, usageMessage, aliases);

        this.methods = method;
        this.commandClass = commandClass;
        this.availableArgs = availableArgs;
        this.commandManager = commandManager;
    }

    @Override
    public boolean execute(CommandSender sender, String commandName, String[] args) {
        List<String> replacedArgs = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        params.add(sender);
        // TODO: Add a class for converting args to an object/primitive depending on the arg placeholder
        // TODO: For example if the arg placeholder is {player} it should be replaced with the player object
        for (int i = 0; i < args.length; i++) {
            Argument<?> argument = commandManager.getArgument(availableArgs.get(i));
            if (argument == null)
                continue;

            replacedArgs.add(argument.getString(args[i]));
            params.add(argument.convert(args[i]));
        }

        System.out.println("Params size 1: " + params.size());

        try {
            if (sender instanceof Player player) {
                handlePlayer(player, commandName, args, replacedArgs, params);
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    public void handlePlayer(Player player, String commandName, String[] args, List<String> replacedArgs, List<Object> params) throws InvocationTargetException, IllegalAccessException {
        if (!commandName.equalsIgnoreCase(this.getName()))
            return;

        for (Object param : params) {
            System.out.println(param);
        }

        Method method = null;
        for (String key : methods.keySet()) {
            System.out.println("Key: " + key);
            String[] split = key.split(" ");
            List<String> splitList = new ArrayList<>();
            for (String s : split) {
                if (s.equalsIgnoreCase(commandName))
                    continue;

                splitList.add(s);
            }
            System.out.println("Split length: " + split.length);

            if (key.equalsIgnoreCase(commandName) && args.length == 0) {
                System.out.println("Key is base command: " + key);
                method = methods.get(key);
                break;
            }

            if (splitList.size() != args.length) {
                continue;
            }

            method = methods.get(key);
        }

        if (method == null)
            return;

        System.out.println("Params size 2: " + params.size());

        System.out.println("Method: " + method);

        if (params.isEmpty()) {
            method.invoke(commandClass);
            return;
        }

        Object[] paramsArray = params.toArray();

        method.invoke(commandClass, paramsArray);
    }
}
