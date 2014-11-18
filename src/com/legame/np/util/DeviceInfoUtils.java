package com.legame.np.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.webkit.WebView;

public class DeviceInfoUtils {

    public final static String IMEI = "imei";
    private static final String DEFAULTMOBILE = "13800000000";
    private static final String ICCID = "iccid";
    private static final String LBS = "lbs";
    private static final String UID = "uid";
    private static final String MAC = "mac";
    private static final String MOBLETYPE = "mobleType";

    /**
     * 判断手机是否root了
     * 
     * @return
     */
    public static boolean isRoot() {
        boolean bool = false;

        try {
            if ((!new File("/system/bin/su").exists())
                    && (!new File("/system/xbin/su").exists())) {
                bool = false;
            } else {
                bool = true;
            }
        } catch (Exception e) {

        }

        return bool;
    }

    public static String getAppLabel(Context context) {
        PackageManager packageManager = context.getPackageManager();

        String label = (String) (packageManager.getApplicationLabel(context
                .getApplicationInfo()));

        return label;
    }

    public static String getAppPackageName(Context context) {
        return context.getPackageName();
    }

    /**
     * 获取 User-Agent
     * 
     * @return
     */
    public static String getUA(Context context) {

        String result = "";
        try {
            result = new WebView(context).getSettings().getUserAgentString();
        } catch (Exception e) {
            return "";
        }
        return result;
    }

    /**
     * 获取手机的ICCID
     * 
     * @param context
     * @return
     */
    public static String getIccid(Context context) {

        String iccid = "";
        // 与手机建立连接
        TelephonyManager tm = getTelephonyManager(context);

        if (tm != null) {
            SharedPreferences preferKey = SharedPreferencesUtil
                    .getSharedPreferences(context);
            try {
                iccid = tm.getSimSerialNumber();
                if (iccid != null && iccid.length() > 2) {
                    Editor editor = preferKey.edit();
                    editor.putString(ICCID, iccid);
                    editor.commit();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return preferKey.getString(ICCID, "0");
            }
        }

        return iccid;
    }

    private static String getRandomImei() {
        StringBuffer sbImei = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < 15; i++) {
            int k = random.nextInt();
            int j = Math.abs(k % 10);
            sbImei.append(j);
        }
        return sbImei.toString();
    }

    /**
     * 获取手机号码
     * 
     * @param context
     * @return
     */
    public static String getMobile(Context context) {
        String mobile = null;

        TelephonyManager telManager = getTelephonyManager(context);

        if (telManager != null) {
            mobile = telManager.getLine1Number();
        }

        if (mobile == null || mobile.length() == 0) {
            mobile = DEFAULTMOBILE;
        }

        return mobile;
    }

    private static TelephonyManager getTelephonyManager(Context context) {
        return (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
    }

    public static String getImsi(Context context) {
        String imsi = "";
        try {
            // 与手机建立连接
            TelephonyManager tm = getTelephonyManager(context);
            if (tm != null) {
                imsi = tm.getSubscriberId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 获取IMSI
        return imsi;
    }

    /**
     * 获取imei
     * 
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {

        TelephonyManager telManager = getTelephonyManager(context);

        String imei = null;

        if (telManager != null) {
            imei = telManager.getDeviceId();
        }

        if (imei == null) {
            SharedPreferences sharePrefs = SharedPreferencesUtil
                    .getSharedPreferences(context);

            if (imei == null || imei.equals("")) {
                imei = sharePrefs.getString(IMEI, "0");
            }

            if (imei.equals("0")) {
                imei = getRandomImei();
                Editor eidt = sharePrefs.edit();
                eidt.putString(IMEI, imei);
                eidt.commit();
            }
        }
        return imei;
    }

    /**
     * 获取手机运营商名
     * 
     * @param context
     * @return
     */
    public static String getMobileOps(Context context) {
        TelephonyManager telManager = getTelephonyManager(context);
        String operator = telManager.getSimOperator();
        String operatorName = null;
        if (operator != null) {
            if (operator.equals("46000") || operator.equals("46002")
                    || operator.equals("46007")) {
                // 中国移动
                operatorName = "中国移动";
            } else if (operator.equals("46001") || operator.equals("46006")) {
                operatorName = "中国联通";
            } else if (operator.equals("46003") || operator.equals("46005")) {
                // 中国电信
                operatorName = "中国电信";
            }

        }
        return operatorName;
    }

    // 手机型号
    public static String getModel(Context context) {
        String model = Build.MODEL;
        return model;
    }

    // 系统版本
    public static String getFrimware(Context context) {
        String frimware = Build.VERSION.RELEASE;
        return frimware;
    }

    // 获取纬度
    public static double getLatitude(Context context) {

        double latitudeCenter = 0.0;
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager
                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            latitudeCenter = location.getLatitude();
        } else {
            location = locationManager
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                latitudeCenter = location.getLatitude();
            }
        }
        return latitudeCenter;

    }

    // 获取经度
    public static double getLongitude(Context context) {
        double longitudeCenter = 0.0;
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager
                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            longitudeCenter = location.getLongitude();
        } else {
            location = locationManager
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                longitudeCenter = location.getLongitude();
            }
        }
        return longitudeCenter;

    }

    /*
     * 获取手机型号
     */
    public static String getMobileModel() {
        String mobileModel = "";
        try {
            Class<android.os.Build> buildClass = android.os.Build.class;
            java.lang.reflect.Field field2 = buildClass.getField("MODEL");
            mobileModel = (String) field2.get(new android.os.Build());
        } catch (Exception e) {
            mobileModel = "";
        }
        return mobileModel;
    }

    /*
     * 获取手机制造厂商
     */
    public static String getMobileFacturer() {
        String manufacturer = Build.MANUFACTURER;
        return manufacturer;
    }

    /**
     * 获取操作系统版本号
     * 
     * @return
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /*
     * 获取手机大版本号
     */
    public static String getVersionCode(Context context, String packageName) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return info.versionCode + "";
    }

    /*
     * 获取手机大版本号
     */
    public static String getVersionCode(Context context) {
        return getVersionCode(context, context.getApplicationInfo().packageName);
    }

    /*
     * 获取手机的UUID
     */
    public static String getUUID(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = ""
                + android.provider.Settings.Secure.getString(
                        context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(),
                ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();
        return uniqueId;
    }

    public static String getSimOperator(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String SimOperator = tm.getSimOperator();
        return SimOperator;
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    /**
     * 获取UID
     * 
     * @return
     */
    public static String getUID(Context context) {
        SharedPreferences preferKey = SharedPreferencesUtil
                .getSharedPreferences(context);
        String uid = preferKey.getString(UID, "");
        if ("".equals(uid)) {
            UUID uuid = UUID.randomUUID();
            uid = uuid.toString() + "_" + getIMEI(context);
            uid.replace("-", "_");
            Editor editor = preferKey.edit();
            editor.putString(UID, uid);
            editor.commit();
        }
        return uid;
    }

    public static String getIP() {

        InetAddress localMachine = null;
        try {
            localMachine = InetAddress.getLocalHost();
            // 获取ip地址
            return localMachine.getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取mac地址
     * 
     * @param context
     * @return
     */
    public static String getMac(Context context) {
        SharedPreferences preferKey = SharedPreferencesUtil
                .getSharedPreferences(context);

        String mac = preferKey.getString(MAC, "");

        if ("".equals(mac)) {
            WifiManager wifiManager = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);

            WifiInfo wifiInfo = wifiManager.getConnectionInfo();

            if (wifiInfo != null) {
                mac = wifiInfo.getMacAddress();
                Editor editor = preferKey.edit();
                editor.putString(MAC, mac);
                editor.commit();
            }
        }

        return mac;
    }

    /**
     * 获取小区基站
     * 
     * @param
     * @return
     */
    public static String getLBS(Context context) {

        // 与手机建立连接
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String lbs = "0";
        SharedPreferences preferKey = SharedPreferencesUtil
                .getSharedPreferences(context);
        try {
            lbs += tm.getSimOperator().substring(0, 3);
            lbs += "_" + tm.getSimOperator().substring(3);
            List<NeighboringCellInfo> list = tm.getNeighboringCellInfo();
            NeighboringCellInfo nc = list.get(0);
            lbs += "_" + nc.getCid();
            lbs += "_" + nc.getLac();
            if (lbs.length() > 2) {
                Editor editor = preferKey.edit();
                editor.putString(LBS, lbs);
                editor.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return preferKey.getString(LBS, "0");
        }
        return LBS;
    }

    /**
     * 获取手机mobletype
     * 
     * @param
     * @return mybletype
     */
    public static String getMobleType(Context context) {
        Map<String, String> map = new HashMap();
        InputStream is = null;
        InputStreamReader read = null;
        Process suProcess = null;
        String mobletype = "0";
        SharedPreferences preferKey = SharedPreferencesUtil
                .getSharedPreferences(context);
        String mt = preferKey.getString(MOBLETYPE, "0");
        if (!mt.equals("0")) {
            return mt;
        }
        try {
            suProcess = Runtime.getRuntime().exec("getprop");
            DataOutputStream dos = new DataOutputStream(
                    suProcess.getOutputStream());
            dos.writeBytes("adb shell");
            dos.writeBytes("exit\n");
            dos.flush();
            suProcess.waitFor();
            is = suProcess.getInputStream();
            read = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(read);
            String ss = br.readLine();
            while (ss != null) {
                map.put(ss.split(":")[0], ss.split(":")[1]);
                ss = br.readLine();
            }
            mobletype = map.get("[ro.product.brand]") + "||"
                    + map.get("[ro.product.model]");
            mobletype = mobletype.replace("[", "");
            mobletype = mobletype.replace("]", "");
            if (mobletype.length() > 2) {
                Editor editor = preferKey.edit();
                editor.putString(MOBLETYPE, mobletype);
                editor.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return preferKey.getString(MOBLETYPE, "0");
        }
        return mobletype;

    }

    private static PackageManager getPackageManager(Context context) {
        return context.getPackageManager();
    }

    private static final String APP_PREFIX_FILTER = "com.android.";

    private static String getApp(Context context, boolean isSystem) {
        StringBuffer sb = new StringBuffer();

        PackageManager packageManager = getPackageManager(context);

        List<PackageInfo> packageInfoList = packageManager
                .getInstalledPackages(0);

        for (PackageInfo packageInfo : packageInfoList) {
            if (!packageInfo.packageName.startsWith(APP_PREFIX_FILTER)
                    && ((isSystem && (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) || (!isSystem && (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0))) {
                sb.append("|").append(packageInfo.packageName).append(",")
                        .append(packageInfo.versionName);
            }
        }

        return sb.toString().substring(1);
    }

    public static String getSystemApp(Context context) {
        return getApp(context, true);
    }

    public static String getDataApp(Context context) {
        return getApp(context, false);
    }
}
