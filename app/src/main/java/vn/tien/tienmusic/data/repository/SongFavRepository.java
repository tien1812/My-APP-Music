package vn.tien.tienmusic.data.repository;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import vn.tien.tienmusic.data.model.Song;
import vn.tien.tienmusic.data.source.SongDao;
import vn.tien.tienmusic.data.source.SongRoomDataBase;

public class SongFavRepository {
    private SongDao mSongDao;
    private LiveData<List<Song>> mFavSongs;
    private static SongFavRepository sSongFavRepository;

    public static SongFavRepository getInstance(Application application){
        if (sSongFavRepository == null){
            sSongFavRepository = new SongFavRepository(application);
        }
        return sSongFavRepository;
    }

    public SongFavRepository(Application application) {
        SongRoomDataBase dataBase = SongRoomDataBase.getInstance(application);
        mSongDao = dataBase.mSongDao();
        mFavSongs = mSongDao.getAllSongs();
    }

    public LiveData<List<Song>> getFavSongs() {
        return mFavSongs;
    }

    public void addSongFav(Song song) {
        new AddSongAsyn(mSongDao).execute(song);
    }

    private static class AddSongAsyn extends AsyncTask<Song, Void, Void> {
        private SongDao mSongDao;

        public AddSongAsyn(SongDao songDao) {
            mSongDao = songDao;
        }

        @Override
        protected Void doInBackground(Song... songs) {
            mSongDao.addFavorite(songs[0]);
            return null;
        }
    }

    public void deleteSongFav(Song song) {
        new DeleteSongAsyn(mSongDao).execute(song);
    }

    private static class DeleteSongAsyn extends AsyncTask<Song, Void, Void> {
        private SongDao mSongDao;

        public DeleteSongAsyn(SongDao songDao) {
            mSongDao = songDao;
        }

        @Override
        protected Void doInBackground(Song... songs) {
            mSongDao.deleteFavorite(songs[0]);
            return null;
        }
    }
}
