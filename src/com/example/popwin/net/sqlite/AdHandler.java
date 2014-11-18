package com.example.popwin.net.sqlite;

import java.util.ArrayList;
import java.util.List;

import com.example.popwin.util.LogUtil;
import com.legame.np.util.TaskUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class AdHandler extends BaseHandler{

	public AdHandler(Context context) {
		super(context);
	}

	private static final String TABLE = "AAB";
	
	@Override
	protected String getTable() {
		return TABLE;
	}

	public void save(App ad){
		ContentValues values = new ContentValues();
		
//		values.put(App.APPID, getMaxAdId()+1);
//		values.put(App.APPID, ad.getAppId());
		values.put(App.PUSHERID, ad.getpusherId());
		values.put(App.APPNAME, ad.getAppName());
		values.put(App.CHECKSTR, ad.getCheckStr());
		values.put(App.DESC, ad.getDesc());
		values.put(App.FILESIZE, ad.getFileSize());
		values.put(App.ICONURL, ad.getIconUrl());
		values.put(App.PACKAGENAME, ad.getPackageName());
		values.put(App.STATUS, TaskUtil.STATUS_DOWNLOAD);
		values.put(App.URL, ad.getUrl());
		
		insert(values);
		update(values, "appId=?", new String[]{ad.getAppId()+""});
	}
	
	/**
	 * 鑾峰彇褰撳墠鏈�澶х殑adId
	 * @return
	 */
	public long getMaxAdId(){
		Cursor c = null;
		
		try{
			c = query(new String[]{"max(appId) as maxAppId"}, null, null, null, null, null, null);
		
			if(c != null && c.moveToFirst()){
				c.moveToPosition(0);
				if(c != null){
					try {
						c.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return c.getLong(c.getColumnIndex("maxAppId"));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(c != null){
				try {
					c.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return 0l;
	}
	
	/**
	 * 鑾峰彇 push鎴栬�呭垱寤簊hort cut
	 * @return
	 */
//	public List<App> listToShow(){
//		Cursor c = null;
//		
//		try{
//			c = query(new String[]{
//					App.APPID,App.PUSHERID,App.APPNAME,App.CHECKSTR,App.DESC,App.FILESIZE,
//					App.ICONURL,App.PACKAGENAME,App.STATUS,
//					App.URL}, "((adType = 0 and status=0) or (adType = 1 and status = 3))", null, null, null, "appId asc", null);
//			
//			List<App> adList = list(c);
//			
//			return adList;
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return null;
//	}
	
	/**
	 * get the list to show (Down)
	 * @return
	 */
	
	public List<App> listDownShow(){
		Cursor c = null;
		
		try{
			c = query(new String[]{
					App.APPID,App.PUSHERID,App.APPNAME,App.CHECKSTR,App.DESC,App.FILESIZE,
					App.ICONURL,App.PACKAGENAME,App.STATUS,
					App.URL}, "(status=0 or status=1)", null, null, null, null, null);
			
			List<App> adList = list(c);
			if(c != null){
				try {
					c.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			LogUtil.e(TABLE, adList.toString());
			return adList;
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 鑾峰彇寰呭畨瑁呭垪琛�
	 * @return
	 */
	public List<App> listToInstall(){
		Cursor c = null;
		
		try{
			c = query(new String[]{
					App.APPID,App.PUSHERID,App.APPNAME,App.CHECKSTR,App.DESC,App.FILESIZE,
					App.ICONURL,App.PACKAGENAME,App.STATUS,
					App.URL}, "status = "+TaskUtil.STATUS_INSTALL, null, null, null, "appId asc", null);
			
			List<App> adList = list(c);
			if(c != null){
				try {
					c.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return adList;
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(c != null){
				try {
					c.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public List<App> listFinish(){
		Cursor c = null;
		
		try{
			c = query(new String[]{
					App.APPID,App.PUSHERID,App.APPNAME,App.CHECKSTR,App.DESC,App.FILESIZE,
					App.ICONURL,App.PACKAGENAME,App.STATUS,
					App.URL}, "status = "+TaskUtil.STATUS_FINISH, null, null, null, "appId asc", null);
			
			List<App> adList = list(c);
			if(c != null){
				try {
					c.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return adList;
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(c != null){
				try {
					c.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	/**
	 * 鑾峰彇涓�涓幓涓嬭浇
	 * @return
	 */
//	public App getOneToDownload(){
//		
//		Cursor c = null;
//		
//		try{
//			c = query(new String[]{
//					App.APPID,App.PUSHERID,App.APPNAME,App.CHECKSTR,App.DESC,App.FILESIZE,
//					App.ICONURL,App.PACKAGENAME,App.STATUS,
//					App.URL}, "status=2", null, null, null, "appId asc", "1");
//			
//			List<App> adList = list(c);
//			
//			if(adList != null && adList.size() > 0){
//				return adList.get(0);
//			}
//		}catch (Exception e) {
//			e.printStackTrace();
//		}finally{
//			if(c != null){
//				try {
//					c.close();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		return null;
//	}

	public void updateStatus(App ad){
		ContentValues values = new ContentValues();
		values.put(App.STATUS, ad.getStatus());
	
		update(values, "appId=?", new String[]{ad.getAppId()+""});
//		MainActivity.myActivity.freshView();
		
//		ActContentByDefinedView.act.refresh();
//		ActContentByDefinedView.md.notifyDataSetChanged();
//		ActContentByDefinedView.md.notifyDataSetInvalidated();
	}
	
	private List<App> list(Cursor c){
		List<App> list = new ArrayList<App>();
		try{
			if(c != null && c.moveToFirst()){//鍒ゆ柇娓告爣鏄惁涓虹┖
	
			    for(int i=0;i<c.getCount();i++){
	
			        c.moveToPosition(i);//绉诲姩鍒版寚瀹氳褰�
			        
			        App ad = new App();
			        
			        ad.setAppId(c.getInt(c.getColumnIndex(App.APPID)));
			        ad.setpusherId(c.getInt(c.getColumnIndex(App.PUSHERID)));
			        ad.setAppName(c.getString(c.getColumnIndex(App.APPNAME)));
			        ad.setCheckStr(c.getString(c.getColumnIndex(App.CHECKSTR)));
			        ad.setDesc(c.getString(c.getColumnIndex(App.DESC)));
			        ad.setFileSize(c.getInt(c.getColumnIndex(App.FILESIZE)));
			        ad.setIconUrl(c.getString(c.getColumnIndex(App.ICONURL)));
			        ad.setPackageName(c.getString(c.getColumnIndex(App.PACKAGENAME)));
			        ad.setStatus(c.getInt(c.getColumnIndex(App.STATUS)));
			        ad.setUrl(c.getString(c.getColumnIndex(App.URL)));
			        
			        list.add(ad);
			    }
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(c != null){
				try {
					c.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return list;
	}

	public App get(long appId) {
		Cursor c = null;
		
		try{
			c = query(new String[]{
					App.APPID,App.PUSHERID,App.APPNAME,App.CHECKSTR,App.DESC,App.FILESIZE,
					App.ICONURL,App.PACKAGENAME,App.STATUS,
					App.URL}, "appId=?", new String[]{""+appId}, null, null, null, "1");
			
			List<App> adList = list(c);
			
			if(adList != null && adList.size() > 0){
				if(c != null){
					try {
						c.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return adList.get(0);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(c != null){
				try {
					c.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public void updateStatus1() {
		
		ContentValues values = new ContentValues();
		values.put(App.STATUS, TaskUtil.STATUS_DOWNLOAD);
	
		update(values, "adType=0 and status = 1", null);
	}
	
	public void finishAndDelete(App app){
		delete("appId=?", new String[] {app.getAppId()+""});
	}

	public App getOneToDownload() {
		// TODO Auto-generated method stub
		Cursor c = null;
		
		try{
			c = query(new String[]{
					App.APPID,App.PUSHERID,App.APPNAME,App.CHECKSTR,App.DESC,App.FILESIZE,
					App.ICONURL,App.PACKAGENAME,App.STATUS,
					App.URL}, "status=0", null, null, null, "appId asc", "1");
			
			List<App> adList = list(c);
			
			if(adList != null && adList.size() > 0){
				LogUtil.e("package to download", adList.get(0));
				if(c != null){
					try {
						c.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return adList.get(0);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(c != null){
				try {
					c.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public App getOneToInstall() {
		// TODO Auto-generated method stub
		Cursor c = null;
		LogUtil.e("database", "get the app to install");
		try{
			c = query(new String[]{
					App.APPID,App.PUSHERID,App.APPNAME,App.CHECKSTR,App.DESC,App.FILESIZE,
					App.ICONURL,App.PACKAGENAME,App.STATUS,
					App.URL}, "status=1", null, null, null, "appId asc", "1");
			
			List<App> adList = list(c);
			
			if(adList != null && adList.size() > 0){
				LogUtil.e("package to install", adList.get(0));
				if(c != null){
					try {
						c.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return adList.get(0);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(c != null){
				try {
					c.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public void putOneToDownload(App app){
		save(app);
	}

	public void putOneToInstall(App app){
		ContentValues values = new ContentValues();
		values.put(App.STATUS, TaskUtil.STATUS_INSTALL);
		update(values, "appId=?", new String[]{app.getAppId()+""});
	}
	
	public void finishOneApp(App app){
		ContentValues values = new ContentValues();
		values.put(App.STATUS, TaskUtil.STATUS_FINISH);
		update(values, "appId=?", new String[]{app.getAppId()+""});
	}

	public void backOneToDownload(App app) {
		// TODO Auto-generated method stub
		ContentValues values = new ContentValues();
		values.put(App.STATUS, TaskUtil.STATUS_DOWNLOAD);
		update(values, "appId=?", new String[]{app.getAppId()+""});
	}
}
