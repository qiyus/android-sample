package com.vigor.thread;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by Vigor on 2016/11/9.
 * 过滤图片文件。
 */
public class ImageFileFilter implements FileFilter {

    @Override
    public boolean accept(File pathname) {
        if (pathname.isDirectory()) {
            return true;
        }

        String s = pathname.getName();
        return s.endsWith(".png") || s.endsWith(".jpg") || s.endsWith(".jpeg");
    }
}
