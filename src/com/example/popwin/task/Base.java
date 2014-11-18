package com.example.popwin.task;

import android.content.Context;
import android.os.Handler;

public class Base {
    protected Handler handler;
    protected Context context;

    public Base(Context context, Handler handler) {
        this.handler = handler;
        this.context = context;
    }

}
