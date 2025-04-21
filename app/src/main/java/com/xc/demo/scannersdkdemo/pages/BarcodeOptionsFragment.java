package com.xc.demo.scannersdkdemo.pages;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.xc.demo.scannersdkdemo.BaseFragment;
import com.xc.demo.scannersdkdemo.R;
import com.xc.demo.scannersdkdemo.databinding.FragmentBarcodeOptionsBinding;
import com.xcheng.scanner.BarcodeType;
//import com.xcheng.scanner.XCBarcodeTag;
import com.xc.demo.scannersdkdemo.XCBarcodeTag;
import com.xcheng.scanner.XcBarcodeScanner;

public class BarcodeOptionsFragment extends BaseFragment {

    private static final String TAG = "BarcodeOptionsFragment";
    FragmentBarcodeOptionsBinding bindingView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bindingView = DataBindingUtil.inflate(inflater, R.layout.fragment_barcode_options, container, false);
        bindingView.setFragment(this);
        refreshData();
        return bindingView.getRoot();
    }

    private void refreshData() {
        // Code11
        boolean isCode11Enable = XcBarcodeScanner.isBarcodeTypeEnabled(BarcodeType.CODE11);
        bindingView.swCode11Enable.setChecked(isCode11Enable);

        int code11CheckDigitDef = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_CODE11_CHECK_DIGIT_MODE);
        bindingView.spCode11CheckDigit.setSelection(code11CheckDigitDef);
        int code11MaxLengthDef = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_CODE11_MAX_LENGTH);
        bindingView.etCode11MaxLength.setText(String.valueOf(code11MaxLengthDef));
        int code11MinLengthDef = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_CODE11_MIN_LENGTH);
        bindingView.etCode11MinLength.setText(String.valueOf(code11MinLengthDef));

        // Code 39
        int code39CheckDigitDef = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_CODE39_CHECK_DIGIT_MODE);
        bindingView.spCode39CheckDigit.setSelection(code39CheckDigitDef);
        int code39CheckSumDef = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_CODE39_START_STOP_TRANSMIT);
        bindingView.swTransmitChar.setChecked(code39CheckSumDef == 1);
        int fullAsc = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_CODE39_FULL_ASCII_ENABLED);
        bindingView.swFullAsc.setChecked(fullAsc == 1);
        int base32 = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_CODE39_BASE32_ENABLED);
        bindingView.swBase32.setChecked(base32 == 1);
        int code39MaxLengthDef = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_CODE39_MAX_LENGTH);
        bindingView.etCode39MaxLength.setText(String.valueOf(code39MaxLengthDef));
        int code39MinLengthDef = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_CODE39_MIN_LENGTH);
        bindingView.etCode39MinLength.setText(String.valueOf(code39MinLengthDef));

        // Code49
        int code49MaxLengthDef = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_CODE49_MAX_LENGTH);
        bindingView.etCode49MaxLength.setText(String.valueOf(code49MaxLengthDef));
        int code49MinLengthDef = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_CODE49_MIN_LENGTH);
        bindingView.etCode49MinLength.setText(String.valueOf(code49MinLengthDef));

        // Code93
        int code93MaxLengthDef = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_CODE93_MAX_LENGTH);
        bindingView.etCode93MaxLength.setText(String.valueOf(code93MaxLengthDef));
        int code93MinLengthDef = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_CODE93_MIN_LENGTH);
        bindingView.etCode93MinLength.setText(String.valueOf(code93MinLengthDef));

        // Code128
        int code128Separators = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_C128_SEPARATOR_ENABLED);
        bindingView.swCode128Separator.setChecked(code128Separators == 1);
        int code128MaxLengthDef = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_CODE128_MAX_LENGTH);
        bindingView.etCode128MaxLength.setText(String.valueOf(code128MaxLengthDef));
        int code128MinLengthDef = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_CODE128_MIN_LENGTH);
        bindingView.etCode128MinLength.setText(String.valueOf(code128MinLengthDef));

        // Codabar
        int codabarCheckDigitDef = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_CODABAR_CHECK_DIGIT_MODE);
        bindingView.spCodabarCheckDigit.setSelection(codabarCheckDigitDef);
        int codabarTransmitCharDef = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_CODABAR_START_STOP_TRANSMIT);
        bindingView.swCodabarTransmitChar.setChecked(codabarTransmitCharDef == 1);
        int codabarMinLengthDef = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_CODABAR_MIN_LENGTH);
        bindingView.etCodabarMinLength.setText(String.valueOf(codabarMinLengthDef));

        // DataMatrix
        int withSeparators = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_DATAMATRIX_SEPARATOR_ENABLED);
        bindingView.swWithSeparators.setChecked(withSeparators == 1);
/*        int outputMaxLength = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_DATAMATRIX_OUTPUT_MAX_LENGTH);
        bindingView.etOutputMaxLength.setText(String.valueOf(outputMaxLength));*/
        int dataMatrixMaxLength = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_DATAMATRIX_MAX_LENGTH);
        bindingView.etMaxLength.setText(String.valueOf(dataMatrixMaxLength));
//        int matrixMode = XcBarcodeScanner.getDataMatrixMode();
//        bindingView.prefDataMatrixSetting.setSelection(matrixMode);

        // EAN8
        int ean8Checksum = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_EAN8_CHECK_DIGIT_TRANSMIT);
        bindingView.swEan8Checksum.setChecked(ean8Checksum == 1);
        int ean8Digit2 = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_EAN8_2CHAR_ADDENDA_ENABLED);
        bindingView.swEan8Digit2.setChecked(ean8Digit2 == 1);
        int ean8Digit5 = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_EAN8_5CHAR_ADDENDA_ENABLED);
        bindingView.swEan8Digit5.setChecked(ean8Digit5 == 1);
        int ean8Required = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_EAN8_ADDENDA_REQUIRED);
        bindingView.swEan8Required.setChecked(ean8Required == 1);
        int ean8Separator = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_EAN8_ADDENDA_SEPARATOR);
        bindingView.swEan8Separator.setChecked(ean8Separator == 1);

        // EAN13
        int checkSumDef = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_EAN13_CHECK_DIGIT_TRANSMIT);
        bindingView.swChecksum.setChecked(checkSumDef == 1);
        int twoAddonDef = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_EAN13_2CHAR_ADDENDA_ENABLED);
        bindingView.sw2addon.setChecked(twoAddonDef == 1);
        int fiveAddonDef = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_EAN13_5CHAR_ADDENDA_ENABLED);
        bindingView.sw5addon.setChecked(fiveAddonDef == 1);
        int requiredAddonDef = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_EAN13_ADDENDA_REQUIRED);
        bindingView.swRequiredAddon.setChecked(requiredAddonDef == 1);
        int separatorAddonDef = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_EAN13_ADDENDA_SEPARATOR);
        bindingView.swSeparatorAddon.setChecked(separatorAddonDef == 1);

        // GS1 128
        int gs128WithSeparator = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_GS1_128_SEPARATOR_ENABLED);
        bindingView.swCode128Separator.setChecked(gs128WithSeparator == 1);

        // GS1 DATABAR
        int gsDatabarExpanded = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_RSS_EXPANDED_ENABLED);
        bindingView.swGs1DatabarExpanded.setChecked(gsDatabarExpanded == 1);
        int gsDatabarLimited = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_RSS_LIMITED_ENABLED);
        bindingView.swGs1DatabarLimited.setChecked(gsDatabarLimited == 1);

        // ITF25
        int itf25CheckDigitDef = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_I25_CHECK_DIGIT_MODE);
        bindingView.spItf25CheckDigit.setSelection(itf25CheckDigitDef);
        int itf25MaxLength = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_I25_MAX_LENGTH);
        bindingView.etItf25MaxLength.setText(String.valueOf(itf25MaxLength));
        int itf25MinLength = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_I25_MIN_LENGTH);
        bindingView.etItf25MinLength.setText(String.valueOf(itf25MinLength));

        // Matrix25
        int checkDigitDef = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_M25_CHECK_DIGIT_MODE);
        bindingView.spCheckDigit.setSelection(checkDigitDef);

        // MSI
        int msiCheckDigitDef = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_MSI_CHECK_DIGIT_MODE);
        bindingView.spMsiCheckDigit.setSelection(msiCheckDigitDef);
        int msiMinLength = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_MSI_MIN_LENGTH);
        bindingView.etMsiMinLength.setText(String.valueOf(msiMinLength));

        // QRCode
        int MQEnable = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_QR_ENABLED);
        bindingView.swMqEnable.setChecked(MQEnable == 1);
/*        int qrcodeMaxLength = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_QR_MAX_OUTPUT_LENGTH);
        bindingView.etQrcodeMaxOutputLength.setText(String.valueOf(qrcodeMaxLength));*/
        int qrcodeMaxLength = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_QR_MAX_LENGTH);
        bindingView.etQrcodeMaxLength.setText(String.valueOf(qrcodeMaxLength));

        // UPCA
        int upcCheckSumDef = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_UPCA_CHECK_DIGIT_TRANSMIT);
        bindingView.swUpcChecksum.setChecked(upcCheckSumDef == 1);
        int numberSystemDef = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_UPCA_NUMBER_SYSTEM_TRANSMIT);
        bindingView.swUpcNumberCheck.setChecked(numberSystemDef == 1);
        int upcTwoAddonDef = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_UPCA_2CHAR_ADDENDA_ENABLED);
        bindingView.swUpc2addon.setChecked(upcTwoAddonDef == 1);
        int upcFiveAddonDef = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_UPCA_5CHAR_ADDENDA_ENABLED);
        bindingView.swUpc5addon.setChecked(upcFiveAddonDef == 1);
        int upcRequiredAddonDef = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_UPCA_ADDENDA_REQUIRED);
        bindingView.swUpcRequiredAddon.setChecked(upcRequiredAddonDef == 1);
        int upcSeparatorAddonDef = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_UPCA_ADDENDA_SEPARATOR);
        bindingView.swUpcSeparatorAddon.setChecked(upcSeparatorAddonDef == 1);
        int countryCodeDef = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_UPCA_ADD_COUNTRY_CODE);
        bindingView.swUpcCountryCode.setChecked(countryCodeDef == 1);

        // UPC-E
        int upceExpend = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_UPCE_EXPAND);
        bindingView.swUpceExpand.setChecked(upceExpend == 1);
        int upceCheckSumDef = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_UPCE_CHECK_DIGIT_TRANSMIT);
        bindingView.swUpceChecksum.setChecked(upceCheckSumDef == 1);
        int upceNumberSystemDef = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_UPCE_NUMBER_SYSTEM_TRANSMIT);
        bindingView.swUpceNumberCheck.setChecked(upceNumberSystemDef == 1);
        int upceTwoAddonDef = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_UPCE_2CHAR_ADDENDA_ENABLED);
        bindingView.swUpce2addon.setChecked(upceTwoAddonDef == 1);
        int upceFiveAddonDef = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_UPCE_5CHAR_ADDENDA_ENABLED);
        bindingView.swUpce5addon.setChecked(upceFiveAddonDef == 1);
        int upceRequiredAddonDef = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_UPCE_ADDENDA_REQUIRED);
        bindingView.swUpceRequiredAddon.setChecked(upceRequiredAddonDef == 1);
        int upceSeparatorAddonDef = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_UPCE_ADDENDA_SEPARATOR);
        bindingView.swUpceSeparatorAddon.setChecked(upceSeparatorAddonDef == 1);
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.d(TAG, "[onCheckedChanged] viewId = " + buttonView.getId() + ", isChecked = " + isChecked);
        if (buttonView.getId() == R.id.sw_code11_enable) {
            // Enable/disable Code11
            XcBarcodeScanner.enableBarcodeType(BarcodeType.CODE11, isChecked);
        } else if (buttonView.getId() == R.id.sw_checksum) {
            XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_EAN13_CHECK_DIGIT_TRANSMIT, isChecked ? 1 : 0);
        } else if (buttonView.getId() == R.id.sw_2addon) {
            XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_EAN13_2CHAR_ADDENDA_ENABLED, isChecked ? 1 : 0);
        } else if (buttonView.getId() == R.id.sw_5addon) {
            XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_EAN13_5CHAR_ADDENDA_ENABLED, isChecked ? 1 : 0);
        } else if (buttonView.getId() == R.id.sw_required_addon) {
            XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_EAN13_ADDENDA_REQUIRED, isChecked ? 1 : 0);
        } else if (buttonView.getId() == R.id.sw_separator_addon) {
            XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_EAN13_ADDENDA_SEPARATOR, isChecked ? 1 : 0);
        } else if (buttonView.getId() == R.id.sw_upc_checksum) {
            XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_UPCA_CHECK_DIGIT_TRANSMIT, isChecked ? 1 : 0);
        } else if (buttonView.getId() == R.id.sw_upc_number_check) {
            XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_UPCA_NUMBER_SYSTEM_TRANSMIT, isChecked ? 1 : 0);
        } else if (buttonView.getId() == R.id.sw_upc_2addon) {
            XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_UPCA_2CHAR_ADDENDA_ENABLED, isChecked ? 1 : 0);
        } else if (buttonView.getId() == R.id.sw_upc_5addon) {
            XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_UPCA_5CHAR_ADDENDA_ENABLED, isChecked ? 1 : 0);
        } else if (buttonView.getId() == R.id.sw_upc_required_addon) {
            XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_UPCA_ADDENDA_REQUIRED, isChecked ? 1 : 0);
        } else if (buttonView.getId() == R.id.sw_upc_separator_addon) {
            XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_UPCA_ADDENDA_SEPARATOR, isChecked ? 1 : 0);
        } else if (buttonView.getId() == R.id.sw_upc_country_code) {
            XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_UPCA_ADD_COUNTRY_CODE, isChecked ? 1 : 0);
        } else if (buttonView.getId() == R.id.sw_transmit_char) {
            XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_CODE39_START_STOP_TRANSMIT, isChecked ? 1 : 0);
        } else if (buttonView.getId() == R.id.sw_full_asc) {
            XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_CODE39_FULL_ASCII_ENABLED, isChecked ? 1 : 0);
        } else if (buttonView.getId() == R.id.sw_base32) {
            XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_CODE39_BASE32_ENABLED, isChecked ? 1 : 0);
        } else if (buttonView.getId() == R.id.sw_with_separators) {
            XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_DATAMATRIX_SEPARATOR_ENABLED, isChecked ? 1 : 0);
        } else if (buttonView.getId() == R.id.sw_ean8_checksum) {
            XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_EAN8_CHECK_DIGIT_TRANSMIT, isChecked ? 1 : 0);
        } else if (buttonView.getId() == R.id.sw_ean8_digit_2) {
            XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_EAN8_2CHAR_ADDENDA_ENABLED, isChecked ? 1 : 0);
        } else if (buttonView.getId() == R.id.sw_ean8_digit_5) {
            XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_EAN8_5CHAR_ADDENDA_ENABLED, isChecked ? 1 : 0);
        } else if (buttonView.getId() == R.id.sw_ean8_required) {
            XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_EAN8_ADDENDA_REQUIRED, isChecked ? 1 : 0);
        } else if (buttonView.getId() == R.id.sw_ean8_separator) {
            XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_EAN8_ADDENDA_SEPARATOR, isChecked ? 1 : 0);
        } else if (buttonView.getId() == R.id.sw_code128_separator) {
            XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_C128_SEPARATOR_ENABLED, isChecked ? 1 : 0);
        } else if (buttonView.getId() == R.id.sw_codabar_transmit_char) {
            XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_CODABAR_START_STOP_TRANSMIT, isChecked ? 1 : 0);
        } else if (buttonView.getId() == R.id.sw_gs1_128_separators) {
            XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_GS1_128_SEPARATOR_ENABLED, isChecked ? 1 : 0);
        } else if (buttonView.getId() == R.id.sw_gs1_databar_expanded) {
            XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_RSS_EXPANDED_ENABLED, isChecked ? 1 : 0);
        } else if (buttonView.getId() == R.id.sw_gs1_databar_limited) {
            XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_RSS_LIMITED_ENABLED, isChecked ? 1 : 0);
        } else if (buttonView.getId() == R.id.sw_mq_enable) {
            XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_QR_ENABLED, isChecked ? 1 : 0);
        } else if (buttonView.getId() == R.id.sw_upce_expand) {
            XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_UPCE_EXPAND, isChecked ? 1 : 0);
        } else if (buttonView.getId() == R.id.sw_upce_checksum) {
            XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_UPCE_CHECK_DIGIT_TRANSMIT, isChecked ? 1 : 0);
        } else if (buttonView.getId() == R.id.sw_upce_number_check) {
            XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_UPCE_NUMBER_SYSTEM_TRANSMIT, isChecked ? 1 : 0);
        } else if (buttonView.getId() == R.id.sw_upce_2addon) {
            XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_UPCE_2CHAR_ADDENDA_ENABLED, isChecked ? 1 : 0);
        } else if (buttonView.getId() == R.id.sw_upce_5addon) {
            XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_UPCE_5CHAR_ADDENDA_ENABLED, isChecked ? 1 : 0);
        } else if (buttonView.getId() == R.id.sw_upce_required_addon) {
            XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_UPCE_ADDENDA_REQUIRED, isChecked ? 1 : 0);
        } else if (buttonView.getId() == R.id.sw_upce_separator_addon) {
            XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_UPCE_ADDENDA_SEPARATOR, isChecked ? 1 : 0);
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "[onItemSelected] viewId = " + parent.getId() + ", position = " + position);
        if (parent.getId() == R.id.sp_code11_check_digit) {
            XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_CODE11_CHECK_DIGIT_MODE, position);
        } else if (parent.getId() == R.id.sp_code39_check_digit) {
            XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_CODE39_CHECK_DIGIT_MODE, position);
        } else if (parent.getId() == R.id.sp_check_digit) {
            XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_M25_CHECK_DIGIT_MODE, position);
        } else if (parent.getId() == R.id.sp_codabar_check_digit) {
            XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_CODABAR_CHECK_DIGIT_MODE, position);
        } else if (parent.getId() == R.id.sp_itf25_check_digit) {
            XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_I25_CHECK_DIGIT_MODE, position);
        } else if (parent.getId() == R.id.sp_msi_check_digit) {
            XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_MSI_CHECK_DIGIT_MODE, position);
//        }  else if (parent.getId() == R.id.pref_data_matrix_setting) {
//            XcBarcodeScanner.setDataMatrixMode(position);
        }
    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        Log.d(TAG, "[onEditorAction] actionId = " + actionId + ", TextView = " + v.getId());
        String val = v.getText().toString().trim();
        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT
                || actionId == EditorInfo.IME_ACTION_PREVIOUS || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            switch (v.getId()) {
                case R.id.et_code11_max_length:
                    XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_CODE11_MAX_LENGTH, val.isEmpty() ? 127 : Integer.parseInt(val));
                    break;
                case R.id.et_code11_min_length:
                    XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_CODE11_MIN_LENGTH, val.isEmpty() ? 6 : Integer.parseInt(val));
                    break;
                case R.id.et_code39_max_length:
                    XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_CODE39_MAX_LENGTH, val.isEmpty() ? 127 : Integer.parseInt(val));
                    break;
                case R.id.et_code39_min_length:
                    XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_CODE39_MIN_LENGTH, val.isEmpty() ? 1 : Integer.parseInt(val));
                    break;
                case R.id.et_code49_max_length:
                    XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_CODE49_MAX_LENGTH, val.isEmpty() ? 127 : Integer.parseInt(val));
                    break;
                case R.id.et_code49_min_length:
                    XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_CODE49_MIN_LENGTH, val.isEmpty() ? 1 : Integer.parseInt(val));
                    break;
                case R.id.et_code93_max_length:
                    XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_CODE93_MAX_LENGTH, val.isEmpty() ? 127 : Integer.parseInt(val));
                    break;
                case R.id.et_code93_min_length:
                    XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_CODE93_MIN_LENGTH, val.isEmpty() ? 2 : Integer.parseInt(val));
                    break;
                case R.id.et_code128_max_length:
                    XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_CODE128_MAX_LENGTH, val.isEmpty() ? 127 : Integer.parseInt(val));
                    break;
                case R.id.et_code128_min_length:
                    XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_CODE128_MIN_LENGTH, val.isEmpty() ? 1 : Integer.parseInt(val));
                    break;
/*                case R.id.et_output_max_length:
                    XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_DATAMATRIX_OUTPUT_MAX_LENGTH, val.isEmpty() ? 0 : Integer.parseInt(val));
                    break;*/
                case R.id.et_max_length:
                    XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_DATAMATRIX_MAX_LENGTH, val.isEmpty() ? 3116 : Integer.parseInt(val));
                    break;
                case R.id.et_codabar_min_length:
                    XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_CODABAR_MIN_LENGTH, val.isEmpty() ? 4 : Integer.parseInt(val));
                    break;
                case R.id.et_itf25_max_length:
                    XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_I25_MAX_LENGTH, val.isEmpty() ? 127 : Integer.parseInt(val));
                    break;
                case R.id.et_itf25_min_length:
                    XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_I25_MIN_LENGTH, val.isEmpty() ? 2 : Integer.parseInt(val));
                    break;
                case R.id.et_msi_min_length:
                    XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_MSI_MIN_LENGTH, val.isEmpty() ? 4 : Integer.parseInt(val));
/*                    break;
                case R.id.et_qrcode_max_output_length:
                    XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_QR_MAX_OUTPUT_LENGTH, val.isEmpty() ? 0 : Integer.parseInt(val));
                    break;*/
                case R.id.et_qrcode_max_length:
                    XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_QR_MAX_LENGTH, val.isEmpty() ? 7089 : Integer.parseInt(val));
                    break;
                default:
                    break;
            }
        }
        return false;
    }
}
