package com.sohu.focus.salesmaster.kernal.log.core.printer.file.naming;


import com.sohu.focus.salesmaster.kernal.log.core.LogLevel;

/**
 * Generate file name according to the log level, different levels lead to different file names.
 *
 * Created by zhaoqiang on 2017/6/14.
 */

public class LevelFileNameGenerator implements FileNameGenerator {

    @Override
    public boolean isFileNameChangeable() {
        return true;
    }

    /**
     * Generate a file name which represent a specific log level.
     */
    @Override
    public String generateFileName(int logLevel, long timestamp) {
        return LogLevel.getLevelName(logLevel);
    }
}
