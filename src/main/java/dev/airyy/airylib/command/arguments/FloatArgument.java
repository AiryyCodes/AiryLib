package dev.airyy.airylib.command.arguments;

public class FloatArgument implements Argument<Float> {
    @Override
    public String getPlaceholder() {
        return "{float}";
    }

    @Override
    public Float convert(String value) {
        return Float.valueOf(value);
    }

    @Override
    public String getString(String value) {
        return String.valueOf(convert(value).floatValue());
    }
}
