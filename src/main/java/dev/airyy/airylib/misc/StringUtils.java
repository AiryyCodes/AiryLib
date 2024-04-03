package dev.airyy.airylib.misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringUtils {

    public static List<String> splitFromIndex(String string, String delimiter, int startIndex) {
        String[] split = string.split(delimiter);
        return Arrays.asList(split).subList(startIndex, split.length);
    }
}
