package vn.tien.tienmusic.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.tien.tienmusic.R;
import vn.tien.tienmusic.constant.ClickListenerItem;
import vn.tien.tienmusic.data.model.Song;
import vn.tien.tienmusic.databinding.ItemTrackBinding;


public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackHoder> {
    private List<Song> mSongs;
    private static ClickListenerItem sClickListener;
    private ItemTrackBinding mTrackBinding;

    public TrackAdapter() {
    }

    public void setData(List<Song> songs) {
        mSongs = songs;
        notifyDataSetChanged();
    }

    public Song getSongAtPosition(int position) {
        return mSongs.get(position);
    }


    public void setClickListener(ClickListenerItem clickListener) {
        sClickListener = clickListener;
    }

    @NonNull
    @Override
    public TrackHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        mTrackBinding = DataBindingUtil.inflate(layoutInflater,
                R.layout.item_track, parent, false);
        return new TrackHoder(mTrackBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackHoder holder, int position) {
        Song song = mSongs.get(position);
        holder.bind(song);
    }

    @Override
    public int getItemCount() {
        return mSongs == null ? 0 : mSongs.size();
    }

    static class TrackHoder extends RecyclerView.ViewHolder {
        private final ItemTrackBinding mBinding;


        public TrackHoder(final ItemTrackBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = getAdapterPosition();
                    if (sClickListener != null) {
                        sClickListener.onClick(mBinding.getSong(), position);
                    }
                }
            });
        }


        public void bind(Song song) {
            mBinding.setSong(song);
            mBinding.executePendingBindings();
        }
    }
}
