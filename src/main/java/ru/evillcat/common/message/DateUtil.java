package ru.evillcat.common.message;

import java.time.format.DateTimeFormatter;

public class DateUtil {

    public static final DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss");

    private DateUtil() {
    }
}
