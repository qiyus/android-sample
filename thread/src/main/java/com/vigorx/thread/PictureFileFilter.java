package com.vigorx.thread;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by songlei on 2016/11/11.
 */
public class PictureFileFilter implements FileFilter {

    @Override
    public boolean accept(File pathname) {
        if (pathname.isDirectory()) {
            return true;
        }

        String s = pathname.getName();
        return s.endsWith(".png") || s.endsWith(".jpg") || s.endsWith(".jpeg");
    }
}
