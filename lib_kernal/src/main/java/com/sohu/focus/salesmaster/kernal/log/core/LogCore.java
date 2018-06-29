package com.sohu.focus.salesmaster.kernal.log.core;

import android.app.Application;

import com.sohu.focus.salesmaster.kernal.log.core.formatter.border.BorderFormatter;
import com.sohu.focus.salesmaster.kernal.log.core.formatter.message.json.JsonFormatter;
import com.sohu.focus.salesmaster.kernal.log.core.formatter.message.object.ObjectFormatter;
import com.sohu.focus.salesmaster.kernal.log.core.formatter.message.throwable.ThrowableFormatter;
import com.sohu.focus.salesmaster.kernal.log.core.formatter.message.xml.XmlFormatter;
import com.sohu.focus.salesmaster.kernal.log.core.formatter.stacktrace.StackTraceFormatter;
import com.sohu.focus.salesmaster.kernal.log.core.formatter.thread.ThreadFormatter;
import com.sohu.focus.salesmaster.kernal.log.core.interceptor.Interceptor;
import com.sohu.focus.salesmaster.kernal.log.core.internal.DefaultsFactory;
import com.sohu.focus.salesmaster.kernal.log.core.internal.Platform;
import com.sohu.focus.salesmaster.kernal.log.core.internal.util.StackTraceUtil;
import com.sohu.focus.salesmaster.kernal.log.core.printer.Printer;
import com.sohu.focus.salesmaster.kernal.log.core.printer.PrinterSet;


/**
 * A log tool which can be used in android or java, the most important feature is it can print the
 * logs to multiple place in the same time, such as android shell, console and file, you can
 * even print the log to the remote server if you want, all of these can be done just within one
 * calling.
 * <br>Also, LogCore is very flexible, almost every component is replaceable.
 * <p>
 * <b>How to use in a general way:</b>
 * <p>
 * <b>1. Initial the log system.</b>
 * <br>Using one of
 * <br>{@link LogCore#init()}
 * <br>{@link LogCore#init(int)},
 * <br>{@link LogCore#init(LogConfiguration)}
 * <br>{@link LogCore#init(Printer...)},
 * <br>{@link LogCore#init(int, Printer...)},
 * <br>{@link LogCore#init(LogConfiguration, Printer...)},
 * <br>that will setup a {@link LogEngine} for a global usage.
 * If you want to use a customized configuration instead of the global one to log something, you can
 * start a customization logging.
 * <p>
 * For android, a best place to do the initialization is {@link Application#onCreate()}.
 * <p>
 * <b>2. Start to log.</b>
 * <p>
 * <b>How to use in a dynamically customizing way after initializing the log system:</b>
 * <p>
 * <b>1. Start a customization.</b>
 * <br>Call any of
 * <br>{@link #logLevel(int)}
 * <br>{@link #tag(String)},
 * <br>{@link #t()},
 * <br>{@link #nt()},
 * <br>{@link #st(int)},
 * <br>{@link #nst()},
 * <br>{@link #b()},
 * <br>{@link #nb()},
 * <br>{@link #jsonFormatter(JsonFormatter)},
 * <br>{@link #xmlFormatter(XmlFormatter)},
 * <br>{@link #threadFormatter(ThreadFormatter)},
 * <br>{@link #stackTraceFormatter(StackTraceFormatter)},
 * <br>{@link #throwableFormatter(ThrowableFormatter)}
 * <br>{@link #borderFormatter(BorderFormatter)}
 * <br>{@link #addObjectFormatter(Class, ObjectFormatter)}
 * <br>{@link #addInterceptor(Interceptor)}
 * <br>{@link #printers(Printer...)},
 * <br>it will return a {@link LogEngine.Builder} object.
 * <p>
 * <b>2. Finish the customization.</b>
 * <br>Continue to setup other fields of the returned {@link LogEngine.Builder}.
 * <p>
 * <b>3. Build a dynamically generated {@link LogEngine}.</b>
 * <br>Call the {@link LogEngine.Builder#build()} of the returned {@link LogEngine.Builder}.
 * <p>
 * <b>4. Start to log.</b>
 * <br>The logging methods of a {@link LogEngine} is completely same as that ones in {@link LogCore}.
 * <br>As a convenience, you can ignore the step 3, just call the logging methods of
 * {@link LogEngine.Builder}, it will automatically build a {@link LogEngine} and call the target
 * logging method.
 * <p>
 * <b>Compatibility:</b>
 * <p>
 * In order to be compatible with {@link android.util.Log}, all the methods of
 * {@link android.util.Log} are supported here.
 * See:
 * <br>{@link Log#v(String, String)}, {@link Log#v(String, String, Throwable)}
 * <br>{@link Log#d(String, String)}, {@link Log#d(String, String, Throwable)}
 * <br>{@link Log#i(String, String)}, {@link Log#i(String, String, Throwable)}
 * <br>{@link Log#w(String, String)}, {@link Log#w(String, String, Throwable)}
 * <br>{@link Log#wtf(String, String)}, {@link Log#wtf(String, String, Throwable)}
 * <br>{@link Log#e(String, String)}, {@link Log#e(String, String, Throwable)}
 * <br>{@link Log#println(int, String, String)}
 * <br>{@link Log#isLoggable(String, int)}
 * <br>{@link Log#getStackTraceString(Throwable)}
 *
 * Created by zhaoqiang on 2017/6/14.
 */

public class LogCore {

    /**
     * Global log configuration.
     */
    static LogConfiguration sLogConfiguration;
    /**
     * Global log printer.
     */
    static Printer sPrinter;
    static boolean sIsInitialized;
    /**
     * Global logger for all direct logging via {@link LogCore}.
     */
    private static LogEngine sLogEngine;

    /**
     * Prevent instance.
     */
    private LogCore() {
    }

    /**
     * get mode({@link LogConfiguration#LOG_MODE_PRINTLN}
     *      and {@link LogConfiguration#LOG_MODE_PRINTLN2}
     *      default is {@link LogConfiguration#LOG_MODE_PRINTLN} )
     *
     * @return
     */
    public static int getLogMode() {
        return sLogConfiguration.logMode;
    }

    /**
     * Initialize log system, should be called only once.
     *
     * @since 1.3.0
     */
    public static void init() {
        init(new LogConfiguration.Builder().build(), DefaultsFactory.createPrinter());
    }

    /**
     * Initialize log system, should be called only once.
     *
     * @param logLevel the log level, logs with a lower level than which would not be printed
     */
    public static void init(int logLevel) {
        init(new LogConfiguration.Builder().logLevel(logLevel).build(),
                DefaultsFactory.createPrinter());
    }

    /**
     * Initialize log system, should be called only once.
     *
     * @param logLevel         the log level, logs with a lower level than which would not be printed
     * @param logConfiguration the log configuration
     * @deprecated the log level is part of log configuration now, use {@link #init(LogConfiguration)}
     * instead, since 1.3.0
     */
    @Deprecated
    public static void init(int logLevel, LogConfiguration logConfiguration) {
        init(new LogConfiguration.Builder(logConfiguration).logLevel(logLevel).build());
    }

    /**
     * Initialize log system, should be called only once.
     *
     * @param logConfiguration the log configuration
     * @since 1.3.0
     */
    public static void init(LogConfiguration logConfiguration) {
        init(logConfiguration, DefaultsFactory.createPrinter());
    }

    /**
     * Initialize log system, should be called only once.
     *
     * @param printers the printers, each log would be printed by all of the printers
     * @since 1.3.0
     */
    public static void init(Printer... printers) {
        init(new LogConfiguration.Builder().build(), printers);
    }

    /**
     * Initialize log system, should be called only once.
     *
     * @param logLevel the log level, logs with a lower level than which would not be printed
     * @param printers the printers, each log would be printed by all of the printers
     */
    public static void init(int logLevel, Printer... printers) {
        init(new LogConfiguration.Builder().logLevel(logLevel).build(), printers);
    }

    /**
     * Initialize log system, should be called only once.
     *
     * @param logLevel         the log level, logs with a lower level than which would not be printed
     * @param logConfiguration the log configuration
     * @param printers         the printers, each log would be printed by all of the printers
     * @deprecated the log level is part of log configuration now,
     * use {@link #init(LogConfiguration, Printer...)} instead, since 1.3.0
     */
    @Deprecated
    public static void init(int logLevel, LogConfiguration logConfiguration, Printer... printers) {
        init(new LogConfiguration.Builder(logConfiguration).logLevel(logLevel).build(), printers);
    }

    /**
     * Initialize log system, should be called only once.
     *
     * @param logConfiguration the log configuration
     * @param printers         the printers, each log would be printed by all of the printers
     * @since 1.3.0
     */
    public static void init(LogConfiguration logConfiguration, Printer... printers) {
        if (sIsInitialized) {
            Platform.get().warn("LogCore is already initialized, do not initialize again");
        }
        sIsInitialized = true;

        if (logConfiguration == null) {
            throw new IllegalArgumentException("Please specify a LogConfiguration");
        }
        sLogConfiguration = logConfiguration;

        sPrinter = new PrinterSet(printers);

        sLogEngine = new LogEngine(sLogConfiguration, sPrinter);
    }

    /**
     * Throw an IllegalStateException if not initialized.
     */
    static void assertInitialization() {
        if (!sIsInitialized) {
            throw new IllegalStateException("Do you forget to initialize LogCore?");
        }
    }

    /**
     * Start to customize a {@link LogEngine} and set the log level.
     *
     * @param logLevel the log level to customize
     * @return the {@link LogEngine.Builder} to build the {@link LogEngine}
     * @since 1.3.0
     */
    public static LogEngine.Builder logLevel(int logLevel) {
        return new LogEngine.Builder().logLevel(logLevel);
    }

    /**
     * Start to customize a {@link LogEngine} and set the tag.
     *
     * @param tag the tag to customize
     * @return the {@link LogEngine.Builder} to build the {@link LogEngine}
     */
    public static LogEngine.Builder tag(String tag) {
        return new LogEngine.Builder().tag(tag);
    }

    /**
     * Start to customize a {@link LogEngine} and enable thread info.
     *
     * @return the {@link LogEngine.Builder} to build the {@link LogEngine}
     */
    public static LogEngine.Builder t() {
        return new LogEngine.Builder().t();
    }

    /**
     * Start to customize a {@link LogEngine} and disable thread info.
     *
     * @return the {@link LogEngine.Builder} to build the {@link LogEngine}
     */
    public static LogEngine.Builder nt() {
        return new LogEngine.Builder().nt();
    }

    /**
     * Start to customize a {@link LogEngine} and enable stack trace.
     *
     * @param depth the number of stack trace elements we should log, 0 if no limitation
     * @return the {@link LogEngine.Builder} to build the {@link LogEngine}
     */
    public static LogEngine.Builder st(int depth) {
        return new LogEngine.Builder().st(depth);
    }

    /**
     * Start to customize a {@link LogEngine} and enable stack trace.
     *
     * @param stackTraceOrigin the origin of stack trace elements from which we should NOT log,
     *                         it can be a package name like "com.elvishew.xlog", a class name
     *                         like "com.yourdomain.logWrapper", or something else between
     *                         package name and class name, like "com.yourdomain.".
     *                         It is mostly used when you are using a logger wrapper
     * @param depth            the number of stack trace elements we should log, 0 if no limitation
     * @return the {@link LogEngine.Builder} to build the {@link LogEngine}
     * @since 1.4.0
     */
    public static LogEngine.Builder st(String stackTraceOrigin, int depth) {
        return new LogEngine.Builder().st(stackTraceOrigin, depth);
    }

    /**
     * Start to customize a {@link LogEngine} and disable stack trace.
     *
     * @return the {@link LogEngine.Builder} to build the {@link LogEngine}
     */
    public static LogEngine.Builder nst() {
        return new LogEngine.Builder().nst();
    }

    /**
     * Start to customize a {@link LogEngine} and enable border.
     *
     * @return the {@link LogEngine.Builder} to build the {@link LogEngine}
     */
    public static LogEngine.Builder b() {
        return new LogEngine.Builder().b();
    }

    /**
     * Start to customize a {@link LogEngine} and disable border.
     *
     * @return the {@link LogEngine.Builder} to build the {@link LogEngine}
     */
    public static LogEngine.Builder nb() {
        return new LogEngine.Builder().nb();
    }

    /**
     * Start to customize a {@link LogEngine} and set the {@link JsonFormatter}.
     *
     * @param jsonFormatter the {@link JsonFormatter} to customize
     * @return the {@link LogEngine.Builder} to build the {@link LogEngine}
     */
    public static LogEngine.Builder jsonFormatter(JsonFormatter jsonFormatter) {
        return new LogEngine.Builder().jsonFormatter(jsonFormatter);
    }

    /**
     * Start to customize a {@link LogEngine} and set the {@link XmlFormatter}.
     *
     * @param xmlFormatter the {@link XmlFormatter} to customize
     * @return the {@link LogEngine.Builder} to build the {@link LogEngine}
     */
    public static LogEngine.Builder xmlFormatter(XmlFormatter xmlFormatter) {
        return new LogEngine.Builder().xmlFormatter(xmlFormatter);
    }

    /**
     * Start to customize a {@link LogEngine} and set the {@link ThrowableFormatter}.
     *
     * @param throwableFormatter the {@link ThrowableFormatter} to customize
     * @return the {@link LogEngine.Builder} to build the {@link LogEngine}
     */
    public static LogEngine.Builder throwableFormatter(ThrowableFormatter throwableFormatter) {
        return new LogEngine.Builder().throwableFormatter(throwableFormatter);
    }

    /**
     * Start to customize a {@link LogEngine} and set the {@link ThreadFormatter}.
     *
     * @param threadFormatter the {@link ThreadFormatter} to customize
     * @return the {@link LogEngine.Builder} to build the {@link LogEngine}
     */
    public static LogEngine.Builder threadFormatter(ThreadFormatter threadFormatter) {
        return new LogEngine.Builder().threadFormatter(threadFormatter);
    }

    /**
     * Start to customize a {@link LogEngine} and set the {@link StackTraceFormatter}.
     *
     * @param stackTraceFormatter the {@link StackTraceFormatter} to customize
     * @return the {@link LogEngine.Builder} to build the {@link LogEngine}
     */
    public static LogEngine.Builder stackTraceFormatter(StackTraceFormatter stackTraceFormatter) {
        return new LogEngine.Builder().stackTraceFormatter(stackTraceFormatter);
    }

    /**
     * Start to customize a {@link LogEngine} and set the {@link BorderFormatter}.
     *
     * @param borderFormatter the {@link BorderFormatter} to customize
     * @return the {@link LogEngine.Builder} to build the {@link LogEngine}
     */
    public static LogEngine.Builder borderFormatter(BorderFormatter borderFormatter) {
        return new LogEngine.Builder().borderFormatter(borderFormatter);
    }

    /**
     * Start to customize a {@link LogEngine} and add an object formatter for specific class of object.
     *
     * @param objectClass     the class of object
     * @param objectFormatter the object formatter to add
     * @param <T>             the type of object
     * @return the {@link LogEngine.Builder} to build the {@link LogEngine}
     * @since 1.1.0
     */
    public static <T> LogEngine.Builder addObjectFormatter(Class<T> objectClass,
                                                           ObjectFormatter<? super T> objectFormatter) {
        return new LogEngine.Builder().addObjectFormatter(objectClass, objectFormatter);
    }

    /**
     * Start to customize a {@link LogEngine} and add an interceptor.
     *
     * @param interceptor the interceptor to add
     * @return the {@link LogEngine.Builder} to build the {@link LogEngine}
     * @since 1.3.0
     */
    public static LogEngine.Builder addInterceptor(Interceptor interceptor) {
        return new LogEngine.Builder().addInterceptor(interceptor);
    }

    /**
     * Start to customize a {@link LogEngine} and set the {@link Printer} array.
     *
     * @param printers the {@link Printer} array to customize
     * @return the {@link LogEngine.Builder} to build the {@link LogEngine}
     */
    public static LogEngine.Builder printers(Printer... printers) {
        return new LogEngine.Builder().printers(printers);
    }

    /**
     * Log an object with level {@link LogLevel#VERBOSE}.
     *
     * @param object the object to log
     * @see LogConfiguration.Builder#addObjectFormatter(Class, ObjectFormatter)
     * @since 1.1.0
     */
    public static void v(Object object) {
        assertInitialization();
        sLogEngine.v(sLogConfiguration.tag, object);
    }

    public static void v(String tag, Object object) {
        assertInitialization();
        sLogEngine.v(tag, object);
    }

    /**
     * Log an array with level {@link LogLevel#VERBOSE}.
     *
     * @param array the array to log
     */
    public static void v(Object[] array) {
        assertInitialization();
        sLogEngine.v(sLogConfiguration.tag, array);
    }

    public static void v(String tag, Object[] array) {
        assertInitialization();
        sLogEngine.v(tag, array);
    }


    /**
     * Log a message with level {@link LogLevel#VERBOSE}.
     *
     * @param msg the message to log
     */
    public static void v(String msg) {
        assertInitialization();
        sLogEngine.v(sLogConfiguration.tag, msg);
    }

    public static void v(String tag,String msg) {
        assertInitialization();
        sLogEngine.v(tag, msg);
    }

    /**
     * Log a message and a throwable with level {@link LogLevel#VERBOSE}.
     *
     * @param msg the message to log
     * @param tr  the throwable to be log
     */
    public static void v(String msg, Throwable tr) {
        assertInitialization();
        sLogEngine.v(sLogConfiguration.tag, msg, tr);
    }

    public static void v(String tag,String msg, Throwable tr) {
        assertInitialization();
        sLogEngine.v(tag, msg, tr);
    }

    /**
     * Log an object with level {@link LogLevel#DEBUG}.
     *
     * @param object the object to log
     * @see LogConfiguration.Builder#addObjectFormatter(Class, ObjectFormatter)
     * @since 1.1.0
     */
    public static void d(Object object) {
        assertInitialization();
        sLogEngine.d(sLogConfiguration.tag, object);
    }

    public static void d(String tag,Object object) {
        assertInitialization();
        sLogEngine.d(tag, object);
    }

    /**
     * Log an array with level {@link LogLevel#DEBUG}.
     *
     * @param array the array to log
     */
    public static void d(String tag, Object[] array) {
        assertInitialization();
        sLogEngine.d(tag, array);
    }

    public static void d(Object[] array) {
        assertInitialization();
        sLogEngine.d(sLogConfiguration.tag, array);
    }


    /**
     * Log a message with level {@link LogLevel#DEBUG}.
     *
     * @param msg the message to log
     */
    public static void d(String msg) {
        assertInitialization();
        sLogEngine.d(sLogConfiguration.tag, msg);
    }

    public static void d(String tag,String msg) {
        assertInitialization();
        sLogEngine.d(tag, msg);
    }

    /**
     * Log a message and a throwable with level {@link LogLevel#DEBUG}.
     *
     * @param msg the message to log
     * @param tr  the throwable to be log
     */
    public static void d(String msg, Throwable tr) {
        assertInitialization();
        sLogEngine.d(sLogConfiguration.tag, msg, tr);
    }

    public static void d(String tag,String msg, Throwable tr) {
        assertInitialization();
        sLogEngine.d(tag, msg, tr);
    }

    /**
     * Log an object with level {@link LogLevel#INFO}.
     *
     * @param object the object to log
     * @see LogConfiguration.Builder#addObjectFormatter(Class, ObjectFormatter)
     * @since 1.1.0
     */
    public static void i(Object object) {
        assertInitialization();
        sLogEngine.i(sLogConfiguration.tag, object);
    }

    public static void i(String tag,Object object) {
        assertInitialization();
        sLogEngine.i(tag, object);
    }

    /**
     * Log an array with level {@link LogLevel#INFO}.
     *
     * @param array the array to log
     */
    public static void i(Object[] array) {
        assertInitialization();
        sLogEngine.i(sLogConfiguration.tag, array);
    }

    public static void i(String tag,Object[] array) {
        assertInitialization();
        sLogEngine.i(tag, array);
    }

    /**
     * Log a message with level {@link LogLevel#INFO}.
     *
     * @param msg the message to log
     */
    public static void i(String msg) {
        assertInitialization();
        sLogEngine.i(sLogConfiguration.tag, msg);
    }

    public static void i(String tag,String msg) {
        assertInitialization();
        sLogEngine.i(tag, msg);
    }

    /**
     * Log a message and a throwable with level {@link LogLevel#INFO}.
     *
     * @param msg the message to log
     * @param tr  the throwable to be log
     */
    public static void i(String msg, Throwable tr) {
        assertInitialization();
        sLogEngine.i(sLogConfiguration.tag, msg, tr);
    }

    public static void i(String tag,String msg, Throwable tr) {
        assertInitialization();
        sLogEngine.i(tag, msg, tr);
    }

    /**
     * Log an object with level {@link LogLevel#WARN}.
     *
     * @param object the object to log
     * @see LogConfiguration.Builder#addObjectFormatter(Class, ObjectFormatter)
     * @since 1.1.0
     */
    public static void w(Object object) {
        assertInitialization();
        sLogEngine.w(sLogConfiguration.tag, object);
    }

    public static void w(String tag,Object object) {
        assertInitialization();
        sLogEngine.w(tag, object);
    }

    /**
     * Log an array with level {@link LogLevel#WARN}.
     *
     * @param array the array to log
     */
    public static void w(Object[] array) {
        assertInitialization();
        sLogEngine.w(sLogConfiguration.tag, array);
    }

    public static void w(String tag,Object[] array) {
        assertInitialization();
        sLogEngine.w(tag, array);
    }

    /**
     * Log a message with level {@link LogLevel#WARN}.
     *
     * @param msg the message to log
     */
    public static void w(String msg) {
        assertInitialization();
        sLogEngine.w(sLogConfiguration.tag, msg);
    }

    public static void w(String tag,String msg) {
        assertInitialization();
        sLogEngine.w(tag, msg);
    }

    /**
     * Log a message and a throwable with level {@link LogLevel#WARN}.
     *
     * @param msg the message to log
     * @param tr  the throwable to be log
     */
    public static void w(String msg, Throwable tr) {
        assertInitialization();
        sLogEngine.w(sLogConfiguration.tag, msg, tr);
    }

    public static void w(String tag,String msg, Throwable tr) {
        assertInitialization();
        sLogEngine.w(tag, msg, tr);
    }

    /**
     * Log an object with level {@link LogLevel#ERROR}.
     *
     * @param object the object to log
     * @see LogConfiguration.Builder#addObjectFormatter(Class, ObjectFormatter)
     * @since 1.1.0
     */
    public static void e(Object object) {
        assertInitialization();
        sLogEngine.e(sLogConfiguration.tag, object);
    }

    public static void e(String tag,Object object) {
        assertInitialization();
        sLogEngine.e(tag, object);
    }

    /**
     * Log an array with level {@link LogLevel#ERROR}.
     *
     * @param array the array to log
     */
    public static void e(Object[] array) {
        assertInitialization();
        sLogEngine.e(sLogConfiguration.tag, array);
    }

    public static void e(String tag,Object[] array) {
        assertInitialization();
        sLogEngine.e(tag, array);
    }

    /**
     * Log a message with level {@link LogLevel#ERROR}.
     *
     * @param msg the message to log
     */
    public static void e(String msg) {
        assertInitialization();
        sLogEngine.e(sLogConfiguration.tag, msg);
    }

    public static void e(String tag,String msg) {
        assertInitialization();
        sLogEngine.e(tag, msg);
    }

    /**
     * Log a message and a throwable with level {@link LogLevel#ERROR}.
     *
     * @param msg the message to log
     * @param tr  the throwable to be log
     */
    public static void e(String msg, Throwable tr) {
        assertInitialization();
        sLogEngine.e(sLogConfiguration.tag, msg, tr);
    }

    public static void e(String tag,String msg, Throwable tr) {
        assertInitialization();
        sLogEngine.e(tag, msg, tr);
    }

    /**
     * Log an object with specific log level.
     *
     * @param logLevel the specific log level
     * @param object   the object to log
     * @see LogConfiguration.Builder#addObjectFormatter(Class, ObjectFormatter)
     * @since 1.4.0
     */
    public static void log(int logLevel, Object object) {
        assertInitialization();
        sLogEngine.log(logLevel,sLogConfiguration.tag, object);
    }

    public static void log(int logLevel,String tag, Object object) {
        assertInitialization();
        sLogEngine.log(logLevel,tag, object);
    }

    /**
     * Log an array with specific log level.
     *
     * @param logLevel the specific log level
     * @param array    the array to log
     * @since 1.4.0
     */
    public static void log(int logLevel, Object[] array) {
        assertInitialization();
        sLogEngine.log(logLevel,sLogConfiguration.tag, array);
    }

    public static void log(int logLevel,String tag, Object[] array) {
        assertInitialization();
        sLogEngine.log(logLevel,tag, array);
    }


    /**
     * Log a message with specific log level.
     *
     * @param logLevel the specific log level
     * @param msg      the message to log
     * @since 1.4.0
     */
    public static void log(int logLevel, String msg) {
        assertInitialization();
        sLogEngine.log(logLevel,sLogConfiguration.tag, msg);
    }

    public static void log(int logLevel,String tag, String msg) {
        assertInitialization();
        sLogEngine.log(logLevel,tag, msg);
    }

    public static void log(int logLevel, String tagStr, String fileName, String funName, int line, String msg) {
        assertInitialization();
        sLogEngine.log(logLevel,tagStr,fileName,funName,line, msg);
    }

    /**
     * Log a message and a throwable with specific log level.
     *
     * @param logLevel the specific log level
     * @param msg      the message to log
     * @param tr       the throwable to be log
     * @since 1.4.0
     */
    public static void log(int logLevel, String msg, Throwable tr) {
        assertInitialization();
        sLogEngine.log(logLevel,sLogConfiguration.tag, msg, tr);
    }

    public static void log(int logLevel,String tag, String msg, Throwable tr) {
        assertInitialization();
        sLogEngine.log(logLevel,tag, msg, tr);
    }

    /**
     * Log a JSON string, with level {@link LogLevel#DEBUG} by default.
     *
     * @param json the JSON string to log
     */
    public static void json(String json) {
        assertInitialization();
        sLogEngine.json(sLogConfiguration.tag,json);
    }

    public static void json(String tag,String json) {
        assertInitialization();
        sLogEngine.json(tag,json);
    }

    /**
     * Log a XML string, with level {@link LogLevel#DEBUG} by default.
     *
     * @param xml the XML string to log
     */
    public static void xml(String xml) {
        assertInitialization();
        sLogEngine.xml(sLogConfiguration.tag,xml);
    }

    public static void xml(String tag,String xml) {
        assertInitialization();
        sLogEngine.xml(tag,xml);
    }

    /**
     * Compatible class with {@link android.util.Log}.
     *
     * @deprecated please use {@link LogCore} instead
     */
    public static class Log {

        /**
         * @deprecated compatible with {@link android.util.Log#v(String, String)}
         */
        public static void v(String tag, String msg) {
            tag(tag).build().v(tag,msg);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#v(String, String, Throwable)}
         */
        public static void v(String tag, String msg, Throwable tr) {
            tag(tag).build().v(msg, tr);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#d(String, String)}
         */
        public static void d(String tag, String msg) {
            tag(tag).build().d(tag,msg);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#d(String, String, Throwable)}
         */
        public static void d(String tag, String msg, Throwable tr) {
            tag(tag).build().d(msg, tr);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#i(String, String)}
         */
        public static void i(String tag, String msg) {
            tag(tag).build().i(tag,msg);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#i(String, String, Throwable)}
         */
        public static void i(String tag, String msg, Throwable tr) {
            tag(tag).build().i(msg, tr);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#w(String, String)}
         */
        public static void w(String tag, String msg) {
            tag(tag).build().w(tag,msg);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#w(String, String, Throwable)}
         */
        public static void w(String tag, String msg, Throwable tr) {
            tag(tag).build().w(msg, tr);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#w(String, Throwable)}
         */
        public static void w(String tag, Throwable tr) {
            tag(tag).build().w("", tr);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#e(String, String)}
         */
        public static void e(String tag, String msg) {
            tag(tag).build().e(tag,msg);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#e(String, String, Throwable)}
         */
        public static void e(String tag, String msg, Throwable tr) {
            tag(tag).build().e(msg, tr);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#wtf(String, String)}
         */
        public static void wtf(String tag, String msg) {
            e(tag, msg);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#wtf(String, Throwable)}
         */
        public static void wtf(String tag, Throwable tr) {
            wtf(tag, "", tr);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#wtf(String, String, Throwable)}
         */
        public static void wtf(String tag, String msg, Throwable tr) {
            e(tag, msg, tr);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#println(int, String, String)}
         */
        public static void println(int logLevel, String tag, String msg) {
            tag(tag).build().println(logLevel, msg);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#isLoggable(String, int)}
         */
        public static boolean isLoggable(String tag, int level) {
            return sLogConfiguration.isLoggable(level);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#getStackTraceString(Throwable)}
         */
        public static String getStackTraceString(Throwable tr) {
            return StackTraceUtil.getStackTraceString(tr);
        }
    }
}
