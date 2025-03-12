package com.xc.demo.scannersdkdemo;

import android.app.Activity;
import android.os.Handler;

import com.xc.demo.scannersdkdemo.scannerUtil.ScannerConnectState;
import com.xc.demo.scannersdkdemo.scannerUtil.ScannerHelper;

public class BaseActivity extends Activity {

    @Override
    protected void onResume() {
        super.onResume();

        if (ScannerHelper.getInstance().getConnectedState() == ScannerConnectState.CONNECTED) {
            onScannerConnectedComplete();
        } else if (ScannerHelper.getInstance().getConnectedState() == ScannerConnectState.CONNECTING) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Wait for 800ms to obtain the connection status
                    if (ScannerHelper.getInstance().getConnectedState() == ScannerConnectState.CONNECTED)
                        onScannerConnectedComplete();
                }
            }, 800);
        }
    }

    public void onScannerConnectedComplete() {
        // Service binding successful, API interface can now be called
    }
}
