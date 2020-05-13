package vn.tien.tienmusic.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import vn.tien.tienmusic.R;
import vn.tien.tienmusic.constant.Constant;
import vn.tien.tienmusic.databinding.ActivityMainBinding;
import vn.tien.tienmusic.ui.favorite.FavoriteFragment;
import vn.tien.tienmusic.ui.mymusic.MyMusicFragment;
import vn.tien.tienmusic.ui.track.TrackFragment;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNavView;
    private ActivityMainBinding mMainBinding;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initView();
        setSeclectBotNavView();
        setUpToolBar();
        checkPermission();
    }

    private void setUpToolBar() {
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.app_name);
        mToolbar.setNavigationIcon(R.drawable.ic_music_note_black_24dp);
    }

    private void initView() {
        mBottomNavView = mMainBinding.botNav;
        mToolbar = mMainBinding.toolBarMain;
        loadFragment(new TrackFragment());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setSeclectBotNavView() {
        mBottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.item_track:
                        fragment = new TrackFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.item_personal:
                        fragment = new MyMusicFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.item_favorite:
                        fragment = new FavoriteFragment();
                        loadFragment(fragment);
                        return true;
                }
                return false;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
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


}
