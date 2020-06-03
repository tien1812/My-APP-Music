package vn.tien.tienmusic.ui;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import vn.tien.tienmusic.R;
import vn.tien.tienmusic.constant.ClickListenerItem;
import vn.tien.tienmusic.constant.Constant;
import vn.tien.tienmusic.constant.ListenerServer;
import vn.tien.tienmusic.constant.MediaPlayerState;
import vn.tien.tienmusic.constant.OnListenerFavorite;
import vn.tien.tienmusic.data.model.Song;
import vn.tien.tienmusic.databinding.ActivityMainBinding;
import vn.tien.tienmusic.service.MusicService;
import vn.tien.tienmusic.ui.favorite.FavoriteFragment;
import vn.tien.tienmusic.ui.mymusic.MyMusicFragment;
import vn.tien.tienmusic.ui.play.PlayMusicActivity;
import vn.tien.tienmusic.ui.search.SearchFragment;
import vn.tien.tienmusic.ui.track.TrackFragment;
import vn.tien.tienmusic.viewmodel.SongFavViewModel;

public class MainActivity extends AppCompatActivity implements OnListenerFavorite, ListenerServer,
        View.OnClickListener, ClickListenerItem {
    private BottomNavigationView mBottomNavView;
    private ActivityMainBinding mMainBinding;
    private TrackFragment mTrackFragment;
    private MyMusicFragment mMyMusicFragment;
    private FavoriteFragment mFavoriteFragment;
    private SearchFragment mSearchFragment;
    private SongFavViewModel mSongFavViewModel;
    private TextView mTextTitle, mTextArtist;
    private ImageView mImgPause, mImgAvatar;
    private RelativeLayout mRelativeLayout;
    private ObservableField<Song> mSongObservable = new ObservableField<>();
    private MusicService mMusicService;
    private ServiceConnection mServiceConnection;
    private boolean mBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initView();
        setSelectBotNavView();
        checkPermission();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                mTrackFragment).commit();
        registerListener();
        bindToService();
    }


    private void registerListener() {
        mImgPause.setOnClickListener(this);
        mRelativeLayout.setOnClickListener(this);
    }

    private void bindToService() {
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                if (iBinder instanceof MusicService.MusicBinder) {
                    mMusicService = ((MusicService.MusicBinder) iBinder).getService();
                    mBound = true;
                    mMusicService.setOnListenerServer(MainActivity.this);
                } else mBound = false;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mBound = false;
            }
        };
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    private void playOrPause() {
        if (!mBound) {
            return;
        }
        if (mMusicService.getState() == MediaPlayerState.PLAYING) {
            mImgPause.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        } else {
            mImgPause.setImageResource(R.drawable.ic_pause_black_24dp);
        }
        mMusicService.playSong();
    }

    private void initView() {
        mBottomNavView = mMainBinding.botNav;
        mTrackFragment = new TrackFragment();
        mMyMusicFragment = new MyMusicFragment();
        mFavoriteFragment = new FavoriteFragment();
        mSearchFragment = new SearchFragment();
        mSongFavViewModel = ViewModelProviders.of(this).get(SongFavViewModel.class);
        mImgPause = mMainBinding.imagePause;
        mImgAvatar = mMainBinding.imgAva;
        mTextTitle = mMainBinding.textTitle;
        mTextArtist = mMainBinding.textArtist;
        mRelativeLayout = mMainBinding.layoutPlay;
        mTextTitle.startAnimation
                (AnimationUtils.loadAnimation(MainActivity.this, R.anim.translate));
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn muốn thoát ứng dụng không?");
        builder.setPositiveButton(Html.fromHtml("<font color='#050407'>Có</font>"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
        builder.setNegativeButton(Html.fromHtml("<font color='#050407'>Không</font>"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setSelectBotNavView() {
        mBottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.item_track:
                        fragment = mTrackFragment;
                        break;
                    case R.id.item_personal:
                        fragment = mMyMusicFragment;
                        break;
                    case R.id.item_favorite:
                        fragment = mFavoriteFragment;
                        break;
                    case R.id.item_search:
                        fragment = mSearchFragment;
                        break;
                    default:
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        fragment).commit();
                return true;
            }
        });
    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        Constant.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        }
    }

    @Override
    public void listenerFav(ImageView imageView, Song song) {
        if (song.isFavorite() == 0) {
            mSongFavViewModel.addSong(song);
            imageView.setImageResource(R.drawable.ic_favorite_red_24dp);
            Toast.makeText(this, "Đã thêm vào danh sách yêu thích",
                    Toast.LENGTH_SHORT).show();
        } else {
            mSongFavViewModel.deleteSong(song);
            Toast.makeText(this, "Đã xoá khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
            imageView.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
    }

    @Override
    public void upDateUi() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextTitle.setText(getCurrentSong().getTitle());
                mTextArtist.setText(getCurrentSong().getUser().getUserName());
                Glide.with(mImgAvatar.getContext()).load(getCurrentSong().
                        getUser().getAvatarUrl())
                        .placeholder(R.drawable.cd)
                        .into(mImgAvatar);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_pause:
                playOrPause();
                break;
            case R.id.layout_play:
                showPlaySong();
                break;
            default:
                break;
        }
    }

    private void showPlaySong() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constant.BUNDLE_LIST,
                (ArrayList<? extends Parcelable>) getSongs());
        bundle.putInt(Constant.POSITION_SONG, getCurrentIndex());
        bundle.putInt(Constant.CURRENT_TIME_SONG, getCurrentTime());
        Intent intent = PlayMusicActivity.getIntent(this);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.start_play, 0);
    }

    @Override
    public void onClickItem(Song song, int position) {
        mMusicService.stopSong();
        mSongObservable.set(song);
        updatePlayMini();
    }

    private void updatePlayMini() {
        mTextTitle.setText(mSongObservable.get().getTitle());
        mTextArtist.setText(mSongObservable.get().getUser().getUserName());
        Glide.with(mImgAvatar.getContext()).load(mSongObservable.get().getUser().getAvatarUrl())
                .placeholder(R.drawable.cd)
                .into(mImgAvatar);
        mRelativeLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("taggg", "onRestart:main ");
        if (getSongs() == null) {
            return;
        } else {
            upDateUi();
        }
        mMusicService.setOnListenerServer(this);
        if (mMusicService.getState() == MediaPlayerState.PLAYING) {
            mImgPause.setImageResource(R.drawable.ic_pause_black_24dp);
        } else mImgPause.setImageResource(R.drawable.ic_play_arrow_black_24dp);
    }

    private Song getCurrentSong() {
        return mMusicService.getCurrentSong();
    }

    private List<Song> getSongs() {
        return mMusicService.getSongs();
    }

    private int getCurrentIndex() {
        return mMusicService.getCurrentIndex();
    }

    private int getCurrentTime() {
        return mMusicService.getCurrentTime();
    }

    @Override
    protected void onDestroy() {
        Log.d("taggg", "onDestroy:main ");
        super.onDestroy();
        mBound = false;
        unbindService(mServiceConnection);
    }
}
