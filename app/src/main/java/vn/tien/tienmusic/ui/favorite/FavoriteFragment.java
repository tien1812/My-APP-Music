package vn.tien.tienmusic.ui.favorite;

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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.tien.tienmusic.R;
import vn.tien.tienmusic.constant.ClickListenerItem;
import vn.tien.tienmusic.constant.Constant;
import vn.tien.tienmusic.data.model.Song;
import vn.tien.tienmusic.databinding.FragmentFavoriteBinding;
import vn.tien.tienmusic.ui.adapter.TrackAdapter;
import vn.tien.tienmusic.ui.play.PlayMusicActivity;
import vn.tien.tienmusic.viewmodel.SongFavViewModel;

public class FavoriteFragment extends Fragment {
    private FragmentFavoriteBinding mFavoriteBinding;
    private TrackAdapter mAdapter;
    private RecyclerView mRecyclerFav;
    private SongFavViewModel mSongFavViewModel;
    private Bundle mBundle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFavoriteBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_favorite, container, false);
        return mFavoriteBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
        setClickItem();
    }

    private void setClickItem() {
        mAdapter.setClickListener(new ClickListenerItem() {
            @Override
            public void onClick(Song song, int position) {
                Intent intent = PlayMusicActivity.getIntent(getContext());
                mBundle.putInt(Constant.POSITION_SONG, position);
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Song song = mAdapter.getSongAtPosition(position);
                mSongFavViewModel.deleteSong(song);
            }
        });
        helper.attachToRecyclerView(mRecyclerFav);
    }

    private void initView() {
        mBundle = new Bundle();
        mRecyclerFav = mFavoriteBinding.recycleFavorite;
        mAdapter = new TrackAdapter();
        mSongFavViewModel = ViewModelProviders.of(this).get(SongFavViewModel.class);
        mSongFavViewModel.getSongs().observe(this, new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                mAdapter.setData(songs);
                mBundle.putParcelableArrayList(Constant.BUNDLE_LIST,
                        (ArrayList<? extends Parcelable>) songs);
            }
        });
        mRecyclerFav.setAdapter(mAdapter);
        mRecyclerFav.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerFav.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        mRecyclerFav.addItemDecoration(dividerItemDecoration);
    }
}
