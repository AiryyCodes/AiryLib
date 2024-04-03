package dev.airyy.airylib.command.arguments;

public class FloatArgument implements Argument<Float> {
    @Override
    public String getPlaceholder() {
        return "{float}";
    }

    @Override
    public Float convert(String value) throws InvalidArgumentException {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            throw new InvalidArgumentException("Argument must be a float");
        }
    }

    @Override
    public String getString(String value) throws InvalidArgumentException {
        return String.valueOf(convert(value).floatValue());
    }
}
