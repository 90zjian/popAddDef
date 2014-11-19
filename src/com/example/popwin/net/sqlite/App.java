package com.example.popwin.net.sqlite;

public class App {

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if (o.toString().equals(this.toString())) {
			return true;
		} else
			return false;
		// return super.equals(o);
	}

	public static final String ICONURL = "iconUrl";
	public static final String DESC = "desc";
	public static final String APPNAME = "appName";
	public static final String PACKAGENAME = "packageName";
	public static final String URL = "url";
	public static final String FILESIZE = "fileSize";
	public static final String CHECKSTR = "checkStr";
	public static final String STATUS = "status";
	public static final String APPID = "appId";
	public static final String PUSHERID = "pusherId";
	public static final String PRIORITY = "priority";

	private int priority = 1;

	private int appId = 0;

	// private int adType = 0;

	private String iconUrl = "";

	private String desc = "";

	private String appName = "";

	private String packageName = "";

	private String url = "";

	private int fileSize = 0;

	private String checkStr = "0";

	private int status = 0;

	private int pusherId = 101;

	public void setPriority(int pri) {
		this.priority = pri;
	}

	public int getPriority() {
		return priority;
	}

	public int getpusherId() {
		return pusherId;
	}

	public void setpusherId(int i) {
		this.pusherId = i;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public String getCheckStr() {
		return checkStr;
	}

	public void setCheckStr(String checkStr) {
		this.checkStr = checkStr;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

}
