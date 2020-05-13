package vn.tien.tienmusic.data.server;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import vn.tien.tienmusic.BuildConfig;
import vn.tien.tienmusic.data.model.Song;

public interface DataServer {
    @GET("/tracks?client_id=" + BuildConfig.API_KEY)
    Observable<List<Song>> getAllSongs();

}
