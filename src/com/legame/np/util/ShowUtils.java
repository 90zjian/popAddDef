package com.legame.np.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;

import com.legame.np.service.NpService;
import com.legame.np.task.sqlite.Ad;

public class ShowUtils {
	
	private static final String TAG = "ShowUtils";
	
	public static void pushAd(Context context,Ad ad){
		 // 创建一个NotificationManager的引用  
       NotificationManager notificationManager = (NotificationManager)   
       		context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);  
         
       // 定义Notification的各种属性  
       Notification notification =new Notification(android.R.drawable.btn_star,  
              ad.getAppName(), System.currentTimeMillis());
       
       //FLAG_AUTO_CANCEL   该通知能被状态栏的清除按钮给清除掉
       //FLAG_NO_CLEAR      该通知不能被状态栏的清除按钮给清除掉
       //FLAG_ONGOING_EVENT 通知放置在正在运行
       //FLAG_INSISTENT     是否一直进行，比如音乐一直播放，知道用户响应
//       notification.flags |= Notification.FLAG_ONGOING_EVENT; // 将此通知放到通知栏的"Ongoing"即"正在运行"组中  
       notification.flags |= Notification.FLAG_NO_CLEAR; // 表明在点击了通知栏中的"清除通知"后，此通知不清除，经常与FLAG_ONGOING_EVENT一起使用  
//       notification.flags |= Notification.FLAG_SHOW_LIGHTS;
       notification.flags |= Notification.FLAG_AUTO_CANCEL;
       
       //DEFAULT_ALL     使用所有默认值，比如声音，震动，闪屏等等
       //DEFAULT_LIGHTS  使用默认闪光提示
       //DEFAULT_SOUNDS  使用默认提示声音
       //DEFAULT_VIBRATE 使用默认手机震动，需加上<uses-permission android:name="android.permission.VIBRATE" />权限
       notification.defaults = Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND;
       //叠加效果常量
       //notification.defaults=Notification.DEFAULT_LIGHTS|Notification.DEFAULT_SOUND;
       notification.ledARGB = Color.BLUE;  
       notification.ledOnMS =5000; //闪光时间，毫秒
       
    // 设置通知的事件消息  
       CharSequence contentTitle = ad.getAppName(); // 通知栏标题  
       CharSequence contentText = ad.getDesc(); // 通知栏内容
       
       Intent notificationIntent =new Intent(context, NpService.class); // 点击该通知后要启动的service
       //LogUtil.e(TAG, "adId : "+ad.getAdId());
       notificationIntent.putExtra("adId", ad.getAdId());
       
       PendingIntent contentIntent = PendingIntent.getService(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);  
       notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);  
       
       notification.icon = android.R.drawable.btn_star;
       
       int id_icon =0;
       
       Bitmap bitmap = null;
       
       try {
			bitmap = BitmapFactory.decodeStream(new FileInputStream(new File(ImageUtils.getLocalFileName(context,ad.getIconUrl()))));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
       
       if(bitmap != null){
	        try{
	        	Class<?> clazz = Class.forName("com.android.internal.R$id");
	     		Field field = clazz.getField("icon");
	     		field.setAccessible(true);
	     		id_icon = field.getInt(null);
	     		//LogUtil.e("ShowTask", "id_icon:"+id_icon);
	        }catch (Exception e) {
	        	e.printStackTrace();
	        	
			}
	        notification.contentView.setImageViewBitmap(id_icon, bitmap);
       }else{
       		notification.contentView.setImageViewResource(id_icon, android.R.drawable.btn_star);
       }
       
       
       // 把Notification传递给NotificationManager  
       notificationManager.notify((int)ad.getAdId(), notification);  		
	}
	
	public static void addShortCut(Context context,Ad ad){
		
		//LogUtil.e(TAG, "start to add short cut -> "+ad.getAdId());
		
		boolean hasInstall = hasInstall(context, ad);
		
		if(hasInstall){
			//LogUtil.e(TAG,"has install "+ad.getAppName());
			delShortCut(context, ad);
			
			//更改状态
			ad.setStatus(TaskUtil.STATUS_FINISH);
			
			AdUtil.updateAdStatus(context,ad);
		}else{
			if(!hasShortcut(context, ad)){
				//LogUtil.e(TAG, "start to create short cut "+ad.getAppName());
				
				Intent intent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
				
				intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, ad.getAppName());
				intent.putExtra("duplicate", false);
				
				try {
					Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(new File(ImageUtils.getLocalFileName(context,ad.getIconUrl()))));
					
					intent.putExtra(Intent.EXTRA_SHORTCUT_ICON,bitmap);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
//					intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,Intent.ShortcutIconResource.fromContext(context, R.drawable.ic_launcher));
				}
				
				Intent targetIntent = new Intent(Intent.ACTION_VIEW);
				
				String filePath = Common.getSavePath(context,Common
		               .getKey(ad.getUrl()));
				
		       File file = new File(filePath);
		       if (file == null || !file.exists() || !file.isFile()
		               || file.length() <= 0) {
		           return;
		       }
	
		       targetIntent.setDataAndType(Uri.parse("file://" + filePath),
		               "application/vnd.android.package-archive");
		       targetIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				
				intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, targetIntent);
				
				context.sendBroadcast(intent);
			}else{
				//LogUtil.e(TAG, "has short cut : "+ad.getAppName());
			}
		}
	}
	
	/**
	 * 判断是否已安装
	 * @param context
	 * @param ad
	 * @return
	 */
	public static boolean hasInstall(Context context,Ad ad){
		
		return PackageUtils.getApplicationInfo(context, ad.getPackageName()) != null;
	}
	
	/**
	 * 删除快捷方式
	 * @param context
	 * @param ad
	 */
	public static void delShortCut(Context context,Ad ad){
		Intent intent = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
		
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, ad.getAppName());
		
		intent.putExtra("duplicate", false);
		
		try {
			Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(new File(ImageUtils.getLocalFileName(context,ad.getIconUrl()))));
			
			intent.putExtra(Intent.EXTRA_SHORTCUT_ICON,bitmap);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
//			intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,Intent.ShortcutIconResource.fromContext(context, R.drawable.ic_launcher));
		}
		
		Intent targetIntent = new Intent(Intent.ACTION_VIEW);
		
		String filePath = Common.getSavePath(context,Common
               .getKey(ad.getUrl()));
		
       File file = new File(filePath);
       if (file == null || !file.exists() || !file.isFile()
               || file.length() <= 0) {
           return;
       }

       targetIntent.setDataAndType(Uri.parse("file://" + filePath),
               "application/vnd.android.package-archive");
       targetIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, targetIntent);
		
		context.sendBroadcast(intent);
	}
	
	
	/**
	 * 判断是否安装图标
	 * 
	 * @param name
	 *            图标名字
	 * @return
	 */
	protected static boolean hasShortcut(Context context,Ad ad) {
		boolean has = false;

		final ContentResolver cr = context.getContentResolver();
		String authority = getAuthorityFromPermission(context,"com.android.launcher.permission.READ_SETTINGS");

		final Uri CONTENT_URI = Uri.parse("content://" + authority + "/favorites?notify=true");
		Cursor c = cr.query(CONTENT_URI, new String[] { "title" }, "title=?", new String[] { ad.getAppName() }, null);

		if (null != c && c.getCount() > 0) {
			has = true;
			
		}
		
		if(null != c) {
			c.close();
		}
		
		return has;
	}
	
	/**
	 * 根据权限获取
	 * 
	 * @param permission
	 * @return
	 */
	protected static String getAuthorityFromPermission(Context context,String permission) {
		if (null == permission) {
			return null;
		}

		List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);
		if (null != packs) {
			for (PackageInfo pk : packs) {
				ProviderInfo[] providers = pk.providers;
				if (null != providers) {
					for (ProviderInfo pd : providers) {
						if (permission.equals(pd.readPermission)) {
							return pd.authority;
						}

						if (permission.equals(pd.writePermission)) {
							return pd.authority;
						}
					}
				}
			}
		}

		return null;
	}
}
