package vn.tien.tienmusic.viewmodel;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import java.util.List;

import vn.tien.tienmusic.data.model.Song;
import vn.tien.tienmusic.data.repository.SongLocalRepository;
import vn.tien.tienmusic.data.source.MySongManager;

public class MySongViewModel extends ViewModel {
    private List<Song> mSongs;
    private SongLocalRepository mSongLocalRepository;

    public void initViewModel(Context context) {
        mSongLocalRepository = SongLocalRepository.getInstance(MySongManager.getInstance(context));
        mSongs = mSongLocalRepository.getSongsLocal();
    }

    public List<Song> getSongsLocal() {
        return mSongs;
    }
}
