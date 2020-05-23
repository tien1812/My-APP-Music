package vn.tien.tienmusic.data.server;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import vn.tien.tienmusic.constant.Constant;
import vn.tien.tienmusic.data.model.Song;

public interface DataServer {
    @GET("tracks" + Constant.CLIENT_ID)
    Observable<List<Song>> getAllSongs();

    @GET("tracks")
    Observable<List<Song>> getSongsSearch(@Query("client_id") String key, @Query("q") String query);
}
