package vn.tien.tienmusic.ui.track;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import vn.tien.tienmusic.constant.OnListenerFavorite;
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
    private Intent mIntent;
    private ProgressBar mProgressBar;
    private OnListenerFavorite mOnListenerFavorite;
    private TextView mTextView;
    private ClickListenerItem mListenerItem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_track, container, false);
        mBinding.setBinding(this);
        mBundle = new Bundle();
        Log.d("tag", "onCreateView: ");
        return mBinding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mOnListenerFavorite = (OnListenerFavorite) context;
        mListenerItem = (ClickListenerItem) context;
    }

    @Override
    public void onStart() {
        super.onStart();
        setUpRecycler();
    }

    private void setUpRecycler() {
        mTextView = mBinding.textMore;
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSongViewModel = ViewModelProviders.of(getActivity()).get(SongViewModel.class);
                mSongViewModel.initViewModel(getContext());
                mSongViewModel.getSongs().observe(getActivity(), new Observer<List<Song>>() {
                    @Override
                    public void onChanged(List<Song> songs) {
                        mTrackAdapter.setData(songs);
                        mProgressBar.setVisibility(View.GONE);
                        mBundle.putParcelableArrayList(Constant.BUNDLE_LIST,
                                (ArrayList<? extends Parcelable>) songs);
                    }
                });
            }
        });
        mSongViewModel = ViewModelProviders.of(getActivity()).get(SongViewModel.class);
        mSongViewModel.initViewModel(getContext());
        mSongViewModel.getSongs().observe(this, new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                List<Song> songs1 = new ArrayList<>();
                songs1.addAll(songs.subList(0, 4));
                mTrackAdapter.setData(songs1);
                mProgressBar.setVisibility(View.GONE);
                mBundle.putParcelableArrayList(Constant.BUNDLE_LIST,
                        (ArrayList<? extends Parcelable>) songs);
            }
        });
        mProgressBar = mBinding.processBar;
        mRecyclerSongs = mBinding.recycleSongs;
        mTrackAdapter = new TrackAdapter();
        mRecyclerSongs.setAdapter(mTrackAdapter);
        mRecyclerSongs.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerSongs.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        mRecyclerSongs.addItemDecoration(dividerItemDecoration);
        mRecyclerSongs.setItemViewCacheSize(Constant.CACHE_SIZE);
        mTrackAdapter.setClickListener(new ClickListenerItem() {
            @Override
            public void onClick(Song song, int position) {
                mIntent = PlayMusicActivity.getIntent(getContext());
                mBundle.putInt(Constant.POSITION_SONG, position);
                mIntent.putExtras(mBundle);
                startActivity(mIntent);
                mListenerItem.onClick(song,position);
            }
        });
        mTrackAdapter.setListenerFavorite(mOnListenerFavorite);
    }
}
