package vn.tien.tienmusic.ui.play;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.tien.tienmusic.R;
import vn.tien.tienmusic.constant.Constant;
import vn.tien.tienmusic.databinding.FragmentAvatarBinding;

public class AvatarFragment extends Fragment {
    private FragmentAvatarBinding mAvatarBinding;
    private CircleImageView mImageViewAvatar;
    private ObjectAnimator mAnimator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAvatarBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_avatar, container, false);
        initView();
        return mAvatarBinding.getRoot();
    }


    public void setAvatar(String url) {
        Glide.with(getContext()).load(url).placeholder(R.drawable.cd).
                into(mImageViewAvatar);
    }

    public void startAnimator(){
        mAnimator.resume();
    }

    private void initView() {
        mImageViewAvatar = mAvatarBinding.imageAvatar;
        mAnimator = ObjectAnimator.ofFloat(mImageViewAvatar, Constant.TYPE_ANIMATOR, 0f, 360f);
        mAnimator.setDuration(Constant.ANIMATOR_DURATION_TIME);
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.start();
    }

    public void pauseAnimator(){
        mAnimator.pause();
    }
}
