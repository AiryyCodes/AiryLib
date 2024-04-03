package dev.airyy.airylib.command.arguments;

public class IntegerArgument implements Argument<Integer> {
    @Override
    public String getPlaceholder() {
        return "{int}";
    }

    @Override
    public Integer convert(String value) throws InvalidArgumentException {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new InvalidArgumentException("Argument must be an integer");
        }
    }

    @Override
    public String getString(String value) throws InvalidArgumentException {
        return String.valueOf(convert(value).intValue());
    }
}
