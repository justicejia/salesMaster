package com.sohu.focus.salesmaster.kernal.bus;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qiangzhao on 2016/12/29.
 */

public class RxEvent implements Serializable {

    private final Map<String, Object> events = new HashMap<>();
    private String tag = "default";

    public void addEvent(String key, Object obj) {
        events.put(key, obj);
    }

    public Map<String, Object> getEvents() {
        return events;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "RxEvent : TAG[" + tag + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RxEvent that = (RxEvent) o;
        if (!tag.equals(that.tag)) return false;
        return events.equals(((RxEvent) o).events);
    }

    @Override
    public int hashCode() {
        int result = tag.hashCode();
        result = 31 * result + events.hashCode();
        return result;
    }
}
