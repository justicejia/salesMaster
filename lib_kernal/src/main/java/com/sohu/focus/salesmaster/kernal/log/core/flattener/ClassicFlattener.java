package com.sohu.focus.salesmaster.kernal.log.core.flattener;

import com.sohu.focus.salesmaster.kernal.log.core.LogLevel;

/**
 * The classic flattener, flatten the log with pattern "{@value #DEFAULT_PATTERN}".
 * <p>
 * Imagine there is a log, with {@link LogLevel#DEBUG} level, "my_tag" tag and "Simple message"
 * message, the flattened log would be: "2016-11-30 13:00:00.000 D/my_tag: Simple message"
 * Created by zhaoqiang on 2017/6/14.
 */

public class ClassicFlattener extends PatternFlattener{

    private static final String DEFAULT_PATTERN = "{d} {l}/{t}: {m}";

    public ClassicFlattener() {
        super(DEFAULT_PATTERN);
    }
}
