package vn.tien.tienmusic.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import vn.tien.tienmusic.BuildConfig;
import vn.tien.tienmusic.constant.Constant;

public class StringUtils {

    public static String formatDuration(int duration) {
        String time = new SimpleDateFormat(Constant.FORMAT_DATE)
                .format(new Date(duration));
        return time;
    }

    public static String formatQuery(String query) {
        String title = query.replace(" ", "%20").trim();
        return title;
    }

    public static String covertUrlFetchMusicGenre(String genre, int limit, int offset) {
        return String.format("%s%s%s&%s&%s=%d&%s=%d", Constant.BASE_URL,
                Constant.PARA_MUSIC_GENRE, genre, Constant.CLIENT_ID,
                Constant.LIMIT, limit, Constant.PARA_OFFSET, offset);
    }
}
