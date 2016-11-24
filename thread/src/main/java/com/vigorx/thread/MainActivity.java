package com.vigorx.thread;

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

public class MainActivity extends AppCompatActivity {
    private final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    private List<Document> mData;
    private GridView mGrid;
    private GridAdapter mAdapter;
    private ImageProcessor<ImageView> mImageProcessor;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        // 对话框设置。
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMessage("全盘搜索中，慢慢等着吧！");

        // 动态获取访问外部存储权限。
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            } else {
                new FileSearcherTask().execute();
            }
        }

        // 启动图片加载线程，等待图片加载消息。
        mImageProcessor = new ImageProcessor(new Handler());
        mImageProcessor.setOnProcessedResultListener(new ImageProcessor.OnProcessedResultListener<ImageView>() {
            @Override
            public void onSuccess(ImageView imageView, Drawable thumbnail) {
                imageView.setImageDrawable(thumbnail);
            }
        });
        mImageProcessor.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                // 启动全盘搜索线程。
                new FileSearcherTask().execute();
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
            mImageProcessor.startThumbnailLoadTask(viewHolder.thumbnail, document.getPath());
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
    public class FileSearcherTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.setMax(100);
            mProgressDialog.show();
        }

        @Override
        protected List<Document> doInBackground(Object[] objects) {
            List<Document> documents = new ArrayList<>();
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File root = Environment.getExternalStorageDirectory().getAbsoluteFile();
                searchPictureFile(root, documents);
            }
            return documents;
        }

        /**
         * 搜索文档。
         * @param root 根目录
         * @param documents 搜索结果
         */
        private void searchPictureFile(File root, List<Document> documents) {
            // 测试进度条的进度设置。
            publishProgress(50);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 过滤掉png、jpg以外的文件。
            File[] list = root.listFiles(new PictureFileFilter());
            if (list != null) {
                for (File file : list) {
                    if (file.isHidden()) {
                        continue;
                    }

                    if (file.isDirectory()) {
                        searchPictureFile(file, documents);
                    } else {
                        String fileName = file.getName();
                        Document document = new Document();
                        document.setTitle(fileName);
                        document.setPath(file.getAbsolutePath());
                        document.setExtension(fileName.substring(fileName.lastIndexOf(".") + 1));
                        documents.add(document);
                    }
                }
            }
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
            int value = (Integer) values[0];
            mProgressDialog.setProgress(value);
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
