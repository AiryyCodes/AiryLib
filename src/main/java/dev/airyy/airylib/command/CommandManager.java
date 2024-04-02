package dev.airyy.airylib.command;

import dev.airyy.airylib.command.annotations.Command;
import dev.airyy.airylib.command.arguments.Argument;
import dev.airyy.airylib.command.arguments.FloatArgument;
import dev.airyy.airylib.command.arguments.IntegerArgument;
import dev.airyy.airylib.command.arguments.PlayerArgument;
import dev.airyy.airylib.reflection.Reflection;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class CommandManager {

    private final JavaPlugin plugin;
    private final CommandMap commandMap;

    private final Map<String, Argument<?>> arguments = new HashMap<>();

    public CommandManager(JavaPlugin plugin) {
        this.plugin = plugin;

        registerArgument(new PlayerArgument());
        registerArgument(new IntegerArgument());
        registerArgument(new FloatArgument());

        try {
            Field commandMapField = plugin.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            commandMap = (CommandMap) commandMapField.get(plugin.getServer());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> void registerCommand(T command) {
        List<Method> methods = Reflection.getAnnotatedMethods(command, Command.class);

        Map<String, Method> methodMap = new HashMap<>();
        Command commandName = methods.get(0).getAnnotation(Command.class);
        String baseCommandName = commandName.value().split(" ")[0];
        List<String> args = new ArrayList<>();
        for (Method method : methods) {
            Command commandAnnotation = method.getAnnotation(Command.class);
            String[] split = commandAnnotation.value().split(" ");
            args.addAll(Arrays.asList(split).subList(1, split.length));
            StringBuilder stringBuilder = new StringBuilder();
            for (String arg : args) {
                stringBuilder.append(arg).append(" ");
            }
            methodMap.put(commandAnnotation.value(), method);
        }

        CommandHandler<T> handler = new CommandHandler<>(methodMap, command, args, this, baseCommandName);
        commandMap.register(plugin.getName(), handler);
    }

    public void registerArgument(Argument<?> argument) {
        arguments.put(argument.getPlaceholder(), argument);
    }

    public Argument<?> getArgument(String placeholder) {
        return arguments.get(placeholder);
    }
}
