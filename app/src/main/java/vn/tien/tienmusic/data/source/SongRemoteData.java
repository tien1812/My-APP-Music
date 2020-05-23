package vn.tien.tienmusic.data.source;

import android.content.Context;

import java.util.List;

import io.reactivex.Observable;
import vn.tien.tienmusic.BuildConfig;
import vn.tien.tienmusic.data.model.Song;
import vn.tien.tienmusic.data.server.DataServer;
import vn.tien.tienmusic.data.server.NetworkServer;

public class SongRemoteData implements SongDataSource.remote, SongDataSource.search {
    private static SongRemoteData sSongRemoteData;
    private DataServer mServer;

    private SongRemoteData(DataServer server) {
        mServer = server;
    }

    public static SongRemoteData getInstance(Context context) {
        if (sSongRemoteData == null) {
            sSongRemoteData = new SongRemoteData(NetworkServer.getInstance(context));
        }
        return sSongRemoteData;
    }

    @Override
    public Observable<List<Song>> getAllSongs() {
        return mServer.getAllSongs();
    }

    @Override
    public Observable<List<Song>> getSearchSong(String query) {
        return mServer.getSongsSearch(BuildConfig.API_KEY,query);
    }

}
