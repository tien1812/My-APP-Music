package vn.tien.tienmusic.ui.mymusic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import vn.tien.tienmusic.R;
import vn.tien.tienmusic.constant.ClickListenerItem;
import vn.tien.tienmusic.constant.Constant;
import vn.tien.tienmusic.constant.OnListenerFavorite;
import vn.tien.tienmusic.data.model.Song;
import vn.tien.tienmusic.databinding.FragmentMymusicBinding;
import vn.tien.tienmusic.ui.adapter.TrackAdapter;
import vn.tien.tienmusic.ui.play.PlayMusicActivity;
import vn.tien.tienmusic.viewmodel.MySongViewModel;

public class MyMusicFragment extends Fragment {
    private FragmentMymusicBinding mMymusicBinding;
    private TrackAdapter mAdapter;
    private RecyclerView mRecyclerMusics;
    private OnListenerFavorite mOnListenerFavorite;
    private ClickListenerItem mListenerItem;
    private MySongViewModel mSongViewModel;
    private List<Song> mSongs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMymusicBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_mymusic, container, false);
        initView();
        setRecycleView();
        return mMymusicBinding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();
        setClickItem();
        Log.d("taggg", "onResume: fragment");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mOnListenerFavorite = (OnListenerFavorite) context;
        mListenerItem = (ClickListenerItem) context;
    }

    private void setRecycleView() {
        mRecyclerMusics.setAdapter(mAdapter);
        mRecyclerMusics.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerMusics.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        mRecyclerMusics.addItemDecoration(dividerItemDecoration);
        mRecyclerMusics.setItemViewCacheSize(Constant.CACHE_SIZE);
        Collections.sort(mSongs, new Comparator<Song>() {
            @Override
            public int compare(Song myMusic, Song t1) {
                return myMusic.getTitle().compareTo(t1.getTitle());
            }
        });
    }

    private void initView() {
        mSongs = new ArrayList<>();
        mSongViewModel = ViewModelProviders.of(this).get(MySongViewModel.class);
        mSongViewModel.initViewModel(getContext());
        mRecyclerMusics = mMymusicBinding.recycleMyMusic;
        mAdapter = new TrackAdapter();
        mSongs.addAll(mSongViewModel.getSongsLocal());
        mAdapter.setData(mSongs);
    }

    private void setClickItem() {
        mAdapter.setClickListener(new ClickListenerItem() {
            @Override
            public void onClickItem(Song song, int position) {
                Intent intent = PlayMusicActivity.getIntent(getContext());
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(Constant.BUNDLE_LIST,
                        (ArrayList<? extends Parcelable>) mSongs);
                bundle.putInt(Constant.POSITION_SONG, position);
                intent.putExtras(bundle);
                startActivity(intent);
                mListenerItem.onClickItem(song, position);
            }
        });
        mAdapter.setListenerFavorite(mOnListenerFavorite);
    }
}
