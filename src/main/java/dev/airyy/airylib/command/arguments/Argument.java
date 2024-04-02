package dev.airyy.airylib.command.arguments;

public interface Argument<T> {

    String getPlaceholder();

    T convert(String value);

    String getString(String value);
}
