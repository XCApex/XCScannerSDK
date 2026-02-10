package com.xc.demo.scannersdkdemo.scannerUtil;

import android.os.Handler;
import android.util.Log;

import com.tools.ScanInfo;
import com.xc.demo.scannersdkdemo.BaseApplication;
import com.xcheng.scanner.ScannerSymResult;
import com.xcheng.scanner.XcBarcodeScanner;

import java.util.ArrayList;

public class ScannerHelper implements ScannerResultSubject, ScannerSymResult {

    private static final String TAG = "ScannerHelper";
    private ScannerConnectState connectState = ScannerConnectState.DISCONNECTED;
    private final ArrayList<ScannerResultObserver> observers;

    private static class ScannerHolder {
        private static final ScannerHelper INSTANCE = new ScannerHelper();
    }

    private ScannerHelper() {
        observers = new ArrayList<>();
    }

    public static ScannerHelper getInstance() {
        return ScannerHolder.INSTANCE;
    }

    /**
     * Obtain the decoding result
     *
     * @param sym
     * @param content
     */
    @Override
    public void onResult(String sym, String content) {
        Log.i(TAG, "[onResult] " + sym + " : " + content);
        notifyObservers(sym, content);
    }

    @Override
    public void attach(ScannerResultObserver observer) {
        if (observer == null)
            throw new NullPointerException("Observer cannot be null");
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
        Log.i(TAG, "[attach] observers.size = " + observers.size());
    }

    @Override
    public void detach(ScannerResultObserver observer) {
        if (observer == null)
            return;
        if (observers.contains(observer)) {
            observers.remove(observer);
        }
        Log.i(TAG, "[detach] observers.size = " + observers.size());
    }

    @Override
    public void notifyObservers(String symbol, String content) {
        ScannerResultObserver[] scanObservers;
        synchronized (this) {
            scanObservers = observers.toArray(new ScannerResultObserver[observers.size()]);
        }
        for (ScannerResultObserver observer : scanObservers) {
            observer.onResult(symbol, content);
        }
    }

    public void clearObservers() {
        observers.clear();
    }

    /**
     * SDK initialization
     */
    public void initScannerSdk() {
        setConnectedState(ScannerConnectState.CONNECTING);
        XcBarcodeScanner.init(BaseApplication.getContext(), this);
        // Binding service takes time, update binding status after 500ms delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setConnectedState(ScannerConnectState.CONNECTED);
            }
        }, 500);
    }

    /**
     * SDK deinitialization
     */
    public void deInitScannerSdk() {
        XcBarcodeScanner.deInit(BaseApplication.getContext());
        setConnectedState(ScannerConnectState.DISCONNECTED);
    }

    /**
     * Check if the SDK version corresponds to the service version number.
     * https://github.com/XCApex/XCScannerSDK/blob/movfast/docs/SDK_Service_Mapping_Table.md
     *
     * @return true:Version matching, you can continue to use this SDK;
     * false:Version mismatch.
     */
    public boolean checkSdkVersionMatches() {
        String sdkVer = getSDKVersion();
        String serviceVer = getServiceVersion();

        if (sdkVer == null || serviceVer == null || sdkVer.isEmpty() || serviceVer.isEmpty()) {
            Log.e(TAG, "Failed to obtain version information.");
            return false;
        }

        Log.i(TAG, "Service version = " + serviceVer + " -> sdk version = " + sdkVer);
        if (sdkVer.equals("1.1.8")) {
            return checkServiceVersionMatches(serviceVer, "1.3.40.1");
        } else if (sdkVer.equals("1.1.9")) {
            return checkServiceVersionMatches(serviceVer, "1.3.40.2.1");
        } else if (sdkVer.equals("1.1.3")) {
            return checkServiceVersionMatches(serviceVer, "1.3.7");
        } else if (sdkVer.equals("1.1.2")) {
            return checkServiceVersionMatches(serviceVer, "1.3.5");
        } else if (sdkVer.equals("1.1.0")) {
            return checkServiceVersionMatches(serviceVer, "1.3.1");
        } else if (sdkVer.equals("1.1.10")) {
            return serviceVer.equals("1.3.40.2.2");
        } else if (sdkVer.equals("1.1.11")) {
            return serviceVer.equals("1.3.40.2.3");
        } else if (sdkVer.equals("1.1.12") || sdkVer.equals("1.1.13")) {
            return serviceVer.equals("1.3.40.2.4")
                    || checkServiceVersionMatches(serviceVer, "1.3.46.2");
        } else if(sdkVer.equals("1.1.14")){
            return serviceVer.equals("1.3.46.7");
        } else if(sdkVer.equals("1.1.15")){
            return serviceVer.equals("1.3.40.2.6");
        } else if(sdkVer.equals("1.1.19")){
            return serviceVer.compareTo("1.3.49.0.13")>=0;
        } else if(sdkVer.compareTo("1.3.49.0.13")>=0){
            return serviceVer.compareTo(sdkVer)>=0;
        } else {
            Log.e(TAG, "DO NOT USE ANY SDK WITHOUT VERSION LISTED IN THE TABLE!!!");
            return false;
        }
    }

    /**
     * High version service, compatible with lower version SDK
     */
    private boolean checkServiceVersionMatches(String currentVersion, String compareVersions) {
        String[] aList = currentVersion.split("\\.");
        String[] bList = compareVersions.split("\\.");
        int length = Math.max(bList.length, aList.length);
        boolean isMatch = true;
        for (int i = 0; i < length; i++) {
            int aIndexCode = aList.length <= i ? 0 : Integer.parseInt(aList[i]);
            int bIndexCode = bList.length <= i ? 0 : Integer.parseInt(bList[i]);
            if (aIndexCode != bIndexCode) {
                isMatch = aIndexCode > bIndexCode;
                break;
            }
        }
        Log.i(TAG, "currentVersion = " + currentVersion
                + ", compareVersions = " + compareVersions
                + ", isMatch = " + isMatch);
        return isMatch;
    }

    private void setConnectedState(ScannerConnectState state) {
        if (connectState != state) {
            Log.e(TAG, "setConnectedState:: [" + connectState + "] -> [" + state + "]");
            connectState = state;
        }
    }

    public ScannerConnectState getConnectedState() {
        return connectState;
    }

    /**
     * Get SDK version number
     *
     * @return SDK version number
     */
    public String getSDKVersion() {
        return XcBarcodeScanner.getSdkVersion(BaseApplication.getContext());
    }

    /**
     * Get service version number
     *
     * @return service version number
     */
    public String getServiceVersion() {
        return XcBarcodeScanner.getServiceVersion();
    }
}
