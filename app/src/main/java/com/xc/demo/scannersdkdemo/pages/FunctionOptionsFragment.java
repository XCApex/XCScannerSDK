package com.xc.demo.scannersdkdemo.pages;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.xc.demo.scannersdkdemo.BaseFragment;
import com.xc.demo.scannersdkdemo.DefaultOptions;
import com.xc.demo.scannersdkdemo.R;
import com.xc.demo.scannersdkdemo.tools.AlertDialogUtils;
import com.xcheng.scanner.XcBarcodeScanner;

import java.util.Arrays;

public class FunctionOptionsFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "XCScannerSDK_FunctionOptions";

    private Spinner mSpTimeOut, mSpTriggerMode, mSpDataReceiveMethod, mSpMultiBarcodeNum, mSpViewSize;
    private Switch mSwExactlyMultiNum;

    private LinearLayout mLyBroadcastAction, mLyBroadcastKey;
    private TextView mTvBroadcastAction, mTvBroadcastKey;

    private Spinner mSpSuccessNotification, mSpFailNotification, mSpNotificationVolume;

    private Switch mSwLedNotification;

    private Spinner mSpAimEnable, mSpIllumeEnable, mSpBrightness;

    private Switch mSwLeftScanEnable, mUseAimidInResult, mScanWhiteList;

    private Spinner mSpPrefixChar, mSpSuffixChar, mSpLetterCase, gsCharEntries;
    private Button mBtnExport, mBtnImport, mBtWhiteListGet, mBtWhiteListAdd, mBtWhiteListDel, but_pcharacter_custom;
    private EditText mEtWhiteList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_funtion_options, container, false);
        initView(rootView);
        return rootView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String funName = "onItemSelected";
        if (parent.getId() == R.id.sp_timeout) {
            int[] timeOut = getResources().getIntArray(R.array.scan_timeout_values);
            Log.i(TAG, funName + ":: timeOut = " + timeOut[position]);
            // set scan timeout
            XcBarcodeScanner.setTimeout(timeOut[position]);
        } else if (parent.getId() == R.id.sp_trigger_mode) {
            String[] triggerMode = getResources().getStringArray(R.array.scan_trigger_mode_values);
            Log.i(TAG, funName + ":: triggerMode = " + triggerMode[position]);
            // set scan trigger mode
            XcBarcodeScanner.setScanTriggerMode(triggerMode[position]);
        } else if (parent.getId() == R.id.sp_data_receive_method) {
            String[] dataReceiveMethod = getResources().getStringArray(R.array.data_receive_method_values);
            Log.i(TAG, funName + ":: dataReceiveMethod = " + dataReceiveMethod[position]);
            // set data receive method
            XcBarcodeScanner.setOutputMethod(dataReceiveMethod[position]);
        } else if (parent.getId() == R.id.sp_multi_barcode_num) {
            int[] multiBarcodeNum = getResources().getIntArray(R.array.multibarcodes_number_values);
            int num = multiBarcodeNum[position];
            boolean isExactlyNum = mSwExactlyMultiNum.isChecked();
            Log.i(TAG, funName + ":: multiBarcodeNum = " + num + " , isExactlyNum = " + isExactlyNum);
            // set multi-barcode number and exactness
            XcBarcodeScanner.setMultiBarcodes(num, isExactlyNum);
        } else if (parent.getId() == R.id.sp_view_size) {
            int[] viewSize = getResources().getIntArray(R.array.scan_viewsize_values);
            Log.i(TAG, funName + ":: viewSize = " + viewSize[position]);
            // set scan view size
            // If multiple barcode recognition is enabled, it is recommended to use a recognition range of 100% frame size
            XcBarcodeScanner.setScanRegionSize(viewSize[position]);
        } else if (parent.getId() == R.id.sp_success_notification) {
            String[] successNotification = getResources().getStringArray(R.array.scan_notification_values);
            Log.i(TAG, funName + ":: successNotification = " + successNotification[position]);
            // set success notification
            XcBarcodeScanner.setSuccessNotification(successNotification[position]);

            Log.d(TAG, "sp_success_notification");
        } else if (parent.getId() == R.id.sp_fail_notification) {
            String[] failNotification = getResources().getStringArray(R.array.scan_notification_values);
            Log.i(TAG, funName + ":: failNotification = " + failNotification[position]);
            // set fail notification
            XcBarcodeScanner.setFailNotification(failNotification[position]);

            Log.d(TAG, "sp_fail_notification");

        } else if (parent.getId() == R.id.scan_notification_volume) {
            String[] notificationVolume = getResources().getStringArray(R.array.scan_notification_volume_entries);
//            Log.d(TAG, funName + ":: setScanVolume = " + notificationVolume[position]);

            int volume = Integer.parseInt(notificationVolume[position].replace("%", ""));
            Log.d(TAG, "volume = " + volume / 100f);
            XcBarcodeScanner.setScanVolume(volume / 100f);
            Log.d(TAG, "scan_notification_volume");

        } else if (parent.getId() == R.id.sp_aim_enable) {
            int[] aimMode = getResources().getIntArray(R.array.aim_lights_values);
            Log.i(TAG, funName + ":: aimMode = " + aimMode[position]);
            // set aim mode
            XcBarcodeScanner.setAimerLightsMode(aimMode[position]);
        } else if (parent.getId() == R.id.sp_illume_enable) {
            int[] illuminationMode = getResources().getIntArray(R.array.illume_lights_values);
            Log.i(TAG, funName + ":: illuminationMode = " + illuminationMode[position]);
            // set illumination mode
            XcBarcodeScanner.setFlashLightsMode(illuminationMode[position]);
        } else if (parent.getId() == R.id.sp_brightness) {
            int[] brightness = getResources().getIntArray(R.array.strobe_brightness_values);
            Log.i(TAG, funName + ":: brightness = " + brightness[position]);
            // set strobe light brightness
            XcBarcodeScanner.setStrobeLightBrightness(brightness[position]);
        } else if (parent.getId() == R.id.sp_prefix_char) {
            String[] prefix = getResources().getStringArray(R.array.prefix_char_entries);
            Log.i(TAG, funName + ":: prefix = " + prefix[position]);
            // set result prefix
            XcBarcodeScanner.setTextPrefix(prefix[position]);
        } else if (parent.getId() == R.id.sp_suffix_char) {
            String[] suffix = getResources().getStringArray(R.array.suffix_char_entries);
            Log.i(TAG, funName + ":: suffix = " + suffix[position]);
            // set result suffix
            XcBarcodeScanner.setTextSuffix(suffix[position]);
        } else if (parent.getId() == R.id.sp_letter_case) {
            String[] letterCase = getResources().getStringArray(R.array.letter_case_values);
            Log.i(TAG, funName + ":: letterCase = " + letterCase[position]);
            // Convert the scan results to uppercase and lowercase
            XcBarcodeScanner.setTextCase(letterCase[position]);
        } else if (parent.getId() == R.id.gs_char_entries) {
            String[] gsCharvalues = getResources().getStringArray(R.array.gs_char_values);
            Log.d(TAG, funName + ":: gsCharvalues = " + gsCharvalues[position]);
            XcBarcodeScanner.escapeSingleCharacterSettings(gsCharvalues[position]);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.d("bo.li", "isChecked = " + isChecked);
        String funName = "onCheckedChanged";
        if (buttonView.getId() == R.id.sw_exactly_multi_num) {
            int numIndex = mSpMultiBarcodeNum.getSelectedItemPosition();
            int[] multiBarcodeNum = getResources().getIntArray(R.array.multibarcodes_number_values);
            int num = multiBarcodeNum[numIndex];
            Log.i(TAG, funName + ":: multiBarcodeNum = " + num + " , isExactlyNum = " + isChecked);
            // set multi-barcode number and exactness
            XcBarcodeScanner.setMultiBarcodes(num, isChecked);
        } else if (buttonView.getId() == R.id.sw_led_notification) {
            Log.i(TAG, funName + ":: ledNotification = " + isChecked);
            // set LED notification
            XcBarcodeScanner.enableSuccessIndicator(isChecked);
        } else if (buttonView.getId() == R.id.sw_left_scan_enable) {
            Log.i(TAG, funName + ":: leftScanKeyEnable = " + isChecked);
            XcBarcodeScanner.setLeftScanKeyEnable(isChecked);
        } else if (buttonView.getId() == R.id.pref_use_aimid_in_result) {
            Log.i(TAG, funName + ":: pref_use_aimid_in_result = " + isChecked);
            XcBarcodeScanner.setUseAimidInResult(isChecked);
        } else if (buttonView.getId() == R.id.pref_whitelist) {
            Log.i(TAG, funName + ":: pref_whitelist = " + isChecked);
            Log.d("syg", funName + ":: pref_whitelist = " + isChecked);
            XcBarcodeScanner.setScanWhiteListEnable(isChecked);
        } else if (buttonView.getId() == R.id.pref_character_modify_enable) {
            Log.d(TAG, funName + ":: pref_character_modify_enable = " + isChecked);
            XcBarcodeScanner.needModifyGsCharacter(isChecked);
        } else if (buttonView.getId() == R.id.pref_enble_character_custom) {
            Log.d(TAG, funName + ":: pref_enble_character_custom = " + isChecked);
            XcBarcodeScanner.customEscapeCharacters(isChecked);
        }
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.ly_broadcast_action) {
            setBroadcastActionDialog();
        } else if (viewId == R.id.ly_broadcast_key) {
            setBroadcastKeyDialog();
        } else if (viewId == R.id.btn_export) {
            String exportPath = Environment.getExternalStorageDirectory().getPath() + "/XCScannerSDK.xml";
            // Export the configuration file to sdcard and rename it to XCScannerSDK.xml
            XcBarcodeScanner.exportSettings(exportPath);
        } else if (viewId == R.id.btn_import) {
            String fileName = "XCScannerSDK";
            String importPath = Environment.getExternalStorageDirectory().getPath() + "/XCScannerSDK.xml";
            XcBarcodeScanner.importSettingsByProfileName(fileName, importPath);
        } else if (viewId == R.id.button_get_whitelist) {
            String whiteList = XcBarcodeScanner.getScanWhiteListPkgs();
            mEtWhiteList.setText(whiteList);
        } else if (viewId == R.id.button_add_whitelist) {
            String whiteList = mEtWhiteList.getText().toString();
            XcBarcodeScanner.addScanWhiteListPkgs(whiteList);
            mEtWhiteList.setText("");
        } else if (viewId == R.id.button_del_whitelist) {
            String whiteList = mEtWhiteList.getText().toString();
            XcBarcodeScanner.delScanWhiteListPkgs(whiteList);
            mEtWhiteList.setText("");
        }
    }

    /**
     * Set custom broadcast action
     */
    private void setBroadcastActionDialog() {
        String curAction = mTvBroadcastAction.getText().toString();
        String curKey = mTvBroadcastKey.getText().toString();
        AlertDialogUtils.showInputDialog(getActivity()
                , getResources().getString(R.string.text_scan_result_action)
                , curAction
                , InputType.TYPE_CLASS_TEXT
                , new AlertDialogUtils.OnInputDialogClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, String userInput) {
                        mTvBroadcastAction.setText(userInput);
                        XcBarcodeScanner.setScanResultBroadcast(userInput, curKey);
                        notifyConfigChanged(DefaultOptions.KEY_CUSTOM_BROADCAST_ACTION, userInput);
                    }
                });
    }

    /**
     * Set custom broadcast key
     */
    private void setBroadcastKeyDialog() {
        String curAction = mTvBroadcastAction.getText().toString();
        String curKey = mTvBroadcastKey.getText().toString();
        AlertDialogUtils.showInputDialog(getActivity()
                , getResources().getString(R.string.text_scan_result_data_key)
                , curKey
                , InputType.TYPE_CLASS_TEXT
                , new AlertDialogUtils.OnInputDialogClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, String userInput) {
                        mTvBroadcastKey.setText(userInput);
                        XcBarcodeScanner.setScanResultBroadcast(curAction, userInput);
                        notifyConfigChanged(DefaultOptions.KEY_CUSTOM_BROADCAST_KEY, userInput);
                    }
                });
    }

    private void initView(View view) {
        mSpTimeOut = view.findViewById(R.id.sp_timeout);
        mSpTriggerMode = view.findViewById(R.id.sp_trigger_mode);
        mSpDataReceiveMethod = view.findViewById(R.id.sp_data_receive_method);
        mSpMultiBarcodeNum = view.findViewById(R.id.sp_multi_barcode_num);
        mSpViewSize = view.findViewById(R.id.sp_view_size);
        mSwExactlyMultiNum = view.findViewById(R.id.sw_exactly_multi_num);
        mSpTimeOut.setSelection(getSpPositionFromDefVal(R.array.scan_timeout_values, DefaultOptions.DEFAULT_SCAN_TIMEOUT_VAL));
        mSpTimeOut.setOnItemSelectedListener(this);
        mSpTriggerMode.setSelection(getSpPositionFromDefVal(R.array.scan_trigger_mode_values, DefaultOptions.DEFAULT_TRIGGER_MODE_VAL));
        mSpTriggerMode.setOnItemSelectedListener(this);
        mSpDataReceiveMethod.setSelection(getSpPositionFromDefVal(R.array.data_receive_method_values, DefaultOptions.DEFAULT_DATA_RECEIVE_METHOD_VAL));
        mSpDataReceiveMethod.setOnItemSelectedListener(this);
        mSpMultiBarcodeNum.setSelection(getSpPositionFromDefVal(R.array.multibarcodes_number_entries, DefaultOptions.DEFAULT_MULTI_NUM_VAL));
        mSpMultiBarcodeNum.setOnItemSelectedListener(this);
        mSpViewSize.setSelection(getSpPositionFromDefVal(R.array.scan_viewsize_values, DefaultOptions.DEFAULT_VIEW_SIZE_VAL));
        mSpViewSize.setOnItemSelectedListener(this);
        mSwExactlyMultiNum.setOnCheckedChangeListener(this);
        mSwExactlyMultiNum.setChecked(DefaultOptions.DEFAULT_EXACTLY_NUM_VAL);

        mLyBroadcastAction = view.findViewById(R.id.ly_broadcast_action);
        mTvBroadcastAction = view.findViewById(R.id.tv_broadcast_action);
        mLyBroadcastKey = view.findViewById(R.id.ly_broadcast_key);
        mTvBroadcastKey = view.findViewById(R.id.tv_broadcast_key);
        mTvBroadcastAction.setText(DefaultOptions.DEFAULT_CUSTOM_BROADCAST_ACTION_VAL);
        mTvBroadcastKey.setText(DefaultOptions.DEFAULT_CUSTOM_BROADCAST_KEY_VAL);
        mLyBroadcastAction.setOnClickListener(this);
        mLyBroadcastKey.setOnClickListener(this);

        mSpSuccessNotification = view.findViewById(R.id.sp_success_notification);
        mSpFailNotification = view.findViewById(R.id.sp_fail_notification);
        mSwLedNotification = view.findViewById(R.id.sw_led_notification);
        mSpSuccessNotification.setSelection(getSpPositionFromDefVal(R.array.scan_notification_values, DefaultOptions.DEFAULT_SUCCESS_NOTIFICATION_VAL));
        mSpSuccessNotification.setOnItemSelectedListener(this);
        mSpFailNotification.setSelection(getSpPositionFromDefVal(R.array.scan_notification_values, DefaultOptions.DEFAULT_FAIL_NOTIFICATION_VAL));
        mSpFailNotification.setOnItemSelectedListener(this);
        mSwLedNotification.setOnCheckedChangeListener(this);

        mSpNotificationVolume = view.findViewById(R.id.scan_notification_volume);
        mSpNotificationVolume.setSelection(getSpPositionFromDefVal(R.array.scan_notification_volume_entries, DefaultOptions.DEFAULT_NOTIFICATION_VOLUME_VAL));
        mSpNotificationVolume.setOnItemSelectedListener(this);

        mSwLedNotification.setOnCheckedChangeListener(this);
        mSwLedNotification.setChecked(DefaultOptions.DEFAULT_LED_NOTIFICATION_VAL);

        mSpAimEnable = view.findViewById(R.id.sp_aim_enable);
        mSpIllumeEnable = view.findViewById(R.id.sp_illume_enable);
        mSpBrightness = view.findViewById(R.id.sp_brightness);
        mSpAimEnable.setSelection(getSpPositionFromDefVal(R.array.aim_lights_values, DefaultOptions.DEFAULT_AIM_MODE_VAL));
        mSpAimEnable.setOnItemSelectedListener(this);
        mSpIllumeEnable.setSelection(getSpPositionFromDefVal(R.array.illume_lights_values, DefaultOptions.DEFAULT_ILLUME_MODE_VAL));
        mSpIllumeEnable.setOnItemSelectedListener(this);
        mSpBrightness.setSelection(getSpPositionFromDefVal(R.array.strobe_brightness_values, DefaultOptions.DEFAULT_BRIGHTNESS_VAL));
        mSpBrightness.setOnItemSelectedListener(this);

        mSwLeftScanEnable = view.findViewById(R.id.sw_left_scan_enable);
        mSwLeftScanEnable.setOnCheckedChangeListener(this);
        mSwLeftScanEnable.setChecked(DefaultOptions.DEFAULT_LEFT_SCAN_ENABLE_VAL);

        mUseAimidInResult = view.findViewById(R.id.pref_use_aimid_in_result);
        mUseAimidInResult.setOnCheckedChangeListener(this);
        mUseAimidInResult.setChecked(XcBarcodeScanner.getUseAimidInResult());

        //syg whitelist

        mScanWhiteList = view.findViewById(R.id.pref_whitelist);
        mScanWhiteList.setOnCheckedChangeListener(this);
        mScanWhiteList.setChecked(XcBarcodeScanner.isScanWhiteListEnable());
        mEtWhiteList = view.findViewById(R.id.et_whitelist);
        mBtWhiteListGet = view.findViewById(R.id.button_get_whitelist);
        mBtWhiteListGet.setOnClickListener(this);
        mBtWhiteListAdd = view.findViewById(R.id.button_add_whitelist);
        mBtWhiteListAdd.setOnClickListener(this);
        mBtWhiteListDel = view.findViewById(R.id.button_del_whitelist);
        mBtWhiteListDel.setOnClickListener(this);

        mSpPrefixChar = view.findViewById(R.id.sp_prefix_char);
        mSpSuffixChar = view.findViewById(R.id.sp_suffix_char);
        mSpLetterCase = view.findViewById(R.id.sp_letter_case);
        mSpPrefixChar.setSelection(getSpPositionFromDefVal(R.array.prefix_char_entries, DefaultOptions.DEFAULT_PREFIX_VAL));
        mSpPrefixChar.setOnItemSelectedListener(this);
        mSpSuffixChar.setSelection(getSpPositionFromDefVal(R.array.suffix_char_entries, DefaultOptions.DEFAULT_SUFFIX_VAL));
        mSpSuffixChar.setOnItemSelectedListener(this);
        mSpLetterCase.setSelection(getSpPositionFromDefVal(R.array.letter_case_entries, DefaultOptions.DEFAULT_LETTER_CASE_VAL));
        mSpLetterCase.setOnItemSelectedListener(this);

        mBtnExport = view.findViewById(R.id.btn_export);
        mBtnImport = view.findViewById(R.id.btn_import);
        mBtnExport.setOnClickListener(this);
        mBtnImport.setOnClickListener(this);

        gsCharEntries = view.findViewById(R.id.gs_char_entries);
        gsCharEntries.setSelection(getSpPositionFromDefVal(R.array.gs_char_values, DefaultOptions.DEFAULT_GS1_FNC1_VAL));
        gsCharEntries.setOnItemSelectedListener(this);

        but_pcharacter_custom = view.findViewById(R.id.but_pcharacter_custom);
        but_pcharacter_custom.setOnClickListener(this);
        but_pcharacter_custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(getContext());
                new AlertDialog.Builder(getContext())
                        .setView(et)
                        .setTitle(R.string.pref_character_custom)
                        .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String input = et.getText().toString();
                                boolean flag = XcBarcodeScanner.customConversionCharacters(input);
                                Toast.makeText(getContext(), flag ? R.string.pref_character_custom_success : R.string.pref_character_custom_failed, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(R.string.text_char_replace_cancel, null)
                        .show();
            }
        });

    }

    private int getSpPositionFromDefVal(int id, String defVal) {
        String[] list = getResources().getStringArray(id);
        int position = Arrays.asList(list).indexOf(defVal);
        return position >= 0 ? position : 0;
    }

    private int getSpPositionFromDefVal(int id, int defVal) {
        int[] list = getResources().getIntArray(id);
        int position = -1;
        for (int i = 0; i < list.length; i++) {
            if (list[i] == defVal) {
                position = i;
                break;
            }
        }
        return position >= 0 ? position : 0;
    }
}
