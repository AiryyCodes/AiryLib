package dev.airyy.airylib.command.arguments;

public class IntegerArgument implements Argument<Integer> {
    @Override
    public String getPlaceholder() {
        return "{int}";
    }

    @Override
    public Integer convert(String value) {
        return Integer.valueOf(value);
    }

    @Override
    public String getString(String value) {
        return String.valueOf(convert(value).intValue());
    }
}
