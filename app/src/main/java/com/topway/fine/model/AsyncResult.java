package com.topway.fine.model;

import java.io.ObjectStreamException;

public class AsyncResult {
    Object tag;
    Object data;

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
