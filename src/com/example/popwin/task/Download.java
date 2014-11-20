package com.example.popwin.task;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;

import com.example.popwin.MainActivity;
import com.example.popwin.net.sqlite.App;
import com.example.popwin.util.Common;
import com.example.popwin.util.LogUtil;
import com.legame.np.util.NetWorkUtils;
import com.legame.np.util.TaskUtil;
import com.legame.np.util.Utils;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * 涓嬭浇锛屾敮鎸佹柇锟�? * 
 * @author leoliu
 * 
 */
public class Download extends BaseWeb{
    private static final String TAG = "Download";
   
    private static long _fileSize = 10000;
    private static File _file = null;

	public static int progress = 0;
    
    public static int getDownloadRate(){
    	if(_file == null){
    		return 0;
    	}else{
    		return (int)(_file.length() * 100 / _fileSize);
    	}
    }
    
    public Download(Context context, Handler handler) {
        super(context, handler);
    }

    /**
     * 
     * @param downloadUrl
     * @param saveFilePath
     * @param fileSize
     *            鏂囦欢澶у皬
     * @return
     */
    public boolean downloadFile(App app) {
    	
        if (!startRequest(TaskUtil.TASK_DOWNLOAD_FAIL)) {
        	LogUtil.e(TAG, "bad Network");
//        	Toast.makeText(context, "网络连接失败", 1).show();
        	MainActivity.myDownHandler.obtainMessage(3).sendToTarget();
            return false;
        }
        MainActivity.myDownHandler.obtainMessage(2, app).sendToTarget();
//      final String downloadUrl = ad.getUrl();
//    	final long fileSize = ad.getFileSize();
        final String downloadUrl=app.getUrl();
        final int fileSize=app.getFileSize();
    	
        String key = Common.getKey(downloadUrl);

        final String saveFilePath = Common.getSavePath(context,key);
        
        LogUtil.e(TAG,"downloadFile -> key : "+key+" ; saveFile : "+saveFilePath);
        
        
        _fileSize = fileSize;
      
        File file = new File(saveFilePath);
        
        _file = file;
        
        RandomAccessFile rndFile = null;

        long currentSize = 0l;

        if (file.exists()) {
            currentSize = file.length();
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        long totalSize = currentSize;

        if (totalSize < fileSize) {
           //LogUtil.e(TAG,"totalSize "+totalSize +"< fileSize "+fileSize);
            HttpURLConnection httpConnection = null;
            BufferedInputStream bis = null;

            try {

                httpConnection = NetWorkUtils.getHttpURLConnection(context,
                        downloadUrl);
                httpConnection.setRequestProperty("User-Agent", "Net");
                if (currentSize > 0l) {
                    httpConnection.setRequestProperty("RANGE", "bytes="
                            + currentSize + "-");
                }
                httpConnection.setConnectTimeout(10000);
                httpConnection.setReadTimeout(20000);

                int responseCode = httpConnection.getResponseCode();

                if (responseCode != 200 && responseCode != 206) {
                   //LogUtil.e(TAG,"璇锋眰url澶辫触:"+responseCode);
                    throw new Exception("璇锋眰url澶辫触");
                }

                bis = new BufferedInputStream(httpConnection.getInputStream());

                rndFile = new RandomAccessFile(file, "rw");
                rndFile.seek(currentSize);

                byte[] buf = new byte[1024];
                int readSize = -1;
                int canDownload = getCanDownload();
               //LogUtil.e(TAG,"downloadFile -> canDownload : "+canDownload);
                try {
                    int localTotalSize = 0;
                    while ((readSize = bis.read(buf)) != -1) {
                        rndFile.write(buf, 0, readSize);

                        totalSize += readSize;
//                        LogUtil.e("totalSize/fileSize", 100*totalSize/fileSize);
                        progress=(int) (100*totalSize/fileSize);
                        localTotalSize += readSize;
                        if (totalSize >= fileSize
                                || localTotalSize >= canDownload) {
                            break;
                        }
                    }
                } catch (Exception e) {
                	//LogUtil.e(TAG,"downloadFile -> exception1 "+e.toString());
                    e.printStackTrace();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
               //LogUtil.e(TAG,"downloadFile -> exception2 "+ex.toString());
            } finally {
            	if(rndFile!=null){
            		try {
						rndFile.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (httpConnection != null) {
                    httpConnection.disconnect();
                }
            }
        }

        if (totalSize >= fileSize) {
            
        	//LogUtil.e(TAG,"downloadFile -> (totalSize >= fileSize && fileSize > 0) == true");
            
        	Message msg = handler.obtainMessage(TaskUtil.TASK_DOWNLOAD_SUCC, app);
        	Bundle data=new Bundle();
        	data.putString("filePath", saveFilePath);
        	data.putString("checkStr", app.getCheckStr());
        	msg.setData(data);
            msg.sendToTarget();
        } else {
        	//LogUtil.e(TAG,"downloadFile -> (totalSize >= fileSize && fileSize > 0) == false");
        	handler.sendEmptyMessage(TaskUtil.TASK_DOWNLOAD_FAIL);
        }

        return totalSize == fileSize && totalSize > 0l;
    }

    private int getCanDownload() {
        if (Utils.isWiFi(context)) {
            return 1024 * 1024 * 100;
        } else {
        	return 1024 * 1024 * 100;
        }
    }

}
