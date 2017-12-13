package com.vigor.thread;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * Created by Vigor on 2016/11/9.
 * 图片表示
 */
public class DocumentActivity extends AppCompatActivity {
    public final static String DOCUMENT_PATH = "document_path";
    private final static int LOAD_FILE_SIZE = 1;
    private final static int LOAD_FILE_DATE = 2;
    private TextView mFileName;
    private TextView mFileSize;
    private TextView mFileLastDate;
    private String mPath;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Intent intent = getIntent();
        mPath = intent.getStringExtra(DOCUMENT_PATH);
        mFileName = (TextView) findViewById(R.id.fileName);
        mFileSize = (TextView) findViewById(R.id.fileSize);
        mFileLastDate = (TextView) findViewById(R.id.fileLastDate);
        mHandler = new FileInfoHandler(this);

        setImage(imageView, mPath);
        setFloating();
        startFileInfoTask();
    }

    private void startFileInfoTask() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(mPath);
                Message message = mHandler.obtainMessage(LOAD_FILE_SIZE, getFileSize(file));
                mHandler.sendMessage(message);
                message = mHandler.obtainMessage(LOAD_FILE_DATE, getFileDate(file));
                mHandler.sendMessageDelayed(message, 10000);
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

    public static class FileInfoHandler extends Handler {
        WeakReference<Activity> mActivity;

        public FileInfoHandler(Activity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            DocumentActivity activity = (DocumentActivity) mActivity.get();

            activity.mFileName.setText(activity.mPath);
            if (msg.what == LOAD_FILE_SIZE) {
                String fileSize = (String) msg.obj;
                activity.mFileSize.setText(fileSize);
            } else if (msg.what == LOAD_FILE_DATE) {
                String fileDate = (String) msg.obj;
                activity.mFileLastDate.setText(fileDate);
            }
        }
    }
}
