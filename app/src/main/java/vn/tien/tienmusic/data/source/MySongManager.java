package vn.tien.tienmusic.data.source;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

import vn.tien.tienmusic.data.model.Song;
import vn.tien.tienmusic.data.model.User;

public class MySongManager {
    private static MySongManager sMySongManager;
    private Context mContext;
    private List<Song> mSongs;

    public MySongManager(Context context) {
        mContext = context;
    }

    public static MySongManager getInstance(Context context) {
        if (sMySongManager == null) {
            sMySongManager = new MySongManager(context);
        }
        return sMySongManager;
    }

    public List<Song> getSongLocal() {
        mSongs = new ArrayList<>();
        ContentResolver resolver = mContext.getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = resolver.query(musicUri,
                null,
                null,
                null,
                null);
        if (cursor != null && cursor.moveToFirst()) {
            int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int durationColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int uriColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            do {
                Long id = cursor.getLong(idColumn);
                String title = cursor.getString(titleColumn);
                String artist = cursor.getString(artistColumn);
                int duration = cursor.getInt(durationColumn);
                String uri = cursor.getString(uriColumn);
                User user = new User(artist);
                mSongs.add(new Song(id, title, duration, uri, user));
            }
            while (cursor.moveToNext());
        }
        return mSongs;
    }
}
