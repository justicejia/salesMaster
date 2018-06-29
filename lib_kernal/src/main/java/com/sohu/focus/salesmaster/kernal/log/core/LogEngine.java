package com.sohu.focus.salesmaster.kernal.log.core;

import com.sohu.focus.salesmaster.kernal.log.core.formatter.border.BorderFormatter;
import com.sohu.focus.salesmaster.kernal.log.core.formatter.message.json.JsonFormatter;
import com.sohu.focus.salesmaster.kernal.log.core.formatter.message.object.ObjectFormatter;
import com.sohu.focus.salesmaster.kernal.log.core.formatter.message.throwable.ThrowableFormatter;
import com.sohu.focus.salesmaster.kernal.log.core.formatter.message.xml.XmlFormatter;
import com.sohu.focus.salesmaster.kernal.log.core.formatter.stacktrace.StackTraceFormatter;
import com.sohu.focus.salesmaster.kernal.log.core.formatter.thread.ThreadFormatter;
import com.sohu.focus.salesmaster.kernal.log.core.interceptor.Interceptor;
import com.sohu.focus.salesmaster.kernal.log.core.internal.DefaultsFactory;
import com.sohu.focus.salesmaster.kernal.log.core.internal.SystemCompat;
import com.sohu.focus.salesmaster.kernal.log.core.internal.util.StackTraceUtil;
import com.sohu.focus.salesmaster.kernal.log.core.printer.Printer;
import com.sohu.focus.salesmaster.kernal.log.core.printer.PrinterSet;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A logger is used to do the real logging work, can use multiple log printers to print the log.
 * <p>
 * A {@link LogEngine} is always generated and mostly accessed by {@link LogCore}, but for customization
 * purpose, you can configure a {@link LogEngine} via the {@link Builder} which is returned by
 * {@link LogCore} when you trying to start a customization using {@link LogCore#tag(String)}
 * or other configuration method, and to use the customized {@link LogEngine}, you should call
 * the {@link Builder#build()} to build a {@link LogEngine}, and then you can log using
 * the {@link LogEngine} assuming that you are using the {@link LogCore} directly.
 * Created by zhaoqiang on 2017/6/14.
 */

public class LogEngine {

    /**
     * The log configuration which you should respect to when logging.
     */
    private LogConfiguration logConfiguration;

    /**
     * The log printer used to print the logs.
     */
    private Printer printer;

    /**
     * Construct a logger.
     *
     * @param logConfiguration the log configuration which you should respect to when logging
     * @param printer          the log printer used to print the log
     */
    /*package*/ LogEngine(LogConfiguration logConfiguration, Printer printer) {
        this.logConfiguration = logConfiguration;
        this.printer = printer;
    }

    /**
     * Construct a logger using builder.
     *
     * @param builder the logger builder
     */
    /*package*/ LogEngine(Builder builder) {
        LogConfiguration.Builder logConfigBuilder = new LogConfiguration.Builder(
                LogCore.sLogConfiguration);

        if (builder.logLevel != 0) {
            logConfigBuilder.logLevel(builder.logLevel);
        }

        if (builder.tag != null) {
            logConfigBuilder.tag(builder.tag);
        }

        if (builder.threadSet) {
            if (builder.withThread) {
                logConfigBuilder.t();
            } else {
                logConfigBuilder.nt();
            }
        }
        if (builder.stackTraceSet) {
            if (builder.withStackTrace) {
                logConfigBuilder.st(builder.stackTraceOrigin, builder.stackTraceDepth);
            } else {
                logConfigBuilder.nst();
            }
        }
        if (builder.borderSet) {
            if (builder.withBorder) {
                logConfigBuilder.b();
            } else {
                logConfigBuilder.nb();
            }
        }

        if (builder.jsonFormatter != null) {
            logConfigBuilder.jsonFormatter(builder.jsonFormatter);
        }
        if (builder.xmlFormatter != null) {
            logConfigBuilder.xmlFormatter(builder.xmlFormatter);
        }
        if (builder.throwableFormatter != null) {
            logConfigBuilder.throwableFormatter(builder.throwableFormatter);
        }
        if (builder.threadFormatter != null) {
            logConfigBuilder.threadFormatter(builder.threadFormatter);
        }
        if (builder.stackTraceFormatter != null) {
            logConfigBuilder.stackTraceFormatter(builder.stackTraceFormatter);
        }
        if (builder.borderFormatter != null) {
            logConfigBuilder.borderFormatter(builder.borderFormatter);
        }
        if (builder.objectFormatters != null) {
            logConfigBuilder.objectFormatters(builder.objectFormatters);
        }
        if (builder.interceptors != null) {
            logConfigBuilder.interceptors(builder.interceptors);
        }
        logConfiguration = logConfigBuilder.build();

        if (builder.printer != null) {
            printer = builder.printer;
        } else {
            printer = LogCore.sPrinter;
        }
    }

    /**
     * Log an object with level {@link LogLevel#VERBOSE}.
     *
     * @param object the object to log
     * @see Builder#addObjectFormatter(Class, ObjectFormatter)
     * @since 1.1.0
     */
    public void v(String tag, Object object) {
        println(LogLevel.VERBOSE, tag, object);
    }

    /**
     * Log an array with level {@link LogLevel#VERBOSE}.
     *
     * @param array the array to log
     */
    public void v(String tag, Object[] array) {
        println(LogLevel.VERBOSE, tag, array);
    }


    /**
     * Log a message with level {@link LogLevel#VERBOSE}.
     *
     * @param msg the message to log
     */
    public void v(String tag, String msg) {
        println(LogLevel.VERBOSE, tag, msg);
    }

    /**
     * Log a message and a throwable with level {@link LogLevel#VERBOSE}.
     *
     * @param msg the message to log
     * @param tr  the throwable to be log
     */
    public void v(String tag, String msg, Throwable tr) {
        println(LogLevel.VERBOSE, tag, msg, tr);
    }

    /**
     * Log an object with level {@link LogLevel#DEBUG}.
     *
     * @param object the object to log
     * @see Builder#addObjectFormatter(Class, ObjectFormatter)
     * @since 1.1.0
     */
    public void d(String tag, Object object) {
        println(LogLevel.DEBUG, tag, object);
    }

    /**
     * Log an array with level {@link LogLevel#DEBUG}.
     *
     * @param array the array to log
     */
    public void d(String tag, Object[] array) {
        println(LogLevel.DEBUG, tag, array);
    }

    /**
     * Log a message with level {@link LogLevel#DEBUG}.
     *
     * @param msg the message to log
     */
    public void d(String tag, String msg) {
        println(LogLevel.DEBUG, tag, msg);
    }

    /**
     * Log a message and a throwable with level {@link LogLevel#DEBUG}.
     *
     * @param msg the message to log
     * @param tr  the throwable to be log
     */
    public void d(String tag, String msg, Throwable tr) {
        println(LogLevel.DEBUG, tag, msg, tr);
    }

    /**
     * Log an object with level {@link LogLevel#INFO}.
     *
     * @param object the object to log
     * @see Builder#addObjectFormatter(Class, ObjectFormatter)
     * @since 1.1.0
     */
    public void i(String tag, Object object) {
        println(LogLevel.INFO, tag, object);
    }

    /**
     * Log an array with level {@link LogLevel#INFO}.
     *
     * @param array the array to log
     */
    public void i(String tag, Object[] array) {
        println(LogLevel.INFO, tag, array);
    }


    /**
     * Log a message with level {@link LogLevel#INFO}.
     *
     * @param msg the message to log
     */
    public void i(String tag, String msg) {
        println(LogLevel.INFO, tag, msg);
    }

    /**
     * Log a message and a throwable with level {@link LogLevel#INFO}.
     *
     * @param msg the message to log
     * @param tr  the throwable to be log
     */
    public void i(String msg, String tag, Throwable tr) {
        println(LogLevel.INFO, tag, msg, tr);
    }

    /**
     * Log an object with level {@link LogLevel#WARN}.
     *
     * @param object the object to log
     * @see Builder#addObjectFormatter(Class, ObjectFormatter)
     * @since 1.1.0
     */
    public void w(String tag, Object object) {
        println(LogLevel.WARN, tag, object);
    }

    /**
     * Log an array with level {@link LogLevel#WARN}.
     *
     * @param array the array to log
     */
    public void w(String tag, Object[] array) {
        println(LogLevel.WARN, tag, array);
    }


    /**
     * Log a message with level {@link LogLevel#WARN}.
     *
     * @param msg the message to log
     */
    public void w(String tag, String msg) {
        println(LogLevel.WARN, tag, msg);
    }

    /**
     * Log a message and a throwable with level {@link LogLevel#WARN}.
     *
     * @param msg the message to log
     * @param tr  the throwable to be log
     */
    public void w(String msg, String tag, Throwable tr) {
        println(LogLevel.WARN, tag, msg, tr);
    }

    /**
     * Log an object with level {@link LogLevel#ERROR}.
     *
     * @param object the object to log
     * @see Builder#addObjectFormatter(Class, ObjectFormatter)
     * @since 1.1.0
     */
    public void e(String tag, Object object) {
        println(LogLevel.ERROR, tag, object);
    }

    /**
     * Log an array with level {@link LogLevel#ERROR}.
     *
     * @param array the array to log
     */
    public void e(String tag, Object[] array) {
        println(LogLevel.ERROR, tag, array);
    }


    /**
     * Log a message with level {@link LogLevel#ERROR}.
     *
     * @param msg the message to log
     */
    public void e(String tag, String msg) {
        println(LogLevel.ERROR, tag, msg);
    }

    /**
     * Log a message and a throwable with level {@link LogLevel#ERROR}.
     *
     * @param msg the message to log
     * @param tr  the throwable to be log
     */
    public void e(String msg, String tag, Throwable tr) {
        println(LogLevel.ERROR, tag, msg, tr);
    }

    /**
     * Log an object with specific log level.
     *
     * @param logLevel the specific log level
     * @param object   the object to log
     * @see Builder#addObjectFormatter(Class, ObjectFormatter)
     * @since 1.4.0
     */
    public void log(int logLevel, String tag, Object object) {
        println(logLevel, tag, object);
    }

    /**
     * Log an array with specific log level.
     *
     * @param logLevel the specific log level
     * @param array    the array to log
     * @since 1.4.0
     */
    public void log(int logLevel, String tag, Object[] array) {
        println(logLevel, tag, array);
    }


    /**
     * Log a message with specific log level.
     *
     * @param logLevel the specific log level
     * @param msg      the message to log
     * @since 1.4.0
     */
    public void log(int logLevel, String tag, String msg) {
        println(logLevel, tag, msg);
    }

    /**
     * Log a message and a throwable with specific log level.
     *
     * @param logLevel the specific log level
     * @param msg      the message to log
     * @param tr       the throwable to be log
     * @since 1.4.0
     */
    public void log(int logLevel, String tag, String msg, Throwable tr) {
        println(logLevel, tag, msg, tr);
    }

    public void log(int logLevel, String tagStr, String fileName, String funName, int line, String msg) {
        if (logLevel < logConfiguration.logLevel) {
            return;
        }
        println2(logLevel, tagStr, fileName, funName, line, msg);
    }

    /**
     * Log a JSON string, with level {@link LogLevel#DEBUG} by default.
     *
     * @param json the JSON string to log
     */
    public void json(String json, String tag) {
        if (LogLevel.DEBUG < logConfiguration.logLevel) {
            return;
        }
        printlnInternal(LogLevel.DEBUG, tag, logConfiguration.jsonFormatter.format(json));
    }

    /**
     * Log a XML string, with level {@link LogLevel#DEBUG} by default.
     *
     * @param xml the XML string to log
     */
    public void xml(String xml, String tag) {
        if (LogLevel.DEBUG < logConfiguration.logLevel) {
            return;
        }
        printlnInternal(LogLevel.DEBUG, tag, logConfiguration.xmlFormatter.format(xml));
    }

    /**
     * Print an object in a new line.
     *
     * @param logLevel the log level of the printing object
     * @param object   the object to print
     */
    private <T> void println(int logLevel, String tag, T object) {
        if (logLevel < logConfiguration.logLevel) {
            return;
        }
        String objectString;
        if (object != null) {
            ObjectFormatter<? super T> objectFormatter = logConfiguration.getObjectFormatter(object);
            if (objectFormatter != null) {
                objectString = objectFormatter.format(object);
            } else {
                objectString = object.toString();
            }
        } else {
            objectString = "null";
        }
        printlnInternal(logLevel, tag, objectString);
    }

    /**
     * Print an array in a new line.
     *
     * @param logLevel the log level of the printing array
     * @param array    the array to print
     */
    private void println(int logLevel, String tag, Object[] array) {
        if (logLevel < logConfiguration.logLevel) {
            return;
        }
        printlnInternal(logLevel, tag, Arrays.deepToString(array));
    }

    /**
     * Print a log in a new line.
     *
     * @param logLevel the log level of the printing log
     */
    private void println(int logLevel, String tag, String msg) {
        if (logLevel < logConfiguration.logLevel) {
            return;
        }
        printlnInternal(logLevel, tag, msg);
    }

    /**
     * Print a log in a new line.
     *
     * @param logLevel the log level of the printing log
     * @param msg      the message you would like to log
     */
    /*package*/ void println(int logLevel, String msg) {
        if (logLevel < logConfiguration.logLevel) {
            return;
        }
        printlnInternal(logLevel, msg);
    }

    /**
     * Print a log in a new line.
     *
     * @param logLevel the log level of the printing log
     * @param msg      the message you would like to log
     * @param tr       an throwable object to log
     */
    private void println(int logLevel, String tag, String msg, Throwable tr) {
        if (logLevel < logConfiguration.logLevel) {
            return;
        }
        printlnInternal(logLevel, tag, ((msg == null || msg.length() == 0)
                ? "" : (msg + SystemCompat.lineSeparator))
                + logConfiguration.throwableFormatter.format(tr));
    }

    private void printlnInternal(int logLevel, String tagStr, String msg) {
        String tag;
        if (CommonUtils.isEmpty(tagStr))
            tag = logConfiguration.tag;
        else
            tag = tagStr;
        String thread = logConfiguration.withThread
                ? logConfiguration.threadFormatter.format(Thread.currentThread())
                : null;
        String stackTrace = logConfiguration.withStackTrace
                ? logConfiguration.stackTraceFormatter.format(
                StackTraceUtil.getCroppedRealStackTrack(new Throwable().getStackTrace(),
                        logConfiguration.stackTraceOrigin,
                        logConfiguration.stackTraceDepth))
                : null;

        if (logConfiguration.interceptors != null) {
            LogItem log = new LogItem(logLevel, tag, thread, stackTrace, msg);
            for (Interceptor interceptor : logConfiguration.interceptors) {
                log = interceptor.intercept(log);
                if (log == null) {
                    // Log is eaten, don't print this log.
                    return;
                }

                // Check if the log still healthy.
                if (log.tag == null || log.msg == null) {
                    throw new IllegalStateException("Interceptor " + interceptor
                            + " should not remove the tag or message of an log,"
                            + " if you don't want to print this log,"
                            + " just return a null when intercept.");
                }
            }

            // Use fields after interception.
            logLevel = log.level;
            tag = log.tag;
            thread = log.threadInfo;
            stackTrace = log.stackTraceInfo;
            msg = log.msg;
        }

        printer.println(logLevel, tag, logConfiguration.withBorder
                ? logConfiguration.borderFormatter.format(new String[]{thread, stackTrace, msg})
                : ((thread != null ? (thread + SystemCompat.lineSeparator) : "")
                + (stackTrace != null ? (stackTrace + SystemCompat.lineSeparator) : "")
                + msg));
    }

    private void println2(int logLevel, String tagStr, String fileName, String funName, int line, String msg) {
        String tag;
        if (CommonUtils.isEmpty(tagStr))
            tag = logConfiguration.tag;
        else
            tag = tagStr;
        String thread = logConfiguration.withThread
                ? logConfiguration.threadFormatter.format(Thread.currentThread())
                : null;
        String stackTrace = logConfiguration.withStackTrace
                ? logConfiguration.stackTraceFormatter.format(
                StackTraceUtil.getCroppedRealStackTrack(new Throwable().getStackTrace(),
                        logConfiguration.stackTraceOrigin,
                        logConfiguration.stackTraceDepth))
                : null;

        if (logConfiguration.interceptors != null) {
            LogItem log = new LogItem(logLevel, tag, thread, stackTrace, msg);
            for (Interceptor interceptor : logConfiguration.interceptors) {
                log = interceptor.intercept(log);
                if (log == null) {
                    // Log is eaten, don't print this log.
                    return;
                }

                // Check if the log still healthy.
                if (log.tag == null || log.msg == null) {
                    throw new IllegalStateException("Interceptor " + interceptor
                            + " should not remove the tag or message of an log,"
                            + " if you don't want to print this log,"
                            + " just return a null when intercept.");
                }
            }

            // Use fields after interception.
            logLevel = log.level;
            tag = log.tag;
            thread = log.threadInfo;
            stackTrace = log.stackTraceInfo;
            msg = log.msg;
        }

        printer.println2(logLevel, tag, fileName, funName, line, logConfiguration.withBorder
                ? logConfiguration.borderFormatter.format(new String[]{thread, stackTrace, msg})
                : ((thread != null ? (thread + SystemCompat.lineSeparator) : "")
                + (stackTrace != null ? (stackTrace + SystemCompat.lineSeparator) : "")
                + msg));
    }

    /**
     * Print a log in a new line internally.
     *
     * @param logLevel the log level of the printing log
     * @param msg      the message you would like to log
     */
    private void printlnInternal(int logLevel, String msg) {
        printlnInternal(logLevel, logConfiguration.tag, msg);
    }

    /**
     * Format a string with arguments.
     *
     * @param format the format string, null if just to concat the arguments
     * @param args   the arguments
     * @return the formatted string
     */
    private String formatArgs(String format, Object... args) {
        if (format != null) {
            return String.format(format, args);
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0, N = args.length; i < N; i++) {
                if (i != 0) {
                    sb.append(", ");
                }
                sb.append(args[i]);
            }
            return sb.toString();
        }
    }

    /**
     * Builder for {@link LogEngine}.
     */
    public static class Builder {

        /**
         * The log level, the logs below of which would not be printed.
         */
        private int logLevel;

        /**
         * The tag string when {@link LogEngine} log.
         */
        private String tag;

        /**
         * Whether we should log with thread info.
         */
        private boolean withThread;

        /**
         * Whether we have enabled/disabled thread info.
         */
        private boolean threadSet;

        /**
         * Whether we should log with stack trace.
         */
        private boolean withStackTrace;

        /**
         * The origin of stack trace elements from which we should NOT log when logging with stack trace,
         * it can be a package name like "com.elvishew.xlog", a class name like "com.yourdomain.logWrapper",
         * or something else between package name and class name, like "com.yourdomain.".
         * <p>
         * It is mostly used when you are using a logger wrapper.
         */
        private String stackTraceOrigin;

        /**
         * The number of stack trace elements we should log when logging with stack trace,
         * 0 if no limitation.
         */
        private int stackTraceDepth;

        /**
         * Whether we have enabled/disabled stack trace.
         */
        private boolean stackTraceSet;

        /**
         * Whether we should log with border.
         */
        private boolean withBorder;

        /**
         * Whether we have enabled/disabled border.
         */
        private boolean borderSet;

        /**
         * The JSON formatter when {@link LogEngine} log a JSON string.
         */
        private JsonFormatter jsonFormatter;

        /**
         * The XML formatter when {@link LogEngine} log a XML string.
         */
        private XmlFormatter xmlFormatter;

        /**
         * The throwable formatter when {@link LogEngine} log a message with throwable.
         */
        private ThrowableFormatter throwableFormatter;

        /**
         * The thread formatter when {@link LogEngine} logging.
         */
        private ThreadFormatter threadFormatter;

        /**
         * The stack trace formatter when {@link LogEngine} logging.
         */
        private StackTraceFormatter stackTraceFormatter;

        /**
         * The border formatter when {@link LogEngine} logging.
         */
        private BorderFormatter borderFormatter;

        /**
         * The object formatters, used when {@link LogEngine} logging an object.
         */
        private Map<Class<?>, ObjectFormatter<?>> objectFormatters;

        /**
         * The intercepts, used when {@link LogEngine} logging.
         */
        private List<Interceptor> interceptors;

        /**
         * The printer used to print the log when {@link LogEngine} log.
         */
        private Printer printer;

        /**
         * Construct a builder, which will perform the same as the global one by default.
         */
        public Builder() {
            LogCore.assertInitialization();
        }

        /**
         * Set the log level, the logs below of which would not be printed.
         *
         * @param logLevel the log level
         * @return the builder
         * @since 1.3.0
         */
        public Builder logLevel(int logLevel) {
            this.logLevel = logLevel;
            return this;
        }

        /**
         * Set the tag string when {@link LogEngine} log.
         *
         * @param tag the tag string when {@link LogEngine} log
         * @return the builder
         */
        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        /**
         * Enable thread info.
         *
         * @return the builder
         */
        public Builder t() {
            this.withThread = true;
            this.threadSet = true;
            return this;
        }

        /**
         * Disable thread info.
         *
         * @return the builder
         */
        public Builder nt() {
            this.withThread = false;
            this.threadSet = true;
            return this;
        }

        /**
         * Enable stack trace.
         *
         * @param depth the number of stack trace elements we should log, 0 if no limitation
         * @return the builder
         */
        public Builder st(int depth) {
            this.withStackTrace = true;
            this.stackTraceDepth = depth;
            this.stackTraceSet = true;
            return this;
        }

        /**
         * Enable stack trace.
         *
         * @param stackTraceOrigin the origin of stack trace elements from which we should NOT log when
         *                         logging with stack trace, it can be a package name like
         *                         "com.elvishew.xlog", a class name like "com.yourdomain.logWrapper",
         *                         or something else between package name and class name, like "com.yourdomain.".
         *                         It is mostly used when you are using a logger wrapper
         * @param depth            the number of stack trace elements we should log, 0 if no limitation
         * @return the builder
         * @since 1.4.0
         */
        public Builder st(String stackTraceOrigin, int depth) {
            this.withStackTrace = true;
            this.stackTraceOrigin = stackTraceOrigin;
            this.stackTraceDepth = depth;
            this.stackTraceSet = true;
            return this;
        }

        /**
         * Disable stack trace.
         *
         * @return the builder
         */
        public Builder nst() {
            this.withStackTrace = false;
            this.stackTraceOrigin = null;
            this.stackTraceDepth = 0;
            this.stackTraceSet = true;
            return this;
        }

        /**
         * Enable border.
         *
         * @return the builder
         */
        public Builder b() {
            this.withBorder = true;
            this.borderSet = true;
            return this;
        }

        /**
         * Disable border.
         *
         * @return the builder
         */
        public Builder nb() {
            this.withBorder = false;
            this.borderSet = true;
            return this;
        }

        /**
         * Set the JSON formatter when {@link LogEngine} log a JSON string.
         *
         * @param jsonFormatter the JSON formatter when {@link LogEngine} log a JSON string
         * @return the builder
         */
        public Builder jsonFormatter(JsonFormatter jsonFormatter) {
            this.jsonFormatter = jsonFormatter;
            return this;
        }

        /**
         * Set the XML formatter when {@link LogEngine} log a XML string.
         *
         * @param xmlFormatter the XML formatter when {@link LogEngine} log a XML string
         * @return the builder
         */
        public Builder xmlFormatter(XmlFormatter xmlFormatter) {
            this.xmlFormatter = xmlFormatter;
            return this;
        }

        /**
         * Set the throwable formatter when {@link LogEngine} log a message with throwable.
         *
         * @param throwableFormatter the throwable formatter when {@link LogEngine} log a message with
         *                           throwable
         * @return the builder
         */
        public Builder throwableFormatter(ThrowableFormatter throwableFormatter) {
            this.throwableFormatter = throwableFormatter;
            return this;
        }

        /**
         * Set the thread formatter when {@link LogEngine} logging.
         *
         * @param threadFormatter the thread formatter when {@link LogEngine} logging
         * @return the builder
         */
        public Builder threadFormatter(ThreadFormatter threadFormatter) {
            this.threadFormatter = threadFormatter;
            return this;
        }

        /**
         * Set the stack trace formatter when {@link LogEngine} logging.
         *
         * @param stackTraceFormatter the stace trace formatter when {@link LogEngine} logging
         * @return the builder
         */
        public Builder stackTraceFormatter(StackTraceFormatter stackTraceFormatter) {
            this.stackTraceFormatter = stackTraceFormatter;
            return this;
        }

        /**
         * Set the border formatter when {@link LogEngine} logging.
         *
         * @param borderFormatter the border formatter when {@link LogEngine} logging
         * @return the builder
         */
        public Builder borderFormatter(BorderFormatter borderFormatter) {
            this.borderFormatter = borderFormatter;
            return this;
        }

        /**
         * Add an object formatter for specific class of object when {@link LogEngine} log an object.
         *
         * @param objectClass     the class of object
         * @param objectFormatter the object formatter to add
         * @param <T>             the type of object
         * @return the builder
         * @since 1.1.0
         */
        public <T> Builder addObjectFormatter(Class<T> objectClass,
                                              ObjectFormatter<? super T> objectFormatter) {
            if (objectFormatters == null) {
                objectFormatters = new HashMap<>(DefaultsFactory.builtinObjectFormatters());
            }
            objectFormatters.put(objectClass, objectFormatter);
            return this;
        }

        /**
         * Add an interceptor when {@link LogEngine} logging.
         *
         * @param interceptor the intercept to add
         * @return the builder
         * @since 1.3.0
         */
        public Builder addInterceptor(Interceptor interceptor) {
            if (interceptors == null) {
                interceptors = new ArrayList<>();
            }
            interceptors.add(interceptor);
            return this;
        }

        /**
         * Set the printers used to print the log when {@link LogEngine} log.
         *
         * @param printers the printers used to print the log when {@link LogEngine} log
         * @return the builder
         */
        public Builder printers(Printer... printers) {
            if (printers.length == 0) {
                // Is there anybody want to reuse the Builder? It's not a good idea, but
                // anyway, in case you want to reuse a builder and do not want the custom
                // printers anymore, just do it.
                this.printer = null;
            } else if (printers.length == 1) {
                this.printer = printers[0];
            } else {
                this.printer = new PrinterSet(printers);
            }
            return this;
        }

        /**
         * Convenience of {@link #build()} and {@link LogEngine#v(String, Object)}.
         *
         * @since 1.1.0
         */
        public void v(String tag, Object object) {
            build().v(tag, object);
        }

        /**
         * Convenience of {@link #build()} and {@link LogEngine#v(String, Object[])}.
         *
         * @since 1.4.0
         */
        public void v(String tag, Object[] array) {
            build().v(tag, array);
        }

        /**
         * Convenience of {@link #build()} and {@link LogEngine#v(String, String)}.
         */
        public void v(String tag, String msg) {
            build().v(tag, msg);
        }

        /**
         * Convenience of {@link #build()} and {@link LogEngine#v(String, String, Throwable)}.
         */
        public void v(String tag, String msg, Throwable tr) {
            build().v(tag, msg, tr);
        }

        /**
         * Convenience of {@link #build()} and {@link LogEngine#d(String, Object)}.
         *
         * @since 1.1.0
         */
        public void d(String tag, Object object) {
            build().d(tag, object);
        }

        /**
         * Convenience of {@link #build()} and {@link LogEngine#d(String, Object[])}.
         *
         * @since 1.4.0
         */
        public void d(String tag, Object[] array) {
            build().d(tag, array);
        }


        /**
         * Convenience of {@link #build()} and {@link LogEngine#d(String, String)}.
         */
        public void d(String tag, String msg) {
            build().d(tag, msg);
        }

        /**
         * Convenience of {@link #build()} and {@link LogEngine#d(String, String, Throwable)}.
         */
        public void d(String tag, String msg, Throwable tr) {
            build().d(tag, msg, tr);
        }

        /**
         * Convenience of {@link #build()} and {@link LogEngine#i(String, Object)}.
         *
         * @since 1.1.0
         */
        public void i(String tag, Object object) {
            build().i(tag, object);
        }

        /**
         * Convenience of {@link #build()} and {@link LogEngine#i(String, Object[])}.
         *
         * @since 1.4.0
         */
        public void i(String tag, Object[] array) {
            build().i(tag, array);
        }


        /**
         * Convenience of {@link #build()} and {@link LogEngine#i(String, String)}.
         */
        public void i(String tag, String msg) {
            build().i(tag, msg);
        }

        /**
         * Convenience of {@link #build()} and {@link LogEngine#i(String, String, Throwable)}.
         */
        public void i(String tag, String msg, Throwable tr) {
            build().i(tag, msg, tr);
        }

        /**
         * Convenience of {@link #build()} and {@link LogEngine#w(String, Object)}.
         *
         * @since 1.1.0
         */
        public void w(String tag, Object object) {
            build().w(tag, object);
        }

        /**
         * Convenience of {@link #build()} and {@link LogEngine#w(String, Object[])}.
         *
         * @since 1.4.0
         */
        public void w(String tag, Object[] array) {
            build().w(tag, array);
        }


        /**
         * Convenience of {@link #build()} and {@link LogEngine#w(String, String)}.
         */
        public void w(String tag, String msg) {
            build().w(tag, msg);
        }

        /**
         * Convenience of {@link #build()} and {@link LogEngine#w(String, String, Throwable)}.
         */
        public void w(String tag, String msg, Throwable tr) {
            build().w(tag, msg, tr);
        }

        /**
         * Convenience of {@link #build()} and {@link LogEngine#e(String, Object)}.
         *
         * @since 1.1.0
         */
        public void e(String tag, Object object) {
            build().e(tag, object);
        }

        /**
         * Convenience of {@link #build()} and {@link LogEngine#e(String, Object[])}.
         *
         * @since 1.4.0
         */
        public void e(String tag, Object[] array) {
            build().e(tag, array);
        }


        /**
         * Convenience of {@link #build()} and {@link LogEngine#e(String, String)}.
         */
        public void e(String tag, String msg) {
            build().e(tag, msg);
        }

        /**
         * Convenience of {@link #build()} and {@link LogEngine#e(String, String, Throwable)}.
         */
        public void e(String tag, String msg, Throwable tr) {
            build().e(tag, msg, tr);
        }

        /**
         * Convenience of {@link #build()} and {@link LogEngine#log(int, String, Object)}.
         *
         * @since 1.4.0
         */
        public void log(int logLevel, String tag, Object object) {
            build().log(logLevel, tag, object);
        }

        /**
         * Convenience of {@link #build()} and {@link LogEngine#log(int, String, Object[])}.
         *
         * @since 1.4.0
         */
        public void log(int logLevel, String tag, Object[] array) {
            build().log(logLevel, tag, array);
        }


        /**
         * Convenience of {@link #build()} and {@link LogEngine#log(int, String, String)}.
         *
         * @since 1.4.0
         */
        public void log(int logLevel, String tag, String msg) {
            build().log(logLevel, tag, msg);
        }

        /**
         * Convenience of {@link #build()} and {@link LogEngine#log(int, String, String, Throwable)}.
         *
         * @since 1.4.0
         */
        public void log(int logLevel, String tag, String msg, Throwable tr) {
            build().log(logLevel, tag, msg, tr);
        }

        /**
         * Convenience of {@link #build()} and {@link LogEngine#json(String, String)}.
         */
        public void json(String tag, String json) {
            build().json(tag, json);
        }

        /**
         * Convenience of {@link #build()} and {@link LogEngine#xml(String, String)}.
         */
        public void xml(String tag, String xml) {
            build().xml(tag, xml);
        }

        /**
         * Builds configured {@link LogEngine} object.
         *
         * @return the built configured {@link LogEngine} object
         */
        public LogEngine build() {
            return new LogEngine(this);
        }
    }
}
