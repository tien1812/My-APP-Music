package vn.tien.tienmusic.viewmodel;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import vn.tien.tienmusic.data.model.Song;
import vn.tien.tienmusic.data.repository.SongRepository;
import vn.tien.tienmusic.data.source.SongRemoteData;
import vn.tien.tienmusic.utils.SongsUtils;

public class SearchViewModel extends ViewModel {
    private MutableLiveData<List<Song>> mSongData;
    private CompositeDisposable mCompositeDisposable;
    private SongRepository mRepository;
    private Context mContext;

    public void initViewModel(Context context) {
        mCompositeDisposable = new CompositeDisposable();
        mRepository = SongRepository.getInstance(SongRemoteData.getInstance(context));
        mContext = context;
    }

    public LiveData<List<Song>> getSongSearchs(String query) {
        if (mSongData == null) {
            mSongData = new MutableLiveData<List<Song>>();
            loadSongs(query);
        }
        return mSongData;
    }

    private void loadSongs(String query) {
        Disposable disposable = mRepository.getSearchSong(query)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(response -> handleReponse(response),
                        error -> handleError(error));
        mCompositeDisposable.add(disposable);
    }

    private void handleReponse(List<Song> response) {
        List<Song> songs = SongsUtils.updateSongs(response);
        mSongData.setValue(songs);
    }

    private void handleError(Throwable error) {
        Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mCompositeDisposable.dispose();
    }
}
