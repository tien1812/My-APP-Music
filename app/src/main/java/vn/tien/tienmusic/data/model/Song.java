package vn.tien.tienmusic.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import vn.tien.tienmusic.utils.StringUtils;

@Entity(tableName = "song_table")
public class Song extends BaseObservable implements Parcelable {
    @NonNull
    @PrimaryKey
    @SerializedName("id")
    private long mId;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("duration")
    private int mDuration;

    @SerializedName("stream_url")
    private String mStreamUrl;

    @Embedded
    @SerializedName("user")
    private User mUser;

    @SerializedName("genre")
    private String mGenre;

    @SerializedName("track_type")
    private String mTrackType;

    @Bindable
    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    @Bindable
    public String getTitle() {
        return mTitle;
    }

    @Ignore
    public Song() {
    }

    public Song(long id) {
        mId = id;
    }

    @Ignore
    public Song(long id, String title, int duration, String streamUrl, User user, String genre,
                String trackType) {
        mId = id;
        mTitle = title;
        mDuration = duration;
        mStreamUrl = streamUrl;
        mUser = user;
        mGenre = genre;
        mTrackType = trackType;
    }

    public Song(long id, String title, int duration, String uri, User user) {
        mId = id;
        mTitle = title;
        mDuration = duration;
        mStreamUrl = uri;
        mUser = user;
    }

    @Bindable
    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    @Bindable
    public int getDuration() {
        return mDuration;
    }

    public String formatDuration(int duration) {
        return StringUtils.formatDuration(duration);
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    @Bindable
    public String getStreamUrl() {
        return mStreamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        mStreamUrl = streamUrl;
    }

    public String getGenre() {
        return mGenre;
    }

    public void setGenre(String genre) {
        mGenre = genre;
    }

    public String getTrackType() {
        return mTrackType;
    }

    public void setTrackType(String trackType) {
        mTrackType = trackType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Song> CREATOR = new Parcelable.Creator<Song>() {

        @Override
        public Song createFromParcel(Parcel parcel) {
            return new Song(parcel);
        }

        @Override
        public Song[] newArray(int i) {
            return new Song[i];
        }
    };

    //thu tu doc va viet phai giong nhau
    private Song(Parcel in) {
        mId = in.readLong();
        mTitle = in.readString();
        mGenre = in.readString();
        mStreamUrl = in.readString();
        mTrackType = in.readString();
        mDuration = in.readInt();
        mUser = in.readParcelable(User.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(mId);
        parcel.writeString(mTitle);
        parcel.writeString(mGenre);
        parcel.writeString(mStreamUrl);
        parcel.writeString(mTrackType);
        parcel.writeInt(mDuration);
        parcel.writeParcelable(mUser, i);
    }
}
