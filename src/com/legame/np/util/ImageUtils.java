package com.legame.np.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

import com.example.popwin.util.Common;
import com.example.popwin.util.LogUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.ImageView;

public class ImageUtils {
	
	/**
	 * download
	 * @param context
	 * @param url
	 */
	public static void downloadNetImg(final Context context,final String packageName){
		
		File file = null;
		LogUtil.e("DownLoadTheImage", packageName);
		String fileName = getLocalFileName(context, packageName);
		if(fileName != null){
			file = new File(fileName);
			
			if(!file.exists()){
				
				Common.dirCreate(file.getParentFile());
				
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			InputStream is = null;
			
			BufferedInputStream bis = null;
			RandomAccessFile rndFile = null;
			
			try {
				HttpURLConnection conn = NetWorkUtils.getHttpURLConnection(context, packageName);
				conn.setDoInput(true);
				conn.connect();
				is = conn.getInputStream();
				
				bis = new BufferedInputStream(is);
				
				rndFile = new RandomAccessFile(file, "rw");
				rndFile.seek(0l);
				
				byte[] buf = new byte[1024];
                int readSize = -1;
                while ((readSize = bis.read(buf)) != -1) {
                	rndFile.write(buf,0,readSize);
                }
				LogUtil.e("DownloadTheImage", "realDownhere--imageSize="+rndFile.length());
				
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if(rndFile != null){
					try {
						rndFile.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				if(bis != null){
					try {
						bis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				if(is != null){
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * show 
	 * @param context
	 * @param imageView
	 * @param url
	 */
	public static void showNetImg(final Context context, final ImageView imageView,
			final String url) {

		final Handler imgHandler = new Handler(Looper.getMainLooper()) {
			@Override
			public void handleMessage(Message message) {
				Bitmap bitmap = (Bitmap) message.obj;

				imageView.setImageBitmap(bitmap);
			}
		};

		new Thread() {
			public void run() {
				
				Bitmap bitmap = getBitmap(context,url);
				
				Message message = imgHandler.obtainMessage();
				message.obj = bitmap;
				
				message.sendToTarget();
			}
		}.start();

	}
	
	private static Bitmap getBitmap(Context context,String url){
		
		Bitmap bitmap = getBitmapLocal(context, url);
		
		if(bitmap == null){
			bitmap = getBitMapRemote(context, url);
		}
		
		return bitmap;
	}
	
	private static Bitmap getBitmapLocal(Context context,String url){
		
		String fileName = getLocalFileName(context, url);
		
		if(fileName != null){
			File file = new File(fileName);
			
			if(file.exists()){
				return BitmapFactory.decodeFile(fileName);
			}
		}
		
		return null;
	}
	
	private static Bitmap getBitMapRemote(Context context,String url){
		
		Bitmap bitmap = null;
		InputStream is = null;
		try {
			HttpURLConnection conn = NetWorkUtils.getHttpURLConnection(context, url);
			conn.setDoInput(true);
			conn.connect();
			is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			
			return bitmap;
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}

	public static String getLocalFileName(Context context,String url){
		String saveDir = Common.getSaveDir(context);
		try {
			String fileName = URLEncoder.encode(url,"utf-8");
			
			return saveDir+"/"+fileName;
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		return null;
	}
}
