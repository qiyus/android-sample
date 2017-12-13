package com.vigor.thread;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class ImageUtil {
    /**
     * 根据路径取得图片文件。
     *
     * @param path   文件路径
     * @param width  文件请求宽度
     * @param height 文件请求高度
     * @return 图片对象
     */
    public static Drawable getImage(String path, int width, int height) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        // 只读取option，不加载图片到内存。
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateInSampleSize(options, width, height);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return new BitmapDrawable(null, bitmap);
    }

    /**
     * 计算缩放比
     * @param options 图片选项
     * @param reqWidth 请求表示宽度
     * @param reqHeight 请求表示高度
     * @return 缩放比例
     */
    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float)height / (float)reqHeight);
            } else {
                inSampleSize = Math.round((float)width / (float)reqWidth);
            }
        }
        return inSampleSize;
    }
}