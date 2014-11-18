package com.example.popwin.util;

import android.util.Log;

public class LogUtil {

	private static final boolean DEBUG = true;
    private static final String PREFIX = "fish";

    public static void e(String tag, Object data) {
        if (DEBUG) {
            Log.e(tag, PREFIX + " : " + data.toString());
        }
    }

    public static void d(String tag, Object data) {
        if (DEBUG) {
            Log.d(tag, PREFIX + " : " + data.toString());
        }
    }

    public static void v(String tag, Object data) {
        if (DEBUG) {
            Log.v(tag, PREFIX + " : " + data.toString());
        }
    }

    public static void i(String tag, Object data) {
        if (DEBUG) {
            Log.i(tag, PREFIX + " : " + data.toString());
        }
    }

    public static void w(String tag, Object data) {
        if (DEBUG) {
            Log.w(tag, PREFIX + " : " + data.toString());
        }
    }
}
