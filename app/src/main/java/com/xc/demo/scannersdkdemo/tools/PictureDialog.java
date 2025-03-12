package com.xc.demo.scannersdkdemo.tools;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xc.demo.scannersdkdemo.R;

public class PictureDialog extends Dialog {
    private TextView mMainText;
    private ImageView mPictureView;

    private Button mOkButton;

    private String title;
    private String mainText;
    private String picPath;
    private Bitmap picBmp;

    public PictureDialog(Context context) {
        super(context);
    }

    public PictureDialog(Context context,
                         String title,
                         String text,
                         String picFilePath) {
        this(context);
        this.title = title;
        this.mainText = text;
        this.picPath = picFilePath;
        this.picBmp = BitmapFactory.decodeFile(picPath);
    }

    public PictureDialog(Context context,
                         String title,
                         String text,
                         Bitmap bmp) {
        this(context);
        this.title = title;
        this.mainText = text;
        this.picBmp = bmp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_dialog);
        mOkButton = (Button) findViewById(R.id.okbutton);
        mMainText = (TextView) findViewById(R.id.maintext);
        mPictureView = (ImageView) findViewById(R.id.picture);
        PictureDialog.this.setTitle(title);
        mMainText.setText(mainText);

        mPictureView.setImageBitmap(picBmp);

        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PictureDialog.this.dismiss();
            }
        });
    }
}
