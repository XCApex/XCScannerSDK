package com.xc.demo.scannersdkdemo.tools;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.xc.demo.scannersdkdemo.R;

public class AlertDialogUtils {

    // Display a simple dialog box
    public static void showSimpleDialog(Context context, String title, String message, String positiveButtonText, DialogInterface.OnClickListener positiveButtonListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonText, positiveButtonListener)
                .setCancelable(true);
        builder.show();
    }

    // Display confirmation dialog box
    public static void showConfirmDialog(Context context, String title, String message, String positiveButtonText, DialogInterface.OnClickListener positiveButtonListener,
                                         String negativeButtonText, DialogInterface.OnClickListener negativeButtonListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonText, positiveButtonListener)
                .setNegativeButton(negativeButtonText, negativeButtonListener)
                .setCancelable(true);
        builder.show();
    }

    // Display dialog box with input box
    public static void showInputDialog(Context context, String title, String defMsg, int inputType, OnInputDialogClickListener positiveButtonListener) {
        final android.widget.EditText input = new android.widget.EditText(context);
        input.setInputType(inputType);
        input.setText(defMsg);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setView(input)
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userInput = input.getText().toString();
                        positiveButtonListener.onClick(dialog, which, userInput);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setCancelable(true);
        builder.show();
    }

    public interface OnInputDialogClickListener {
        void onClick(DialogInterface dialog, int which, String userInput);
    }
}
