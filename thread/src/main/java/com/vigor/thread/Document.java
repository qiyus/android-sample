package com.vigor.thread;

/**
 * Created by Vigor on 2016/11/9.
 * 图片文件情报
 */
public class Document {
    private String title;
    private String path;
    private String extension;

    public Document(String title, String path, String extension) {
        this.title = title;
        this.path = path;
        this.extension = extension;
    }

    public String getTitle() {
        return title;
    }

    public String getPath() {
        return path;
    }

    public String getExtension() {
        return extension;
    }

}
