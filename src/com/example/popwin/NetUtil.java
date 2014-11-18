package com.example.popwin;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class NetUtil {
	public static Bitmap getBitmapFromServer(String imagePath) {  
	      
	    HttpGet get = new HttpGet(imagePath);
	    HttpClient client = new DefaultHttpClient();
	    Bitmap pic = null;
	    try {
	        HttpResponse response = client.execute(get);
	        HttpEntity entity = response.getEntity();
	        InputStream is = entity.getContent();
	        pic = BitmapFactory.decodeStream(is);   // 关键是这句代码
	        write(Bitmap2Bytes(pic));System.out.println("up is write to sdcard");
	    } catch (ClientProtocolException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return pic;
	}
	
	// Bitmap转byte数组
	public static byte[] Bitmap2Bytes(Bitmap bm) {
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);//png类型
	        return baos.toByteArray();
	}

	// 写到sdcard中
	public static void write(byte[] bs) throws IOException{
	        FileOutputStream out=new FileOutputStream(new File(Environment.getExternalStorageDirectory().getPath()+"/mygame/pic/test.png"));
	        out.write(bs);
	        out.flush();
	        out.close();
	}

	
//	unuseful
	public static String getApkmapFromServer(String linkUrl,String name) {
		// TODO Auto-generated method stub
		String newFilename = null;
		try {
			// 构造URL   
	         URL url = new URL(linkUrl);   
	         // 打开连接   
	         URLConnection con = url.openConnection();
	         //获得文件的长度
	         int contentLength = con.getContentLength();
	         System.out.println("长度 :"+contentLength);
	         // 输入流   
	         InputStream is = con.getInputStream();  
	         // 1K的数据缓冲   
	         byte[] bs = new byte[1024];   
	         // 读取到的数据长度   
	         int len;   
	         newFilename = Environment.getExternalStorageDirectory()+name+".apk";
			// 输出的文件流   
		         File newfile = new File(Environment.getExternalStorageDirectory(), name+".apk");
		         try{
		           if(!newfile.exists())
		                  newfile.createNewFile();
		          }catch(IOException e){
		              Log.e("IOException", "exception in createNewFile() method");
		          }
		         System.out.println(newfile.getPath());
	         OutputStream os = new FileOutputStream(newFilename);   
	         // 开始读取   
	         while ((len = is.read(bs)) != -1) {   
	             os.write(bs, 0, len);   
	         }  
	         // 完毕，关闭所有链接   
	         os.close();  
	         is.close();
	            
	} catch (Exception e) {
	        e.printStackTrace();
	}

		return newFilename;
	}
}
