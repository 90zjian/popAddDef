package com.legame.np.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import com.example.popwin.util.LogUtil;
import com.legame.np.org.AesCrypto;

import android.content.Context;

public class PostDataUtil {

    private static final String TAG = "PostDataUtil";

    public static String postData(Context context, String _url, String body) {
       LogUtil.e(TAG, "postData -> url : " + _url + " ; body : " + body);
        return postData(context, _url,AesCrypto.encrypt(body, Configs.password));
    }

    public static String postData(Context context, String _url, byte[] byt) {
        HttpURLConnection httpUrlConnection = null;
        String result = null;
        OutputStream outStrm = null;
        InputStream inStrm = null;
        ByteArrayOutputStream baos = null;

        try {
            httpUrlConnection = NetWorkUtils
                    .getHttpURLConnection(context, _url);

            httpUrlConnection.setAllowUserInteraction(true);
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setRequestProperty("Content-type",
                    "application/x-java-serialized-object");
            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setConnectTimeout(20000);
            outStrm = httpUrlConnection.getOutputStream();

            outStrm.write(byt);
            outStrm.flush();

            inStrm = httpUrlConnection.getInputStream();
            int read = -1;
            baos = new ByteArrayOutputStream();
            while ((read = inStrm.read()) != -1) {
                baos.write(read);
            }
            byte[] data = baos.toByteArray();

            result = new String(AesCrypto.decrypt(data, Configs.password),
                    "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        } finally {
            if (outStrm != null) {
                try {
                    outStrm.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (inStrm != null) {
                try {
                    inStrm.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

}
