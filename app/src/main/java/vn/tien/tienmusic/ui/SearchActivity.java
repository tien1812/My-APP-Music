package vn.tien.tienmusic.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
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
import vn.tien.tienmusic.databinding.ActivitySearchBinding;
import vn.tien.tienmusic.ui.adapter.TrackAdapter;
import vn.tien.tienmusic.ui.play.PlayMusicActivity;
import vn.tien.tienmusic.utils.StringUtils;
import vn.tien.tienmusic.viewmodel.SearchViewModel;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener,
        SearchView.OnQueryTextListener {
    private ActivitySearchBinding mBinding;
    private RecyclerView mRecyclerSearch;
    private SearchView mSearchView;
    private TrackAdapter mTrackAdapter;
    private Intent mIntent;
    private Bundle mBundle = new Bundle();
    private SearchViewModel mViewModel;
    private ImageView mImgBack;
    private TextView mTextTitle, mTextKq;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        initView();
        setUpRecycleView();
        registerListener();
        Log.d("tagg","onCreate Search");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTrackAdapter.setClickListener(new ClickListenerItem() {
            @Override
            public void onClick(Song song, int position) {
                mIntent = PlayMusicActivity.getIntent(getApplicationContext());
                mBundle.putInt(Constant.POSITION_SONG, position);
                mIntent.putExtras(mBundle);
                startActivity(mIntent);
            }
        });
    }

    private void searchSong(String query) {
        mViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        mViewModel.initViewModel(this);
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
        mRecyclerSearch.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerSearch.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        mRecyclerSearch.addItemDecoration(dividerItemDecoration);
        mRecyclerSearch.setItemViewCacheSize(Constant.CACHE_SIZE);
    }

    private void initView() {
        mImgBack = mBinding.imgBack;
        mRecyclerSearch = mBinding.recycleSearch;
        mSearchView = mBinding.searchSongs;
        mTextTitle = mBinding.title;
        mTextKq = mBinding.textKq;
        mProgressBar = mBinding.processBar;
    }

    private void registerListener() {
        mImgBack.setOnClickListener(this);
        mSearchView.setOnQueryTextListener(this);
    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        return intent;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            default:
                break;
        }
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
