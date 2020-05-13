package vn.tien.tienmusic.data.repository;

import java.util.List;

import io.reactivex.Observable;
import vn.tien.tienmusic.data.model.Song;
import vn.tien.tienmusic.data.model.User;
import vn.tien.tienmusic.data.source.SongDataSource;
import vn.tien.tienmusic.data.source.SongRemoteData;

public class SongRepository implements SongDataSource.remote {
    private static SongRepository sSongRepository;
    private SongRemoteData mSongRemoteData;

    public SongRepository(SongRemoteData songRemoteData) {
        mSongRemoteData = songRemoteData;
    }

    public static SongRepository getInstance(SongRemoteData remoteData) {
        if (sSongRepository == null) {
            sSongRepository = new SongRepository(remoteData);
        }
        return sSongRepository;
    }

    @Override
    public Observable<List<Song>> getAllSongs() {
        return mSongRemoteData.getAllSongs();
    }
}
