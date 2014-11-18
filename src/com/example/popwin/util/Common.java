package com.example.popwin.util;

import java.io.File;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;

public class Common {
	
	private static final String DEFULT_FILEPATH = "legame";
	
    /**
     * 判断是否email
     * 
     * @param email
     * @return
     */
    public boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    /**
     * 是否数字
     * 
     * @param str
     * @return
     */
    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }


    /**
     * 获取sd卡路�?
     * 
     * @return
     */
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = android.os.Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = android.os.Environment.getExternalStorageDirectory();// 鑾峰彇璺熺洰锟�
        }
        return sdDir == null ? "" : sdDir.toString();

    }

    public static String getSaveDir(Context context) {
        return getSDPath() + "/"+DEFULT_FILEPATH+"/"+context.getPackageName()+"/download";
    }

    public static String getSavePath(Context context,String key) {
        File dir = new File(getSaveDir(context));

        dirCreate(dir);

        return dir.getAbsolutePath() + "/" + key + ".apk";
    }
    
    public static String getImagePath(Context context,String key) {
        File dir = new File(getSaveDir(context));

        dirCreate(dir);

        return dir.getAbsolutePath() + "/" + key + ".png";
    }

    public static String getAppPath(String key) {

        return getSDPath() + "/app/" + key + "/";
    }

    public static void dirCreate(File dir) {

        if (!dir.exists()) {
            dirCreate(dir.getParentFile());
            dir.mkdir();
        }
    }

    // MD5
    public static String MD5(String str) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);

        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    public static boolean deleteFile(File file) {
        if (file == null)
            return false;
        if (file.isFile()) {
            return file.delete();
        } else {
            File[] files = file.listFiles();
            if (files != null && files.length != 0)
                for (File f : files) {
                    if (!deleteFile(f)) {
                        return false;
                    }
                }
            file.delete();
        }
        return true;
    }

    public static String getKey(String downloadUrl) {
        return downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1,
                downloadUrl.lastIndexOf("."));
    }
}
