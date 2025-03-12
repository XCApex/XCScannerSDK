package com.xc.demo.scannersdkdemo.tools;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.xc.demo.scannersdkdemo.BaseApplication;

import java.nio.ByteBuffer;

public class Utils {

    /**
     * 8位灰度转Bitmap
     * <p>
     * 图像宽度必须能被4整除
     *
     * @param data   裸数据
     * @param width  图像宽度
     * @param height 图像高度
     * @return
     */
    public static Bitmap raw8ToBitmap(byte[] data, int width, int height) {
        byte[] Bits = new byte[data.length * 4]; //RGBA 数组

        int i;
        for (i = 0; i < data.length; i++) {
            // 原理：4个字节表示一个灰度，则RGB  = 灰度值，最后一个Alpha = 0xff;
            Bits[i * 4 + 0] = data[i]; // R
            Bits[i * 4 + 1] = data[i]; // G
            Bits[i * 4 + 2] = data[i]; // B
            Bits[i * 4 + 3] = -1;  // 0xFF, A
        }

        // Bitmap.Config.ARGB_8888 表示：图像模式为8位
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bmp.copyPixelsFromBuffer(ByteBuffer.wrap(Bits));

        return bmp;
    }

    public static Bitmap rotateImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);

        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (resizedBitmap != bitmap && bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }

        return resizedBitmap;
    }

    public static String getAppVersionName() {
        String localVersion = "";
        try {
            PackageInfo packageInfo = BaseApplication.getContext().getPackageManager().getPackageInfo(BaseApplication.getContext().getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localVersion;
    }
}
