package vn.tien.tienmusic.ui.play;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import vn.tien.tienmusic.R;
import vn.tien.tienmusic.constant.Constant;
import vn.tien.tienmusic.constant.ListenerServer;
import vn.tien.tienmusic.constant.MediaPlayerState;
import vn.tien.tienmusic.constant.OnListenerItemPlaylist;
import vn.tien.tienmusic.data.model.Song;
import vn.tien.tienmusic.databinding.ActivityPlayBinding;
import vn.tien.tienmusic.service.MusicService;
import vn.tien.tienmusic.utils.StringUtils;
import vn.tien.tienmusic.viewmodel.SongFavViewModel;

public class PlayMusicActivity extends AppCompatActivity implements View.OnClickListener,
        ListenerServer, OnListenerItemPlaylist {
    private ActivityPlayBinding mBinding;
    private Toolbar mToolbar;
    private TextView mTextDuration, mTextTime;
    private static ViewPagerPlayScreen sPagerPlayScreen;
    private AvatarFragment mAvatarFragment;
    private PlayListFragment mPlayListFragment;
    private ViewPager mViewPager;
    private ServiceConnection mServiceConnection;
    private Boolean mBound;
    private MusicService mMusicService;
    private FloatingActionButton mBtnPlay;
    private ImageView mBtnNext, mBtnPre, mBtnRandom, mBtnRepeat, mBtnFavorite;
    public static ArrayList<Song> mSongs = new ArrayList<>();
    public static int mPosSong;
    private SeekBar mSeekBar;
    private SongFavViewModel mSongFavViewModel;
    private UpDateSeekbar mUpDateSeekbar;
    private ImageView mImageFavorite;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_play);
        initView();
        getData();
        setToolbar();
        customViewPager();
        bindtoService();
    }

    private void initView() {
        mToolbar = mBinding.toolBarPlay;
        mTextDuration = mBinding.textDuration;
        mViewPager = mBinding.viewPagerPlay;
        mBtnPlay = mBinding.btnPlay;
        mBtnPlay.setOnClickListener(this);
        mBtnNext = mBinding.imageNext;
        mBtnNext.setOnClickListener(this);
        mBtnPre = mBinding.imagePre;
        mBtnPre.setOnClickListener(this);
        mTextTime = mBinding.textTime;
        mBtnRandom = mBinding.imageRandom;
        mBtnRandom.setOnClickListener(this);
        mBtnRepeat = mBinding.imageRepeat;
        mBtnRepeat.setOnClickListener(this);
        mSeekBar = mBinding.seekBar;
        mImageFavorite = mBinding.imageFavorite;
        mImageFavorite.setOnClickListener(this);
        mSongFavViewModel = ViewModelProviders.of(this).get(SongFavViewModel.class);
    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        mSongs = bundle.getParcelableArrayList(Constant.BUNDLE_LIST);
        mPosSong = bundle.getInt(Constant.POSITION_SONG);
    }

    private void setToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp);
    }

    private void customViewPager() {
        mAvatarFragment = new AvatarFragment();
        mPlayListFragment = new PlayListFragment();
        sPagerPlayScreen = new ViewPagerPlayScreen(getSupportFragmentManager(), 2);
        sPagerPlayScreen.addFragment(mAvatarFragment);
        sPagerPlayScreen.addFragment(mPlayListFragment);
        mViewPager.setAdapter(sPagerPlayScreen);
    }

    private void bindtoService() {
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                if (iBinder instanceof MusicService.MusicBinder) {
                    mMusicService = ((MusicService.MusicBinder) iBinder).getService();
                    mBound = true;
                    mMusicService.setSongs(mSongs);
                    mMusicService.setCurrentIndex(mPosSong);
                    mMusicService.setOnListenerServer(PlayMusicActivity.this);
                    playOrPause();
                    upDateUi();
                } else {
                    mBound = false;
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mBound = false;
            }
        };
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, mServiceConnection, Service.BIND_AUTO_CREATE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void addSongFav() {
            Toast.makeText(PlayMusicActivity.this, "Favorited", Toast.LENGTH_SHORT).show();
            mSongFavViewModel.addSong(getCurrentSong());
            mImageFavorite.setImageResource(R.drawable.ic_favorite_red_24dp);
    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, PlayMusicActivity.class);
        return intent;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_play:
                playOrPause();
                break;
            case R.id.image_next:
                nextSong();
                break;
            case R.id.image_pre:
                preSong();
                break;
            case R.id.image_random:
                randomSong();
                break;
            case R.id.image_repeat:
                repeatSong();
                break;
            case R.id.image_favorite:
                addSongFav();
            default:
                break;
        }
    }

    private void repeatSong() {
        mMusicService.repeatSong();
        if (mMusicService.isRepeat() == true) {
            Toast.makeText(PlayMusicActivity.this, "Repeat : On",
                    Toast.LENGTH_SHORT).show();
            mBtnRepeat.setImageResource(R.drawable.ic_loop_accent_24dp);
        } else {
            Toast.makeText(PlayMusicActivity.this, "Repeat : Off",
                    Toast.LENGTH_SHORT).show();
            mBtnRepeat.setImageResource(R.drawable.ic_loop_black_24dp);
        }
    }

    private void randomSong() {
        mMusicService.randomSong();
        if (mMusicService.isRandom() == true) {
            Toast.makeText(PlayMusicActivity.this, "Random : On", Toast.LENGTH_SHORT).show();
            mBtnRandom.setImageResource(R.drawable.ic_shuffle_accent_24dp);
        } else {
            Toast.makeText(PlayMusicActivity.this, "Random : Off", Toast.LENGTH_SHORT).show();
            mBtnRandom.setImageResource(R.drawable.ic_shuffle_black_24dp);
        }
    }

    private void preSong() {
        if (!mBound) {
            return;
        }
        mMusicService.preSong();
    }

    private void nextSong() {
        if (!mBound) {
            return;
        }
        mMusicService.nextSong();
    }

    private void playOrPause() {
        if (!mBound) {
            return;
        }
        if (mMusicService.getState() == MediaPlayerState.PLAYING) {
            mBtnPlay.setImageLevel(Constant.IMAGE_LEVEL_PLAY);
            mAvatarFragment.pauseAnimator();
        } else {
            mBtnPlay.setImageLevel(Constant.IMAGE_LEVEL_PAUSE);
            mAvatarFragment.startAnimator();
        }
        mMusicService.playSong();
    }

    private Song getCurrentSong() {
        return mMusicService.getCurrentSong();
    }

    @Override
    public void upDateUi() {
        if (!mBound) {
            return;
        }
        if (mUpDateSeekbar == null) {
            upDateSeekbar();
        }
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAvatarFragment.setAvatar(getCurrentSong().getUser().getAvatarUrl(),
                        getCurrentSong().getTitle(), getCurrentSong().getUser().getUserName());
                String time = StringUtils.formatDuration(getCurrentSong().getDuration());
                mTextDuration.setText(time);
                mSeekBar.setMax(getCurrentSong().getDuration());
            }
        });
    }

    private void upDateSeekbar() {
        mUpDateSeekbar = new UpDateSeekbar();
        mUpDateSeekbar.execute();
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mMusicService.seekTo(seekBar);
            }
        });
    }

    @Override
    public void onClick(int position) {
        mMusicService.playSong(position);
        upDateUi();
    }

    private class UpDateSeekbar extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            while (true) {
                if (isCancelled()) {
                    break;
                }
                try {
                    Thread.sleep(Constant.DELAY_TIME);
                    publishProgress();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            mTextTime.setText(StringUtils.formatDuration(mMusicService.getCurrentTime()));
            mSeekBar.setProgress(mMusicService.getCurrentTime());
        }
    }

    @Override
    protected void onDestroy() {
        unbindService(mServiceConnection);
        mBound = false;
        if (mUpDateSeekbar != null) {
            mUpDateSeekbar.cancel(true);
        }
        super.onDestroy();
        Log.d("taggg", "onDestroy:Playmusic ");
    }
}
