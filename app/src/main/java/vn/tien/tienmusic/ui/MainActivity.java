package vn.tien.tienmusic.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import vn.tien.tienmusic.R;
import vn.tien.tienmusic.constant.ClickListenerItem;
import vn.tien.tienmusic.constant.Constant;
import vn.tien.tienmusic.data.model.Song;
import vn.tien.tienmusic.databinding.ActivityMainBinding;
import vn.tien.tienmusic.ui.favorite.FavoriteFragment;
import vn.tien.tienmusic.ui.mymusic.MyMusicFragment;
import vn.tien.tienmusic.ui.track.TrackFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ClickListenerItem {
    private BottomNavigationView mBottomNavView;
    private ActivityMainBinding mMainBinding;
    private Toolbar mToolbar;
    private ImageView mAvatar;
    private TextView mTextTrack;
    private TextView mTextArtist;
    private RelativeLayout mLayoutPlay;
    private ImageView mBtnPlay;
    private TrackFragment mTrackFragment;
    private MyMusicFragment mMyMusicFragment;
    private FavoriteFragment mFavoriteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initView();
        register();
        setSelectBotNavView();
        setUpToolBar();
        checkPermission();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                mTrackFragment).commit();
    }

    private void setUpToolBar() {
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.app_name);
        mToolbar.setNavigationIcon(R.drawable.ic_music_note_black_24dp);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_view:
                Intent intent = SearchActivity.getIntent(this);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        mBottomNavView = mMainBinding.botNav;
        mToolbar = mMainBinding.toolBarMain;
        mAvatar = mMainBinding.imgTrack;
        mTextTrack = mMainBinding.textTrack;
        mTextArtist = mMainBinding.textArtist;
        mLayoutPlay = mMainBinding.layoutPlay;
        mBtnPlay = mMainBinding.imgPlay;
        mTrackFragment = new TrackFragment();
        mMyMusicFragment = new MyMusicFragment();
        mFavoriteFragment = new FavoriteFragment();
    }

    private void register() {
        mBtnPlay.setOnClickListener(this);
        mLayoutPlay.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn muốn thoát ứng dụng không?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_play:
                break;
            case R.id.layout_play:
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(Song song, int position) {
        if (song == null) {
            return;
        } else {
            mLayoutPlay.setVisibility(View.VISIBLE);
            Glide.with(mAvatar.getContext()).load(song.getUser().getAvatarUrl())
                    .placeholder(R.drawable.cd)
                    .into(mAvatar);
            mTextTrack.setText(song
                    .getTitle());
            mTextArtist.setText(song.getUser().getUserName());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("taggg", "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("taggg", "onDestroy: MainActivity");
    }
}
