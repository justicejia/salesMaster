package com.sohu.focus.salesmaster.kernal.imageloader.compress;

import java.io.File;

/**
 * Created by qiangzhao on 2016/12/27.
 */

public class CompressFileWrapper {

    public File file;
    public String tag = "default";

    public CompressFileWrapper(File file) {
        this.file = file;
    }

    public CompressFileWrapper(File file, String tag) {
        this.file = file;
        this.tag = tag;
    }
}
