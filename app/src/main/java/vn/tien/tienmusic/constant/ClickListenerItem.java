package vn.tien.tienmusic.constant;

import java.util.List;

import vn.tien.tienmusic.data.model.Song;

public interface ClickListenerItem {
    void onClickItem(List<Song> songs, int position);
}
