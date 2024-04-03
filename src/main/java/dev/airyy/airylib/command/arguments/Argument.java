package dev.airyy.airylib.command.arguments;

public interface Argument<T> {

    String getPlaceholder();

    T convert(String value) throws InvalidArgumentException;

    String getString(String value) throws InvalidArgumentException;
}
