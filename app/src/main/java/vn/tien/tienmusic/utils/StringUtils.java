package vn.tien.tienmusic.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import vn.tien.tienmusic.constant.Constant;

public class StringUtils {

    public static String formatDuration(int duration) {
        String time = new SimpleDateFormat(Constant.FORMAT_DATE)
                .format(new Date(duration));
        return time;
    }
}
