package com.sohu.focus.salesmaster.kernal.log.core.printer.file.naming;

/**
 * Generate a file name that is changeless.
 *
 * Created by zhaoqiang on 2017/6/14.
 */

public class ChangelessFileNameGenerator implements FileNameGenerator {

    private final String fileName;

    /**
     * Constructor.
     *
     * @param fileName the changeless file name
     */
    public ChangelessFileNameGenerator(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public boolean isFileNameChangeable() {
        return false;
    }

    @Override
    public String generateFileName(int logLevel, long timestamp) {
        return fileName;
    }
}
