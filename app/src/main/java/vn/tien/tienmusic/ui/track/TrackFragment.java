package vn.tien.tienmusic.ui.track;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.tien.tienmusic.R;
import vn.tien.tienmusic.constant.ClickListenerItem;
import vn.tien.tienmusic.constant.Constant;
import vn.tien.tienmusic.data.model.Song;
import vn.tien.tienmusic.databinding.FragmentTrackBinding;
import vn.tien.tienmusic.ui.adapter.TrackAdapter;
import vn.tien.tienmusic.ui.play.PlayMusicActivity;
import vn.tien.tienmusic.viewmodel.SongViewModel;

public class TrackFragment extends Fragment {
    private RecyclerView mRecyclerSongs;
    private TrackAdapter mTrackAdapter;
    private FragmentTrackBinding mBinding;
    private SongViewModel mSongViewModel;
    private Bundle mBundle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_track, container, false);
        mBinding.setBinding(this);
        mBundle = new Bundle();
        setUpRecycler();
        return mBinding.getRoot();
    }

    private void setUpRecycler() {
        mRecyclerSongs = mBinding.recycleSongs;
        mTrackAdapter = new TrackAdapter();
        mSongViewModel = ViewModelProviders.of(getActivity()).get(SongViewModel.class);
        mSongViewModel.initViewModel(getContext());
        mSongViewModel.getSongs().observe(this, new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                mTrackAdapter.setData(songs);
                mBundle.putParcelableArrayList(Constant.BUNDLE_LIST,
                        (ArrayList<? extends Parcelable>) songs);
            }
        });
        mRecyclerSongs.setAdapter(mTrackAdapter);
        mRecyclerSongs.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerSongs.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        mRecyclerSongs.addItemDecoration(dividerItemDecoration);
        mRecyclerSongs.setItemViewCacheSize(Constant.CACHE_SIZE);
        mTrackAdapter.setClickListener(new ClickListenerItem() {
            @Override
            public void onClick(int position) {
                Intent intent = PlayMusicActivity.getIntent(getContext());
                mBundle.putInt(Constant.POSITION_SONG, position);
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });
    }
}
