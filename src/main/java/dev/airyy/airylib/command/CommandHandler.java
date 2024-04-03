package dev.airyy.airylib.command;

import dev.airyy.airylib.command.arguments.Argument;
import dev.airyy.airylib.command.arguments.InvalidArgumentException;
import dev.airyy.airylib.misc.StringUtils;
import dev.airyy.airylib.reflection.Reflection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public final class CommandHandler<T> extends Command {

    private final Map<String, AbstractMap.SimpleEntry<dev.airyy.airylib.command.annotations.Command, Method>> methods;
    private final T commandClass;
    private final List<String> availableArgs;
    private final CommandManager commandManager;


    public CommandHandler(Map<String, AbstractMap.SimpleEntry<dev.airyy.airylib.command.annotations.Command, Method>> methods, T commandClass, List<String> availableArgs, CommandManager commandManager, String name) {
        super(name);

        this.methods = methods;
        this.commandClass = commandClass;
        this.availableArgs = availableArgs;
        this.commandManager = commandManager;
    }

    public CommandHandler(Map<String, AbstractMap.SimpleEntry<dev.airyy.airylib.command.annotations.Command, Method>> method, T commandClass, List<String> availableArgs, CommandManager commandManager, String name, String description, String usageMessage, List<String> aliases) {
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
        dev.airyy.airylib.command.annotations.Command commandAnnotation = null;
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
                commandAnnotation = methods.get(key).getKey();
                break;
            }

            if (splitList.size() != args.length) {
                continue;
            }

            method = methods.get(key).getValue();
            commandAnnotation = methods.get(key).getKey();
        }

        if (method == null)
            return true;

        List<String> argsList = StringUtils.splitFromIndex(commandAnnotation.value(), " ", 1);

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

        Object[] paramsArray = params.toArray();
        callMethod(method, params);
    }

    private void callMethod(Method method, List<Object> params) throws InvocationTargetException, IllegalAccessException {
        method.invoke(commandClass, params.toArray());
    }
}
