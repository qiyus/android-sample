package com.vigorx.thread;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class DocumentActivity extends AppCompatActivity {
    public final static String DOCUMENT_PATH = "document_path";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Intent intent = getIntent();
        String path = intent.getStringExtra(DOCUMENT_PATH);

        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        Drawable drawable = ImageProcessor.getImage(path, point.x, point.y);
        imageView.setImageDrawable(drawable);
    }
}
