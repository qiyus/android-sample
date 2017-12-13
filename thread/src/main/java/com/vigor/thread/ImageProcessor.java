package com.vigor.thread;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vigor on 2016/11/9.
 * Image缩略图加工（HandlerThread）
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
     * @param listener 回调接口
     */
    public void setOnProcessedResultListener(OnProcessedResultListener<Handle> listener) {
        mListener = listener;
    }

    /**
     * 追加图片处理任务。
     * @param handle ImageView对象，由调用者传入。
     * @param path 图片路径。
     */
    public void loadThumbnail(Handle handle, String path, int width, int height) {
        mRequestMap.put(handle, path);
        Message message = mHandler.obtainMessage(MESSAGE_THUMBNAIL, handle);
        message.arg1 = width;
        message.arg2 = height;
        message.sendToTarget();
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_THUMBNAIL) {
                    final Handle handle = (Handle) msg.obj;
                    final int width = msg.arg1;
                    final int height = msg.arg2;
                    final String path = mRequestMap.get(handle);
                    if (path == null) {
                        return;
                    }
                    final Drawable thumbnail = ImageUtil.getImage(path, width, height);
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

}
