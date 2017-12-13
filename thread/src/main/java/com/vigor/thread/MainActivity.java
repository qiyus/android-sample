package com.vigor.thread;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vigor on 2016/11/9.
 * 主画面
 */
public class MainActivity extends AppCompatActivity {
    private final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private final int FILE_COUNT = 500;
    private List<Document> mData;
    private GridView mGrid;
    private GridAdapter mAdapter;
    private ImageProcessor<ImageView> mImageProcessor;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 设置列表视图
        setImageGridView();

        // 对话框设置。
        setProgressDialog();

        // 动态获取访问外部存储权限。
        getStoragePermission();

        // 启动图片加载线程，等待图片加载消息。
        startImageProcessor();
    }

    private void setImageGridView() {
        mGrid = (GridView) findViewById(R.id.gridView);
        mAdapter = new GridAdapter();
        mGrid.setAdapter(mAdapter);
        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent toDocument = new Intent(MainActivity.this, DocumentActivity.class);
                toDocument.putExtra(DocumentActivity.DOCUMENT_PATH, mData.get(i).getPath());
                startActivity(toDocument);
            }
        });
    }

    private void setProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        // 设置是否可以通过点击Back键取消
        mProgressDialog.setCancelable(false);
        // 设置在点击Dialog外是否取消Dialog进度条
        mProgressDialog.setCanceledOnTouchOutside(false);
        // 设置水平进度条
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // 设置提示的title的图标，默认是没有的
        mProgressDialog.setIcon(android.R.drawable.ic_dialog_info);
        mProgressDialog.setTitle(getString(R.string.info));
        mProgressDialog.setMessage(String.format(getString(R.string.searching), FILE_COUNT));
        mProgressDialog.setMax(FILE_COUNT);
    }

    private void startImageProcessor() {
        mImageProcessor = new ImageProcessor(new Handler());
        mImageProcessor.setOnProcessedResultListener(new ImageProcessor.OnProcessedResultListener<ImageView>() {
            @Override
            public void onSuccess(ImageView imageView, Drawable thumbnail) {
                imageView.setImageDrawable(thumbnail);
            }
        });
        mImageProcessor.start();
    }

    private void getStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            } else {
                new ImageSearcherTask().execute();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放图片加载线程.
        mImageProcessor.quit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                // 启动全盘搜索线程。
                new ImageSearcherTask().execute();
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * 缩略图列表的adapter。
     */
    public class GridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (mData != null) {
                return mData.size();
            }
            return 0;
        }

        @Override
        public Document getItem(int i) {
            if (mData != null) {
                return mData.get(i);
            }
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                viewHolder = new ViewHolder();
                view = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_grid_main, null);
                viewHolder.title = (TextView) view.findViewById(R.id.grid_title);
                viewHolder.thumbnail = (ImageView) view.findViewById(R.id.grid_thumbnail);
                viewHolder.extension = (TextView) view.findViewById(R.id.grid_extension);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            Document document = mData.get(i);
            mImageProcessor.loadThumbnail(viewHolder.thumbnail,
                    document.getPath(), 320, 320);
            viewHolder.title.setText(document.getTitle());
            viewHolder.extension.setText(document.getExtension());
            return view;
        }

        class ViewHolder {
            ImageView thumbnail;
            TextView title;
            TextView extension;
        }
    }

    /**
     * 遍历sdcard，取得图片列表。
     */
    public class ImageSearcherTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        /**
         * Runs on the UI thread after {@link #publishProgress} is invoked.
         * The specified values are the values passed to {@link #publishProgress}.
         *
         * @param values The values indicating progress.
         * @see #publishProgress
         * @see #doInBackground
         */
        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
            int value = (int) values[0];
            mProgressDialog.setProgress(value);
        }

        @Override
        protected List<Document> doInBackground(Object[] objects) {
            List<Document> documents = new ArrayList<>();
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File root = Environment.getExternalStorageDirectory().getAbsoluteFile();
                searchImageFile(root, documents, 0);
            }
            return documents;
        }

        /**
         * 搜索文档。
         * @param root 根目录
         * @param documents 搜索结果
         */
        private int searchImageFile(File root, List<Document> documents, int count) {
            // 过滤掉png、jpg以外的文件。
            File[] list = root.listFiles(new ImageFileFilter());
            if (list != null && count < FILE_COUNT) {
                for (File file : list) {
                    if (file.isHidden()) {
                        continue;
                    }

                    if (file.isDirectory()) {
                        count += searchImageFile(file, documents, count);
                    } else {
                        if (count >= FILE_COUNT) {
                            return count;
                        }
                        String fileName = file.getName();
                        synchronized (this) {
                            Document document = new Document(fileName,
                                    file.getAbsolutePath(),
                                    fileName.substring(fileName.lastIndexOf(".") + 1));
                            documents.add(document);
                            count++;
                        }
                        publishProgress(count);
                        try {
                            // 为了主线程更新UI
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }
            return count;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            mData = (List<Document>) o;
            mProgressDialog.dismiss();
            mAdapter.notifyDataSetChanged();
        }
    }
}
