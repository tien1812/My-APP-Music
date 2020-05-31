package vn.tien.tienmusic.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import vn.tien.tienmusic.R;
import vn.tien.tienmusic.databinding.FragmentSearchBinding;
import vn.tien.tienmusic.ui.SearchActivity;

public class SearchFragment extends Fragment implements View.OnClickListener {
    private FragmentSearchBinding mSearchBinding;
    private Button mBtnSearch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mSearchBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container,
                false);
        return mSearchBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
        registerListener();
    }

    private void registerListener() {
        mBtnSearch.setOnClickListener(this);
    }

    private void initView() {
        mBtnSearch = mSearchBinding.btnSearch;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search:
                Intent intent = SearchActivity.getIntent(getContext());
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
