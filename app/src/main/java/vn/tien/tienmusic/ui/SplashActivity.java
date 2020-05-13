package vn.tien.tienmusic.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import vn.tien.tienmusic.R;
import vn.tien.tienmusic.constant.Constant;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = MainActivity.getIntent(SplashActivity.this);
                startActivity(intent);
                finish();
            }
        }, Constant.SPLASH_DELAY_TIME);
    }
}
