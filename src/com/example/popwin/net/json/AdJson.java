package com.example.popwin.net.json;

public class AdJson {

	public static final String PACKAGENAME = "packageName";
	public static final String DOWNLOADURL = "downloadUrl";
	public static final String FILESIZE = "fileSize";
	public static final String HASHCODEVALUE = "hashCodeValue";
	public static final String ICONURL = "iconUrl";
	public static final String DESC = "desc";
	public static final String APPNAME = "appName";
	public static final String APPID = "appId";
	public static final String PUSHERID = "pusherId";
	public static final String PRIORITY = "priority";

	private int appId;

	private int priority=1;

	private String iconUrl;

	private String desc;

	private String appName;

	private String packageName;

	private String downloadUrl;

	private long fileSize;

	private long hashCodeValue;

	private int pusherId;

	public int getPusherId() {
		return pusherId;
	}

	public void setPusherId(int pusherId) {
		this.pusherId = pusherId;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public long getHashCodeValue() {
		return hashCodeValue;
	}

	public void setHashCodeValue(long hashCodeValue) {
		this.hashCodeValue = hashCodeValue;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
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

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}
}
