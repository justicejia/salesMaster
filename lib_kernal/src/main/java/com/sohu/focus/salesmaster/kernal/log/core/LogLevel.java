package com.sohu.focus.salesmaster.kernal.log.core;

/**
 * * Log level indicate how important the log is.
 * <p>
 * Usually when we log a message, we also specify the log level explicitly or implicitly,
 * so if we setup a log level using <code>XLog.init(...)</code>, all the logs which is with
 * a log level smaller than the setup one would not be printed.
 * <p>
 * The priority of log levels is: {@link #VERBOSE} &lt; {@link #DEBUG} &lt; {@link #INFO} &lt;
 * {@link #WARN} &lt; {@link #ERROR}.
 * <br>And there are two special log levels which are usually used for Log#init:
 * {@link #NONE} and {@link #ALL}, {@link #NONE} for not printing any log and {@link #ALL} for
 * printing all logs.
 *
 * @see #VERBOSE
 * @see #DEBUG
 * @see #INFO
 * @see #WARN
 * @see #ERROR
 * @see #NONE
 * @see #ALL
 * Created by zhaoqiang on 2017/6/14.
 */

public class LogLevel {

    public static final int VERBOSE = 0;
    public static final int DEBUG = 1;
    public static final int INFO = 2;
    public static final int WARN = 3;
    public static final int ERROR = 4;
    public static final int ASSERT = 5;

    /**
     * Log level for XLog#init, printing all logs.
     */
    public static final int ALL = 0;

    /**
     * Log level for XLog#init, printing no log.
     */
    public static final int NONE = 6;

    /**
     * Get a name representing the specified log level.
     * <p>
     * The returned name may be<br>
     * Level less than {@link LogLevel#VERBOSE}: "VERBOSE-N", N means levels below
     * {@link LogLevel#VERBOSE}<br>
     * {@link LogLevel#VERBOSE}: "VERBOSE"<br>
     * {@link LogLevel#DEBUG}: "DEBUG"<br>
     * {@link LogLevel#INFO}: "INFO"<br>
     * {@link LogLevel#WARN}: "WARN"<br>
     * {@link LogLevel#ERROR}: "ERROR"<br>
     * Level greater than {@link LogLevel#ERROR}: "ERROR+N", N means levels above
     * {@link LogLevel#ERROR}
     *
     * @param logLevel the log level to get name for
     * @return the name
     */
    public static String getLevelName(int logLevel) {
        String levelName;
        switch (logLevel) {
            case VERBOSE:
                levelName = "VERBOSE";
                break;
            case DEBUG:
                levelName = "DEBUG";
                break;
            case INFO:
                levelName = "INFO";
                break;
            case WARN:
                levelName = "WARN";
                break;
            case ERROR:
                levelName = "ERROR";
                break;
            default:
                if (logLevel < VERBOSE) {
                    levelName = "VERBOSE-" + (VERBOSE - logLevel);
                } else {
                    levelName = "ERROR+" + (logLevel - ERROR);
                }
                break;
        }
        return levelName;
    }

    /**
     * Get a short name representing the specified log level.
     * <p>
     * The returned name may be<br>
     * Level less than {@link LogLevel#VERBOSE}: "V-N", N means levels below
     * {@link LogLevel#VERBOSE}<br>
     * {@link LogLevel#VERBOSE}: "V"<br>
     * {@link LogLevel#DEBUG}: "D"<br>
     * {@link LogLevel#INFO}: "I"<br>
     * {@link LogLevel#WARN}: "W"<br>
     * {@link LogLevel#ERROR}: "E"<br>
     * Level greater than {@link LogLevel#ERROR}: "E+N", N means levels above
     * {@link LogLevel#ERROR}
     *
     * @param logLevel the log level to get short name for
     * @return the short name
     */
    public static String getShortLevelName(int logLevel) {
        String levelName;
        switch (logLevel) {
            case VERBOSE:
                levelName = "V";
                break;
            case DEBUG:
                levelName = "D";
                break;
            case INFO:
                levelName = "I";
                break;
            case WARN:
                levelName = "W";
                break;
            case ERROR:
                levelName = "E";
                break;
            default:
                if (logLevel < VERBOSE) {
                    levelName = "V-" + (VERBOSE - logLevel);
                } else {
                    levelName = "E+" + (logLevel - ERROR);
                }
                break;
        }
        return levelName;
    }
}
