package com.sohu.focus.salesmaster.kernal.log.core.formatter.border;


import com.sohu.focus.salesmaster.kernal.log.core.formatter.Formatter;

/**
 * The border formatter used to wrap string segments with borders when logging.
 * <p>
 * e.g:
 * <br>
 * <br>╔════════════════════════════════════════════════════════════════════════════
 * <br>║Thread: main
 * <br>╟────────────────────────────────────────────────────────────────────────────
 * <br>║	├ com.SampleClassB.sampleMethodB(SampleClassB.java:100)
 * <br>║	└ com.SampleClassA.sampleMethodA(SampleClassA.java:50)
 * <br>╟────────────────────────────────────────────────────────────────────────────
 * <br>║Hear is a simple message
 * <br>╚════════════════════════════════════════════════════════════════════════════
 *
 * Created by zhaoqiang on 2017/6/14.
 */

public interface BorderFormatter extends Formatter<String[]> {
}
