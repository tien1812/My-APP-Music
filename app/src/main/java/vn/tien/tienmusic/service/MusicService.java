package vn.tien.tienmusic.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.masoudss.lib.WaveformSeekBar;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import vn.tien.tienmusic.R;
import vn.tien.tienmusic.constant.ListenerServer;
import vn.tien.tienmusic.constant.MediaPlayerState;
import vn.tien.tienmusic.data.model.Song;
import vn.tien.tienmusic.notification.NotificationAction;
import vn.tien.tienmusic.ui.MainActivity;

public class MusicService extends Service implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, ListenerServer{
    private IBinder mIBinder;
    private MediaPlayer mPlayer;
    private int mState;
    private List<Song> mSongs;
    private int mCurrentIndex;
    private boolean mRepeat;
    private boolean mRandom;
    private ListenerServer mOnListenerServer;

    @Override
    public void onCreate() {
        Log.d("tagg", "onCreate: ");
        super.onCreate();
        mIBinder = new MusicBinder();
        mPlayer = new MediaPlayer();
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnCompletionListener(this);
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("tagg", "onBind: ");
        return mIBinder;
    }

    public void setOnListenerServer(ListenerServer onListenerServer) {
        mOnListenerServer = onListenerServer;
    }

    public Song getCurrentSong() {
        return mSongs.get(mCurrentIndex);
    }

    public List<Song> getSongs() {
        return mSongs;
    }

    public int getCurrentTime() {
        return mPlayer.getCurrentPosition();
    }

    public int getCurrentIndex() {
        return mCurrentIndex;
    }

    public boolean isRandom() {
        return mRandom;
    }

    public int getState() {
        return mState;
    }

    public void setSongs(List<Song> songs) {
        mSongs = songs;
    }

    public void setCurrentIndex(int currentIndex) {
        mCurrentIndex = currentIndex;
    }

    public boolean isRepeat() {
        return mRepeat;
    }

    public void nextSong() {
        if (mRepeat) {
            mPlayer.isLooping();
        } else {
            if (mRandom == true) {
                mCurrentIndex = new Random().nextInt(mSongs.size() - 1);
            } else {
                if (mCurrentIndex == mSongs.size() - 1) {
                    mCurrentIndex = 0;
                } else mCurrentIndex++;
            }
        }
        stopSong();
        playSong();
    }

    public void preSong() {
        if (mRepeat) {
            mPlayer.isLooping();
        } else {
            if (mRandom == true) {
                mCurrentIndex = new Random().nextInt(mSongs.size() - 1);
            } else {
                if (mCurrentIndex == 0) {
                    mCurrentIndex = mSongs.size() - 1;
                } else mCurrentIndex--;
            }
        }
        stopSong();
        playSong();
    }

    public void stopSong() {
        if (mState == MediaPlayerState.PLAYING || mState == MediaPlayerState.PAUSED) {
            mPlayer.stop();
            mPlayer.reset();
            mState = MediaPlayerState.STOPPED;
        }
    }

    public void randomSong() {
        if (mRandom == false) {
            mRandom = true;
        } else mRandom = false;
    }

    public void repeatSong() {
        if (mRepeat == false) {
            mRepeat = true;
        } else mRepeat = false;
    }

    public void playSong(int position) {
        mCurrentIndex = position;
        stopSong();
        playSong();
    }

    public void playingContinue(){
        if (mState == MediaPlayerState.PLAYING) {
            upDateUi();
            mPlayer.start();
            return;
        }if (mState == MediaPlayerState.PAUSED){
            mPlayer.pause();
            upDateUi();
            return;
        }
    }

    public void playSong() {
        Song song = mSongs.get(mCurrentIndex);
        String url = song.getStreamUrl();
        if (mState == MediaPlayerState.IDLE | mState == MediaPlayerState.STOPPED) {
            try {
                mPlayer.setDataSource(url);
                mPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mState = MediaPlayerState.PLAYING;
            upDateUi();
            return;
        }
        if (mState == MediaPlayerState.PLAYING) {
            mPlayer.pause();
            mState = MediaPlayerState.PAUSED;
            upDateUi();
            return;
        }
        mPlayer.start();
        mState = MediaPlayerState.PLAYING;
        upDateUi();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        nextSong();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    @Override
    public void upDateUi() {
        mOnListenerServer.upDateUi();
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    public void seekTo(SeekBar seekBar) {
        mPlayer.seekTo(seekBar.getProgress());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
