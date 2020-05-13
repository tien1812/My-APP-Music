package vn.tien.tienmusic.data.source;

import java.util.List;

import io.reactivex.Observable;
import vn.tien.tienmusic.data.model.Song;

public interface SongDataSource {
    interface remote {
        Observable<List<Song>> getAllSongs();
    }
}
