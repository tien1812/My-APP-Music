package vn.tien.tienmusic.data.source;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import vn.tien.tienmusic.data.model.Song;

@Database(entities = {Song.class} , version = 1 ,exportSchema = false)
public abstract class SongRoomDataBase extends RoomDatabase {
    public abstract SongDao mSongDao();

    private static SongRoomDataBase sINSTANCE;

    public static SongRoomDataBase getInstance(Context context) {
        if (sINSTANCE == null) {
            sINSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    SongRoomDataBase.class, "song_table").fallbackToDestructiveMigration()
                    .build();
        }
        return sINSTANCE;
    }

}
