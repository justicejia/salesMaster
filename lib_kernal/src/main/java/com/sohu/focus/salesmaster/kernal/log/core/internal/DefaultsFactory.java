package com.sohu.focus.salesmaster.kernal.log.core.internal;

import android.content.Intent;
import android.os.Bundle;


import com.sohu.focus.salesmaster.kernal.log.core.flattener.DefaultFlattener;
import com.sohu.focus.salesmaster.kernal.log.core.flattener.Flattener;
import com.sohu.focus.salesmaster.kernal.log.core.formatter.border.BorderFormatter;
import com.sohu.focus.salesmaster.kernal.log.core.formatter.border.DefaultBorderFormatter;
import com.sohu.focus.salesmaster.kernal.log.core.formatter.message.json.DefaultJsonFormatter;
import com.sohu.focus.salesmaster.kernal.log.core.formatter.message.json.JsonFormatter;
import com.sohu.focus.salesmaster.kernal.log.core.formatter.message.object.BundleFormatter;
import com.sohu.focus.salesmaster.kernal.log.core.formatter.message.object.IntentFormatter;
import com.sohu.focus.salesmaster.kernal.log.core.formatter.message.object.ObjectFormatter;
import com.sohu.focus.salesmaster.kernal.log.core.formatter.message.throwable.DefaultThrowableFormatter;
import com.sohu.focus.salesmaster.kernal.log.core.formatter.message.throwable.ThrowableFormatter;
import com.sohu.focus.salesmaster.kernal.log.core.formatter.message.xml.DefaultXmlFormatter;
import com.sohu.focus.salesmaster.kernal.log.core.formatter.message.xml.XmlFormatter;
import com.sohu.focus.salesmaster.kernal.log.core.formatter.stacktrace.DefaultStackTraceFormatter;
import com.sohu.focus.salesmaster.kernal.log.core.formatter.stacktrace.StackTraceFormatter;
import com.sohu.focus.salesmaster.kernal.log.core.formatter.thread.DefaultThreadFormatter;
import com.sohu.focus.salesmaster.kernal.log.core.formatter.thread.ThreadFormatter;
import com.sohu.focus.salesmaster.kernal.log.core.printer.Printer;
import com.sohu.focus.salesmaster.kernal.log.core.printer.file.backup.BackupStrategy;
import com.sohu.focus.salesmaster.kernal.log.core.printer.file.backup.FileSizeBackupStrategy;
import com.sohu.focus.salesmaster.kernal.log.core.printer.file.naming.ChangelessFileNameGenerator;
import com.sohu.focus.salesmaster.kernal.log.core.printer.file.naming.FileNameGenerator;
import com.sohu.focus.salesmaster.kernal.log.core.printer.file.FilePrinter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory for providing default implementation.
 *
 * Created by zhaoqiang on 2017/6/14.
 */

public class DefaultsFactory {

    private static final String DEFAULT_LOG_FILE_NAME = "log";

    private static final long DEFAULT_LOG_FILE_MAX_SIZE = 1024 * 1024; // 1M bytes;

    private static final Map<Class<?>, ObjectFormatter<?>> BUILTIN_OBJECT_FORMATTERS;

    static {
        Map<Class<?>, ObjectFormatter<?>> objectFormatters = new HashMap<>();
        objectFormatters.put(Bundle.class, new BundleFormatter());
        objectFormatters.put(Intent.class, new IntentFormatter());
        BUILTIN_OBJECT_FORMATTERS = Collections.unmodifiableMap(objectFormatters);
    }

    /**
     * Create the default JSON formatter.
     */
    public static JsonFormatter createJsonFormatter() {
        return new DefaultJsonFormatter();
    }

    /**
     * Create the default XML formatter.
     */
    public static XmlFormatter createXmlFormatter() {
        return new DefaultXmlFormatter();
    }

    /**
     * Create the default throwable formatter.
     */
    public static ThrowableFormatter createThrowableFormatter() {
        return new DefaultThrowableFormatter();
    }

    /**
     * Create the default thread formatter.
     */
    public static ThreadFormatter createThreadFormatter() {
        return new DefaultThreadFormatter();
    }

    /**
     * Create the default stack trace formatter.
     */
    public static StackTraceFormatter createStackTraceFormatter() {
        return new DefaultStackTraceFormatter();
    }

    /**
     * Create the default border formatter.
     */
    public static BorderFormatter createBorderFormatter() {
        return new DefaultBorderFormatter();
    }

    /**
     * Create the default log flattener.
     */
    public static Flattener createFlattener() {
        return new DefaultFlattener();
    }

    /**
     * Create the default printer.
     */
    public static Printer createPrinter() {
        return Platform.get().defaultPrinter();
    }

    /**
     * Create the default file name generator for {@link FilePrinter}.
     */
    public static FileNameGenerator createFileNameGenerator() {
        return new ChangelessFileNameGenerator(DEFAULT_LOG_FILE_NAME);
    }

    /**
     * Create the default backup strategy for {@link FilePrinter}.
     */
    public static BackupStrategy createBackupStrategy() {
        return new FileSizeBackupStrategy(DEFAULT_LOG_FILE_MAX_SIZE);
    }

    /**
     * Get the builtin object formatters.
     *
     * @return the builtin object formatters
     */
    public static Map<Class<?>, ObjectFormatter<?>> builtinObjectFormatters() {
        return BUILTIN_OBJECT_FORMATTERS;
    }
}
