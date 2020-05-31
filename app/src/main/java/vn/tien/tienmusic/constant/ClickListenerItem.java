package vn.tien.tienmusic.constant;

import vn.tien.tienmusic.data.model.Song;

public interface ClickListenerItem {
    void onClickItem(Song song, int position);
}
