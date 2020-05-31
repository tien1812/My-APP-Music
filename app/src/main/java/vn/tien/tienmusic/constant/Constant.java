package vn.tien.tienmusic.constant;

import vn.tien.tienmusic.BuildConfig;

public class Constant {
    public static final int SPLASH_DELAY_TIME = 3000;
    public static final int CACHE_SIZE = 40;
    public static final int DELAY_TIME = 100;
    public static final String FORMAT_DATE = "mm:ss";
    public static final String BUNDLE_LIST = "list";
    public static final int ANIMATOR_DURATION_TIME = 10000;
    public static final String TYPE_ANIMATOR = "rotation";
    public static final String CLIENT_ID = "?client_id=" + BuildConfig.API_KEY;
    public static final String POSITION_SONG = "position";
    public static final int IMAGE_LEVEL_PLAY = 1;
    public static final int IMAGE_LEVEL_PAUSE = 4;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final String PARA_MUSIC_GENRE = "charts?kind=top&genre=soundcloud%3Agenres%3A";
    public static final String LIMIT = "limit";
    public static final String PARA_OFFSET = "offset";
    public static final String BASE_URL = "https://api-v2.soundcloud.com/";
}
