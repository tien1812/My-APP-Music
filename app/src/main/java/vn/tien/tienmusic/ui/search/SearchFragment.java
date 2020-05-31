package vn.tien.tienmusic.ui.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
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
import vn.tien.tienmusic.databinding.FragmentSearchBinding;
import vn.tien.tienmusic.ui.adapter.TrackAdapter;
import vn.tien.tienmusic.ui.play.PlayMusicActivity;
import vn.tien.tienmusic.utils.StringUtils;
import vn.tien.tienmusic.viewmodel.SearchViewModel;

public class SearchFragment extends Fragment implements
        SearchView.OnQueryTextListener {
    private FragmentSearchBinding mSearchBinding;
    private RecyclerView mRecyclerSearch;
    private SearchView mSearchView;
    private TrackAdapter mTrackAdapter;
    private Intent mIntent;
    private Bundle mBundle = new Bundle();
    private SearchViewModel mViewModel;
    private TextView mTextTitle, mTextKq;
    private ProgressBar mProgressBar;
    private OnListenerFavorite mOnListenerFavorite;
    private ClickListenerItem mListenerItem;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mSearchBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container,
                false);
        initView();
        registerListener();
        setUpRecycleView();
        return mSearchBinding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListenerItem = (ClickListenerItem) context;
        mOnListenerFavorite = (OnListenerFavorite) context;
    }

    @Override
    public void onStart() {
        super.onStart();
        setAdapter();
    }

    private void searchSong(String query) {
        mViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        mViewModel.initViewModel(getContext());
        mViewModel.getSongSearchs(query).observe(this, new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                mTrackAdapter.setData(songs);
                mProgressBar.setVisibility(View.GONE);
                mBundle.putParcelableArrayList(Constant.BUNDLE_LIST,
                        (ArrayList<? extends Parcelable>) songs);
            }
        });
    }

    private void setUpRecycleView() {
        mTrackAdapter = new TrackAdapter();
        mRecyclerSearch.setAdapter(mTrackAdapter);
        mRecyclerSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerSearch.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        mRecyclerSearch.addItemDecoration(dividerItemDecoration);
        mRecyclerSearch.setItemViewCacheSize(Constant.CACHE_SIZE);
    }

    private void setAdapter() {
        mTrackAdapter.setClickListener(new ClickListenerItem() {
            @Override
            public void onClickItem(Song song, int position) {
                mIntent = PlayMusicActivity.getIntent(getContext());
                mBundle.putInt(Constant.POSITION_SONG, position);
                mIntent.putExtras(mBundle);
                startActivity(mIntent);
                mListenerItem.onClickItem(song, position);
            }
        });
        mTrackAdapter.setListenerFavorite(mOnListenerFavorite);
    }

    private void initView() {
        mRecyclerSearch = mSearchBinding.recycleSearch;
        mSearchView = mSearchBinding.searchSongs;
        mTextTitle = mSearchBinding.title;
        mTextKq = mSearchBinding.textKq;
        mProgressBar = mSearchBinding.processBar;
    }

    private void registerListener() {
        mSearchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mTextKq.setVisibility(View.VISIBLE);
        mTextTitle.setText(query.trim());
        String title = StringUtils.formatQuery(query);
        mProgressBar.setVisibility(View.VISIBLE);
        searchSong(title);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mTrackAdapter.setData(null);
        return true;
    }
}
