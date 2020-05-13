package vn.tien.tienmusic.data.source;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import vn.tien.tienmusic.data.model.Song;

@Dao
public interface SongDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addFavorite(Song song);

    @Delete
    void deleteFavorite(Song song);

    @Query("SELECT * from song_table")
    LiveData<List<Song>> getAllSongs();
}
