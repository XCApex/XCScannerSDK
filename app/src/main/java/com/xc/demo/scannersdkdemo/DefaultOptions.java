package com.xc.demo.scannersdkdemo;

//import com.xcheng.scanner.ScanTriggerMode;
import com.xc.demo.scannersdkdemo.XCBarcodeTag;

public class DefaultOptions {
    public static final int DEFAULT_SCAN_TIMEOUT_VAL = 2; // 2 seconds

    public static final String DEFAULT_TRIGGER_MODE_VAL = ScanTriggerMode.STOP_ON_RELEASE;

    public static final String DEFAULT_DATA_RECEIVE_METHOD_VAL = "BROADCAST_EVENT/CLIPBOARD_EVENT"; // Output barcode data via broadcast and clipboard simulate

    public static final int DEFAULT_MULTI_NUM_VAL = 1;

    public static final boolean DEFAULT_EXACTLY_NUM_VAL = false;

    public static final int DEFAULT_VIEW_SIZE_VAL = 0; // 100%

    public static final int DEFAULT_LOOP_INTERVAL_VAL = 1000; // 1 second

    public static final String KEY_CUSTOM_BROADCAST_ACTION = "custom_broadcast_action";
    public static final String DEFAULT_CUSTOM_BROADCAST_ACTION_VAL = "custom.broadcast.action";

    public static final String KEY_CUSTOM_BROADCAST_KEY = "custom_broadcast_key";
    public static final String DEFAULT_CUSTOM_BROADCAST_KEY_VAL = "custom.broadcast.key";

    public static final String DEFAULT_SUCCESS_NOTIFICATION_VAL = "Sound";

    public static final String DEFAULT_FAIL_NOTIFICATION_VAL = "Mute";

    public static final String DEFAULT_NOTIFICATION_VOLUME_VAL = "100%";

    public static final Boolean DEFAULT_LED_NOTIFICATION_VAL = true;

    public static final int DEFAULT_AIM_MODE_VAL = 1; // TurnOn while scanning.

    public static final int DEFAULT_ILLUME_MODE_VAL = 2; // Illume and Strobe.

    public static final int DEFAULT_BRIGHTNESS_VAL = 5; // WEAK_BRIGHTNESS

    public static final boolean DEFAULT_LEFT_SCAN_ENABLE_VAL = true;

    public static final String DEFAULT_PREFIX_VAL = "Empty";

    public static final String DEFAULT_SUFFIX_VAL = "Empty";

    public static final String DEFAULT_LETTER_CASE_VAL = "NONE_CASE"; // not convert uppercase and lowercase letters
}
