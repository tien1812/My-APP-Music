package vn.tien.tienmusic.ui.favorite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import vn.tien.tienmusic.constant.OnListenerFavorite;
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
    private OnListenerFavorite mOnListenerFavorite;
    private ClickListenerItem mListenerItem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFavoriteBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_favorite, container, false);
        initView();
        return mFavoriteBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        setClickItem();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mOnListenerFavorite = (OnListenerFavorite) context;
        mListenerItem = (ClickListenerItem) context;
    }

    private void setClickItem() {
        mAdapter.setClickListener(new ClickListenerItem() {
            @Override
            public void onClickItem(List<Song> songs, int position) {
                mListenerItem.onClickItem(songs, position);
            }
        });
        mAdapter.setListenerFavorite(mOnListenerFavorite);

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
                song.setIsFavorite(0);
                Toast.makeText(getContext(), "Đã xoá khỏi danh sách yêu thích",
                        Toast.LENGTH_SHORT).show();
            }
        });
        helper.attachToRecyclerView(mRecyclerFav);
    }

    private void initView() {
        mRecyclerFav = mFavoriteBinding.recycleFavorite;
        mAdapter = new TrackAdapter();
        mSongFavViewModel = ViewModelProviders.of(this).get(SongFavViewModel.class);
        mSongFavViewModel.getSongs().observe(this, new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                mAdapter.setData(songs);
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
