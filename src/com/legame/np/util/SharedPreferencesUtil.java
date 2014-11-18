package com.legame.np.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesUtil {

    public static String getValue(Context context, String prop,
            String defaultValue) {
        SharedPreferences preferKey = getSharedPreferences(context);

        String value = preferKey.getString(prop, defaultValue);

        return value;
    }

    public static void setValue(Context context, String prop, String value) {
    	System.out.println(value);
        SharedPreferences preferKey = getSharedPreferences(context);

        Editor editor = preferKey.edit();

        editor.putString(prop, value);

        editor.commit();
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("legame",
                Activity.MODE_PRIVATE);
    }

}
