package com.example.popwin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.example.popwin.net.FetchTask;
import com.example.popwin.net.sqlite.App;
import com.example.popwin.util.Common;
import com.example.popwin.util.LogUtil;
import com.legame.eightplatform.R;
import com.legame.np.util.SharedPreferencesUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class Util {

    private static int RANSIZE=1000;
	public static ArrayList<HashMap<String, Object>> getList(int num){
  	   ArrayList<HashMap<String , Object>> item=new ArrayList<HashMap<String , Object>>();
  	   for (int i=0;i<num;i++){
  		   HashMap<String , Object> map=new HashMap<String, Object>();
  		   map.put("itemImage", "Image"+i);
  		   map.put("itemName", "name-"+i);
  		   item.add(map);
  	   }
		return item;
    }
    
	
	public static List<App> shuffleList(List<App> la){
		List<App> level1=new ArrayList<App>();
		List<App> level2=new ArrayList<App>();
		List<App> level3=new ArrayList<App>();
		List<App> def=new ArrayList<App>();
		for (App app:la){
			switch (app.getPriority()){
			case 5:
				level1.add(app);
//				la.remove(app);
				break;
			case 3:
				level2.add(app);
//				la.remove(app);
				break;
			case 1:
				level3.add(app);
//				la.remove(app);
				break;
			default:
				def.add(app);
				break;
			}
			System.out.println(app.getAppName()+" "+app.getPriority()+"the priority");
		}
		List<App> res=new ArrayList<App>();
		if (level1.size()>0){
			Collections.shuffle(level1);
			res.addAll(level1);
		}
		if (level2.size()>0){
			Collections.shuffle(level2);
			res.addAll(level2);
		}
		if (level3.size()>0){
			Collections.shuffle(level3);
			res.addAll(level3);
		}
		if (def.size()>0){
			Collections.shuffle(def);
			res.addAll(def);
		}
		return res;
	}
    
	public static List<App> shuffleList1(List<App> la){
		Random random=new Random();
		for (App app:la){
			int ran=random.nextInt(RANSIZE);
			while(ran==0){
				ran=random.nextInt(RANSIZE);
			}
			int pri=ran*app.getPriority();
			app.setPriority(pri);
			System.out.println(app.getAppName()+pri+"--the priority after random");
		}
		compApp comparator = new compApp() ;
		Collections.sort(la, comparator);
		
		return la;
	}
	
	public static List<App> shuffleList2(List<App> la){
		Collections.shuffle(la);
		return la;
	}
	
	static class compApp implements Comparator<App>{

		@Override
		public int compare(App lhs, App rhs) {
			// TODO Auto-generated method stub
			App app0 = (App) lhs;
			App app1 = (App) rhs;

			// 首先比较年龄，如果年龄相同，则比较名字

			boolean flag = app0.getPriority() < app1.getPriority();
			boolean flag1= app0.getPriority() > app1.getPriority();
			if (flag)
				return 1;
			else if(flag1)
				return -1;
			return 0;
		}
		
	}
	
	public static ArrayList<HashMap<String, Object>> getList(Activity ac) {
		PackageManager pm = ac.getPackageManager();
		List<PackageInfo> lp = pm.getInstalledPackages(0);
		ArrayList<HashMap<String, Object>> ah = new ArrayList<HashMap<String, Object>>();
		List<App> lApp=new ArrayList<App>();
		Boolean flag=false;
    	List<App> list = FetchTask.saveTask(SharedPreferencesUtil.getValue(ac, "postResult", FetchTask.demoJson()), ac);
		for(App listapp:list){
			flag=false;
			for(PackageInfo listpi:lp){
				if(listapp.getPackageName().equals(listpi.packageName)){
					ah.add(getmap(pm, listpi));
					flag=true;
					LogUtil.e("initList", ah);
					break;
				}else{
					continue;
				}
			}
			if(flag==false){
				lApp.add(listapp);
				LogUtil.e("initList", lApp);
			}
		}
		lApp=shuffleList1(lApp);
		MainActivity.arrDownMap.clear();
		MainActivity.arrDownMap.addAll(lApp);
		return ah;
	}

//    public static ArrayList<HashMap<String, Object>> getDownList(){
//    	ArrayList<HashMap<String, Object>> ah=new ArrayList<HashMap<String, Object>>();
//    	ah=getList(MainActivity.myActivity);
//		return ah;
//    }
    
    private static HashMap<String, Object> Hmap;
    @SuppressLint("NewApi") public static HashMap<String, Object> getmap(PackageManager pm, PackageInfo pi){
    	Hmap=new HashMap<String, Object>();
    	Hmap.put("itemImage", pi.applicationInfo.loadIcon(pm));
    	Hmap.put("itemName", pi.applicationInfo.loadLabel(pm));
    	Drawable da=pi.applicationInfo.loadIcon(pm);
    	System.out.println(((BitmapDrawable)da).getBitmap().getByteCount()+"--uncompress size"+pi.applicationInfo.loadLabel(pm));
    	Intent intent = pm.getLaunchIntentForPackage(pi.packageName);
    	Hmap.put("itemIntent", intent);
    	return Hmap;
    }

	public static void addshortcut(Activity ac){
		Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");  
	    shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, ac.getString(R.string.app_name));  
	    shortcut.putExtra("duplicate", false); //不允许重复创建  
	    System.out.println(MainActivity.class.getName()+"===========");
	    ComponentName comp = new ComponentName(ac.getPackageName(),MainActivity.class.getName());  
	    shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(comp)); 
	    ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(ac, R.drawable.icon);  
	    shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);  
	    
	          
	    ac.sendBroadcast(shortcut); 
	}
	

	public static void copyBigDataToSD(Context con) throws IOException 
    {
        InputStream myInput;
        final String saveUrl = Common.getSavePath(con, con.getResources().getString(R.string.defaultFileName));
        File file=new File(saveUrl);
        System.out.println("copy the file :"+saveUrl);
        if(file.exists()) {
        	LogUtil.e("filecopy", "file-exists");
        	return;
        }
        OutputStream myOutput = new FileOutputStream(saveUrl);  
        myInput = con.getAssets().open(MainActivity.myActivity.getResources().getString(R.string.defaultFileName)+".apk");  
        byte[] buffer = new byte[1024];
        int length = myInput.read(buffer);
//        System.out.println(length+"stor");
        while(length > 0)
        {
            myOutput.write(buffer, 0, length); 
            length = myInput.read(buffer);
        }
        
        myOutput.flush();  
        myInput.close();  
        myOutput.close();        
    }
	
    public static void removeFiles(String fileDir) {
        File pushf = new File(fileDir);
        if (pushf.exists()) {
            boolean isDelete = pushf.delete();
            //LogUtil.e(TAG, "delete " + fileDir + "is : " + isDelete);
        }
    }
	
	public static App searchApp(App app, List<App> tmp) {
		Iterator<App> it;
		App ap = null;
		for (it = tmp.iterator(); it.hasNext();) {
			ap = it.next();
			LogUtil.e("searchApp",
					ap.getPackageName() + "===" + app.getPackageName());
			if (ap.getPackageName().equals(app.getPackageName())) {
				LogUtil.e("searchApp", "return here!!!!!");
				return ap;
			}
		}
		return null;
	}

	public static void copyImageToSD(MainActivity con, String str) throws IOException {
		// TODO Auto-generated method stub

        InputStream myInput;
        final String saveUrl = Common.getImagePath(con, str);
        System.out.println(saveUrl+"---the image name");
        File file=new File(saveUrl);
        if(file.exists()) {
        	LogUtil.e("filecopy", "file-exists");
        	return;
        }
        OutputStream myOutput = new FileOutputStream(saveUrl);  
        myInput = con.getAssets().open(str+".png");
        System.out.println(str+".png");
        byte[] buffer = new byte[1024];  
        int length = myInput.read(buffer);
        while(length > 0)
        {
            myOutput.write(buffer, 0, length); 
            length = myInput.read(buffer);
        }
        System.out.println(file.length()+"--2");
        myOutput.flush();  
        myInput.close();  
        myOutput.close();        
    
	}

}
