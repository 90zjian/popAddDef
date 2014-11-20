package com.example.popwin;

import java.io.File;

import com.example.popwin.net.sqlite.App;
import com.example.popwin.util.Common;
import com.example.popwin.util.LogUtil;

public class MyThread extends Thread {

	private App app;
	public static Boolean running=false;
	public MyThread(App app) {
		this.app = app;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// String appName = app.getAppName();
		running=true;
		App a = null;
		int percent = 0;

		while (comp() < 100) {
			percent = comp();
			a = Util.searchApp(app, MainActivity.arrDownMap_copy);
			if (MainActivity.arrDownMap_copy.contains(a)) {
				a.setAppName(percent + "%");
			}
			LogUtil.e("download", percent + "%");
			MainActivity.freshViewHandler.obtainMessage(1).sendToTarget();
			try {
				Thread.sleep(1000);
				LogUtil.e("Downloading", percent + "percent");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (a != null) {
			a.setAppName("ÏÂÔØÍê³É");
			MainActivity.freshViewHandler.obtainMessage(1).sendToTarget();
		}
		running=false;
	}

	public String getFilePath() {
		final String downloadUrl = app.getUrl();
		String key = Common.getKey(downloadUrl);
		String saveFilePath = Common.getSavePath(MainActivity.myActivity, key);
		LogUtil.e("get the size of downloading file", "downloadFile -> key : "
				+ key + " ; saveFile : " + saveFilePath);
		return saveFilePath;
	};

	public int comp() {
		String saveFilePath = getFilePath();
		int fileSize = app.getFileSize();
		File file = new File(saveFilePath);
		long currentSize = file.length();
		return (int) (currentSize * 100 / fileSize);
	}
}
