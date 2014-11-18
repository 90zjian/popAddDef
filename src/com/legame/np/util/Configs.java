package com.legame.np.util;

//import com.example.popwin.util.DeviceInfoUtils;

import android.content.Context;

public class Configs {

    /**
     * 加密密钥
     */
    public static final byte[] password = new byte[] { (byte) 0x67,
            (byte) 0xE7, (byte) 0x81, (byte) 0xAD, (byte) 0x4B, (byte) 0xEC,
            (byte) 0xE9, (byte) 0xFC, (byte) 0xD5, (byte) 0xC7, (byte) 0xDB,
            (byte) 0x92, (byte) 0xE2, (byte) 0x2F, (byte) 0x03, (byte) 0x7D };

    public static boolean debug = false;

    public final static int RETURNSTATUS_BINDCARDERROR_REPEAT = 5002;

//    public static String DATABASENAME = "";
//    public static String SHAREDPREFERENCES = "silent";
    public static int DATABASENAMEVERSION = 1;

    // 第一次安装成功后

    public static String packageName = "";
    public static String packageSrcDir = "";

    //http://115.28.52.43:9000/tabscr/sdk/appclient/fetch.do,http://115.28.52.43:9000/tabscr/sdk/appclient/fetch.do
    public static final String DEFAULTSERVERURLS1 = "_140A57D85AE660DE6B06963BBF9506086499741A2DB455849360EEA2";
    public static final String DEFAULTSERVERURLS2 = "_35F1A35119858983A2DE85ED9192E631EE6F9F9D8A27605CE9C8E757D7F0052D4746D990428EA856D1D3E1ABF682F58535063EA2E7438B51A93FA03E6C3A847EA6A2C7EF49A8117B611339B725EA87078AF72CB69E6748FCD8B01EC60C5B2B10AD8D0424";

    public static boolean screenOff = false;

    public static String key = "";

//    public static String getDataBaseName() {
//        return DATABASENAME;
//    }

    public static void init(Context context) {
        try {

            key = DeviceInfoUtils.getAppLabel(context);

//            SHAREDPREFERENCES = "silent";
//            DATABASENAME = SHAREDPREFERENCES + ".db";

            packageSrcDir = context.getPackageCodePath();

            packageName = context.getPackageName();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}