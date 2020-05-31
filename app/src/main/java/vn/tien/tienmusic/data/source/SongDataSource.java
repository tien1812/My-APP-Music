package vn.tien.tienmusic.data.source;

import androidx.databinding.ObservableList;

import java.util.List;

import io.reactivex.Observable;
import vn.tien.tienmusic.data.model.Song;

public interface SongDataSource {
    interface remote {
        Observable<List<Song>> getAllSongs();
    }

    interface search {
       Observable<List<Song>> getSearchSong(String query);
    }

    interface local {
        List<Song> getSongsLocal();
    }
}
