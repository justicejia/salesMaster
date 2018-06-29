package com.sohu.focus.salesmaster.kernal.log.core.printer.file.backup;

import java.io.File;

/**
 * Limit the file size of a max length.
 *
 * Created by zhaoqiang on 2017/6/14.
 */

public class FileSizeBackupStrategy implements BackupStrategy {

    private long maxSize;

    /**
     * Constructor.
     *
     * @param maxSize the max size the file can reach
     */
    public FileSizeBackupStrategy(long maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public boolean shouldBackup(File file) {
        return file.length() > maxSize;
    }
}
