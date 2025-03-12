package com.xc.demo.scannersdkdemo;

import android.app.Fragment;
import android.content.Context;

public class BaseFragment extends Fragment {

    private OnConfigChangedListener listener;

    public interface OnConfigChangedListener {
        void onConfigChanged(String key, Object obj);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnConfigChangedListener) {
            listener = (OnConfigChangedListener) context;
        }
    }

    public void notifyConfigChanged(String key, Object obj) {
        if (listener != null) {
            listener.onConfigChanged(key, obj);
        }
    }
}
