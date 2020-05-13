package vn.tien.tienmusic.data.server;

import android.content.Context;

public class NetworkServer {
    private static DataServer sDataServer;

    public static DataServer getInstance(Context context) {
        if (sDataServer == null) {
            sDataServer = SongClient.getInstance(context).create(DataServer.class);
        }
        return sDataServer;
    }
}
