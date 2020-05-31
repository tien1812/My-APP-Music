package vn.tien.tienmusic.data.repository;

import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.Observable;
import vn.tien.tienmusic.data.model.Song;
import vn.tien.tienmusic.data.source.MySongManager;
import vn.tien.tienmusic.data.source.SongDataSource;

public class SongLocalRepository implements SongDataSource.local {
    private static SongLocalRepository sSongLocalRepository;
    private MySongManager mMySongManager;

    public SongLocalRepository(MySongManager mySongManager) {
        mMySongManager = mySongManager;
    }

    public static SongLocalRepository getInstance(MySongManager mySongManager) {
        if (sSongLocalRepository == null) {
            sSongLocalRepository = new SongLocalRepository(mySongManager);
        }
        return sSongLocalRepository;
    }

    @Override
    public List<Song> getSongsLocal() {
        return mMySongManager.getSongLocal();
    }
}
