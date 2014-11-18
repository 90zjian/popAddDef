package com.legame.np.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import com.legame.np.org.AesCrypto;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;

/**
 * 
 * 
 * @author WEIXING
 * 
 */
public class Utils {

    public static String HASHCODE = "TEST_HASHCODE";
    private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
    
    /**
     * 鍒ゆ柇褰撴湡鏄惁鏄痺ifi缃戠粶
     */
    public static boolean isWiFi(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo activeNetInfo = connectivity.getActiveNetworkInfo();
            if (activeNetInfo != null
                    && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {

                return true;
            }
        }
        return false;
    }

    public static String toHexString(byte[] b) {
        if (b == null || b.length == 0) {
            return null;
        }

        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    /**
     * 鍒囨崲缃戠粶
     * 
     * @param resolver
     * @param newAPN
     * @return
     */
    public static final Uri CURRENT_APN_URI = Uri
            .parse("content://telephony/carriers/preferapn");
    public static final Uri APN_LIST_URI = Uri
            .parse("content://telephony/carriers");
    
    
    public static int updateCurrentAPN(Context context, String newAPN) {
        Cursor cursor = null;
        try {
        	ContentResolver resolver = context.getContentResolver();
        	
            cursor = resolver.query(APN_LIST_URI, null,
                    " apn = ? and current = 1",
                    new String[] { newAPN.toLowerCase() }, null);
            String apnId = null;
            if (cursor != null && cursor.moveToFirst()) {
                apnId = cursor.getString(cursor.getColumnIndex("_id"));
            }
            cursor.close();

            if (apnId != null) {
                ContentValues values = new ContentValues();
                values.put("apn_id", apnId);
                resolver.update(CURRENT_APN_URI, values, null, null);
                
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return 1;
    }

    /**
     * 鑾峰彇褰撳墠鐨勭綉缁滅姸锟�?0锛氭病鏈夌綉锟�?1锛歐IFI缃戠粶 2锛歸ap缃戠粶 3锛歯et缃戠粶
     */
    public static int getAPNType(Context context) {
        int netType = NetWorkUtils.APNTYPE_NONE;
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connMgr != null) {
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo == null) {
                return netType;
            }
            int nType = networkInfo.getType();
            if (nType == ConnectivityManager.TYPE_MOBILE) {
                String extraInfo = networkInfo.getExtraInfo().toLowerCase();
                if (extraInfo.endsWith("net")) {
                    netType = NetWorkUtils.APNTYPE_NET;
                } else {
                    netType = NetWorkUtils.APNTYPE_WAP;
                }
            } else if (nType == ConnectivityManager.TYPE_WIFI) {
                netType = NetWorkUtils.APNTYPE_WIFI;
            }
        }
        return netType;
    }

    
    public static String getApnName(Context context){
    	String apnName = "";
    	
    	ConnectivityManager connMgr = (ConnectivityManager) context
	                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connMgr != null) {
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null) {
            	int nType = networkInfo.getType();
	            if (nType == ConnectivityManager.TYPE_MOBILE) {
	                apnName = networkInfo.getExtraInfo().toLowerCase();
	            } 
            }
        }
        
        return apnName;
    }
	
 
    public static String GetHexString(byte[] b) {
        String hs = "";//
        String stmp = "";

        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs.toUpperCase();
    }

    public static byte[] GetBytes(String key, int len) {

        char[] keys = key.toCharArray();
        byte[] buffer = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            buffer[i / 2] = (byte) (ToHexValue(keys[i], true) + ToHexValue(
                    keys[i + 1], false));
        }
        return buffer;
    }

    private static byte ToHexValue(char c, boolean high) {
        byte num;
        if ((c >= '0') && (c <= '9')) {
            num = (byte) (c - '0');
        } else if ((c >= 'a') && (c <= 'f')) {
            num = (byte) ((c - 'a') + 10);
        } else {
            if ((c < 'A') || (c > 'F')) {
                throw new RuntimeException();
            }
            num = (byte) ((c - 'A') + 10);
        }
        if (high) {
            num = (byte) (num << 4);
        }
        return num;
    }

    public static String getSimOperator(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String SimOperator = tm.getSimOperator();
        return SimOperator;
    }

    public static int getResource(Context context,String name,String type){
		try {
			return context.getPackageManager().getResourcesForApplication(context.getPackageName()).getIdentifier(name, type, context.getPackageName());
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
    
    public static String decrypt(String s){
   	 try {
        	byte[] bytes = AesCrypto.decrypt(Utils.GetBytes(s, s.length()), Configs.password);
			return new String(bytes,"utf-8").trim();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        
        return "";
   }
   
   
   public static String encrypt(String s){
   	return toHexString(AesCrypto.encrypt(s,Configs.password));
   }
   
   //鏄惁浠樿繃璐�
   private static final String NAME_HASFEE = "hasFee";
   
   public static boolean hasFee(Context context){
	   return Boolean.parseBoolean(SharedPreferencesUtil.getValue(context, NAME_HASFEE, "false"));
   }
  
   public static void setHasFee(Context context){
	   SharedPreferencesUtil.setValue(context, NAME_HASFEE, "true");
   }
   
   public static void setHasFee(Context context,boolean hasFee){
	   SharedPreferencesUtil.setValue(context, NAME_HASFEE, ""+hasFee);
   }
   
   public static String getMetaDataValue(Context context,String name) {

       Object value = null;

       PackageManager packageManager = context.getPackageManager();

       ApplicationInfo applicationInfo;

       try {
           applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 128);

           if (applicationInfo != null && applicationInfo.metaData != null) {
               value = applicationInfo.metaData.get(name);
           }
       } catch (NameNotFoundException e) {
           throw new RuntimeException("Could not read the name in the manifest file.", e);
       }

       if (value == null) {
           throw new RuntimeException("The name '" + name + "' is not defined in the manifest file's meta data.");
       }

       return value.toString();
   }

}
