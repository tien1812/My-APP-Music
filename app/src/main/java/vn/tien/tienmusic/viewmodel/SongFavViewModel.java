package vn.tien.tienmusic.viewmodel;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import vn.tien.tienmusic.data.model.Song;
import vn.tien.tienmusic.data.repository.SongFavRepository;

public class SongFavViewModel extends AndroidViewModel {
    private SongFavRepository mFavRepository;
    private LiveData<List<Song>> mSongs;
    private Context mContext;

    public SongFavViewModel(@NonNull Application application) {
        super(application);
        mFavRepository = new SongFavRepository(application);
        mSongs = mFavRepository.getFavSongs();
        mContext = application;
    }

    public LiveData<List<Song>> getSongs() {
        return mSongs;
    }

    public void addSong(Song song) {
        mFavRepository.addSongFav(song);
        song.setIsFavorite(1);
    }

    public void deleteSong(Song song) {
        mFavRepository.deleteSongFav(song);
        song.setIsFavorite(0);
    }

}
