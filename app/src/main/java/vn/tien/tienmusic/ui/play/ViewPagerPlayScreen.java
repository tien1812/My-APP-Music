package vn.tien.tienmusic.ui.play;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerPlayScreen extends FragmentPagerAdapter {
    private final List<Fragment> mFragments = new ArrayList<>();

    public void addFragment(Fragment fragment) {
        mFragments.add(fragment);
    }

    public ViewPagerPlayScreen(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }

}
