package com.sohu.focus.salesmaster.kernal.log.core.printer.file.backup;

import java.io.File;

/**
 * Decide whether the log file should be backup and use a new file for next logging.
 *
 * Created by zhaoqiang on 2017/6/14.
 */

public interface BackupStrategy {

    /**
     * Whether we should backup a specified log file.
     *
     * @param file the log file
     * @return true is we should backup the log file
     */
    boolean shouldBackup(File file);
}
