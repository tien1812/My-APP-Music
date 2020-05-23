package vn.tien.tienmusic.ui.play;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import vn.tien.tienmusic.R;
import vn.tien.tienmusic.constant.ClickListenerItem;
import vn.tien.tienmusic.constant.Constant;
import vn.tien.tienmusic.constant.OnListenerItemPlaylist;
import vn.tien.tienmusic.data.model.Song;
import vn.tien.tienmusic.databinding.FragmentPlaylistBinding;
import vn.tien.tienmusic.ui.adapter.TrackAdapter;

public class PlayListFragment extends Fragment {
    private FragmentPlaylistBinding mPlaylistBinding;
    private RecyclerView mRecyclerPlaylist;
    private TextView mTextTitle, mTextArtist, mTextGenre;
    private TrackAdapter mListAdapter;
    private OnListenerItemPlaylist mListenerItemPlaylist;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPlaylistBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_playlist, container, false);
        initView();
        setUpRecycleView();
        return mPlaylistBinding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListenerItemPlaylist = (OnListenerItemPlaylist) context;
    }

    private void setUpRecycleView() {
        mListAdapter = new TrackAdapter();
        mListAdapter.setData(PlayMusicActivity.mSongs);
        mRecyclerPlaylist.setAdapter(mListAdapter);
        mRecyclerPlaylist.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerPlaylist.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        mRecyclerPlaylist.addItemDecoration(dividerItemDecoration);
        mRecyclerPlaylist.setItemViewCacheSize(Constant.CACHE_SIZE);
        mListAdapter.setClickListener(new ClickListenerItem() {
            @Override
            public void onClick(Song song, int position) {
                mListenerItemPlaylist.onClick(position);
            }
        });
    }

    public void setDatatoView(String title,
                              String artist,
                              String genre,
                              String trackType) {
        mTextTitle.setText(title);
        mTextArtist.setText(artist);
        if (genre != null) {
            mTextGenre.setText(genre);
        }
        if (trackType != null) {
            mTextGenre.setText(trackType);
        }
    }

    private void initView() {
        mRecyclerPlaylist = mPlaylistBinding.recyclePlaylist;
        mTextTitle = mPlaylistBinding.titleSong;
        mTextArtist = mPlaylistBinding.artist;
        mTextGenre = mPlaylistBinding.genre;
    }
}