package com.legame.np.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 
 * @author leoliu
 * 
 */
public class NetWorkUtils {

    private static final String TAG = "NetWorkUtils";

    public static final int APNTYPE_NONE = 0;
    public static final int APNTYPE_WIFI = 1;
    public static final int APNTYPE_WAP = 2;
    public static final int APNTYPE_NET = 3;

    /**
     * 是否有无线网
     * 
     * @param context
     * @return
     */
    public static boolean checkNetWork(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetInfo = connectivityManager
                    .getActiveNetworkInfo();
            NetworkInfo mobNetInfo = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (activeNetInfo == null || !activeNetInfo.isAvailable()
                    || !activeNetInfo.isConnected()) {
                if (mobNetInfo == null || !mobNetInfo.isAvailable()
                        || !mobNetInfo.isConnected()) {
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } catch (Exception e) {
        	e.printStackTrace();
            return false;
        }
    }

    /**
     * 是否有wifi网络
     * 
     * @param context
     * @return
     */
    public static boolean checkWifiNetWork(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetInfo = connectivityManager
                    .getActiveNetworkInfo();
            NetworkInfo mobNetInfo = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (activeNetInfo == null || !activeNetInfo.isAvailable()
                    || !activeNetInfo.isConnected()) {
                if (mobNetInfo == null || !mobNetInfo.isAvailable()
                        || !mobNetInfo.isConnected()) {
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static HttpURLConnection getHttpURLConnection(Context context,
            String _url) throws IOException {
        URL url = new URL(_url);

        int apnType = Utils.getAPNType(context);

        HttpURLConnection httpUrlConnection = null;

        if (apnType == APNTYPE_WAP) {
            Proxy proxy = null;
            String operator = Utils.getSimOperator(context);

            if ("46003".equals(operator) || "46005".equals(operator)) {
                proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
                        "10.0.0.200", 80));
            } else {
                proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
                        "10.0.0.172", 80));
            }

            httpUrlConnection = (HttpURLConnection) url.openConnection(proxy);

           //LogUtil.e(TAG, "postData -> apnTypes = 2 ,use proxy");
        } else {
            URLConnection rulConnection = url.openConnection();
            httpUrlConnection = (HttpURLConnection) rulConnection;
           //LogUtil.e(TAG, "postData -> apnTypes = " + apnType);
        }

        return httpUrlConnection;
    }
}
