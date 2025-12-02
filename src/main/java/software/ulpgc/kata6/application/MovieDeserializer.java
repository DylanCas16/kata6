package software.ulpgc.kata6.application;

import software.ulpgc.kata6.architecture.Model.Movie;

public class MovieDeserializer {

    public static Movie fromTsv(String str) {
        return fromTsv(str.split("\t"));
    }

    private static Movie fromTsv(String[] split) {
        return new Movie(split[2], toInt(split[5]), toInt(split[7]));
    }

    private static int toInt(String str) {
        if (isVoid(str)) return -1;
        return Integer.parseInt(str);
    }

    private static boolean isVoid(String str) {
        return str.equals("\\N");
    }
}
