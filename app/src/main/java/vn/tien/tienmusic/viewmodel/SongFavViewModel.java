package vn.tien.tienmusic.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import vn.tien.tienmusic.data.model.Song;
import vn.tien.tienmusic.data.repository.SongFavRepository;

public class SongFavViewModel extends AndroidViewModel {
    private SongFavRepository mFavRepository;
    private LiveData<List<Song>> mSongs;
    private CompositeDisposable mCompositeDisposable;

    public SongFavViewModel(@NonNull Application application) {
        super(application);
        mFavRepository = new SongFavRepository(application);
        mSongs = mFavRepository.getFavSongs();
        mCompositeDisposable = new CompositeDisposable();
    }

    public LiveData<List<Song>> getSongs() {
        return mSongs;
    }

    public void addSong(Song song) {
        mFavRepository.addSongFav(song);
    }

    public void deleteSong(Song song) {
        mFavRepository.deleteSongFav(song);
    }
}
