package vn.tien.tienmusic.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.room.ColumnInfo;
import androidx.room.Ignore;

import com.bumptech.glide.Glide;
import com.google.gson.annotations.SerializedName;

import vn.tien.tienmusic.R;

public class User extends BaseObservable implements Parcelable {
    @SerializedName("id")
    @ColumnInfo(name = "ID")
    private long mID;

    @ColumnInfo(name = "username")
    @SerializedName("username")
    private String mUserName;

    @ColumnInfo(name = "avatar")
    @SerializedName("avatar_url")
    private String mAvatarUrl;

    @Ignore
    public User() {
    }

    public User(String userName) {
        mUserName = userName;
    }

    public long getID() {
        return mID;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public void setID(long ID) {
        mID = ID;
    }


    @Bindable
    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    @Bindable
    public String getAvatarUrl() {
        return mAvatarUrl;
    }

    @BindingAdapter({"mAvatarUrl"})
    public static void loadImage(ImageView view, String imagUrl) {
        Glide.with(view.getContext()).load(imagUrl).placeholder(R.drawable.cd).into(view);
    }

    public void setAvatarUrl(String avatarUrl) {
        mAvatarUrl = avatarUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mAvatarUrl);
        parcel.writeString(mUserName);
        parcel.writeLong(mID);
    }

    private User(Parcel in) {
        mAvatarUrl = in.readString();
        mUserName = in.readString();
        mID = in.readLong();
    }

}
