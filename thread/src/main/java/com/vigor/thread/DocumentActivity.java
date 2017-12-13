package com.vigor.thread;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Vigor on 2016/11/9.
 * 图片表示（Handler设置文件情报）
 */
public class DocumentActivity extends AppCompatActivity {
    public final static String DOCUMENT_PATH = "document_path";
    private final static int LOAD_FILE_SIZE = 1;
    private final static int LOAD_FILE_DATE = 2;
    private final static int CURRENT_DATE = 3;
    private final static String UPDATE_TIME_THREAD = "update current time";
    private TextView mFileName;
    private TextView mFileSize;
    private TextView mFileDate;
    private TextView mCurrentTime;
    private String mPath;
    private Handler mUIHandler;
    private Handler mUpdateTimeHandler;
    private HandlerThread mUpdateTimeThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Intent intent = getIntent();
        mPath = intent.getStringExtra(DOCUMENT_PATH);
        mFileName = (TextView) findViewById(R.id.fileName);
        mFileSize = (TextView) findViewById(R.id.fileSize);
        mFileDate = (TextView) findViewById(R.id.fileLastDate);
        mCurrentTime = (TextView) findViewById(R.id.currentTime);
        mUIHandler = new UIHandler(this);

        setImage(imageView, mPath);
        setFilePath();
        setFileSizeAndDate();
        setCurrentTime();
        setFloating();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUIHandler.removeCallbacksAndMessages(null);
        mUpdateTimeHandler.removeCallbacksAndMessages(null);
        mUpdateTimeThread.quit();
    }

    private void setCurrentTime() {
        mUpdateTimeThread = new HandlerThread(UPDATE_TIME_THREAD);
        mUpdateTimeThread.start();
        mUpdateTimeHandler = new Handler(mUpdateTimeThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                System.out.println("UpdateTimeHandler: " + Thread.currentThread().getName());
                Date date = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = format.format(date);
                Message message = mUIHandler.obtainMessage(CURRENT_DATE, time);
                mUIHandler.sendMessage(message);
                super.handleMessage(msg);
            }
        };

        for (int i = 0; i < 7; i++) {
            mUpdateTimeHandler.sendEmptyMessageDelayed(i, i * 1000);
        }
    }

    private void setFilePath() {
        mUIHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFileName.setText(mPath);
            }
        }, 500);
    }

    private void setFileSizeAndDate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(mPath);
                Message message = mUIHandler.obtainMessage(LOAD_FILE_SIZE, getFileSize(file));
                mUIHandler.sendMessage(message);
                message = mUIHandler.obtainMessage(LOAD_FILE_DATE, getFileDate(file));
                mUIHandler.sendMessageDelayed(message, 1000);
            }

            private String getFileDate(File file) {
                String fileDate = "";
                if (file != null && file.exists() && file.isFile()) {
                    long lastModified = file.lastModified();
                    Date date = new Date(lastModified);
                    fileDate = date.toString();
                }
                return fileDate;
            }

            private String getFileSize(File file) {
                String fileSize = "";
                if (file != null && file.exists() && file.isFile()) {
                    long fileLength = file.length();
                    DecimalFormat decimalFormat = new DecimalFormat("#.00");
                    if (fileLength < 1024) {
                        fileSize = decimalFormat.format((double) fileLength) + " Byte";
                    } else if (fileLength < 1048576) {
                        fileSize = decimalFormat.format((double) fileLength / 1024) + " KB";
                    } else if (fileLength < 1073741824) {
                        fileSize = decimalFormat.format((double) fileLength / 1048576) + " MB";
                    } else if (fileLength < 1073741824) {
                        fileSize = decimalFormat.format((double) fileLength / 1073741824) + " GB";
                    }
                }
                return fileSize;
            }
        }).start();
    }

    private void setImage(ImageView imageView, String path) {
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        Drawable drawable = ImageUtil.getImage(path, point.x, point.y);
        imageView.setImageDrawable(drawable);
    }

    private void setFloating() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public static class UIHandler extends Handler {
        WeakReference<Activity> mActivity;

        public UIHandler(Activity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            System.out.println("UIHandler: " + Thread.currentThread().getName());
            DocumentActivity activity = (DocumentActivity) mActivity.get();
            if (activity == null) {
                return;
            }

            switch (msg.what) {
                case LOAD_FILE_SIZE:
                    activity.mFileSize.setText((String) msg.obj);
                    break;
                case LOAD_FILE_DATE:
                    activity.mFileDate.setText((String) msg.obj);
                    break;
                case CURRENT_DATE:
                    activity.mCurrentTime.setText((String) msg.obj);
                    break;
                default:
            }
        }
    }
}
