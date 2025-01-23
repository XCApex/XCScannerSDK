package com.xc.demo.scannersdkdemo.pages;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.tools.XCImage;
import com.xc.demo.scannersdkdemo.BaseActivity;
import com.xc.demo.scannersdkdemo.BaseFragment;
import com.xc.demo.scannersdkdemo.DefaultOptions;
import com.xc.demo.scannersdkdemo.R;
import com.xc.demo.scannersdkdemo.scannerUtil.ScannerHelper;
import com.xc.demo.scannersdkdemo.scannerUtil.ScannerResultObserver;
import com.xc.demo.scannersdkdemo.tools.AlertDialogUtils;
import com.xc.demo.scannersdkdemo.tools.PictureDialog;
import com.xc.demo.scannersdkdemo.tools.Utils;
import com.xcheng.scanner.LicenseState;
//import com.xcheng.scanner.ScanTriggerMode;
import com.xc.demo.scannersdkdemo.ScanTriggerMode;
import com.xcheng.scanner.XcBarcodeScanner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, View.OnTouchListener, AdapterView.OnItemSelectedListener, ScannerResultObserver, BaseFragment.OnConfigChangedListener {

    private static final String TAG = "XCScannerSDK_MainActivity";
    private long mLastBackPressedTime = 0;
    private FunctionOptionsFragment mFunctionOptionsFragment;
    private BarcodeOptionsFragment mBarcodeOptionsFragment;

    private Button mBtnActiveLicense, mBtnScan, mBtnGetLastImg;
    private ToggleButton mTbScanServerControl;
    private Spinner mSpScanMode;
    private TextView mTextResult;
    private String mAllResult = "";
    private boolean isLoopScanMode = false;

    // Custom Broadcast
    private String mScanResultReceiverAction = DefaultOptions.DEFAULT_CUSTOM_BROADCAST_ACTION_VAL;
    private String mScanResultReceiverKey = DefaultOptions.DEFAULT_CUSTOM_BROADCAST_KEY_VAL;
    private CustomBroadcastResultReceiver mBroadcastResultReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        mFunctionOptionsFragment = new FunctionOptionsFragment();
        mBarcodeOptionsFragment = new BarcodeOptionsFragment();

        // Register to receive broadcast of scan results
        registerBroadcastResultReceiver();
    }

    private void initView() {
        mBtnActiveLicense = findViewById(R.id.btn_active_license);
        mTbScanServerControl = findViewById(R.id.tb_scan_server_control);
        mBtnScan = findViewById(R.id.btn_scan);
        mBtnGetLastImg = findViewById(R.id.btn_get_last_img);
        mTextResult = findViewById(R.id.txt_result);
        mSpScanMode = findViewById(R.id.sp_scan_mode);

        mBtnActiveLicense.setOnClickListener(this::onClick);
        mTbScanServerControl.setOnCheckedChangeListener(this::onCheckedChanged);
        mBtnScan.setOnTouchListener(this::onTouch);
        mBtnGetLastImg.setOnClickListener(this::onClick);
        mSpScanMode.setOnItemSelectedListener(this);

        mTextResult.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    @Override
    public void onScannerConnectedComplete() {
        super.onScannerConnectedComplete();
        // When the SDK version does not match the service version,
        // it is recommended to immediately terminate subsequent interface calls
        if (!ScannerHelper.getInstance().checkSdkVersionMatches()) {
            Log.e(TAG, "SDK version does not match the service version." +
                    "Continuing to use may pose a risk of crashing.");
            AlertDialogUtils.showSimpleDialog(MainActivity.this, getResources().getString(R.string.version_match)
                    , getResources().getString(R.string.version_match_error), "OK", null);
        }

        // Attach the observer to receive the result of the scanning
        ScannerHelper.getInstance().attach(this);

        // Set the custom broadcast action and key
        // Barcode data output mode' must include the 'Broadcast' output method
        XcBarcodeScanner.setScanResultBroadcast(mScanResultReceiverAction, mScanResultReceiverKey);

        // Get SDK version and service version
        String sdkVer = ScannerHelper.getInstance().getSDKVersion();
        String serviceVer = ScannerHelper.getInstance().getServiceVersion();
        String appVer = Utils.getAppVersionName();

        // Get license state
        String licMsg = getLicenseState();

        // Get current scan service state
        onScanServiceStateChanged();

        // Display the SDK version and service version and license state
        mAllResult = mAllResult + "\n" +
                "APP ver: " + appVer + "\n" +
                "SDK ver: " + sdkVer + "\n" +
                "Service ver: " + serviceVer + "\n" +
                licMsg;
        mTextResult.setText(mAllResult);
        scrollToBottom();
    }

    /**
     * Get the activation status of the decoding library.
     * If it is not activated, manual activation is required.
     */
    private String getLicenseState() {
        int licState = XcBarcodeScanner.getLicenseState();
        String licMsg = "";
        switch (licState) {
            case LicenseState.INACTIVE:
                licMsg = "Need to active license firstly!";
                mBtnActiveLicense.setEnabled(true);
                break;

            case LicenseState.ACTIVATING:
                licMsg = "License activating...";
                mBtnActiveLicense.setEnabled(false);
                break;

            case LicenseState.ACTIVED:
                licMsg = "License actived, be happy!";
                mBtnActiveLicense.setEnabled(false);
                break;

            case LicenseState.INVALID:
                licMsg = "License invalid, check with vendor please!";
                mBtnActiveLicense.setEnabled(true);
                break;

            case LicenseState.NETWORK_ISSUE:
                licMsg = "Need network to active license!";
                mBtnActiveLicense.setEnabled(true);
                break;

            case LicenseState.EXPIRED:
                licMsg = "License expired, check with vendor please!";
                mBtnActiveLicense.setEnabled(true);
                break;
        }
        return licMsg;
    }

    /**
     * Get the current running status of the scanning service
     */
    private void onScanServiceStateChanged() {
        boolean isServiceSuspending = XcBarcodeScanner.isScanServiceSuspending();
        if (isServiceSuspending) { // Service suspended, unable to trigger scan code
            AlertDialogUtils.showSimpleDialog(MainActivity.this, "Service state:", "Scan service is suspending, resume it firstly.", "OK", null);
            mBtnScan.setEnabled(false);
            mTbScanServerControl.setChecked(true);
        } else { // Service recovery, can trigger scanning code
            mBtnScan.setEnabled(true);
            mTbScanServerControl.setChecked(false);
        }
    }

    /**
     * Obtain scanning results
     *
     * @param symbol  Barcode Type
     * @param content
     */
    @Override
    public void onResult(String symbol, String content) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAllResult = mAllResult + "\n" + symbol + " : " + content;
                mTextResult.setText(mAllResult);
                scrollToBottom();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (!v.isPressed()) return;
        if (v.getId() == R.id.btn_active_license) {
            // Activate decoding library
            XcBarcodeScanner.activateLicense();
        } else if (v.getId() == R.id.btn_get_last_img) {
            // Obtain decoded images
            getLastDecodeImage();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!buttonView.isPressed()) return;
        if (buttonView.getId() == R.id.tb_scan_server_control) {
            Log.e(TAG, "ScanServerControl onCheckedChanged:: " + isChecked);
            if (isChecked) {
                // Suspend scanning service
                XcBarcodeScanner.suspendScanService();
                mBtnScan.setEnabled(false);
            } else {
                // Resume scanning service
                XcBarcodeScanner.resumeScanService();
                mBtnScan.setEnabled(true);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Log.d(TAG,"isLoopScanMode = " + isLoopScanMode);
            if (isLoopScanMode) {
                // If loop scan enabled, we use only ACTION_DOWN to control start or stop.
                if (XcBarcodeScanner.isLoopScanRunning()) {
                    XcBarcodeScanner.stopLoopScan();
                } else {
                    // set loop scan interval
                    XcBarcodeScanner.setLoopScanInterval(DefaultOptions.DEFAULT_LOOP_INTERVAL_VAL);
                    XcBarcodeScanner.startLoopScan();
                }
            } else {
                // Trigger scanning action
                XcBarcodeScanner.startScan();
            }
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (isLoopScanMode) {
                // If loop scan enabled, not need process ACTION_UP event.
            } else {
                // Stop scanning action
                if (XcBarcodeScanner.getScanTriggerMode().equals(ScanTriggerMode.STOP_ON_RELEASE)) {
                    XcBarcodeScanner.stopScan();
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.sp_scan_mode) {
            onScanModeChanged(position == 1);

            Log.d(TAG,"position = " + position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void onScanModeChanged(boolean isLoopScan) {
        if (isLoopScanMode == isLoopScan) return;

        // Stop scanning firstly, then change scan mode
        XcBarcodeScanner.stopScan();
        if (XcBarcodeScanner.isLoopScanRunning()) {
            XcBarcodeScanner.stopLoopScan();

            Log.d(TAG, "demo stopLoopScan()");
        }

        isLoopScanMode = isLoopScan;
        if (isLoopScan) {
            mBtnScan.setText(R.string.scanloop);
        } else {
            mBtnScan.setText(R.string.scan);
        }
    }

    private void scrollToBottom() {
        try {
            int offset = mTextResult.getLayout().getLineTop(mTextResult.getLineCount()) + mTextResult.getCompoundPaddingTop() + mTextResult.getCompoundPaddingBottom();
            if (offset > mTextResult.getHeight()) {
                mTextResult.scrollTo(0, offset - mTextResult.getHeight());
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtain decoded images
     */
    private void getLastDecodeImage() {
        String imageStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + File.separator + "demoImgSave";
        File storageDir = new File(imageStorageDir + File.separator);
        if (!storageDir.exists()) {
            Log.d(TAG, "Dir not exist, create" + storageDir);
            storageDir.mkdirs();
        }

        XCImage lastImg = XcBarcodeScanner.getLastDecodeImage();
        if (lastImg != null) {
            int width = lastImg.getWidth();
            int height = lastImg.getHeight();
            int stride = lastImg.getStride();

            byte[] data = lastImg.getData();
            if (data != null) {
                Bitmap rawBmp = Utils.raw8ToBitmap(data, width, height);
                Bitmap bmp = Utils.rotateImageView(180, rawBmp);
                String imgFilePath = imageStorageDir + File.separator + "lastImage_w" + width + "_h" + height + "_s" + stride + ".png";
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, bos);
                byte[] bmpData = bos.toByteArray();

                try {
                    FileOutputStream imgFo = new FileOutputStream(imgFilePath);
                    imgFo.write(bmpData);
                    imgFo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String infoStr = "W=" + lastImg.getWidth() + ", " +
                        "H=" + lastImg.getHeight() + ", " +
                        "Size=" + lastImg.getData().length + " Bytes";
                infoStr += "\n" + "Image path: " + imgFilePath;
                PictureDialog pictureDialog = new PictureDialog(this, "Image Info:", infoStr, bmp);
                pictureDialog.show();
            } else {
                AlertDialogUtils.showSimpleDialog(MainActivity.this, "", "No image!", "OK", null);
            }
        } else {
            AlertDialogUtils.showSimpleDialog(MainActivity.this, "", "No image!", "OK", null);
        }
    }

    @Override
    public void onConfigChanged(String key, Object obj) {
        Log.i(TAG, "[onConfigChanged] key: " + key + ", obj: " + obj);
        if (key.equals(DefaultOptions.KEY_CUSTOM_BROADCAST_ACTION)) {
            mScanResultReceiverAction = (String) obj;
            registerBroadcastResultReceiver();
        } else if (key.equals(DefaultOptions.KEY_CUSTOM_BROADCAST_KEY)) {
            mScanResultReceiverKey = (String) obj;
            registerBroadcastResultReceiver();
        }
    }

    /**
     * Customize a broadcast receiver to receive scan results
     */
    private class CustomBroadcastResultReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) return;
            String action = intent.getAction();
            Log.i(TAG, "[CustomBroadcastResultReceiver]action = " + action);
            if (mScanResultReceiverAction.equals(action)) {
                mAllResult = mAllResult + "\nbroadcast results:" + intent.getStringExtra(mScanResultReceiverKey);
                mTextResult.setText(mAllResult);
                scrollToBottom();
            }
        }
    }

    private void registerBroadcastResultReceiver() {
        try {
            if (mBroadcastResultReceiver != null)
                unregisterReceiver(mBroadcastResultReceiver);
            mBroadcastResultReceiver = new CustomBroadcastResultReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(mScanResultReceiverAction);
            registerReceiver(mBroadcastResultReceiver, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_barcode_options) {
            showFragment(mBarcodeOptionsFragment);
            return true;
        } else if (id == R.id.action_function_options) {
            showFragment(mFunctionOptionsFragment);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAllResult = "";
        // Detach the observer to avoid memory leaks
        ScannerHelper.getInstance().detach(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // unregister
        try {
            if (mBroadcastResultReceiver != null)
                unregisterReceiver(mBroadcastResultReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (mFunctionOptionsFragment.isVisible()) {
            hideFragment(mFunctionOptionsFragment);
        } else if (mBarcodeOptionsFragment.isVisible()) {
            hideFragment(mBarcodeOptionsFragment);
        } else {
            long currentTime = System.currentTimeMillis();
            if (currentTime - mLastBackPressedTime < 2000) {
                super.onBackPressed();
            } else {
                mLastBackPressedTime = currentTime;
            }
        }
    }

    private void showFragment(Fragment fragment) {
        if (fragment == null) return;
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (!fragment.isAdded()) {
            fragmentTransaction.add(R.id.fragment_container, fragment);
        }
        fragmentTransaction.show(fragment);
        fragmentTransaction.commit();
    }

    private void hideFragment(Fragment fragment) {
        if (fragment == null) return;
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(fragment);
        fragmentTransaction.commit();
    }
}