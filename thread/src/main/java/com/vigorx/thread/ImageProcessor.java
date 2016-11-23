package com.vigorx.thread;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by songlei on 2016/11/9.
 */
public class ImageProcessor<Handle> extends HandlerThread {
    private static final String TAG = "ImageProcessor";
    private static final int MESSAGE_THUMBNAIL = 0;
    private final Handler mResponseHandler;
    private OnProcessedResultListener<Handle> mListener;
    private Handler mHandler;
    private Map<Handle, String> mRequestMap = Collections.synchronizedMap(new HashMap<Handle, String>());

    public ImageProcessor(Handler responseHandler) {
        super(TAG);
        this.mResponseHandler = responseHandler;
    }

    /**
     * 回调接口
     * @param <Handle>
     */
    public interface OnProcessedResultListener<Handle> {
        void onSuccess(Handle handle, Drawable thumbnail);
    }

    /**
     * 设置回调处理监听器。
     * @param listener
     */
    public void setOnProcessedResultListener(OnProcessedResultListener<Handle> listener) {
        mListener = listener;
    }

    public void startThumbnailLoadTask(Handle handle, String path) {
        mRequestMap.put(handle, path);
        mHandler.obtainMessage(MESSAGE_THUMBNAIL, handle).sendToTarget();
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_THUMBNAIL) {
                    final Handle handle = (Handle) msg.obj;
                    final String path = mRequestMap.get(handle);
                    if (path == null) {
                        return;
                    }
                    final Drawable thumbnail = getImage(path, 320, 320);
                    mResponseHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            // 任务未处理完毕，list滑动导致handle和path不一致，旧的任务忽略。
                            if (mRequestMap.get(handle) != path) {
                                return;
                            }
                            // 将取得的图片设置到主画面的列表中。
                            mListener.onSuccess(handle, thumbnail);
                        }
                    });
                }
                super.handleMessage(msg);
            }
        };
    }

    /**
     * 根据路径取得图片文件。
     * @param path
     * @param width
     * @param height
     * @return
     */
    public static Drawable getImage(String path, int width, int height) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateInSampleSize(options, width, height);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return new BitmapDrawable(null, bitmap);
    }

    /**
     * 计算缩放比
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
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
