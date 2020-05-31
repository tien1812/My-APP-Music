package vn.tien.tienmusic.data.source;

import android.content.Context;

public class MySongManager {
    private static MySongManager sMySongManager;
    private Context mContext;

    public MySongManager(Context context) {
        mContext = context;
    }

    public static MySongManager getInstance(Context context){
        if (sMySongManager == null){
            sMySongManager = new MySongManager(context);
        }
        return sMySongManager;
    }
}
