package com.legame.np.util;

import java.io.File;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

public class PackageUtils {

    public static final String TAG = "PackageUtils";

    public static final int install(Context context, String filePath) {
        
        return installNormal(context, filePath) ? INSTALL_SUCCEEDED
                : INSTALL_FAILED_INVALID_URI;
     
    }

    /**
     * install package normal by system intent
     * 
     * @param context
     * @param filePath
     *            file path of package
     * @return whether apk exist
     */
    public static boolean installNormal(Context context, String filePath) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        File file = new File(filePath);
        if (file == null || !file.exists() || !file.isFile()
                || file.length() <= 0) {
            return false;
        }

        i.setDataAndType(Uri.parse("file://" + filePath),
                "application/vnd.android.package-archive");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        return true;
    }

    public static ApplicationInfo getApplicationInfo(Context context,
            String packageName) {
        return getApplicationInfo(context.getPackageManager(), packageName);
    }

    public static ApplicationInfo getApplicationInfo(
            PackageManager packageManager, String packageName) {
        try {
            return packageManager.getApplicationInfo(packageName, 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Installation return code<br/>
     * install success.
     */
    public static final int INSTALL_SUCCEEDED = 1;
   
    /**
     * Installation return code<br/>
     * the URI passed in is invalid.
     */
    public static final int INSTALL_FAILED_INVALID_URI = -3;

   
}
