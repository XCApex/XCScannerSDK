package com.xc.demo.scannersdkdemo;

import android.app.Application;
import android.content.Context;

import com.xc.demo.scannersdkdemo.scannerUtil.ScannerHelper;

public class BaseApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        // sdk initialization
        ScannerHelper.getInstance().initScannerSdk();
    }

    public static Context getContext() {
        return context;
    }
}
