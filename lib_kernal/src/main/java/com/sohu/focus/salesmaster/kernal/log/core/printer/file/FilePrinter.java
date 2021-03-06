package com.sohu.focus.salesmaster.kernal.log.core.printer.file;

import android.os.Environment;


import com.sohu.focus.salesmaster.kernal.BaseApplication;
import com.sohu.focus.salesmaster.kernal.log.core.LogLevel;
import com.sohu.focus.salesmaster.kernal.log.core.flattener.Flattener;
import com.sohu.focus.salesmaster.kernal.log.core.internal.DefaultsFactory;
import com.sohu.focus.salesmaster.kernal.log.core.printer.ILifecyclePrinter;
import com.sohu.focus.salesmaster.kernal.log.core.printer.Printer;
import com.sohu.focus.salesmaster.kernal.log.core.printer.file.backup.BackupStrategy;
import com.sohu.focus.salesmaster.kernal.log.core.printer.file.naming.FileNameGenerator;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Log {@link Printer} using file system. When print a log, it will print it to the specified file.
 * <p>
 * Use the {@link Builder} to construct a {@link FilePrinter} object.
 * <p>
 * Created by zhaoqiang on 2017/6/14.
 */

public class FilePrinter implements ILifecyclePrinter {

    public static final StringBuilder DEVICE_DESC = new StringBuilder();
    public static final String FILE_LINE = "\n=====================================LINE========================================\n";
    /**
     * Use worker, write logs asynchronously.
     */
    private static final boolean USE_WORKER = true;
    private static final String THREAD_NAME = "file_printer_thread";

    static {
        try {
            DEVICE_DESC.append("band | ").append(android.os.Build.BRAND);
            DEVICE_DESC.append(" | model | ").append(android.os.Build.MODEL);
            DEVICE_DESC.append(" | android | ").append(android.os.Build.VERSION.RELEASE);
            DEVICE_DESC.append(" | w | ").append(BaseApplication.getApplication().getResources().getDisplayMetrics().widthPixels);
            DEVICE_DESC.append(" | h | ").append(BaseApplication.getApplication().getResources().getDisplayMetrics().heightPixels);
            DEVICE_DESC.append(" | sd | ").append(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED));
            DEVICE_DESC.append(" | appver | ").append(CommonUtils.getVersionName(BaseApplication.getApplication()));
        } catch (Exception e) {
        }
    }

    /**
     * The folder path of log file.
     */
    private final String folderPath;
    /**
     * The file name generator for log file.
     */
    private final FileNameGenerator fileNameGenerator;
    /**
     * The backup strategy for log file.
     */
    private final BackupStrategy backupStrategy;
    /**
     * The log flattener when print a log.
     */
    private Flattener flattener;
    /**
     * Log writer.
     */
    private Writer writer;
    /**
     * Lowest File Log Level
     */
    private int saveLogLevel = LogLevel.DEBUG;
    private volatile Worker worker;

    /*package*/ FilePrinter(Builder builder) {
        folderPath = builder.folderPath;
        fileNameGenerator = builder.fileNameGenerator;
        backupStrategy = builder.backupStrategy;
        flattener = builder.flattener;
        saveLogLevel = builder.saveLogLevel;

        writer = new Writer();
        if (USE_WORKER) {
            worker = new Worker();
        }

        checkLogFolder();
    }

    /**
     * Make sure the folder of log file exists.
     */
    private void checkLogFolder() {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    @Override
    public void println(int logLevel, String tag, String msg) {
        if (logLevel < saveLogLevel)
            return;
        if (USE_WORKER) {
            if (!worker.isStarted()) {
                worker.start();
            }
            worker.enqueue(new LogItem(logLevel, tag, msg));
        } else {
            doPrintln(logLevel, tag, msg);
        }
    }

    @Override
    public void println2(int logLevel, String tagStr, String fileName, String funName, int line, String msg) {
        println(logLevel, tagStr, msg);
    }

    /**
     * Do the real job of writing log to file.
     */
    void doPrintln(int logLevel, String tag, String msg) {
        String lastFileName = writer.getLastFileName();
        boolean isNewFile = false;
        if (lastFileName == null || fileNameGenerator.isFileNameChangeable()) {
            String newFileName = fileNameGenerator.generateFileName(logLevel, System.currentTimeMillis());
            if (newFileName == null || newFileName.trim().length() == 0) {
                throw new IllegalArgumentException("File name should not be empty.");
            }
            if (!newFileName.equals(lastFileName)) {
                if (writer.isOpened()) {
                    writer.close();
                }
                if (!new File(folderPath, newFileName).exists()) {
                    isNewFile = true;
                }
                if (!writer.open(newFileName)) {
                    return;
                }
                lastFileName = newFileName;
            }
        }

        if (!writer.getFile().exists()) {
            if (writer.isOpened()) {
                writer.close();
            }

            if (!writer.open(lastFileName)) {
                return;
            }

            isNewFile = true;

        }

        File lastFile = writer.getFile();
        if (backupStrategy.shouldBackup(lastFile)) {
            // Backup the log file, and create a new log file.
            writer.close();
            File backupFile = new File(folderPath, lastFileName + ".bak");
            if (backupFile.exists()) {
                backupFile.delete();
            }
            lastFile.renameTo(backupFile);
            if (!writer.open(lastFileName)) {
                return;
            }
        }
        String flattenedLog;
        if (isNewFile)
            flattenedLog = DEVICE_DESC.toString() + "\n\n" + flattener.flatten(logLevel, tag, msg).toString();
        else
            flattenedLog = flattener.flatten(logLevel, tag, msg).toString();
        writer.appendLog(flattenedLog);
    }

    @Override
    public void initIfNeed() {

    }

    @Override
    public void close() {

    }

    /**
     * Builder for {@link FilePrinter}.
     */
    public static class Builder {

        /**
         * The folder path of log file.
         */
        String folderPath;

        /**
         * The file name generator for log file.
         */
        FileNameGenerator fileNameGenerator;

        /**
         * The backup strategy for log file.
         */
        BackupStrategy backupStrategy;

        /**
         * The log flattener when print a log.
         */
        Flattener flattener;

        /**
         * Lowest File Log Level
         */
        private int saveLogLevel = LogLevel.DEBUG;

        /**
         * Construct a builder.
         *
         * @param folderPath the folder path of log file
         */
        public Builder(String folderPath) {
            this.folderPath = folderPath;
        }

        /**
         * Set the file name generator for log file.
         *
         * @param fileNameGenerator the file name generator for log file
         * @return the builder
         */
        public Builder fileNameGenerator(FileNameGenerator fileNameGenerator) {
            this.fileNameGenerator = fileNameGenerator;
            return this;
        }

        /**
         * Set the backup strategy for log file.
         *
         * @param backupStrategy the backup strategy for log file
         * @return the builder
         */
        public Builder backupStrategy(BackupStrategy backupStrategy) {
            this.backupStrategy = backupStrategy;
            return this;
        }

        /**
         * Set the log flattener when print a log.
         *
         * @param flattener the log flattener when print a log
         * @return the builder
         */
        public Builder logFlattener(Flattener flattener) {
            this.flattener = flattener;
            return this;
        }

        public Builder lowestLogLevel(int level) {
            this.saveLogLevel = level;
            return this;
        }

        /**
         * Build configured {@link FilePrinter} object.
         *
         * @return the built configured {@link FilePrinter} object
         */
        public FilePrinter build() {
            fillEmptyFields();
            return new FilePrinter(this);
        }

        private void fillEmptyFields() {
            if (fileNameGenerator == null) {
                fileNameGenerator = DefaultsFactory.createFileNameGenerator();
            }
            if (backupStrategy == null) {
                backupStrategy = DefaultsFactory.createBackupStrategy();
            }
            if (flattener == null) {
                flattener = DefaultsFactory.createFlattener();
            }
        }
    }

    private class LogItem {

        int level;
        String tag;
        String msg;

        LogItem(int level, String tag, String msg) {
            this.level = level;
            this.tag = tag;
            this.msg = msg;
        }
    }

    /**
     * Work in background, we can enqueue the logs, and the worker will dispatch them.
     */
    private class Worker implements Runnable {

        private BlockingQueue<LogItem> logs = new LinkedBlockingQueue<>();

        private volatile boolean started;

        /**
         * Enqueue the log.
         *
         * @param log the log to be written to file
         */
        void enqueue(LogItem log) {
            try {
                logs.put(log);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /**
         * Whether the worker is started.
         *
         * @return true if started, false otherwise
         */
        boolean isStarted() {
            synchronized (this) {
                return started;
            }
        }

        /**
         * Start the worker.
         */
        void start() {
            synchronized (this) {
                new Thread(this, THREAD_NAME).start();
                started = true;
            }
        }

        @Override
        public void run() {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            LogItem log;
            try {
                while ((log = logs.take()) != null) {
                    doPrintln(log.level, log.tag, log.msg);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
                synchronized (this) {
                    started = false;
                }
            }
        }
    }

    /**
     * Used to write the flattened logs to the log file.
     */
    private class Writer {

        /**
         * The file name of last used log file.
         */
        private String lastFileName;

        /**
         * The current log file.
         */
        private File logFile;

        private BufferedWriter bufferedWriter;

        /**
         * Whether the log file is opened.
         *
         * @return true if opened, false otherwise
         */
        boolean isOpened() {
            return bufferedWriter != null;
        }

        /**
         * Get the name of last used log file.
         *
         * @return the name of last used log file, maybe null
         */
        String getLastFileName() {
            return lastFileName;
        }

        /**
         * Get the current log file.
         *
         * @return the current log file, maybe null
         */
        File getFile() {
            return logFile;
        }

        /**
         * Open the file of specific name to be written into.
         *
         * @param newFileName the specific file name
         * @return true if opened successfully, false otherwise
         */
        boolean open(String newFileName) {
            lastFileName = newFileName;
            logFile = new File(folderPath, newFileName);

            // Create log file if not exists.
            if (!logFile.exists()) {
                try {
                    File parent = logFile.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                    logFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    lastFileName = null;
                    logFile = null;
                    return false;
                }
            }

            // Create buffered writer.
            try {
                bufferedWriter = new BufferedWriter(new FileWriter(logFile, true));
            } catch (Exception e) {
                e.printStackTrace();
                lastFileName = null;
                logFile = null;
                return false;
            }
            return true;
        }

        /**
         * Close the current log file if it is opened.
         *
         * @return true if closed successfully, false otherwise
         */
        boolean close() {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                } finally {
                    bufferedWriter = null;
                    lastFileName = null;
                    logFile = null;
                }
            }
            return true;
        }

        /**
         * Append the flattened log to the end of current opened log file.
         *
         * @param flattenedLog the flattened log
         */
        void appendLog(String flattenedLog) {
            try {
                bufferedWriter.write(flattenedLog);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            } catch (IOException e) {
            }
        }
    }
}
