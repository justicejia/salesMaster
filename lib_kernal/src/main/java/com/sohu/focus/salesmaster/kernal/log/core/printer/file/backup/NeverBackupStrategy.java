package com.sohu.focus.salesmaster.kernal.log.core.printer.file.backup;

import java.io.File;

/**
 * Never backup the log file.
 *
 * Created by zhaoqiang on 2017/6/14.
 */

public class NeverBackupStrategy implements BackupStrategy {

    @Override
    public boolean shouldBackup(File file) {
        return false;
    }
}
