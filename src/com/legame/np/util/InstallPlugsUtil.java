package com.legame.np.util;

import android.content.Context;

public class InstallPlugsUtil {

    /**
     * 静默安装
     * 
     * @param context
     * @param filePath
     * @return
     */
    public static boolean install(Context context, String filePath) {
        
        return PackageUtils.install(context, filePath) == PackageUtils.INSTALL_SUCCEEDED;
    }

}
