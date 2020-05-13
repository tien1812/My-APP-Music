package vn.tien.tienmusic.utils;

import java.util.ArrayList;
import java.util.List;

import vn.tien.tienmusic.constant.Constant;
import vn.tien.tienmusic.data.model.Song;

public class SongsUtils {
    public static List<Song> updateSongs(List<Song> songs) {
        List<Song> newSongs = new ArrayList<>();
        for (int i = 0; i < songs.size(); i++) {
            Song song = songs.get(i);
            song.setStreamUrl(song.getStreamUrl() + Constant.CLIENT_ID);
            newSongs.add(song);
        }
        return newSongs;
    }
}
