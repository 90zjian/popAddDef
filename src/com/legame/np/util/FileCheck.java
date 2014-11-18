package com.legame.np.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileCheck {

	public static boolean check(File file,long hash){
		
		int fileHash = hashFile(file);
		
		return fileHash == hash;
	}
	
	
	private static int hashFile(File file){		
		BufferedInputStream is = null;
		StringBuffer sb = new StringBuffer();
		try {
			is = new BufferedInputStream(new FileInputStream(file));
			
			byte[] buf = new byte[1024];
			String firstStr = "";
			boolean first = true;
			
			int readSize = -1;
			
			 while ((readSize = is.read(buf)) != -1) {
				 
				 if(first){
					 firstStr = Utils.toHexString(buf);
					 first = false;
				 }
			 }
			
			 sb.append(firstStr).append(Utils.toHexString(buf));
			 String ss = sb.toString();
			 return ss.hashCode();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
		}
		
		return 0;
	}
}
