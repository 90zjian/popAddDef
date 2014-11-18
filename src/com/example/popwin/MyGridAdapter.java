package com.example.popwin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.popwin.net.sqlite.AdHandler;
import com.example.popwin.net.sqlite.App;
import com.example.popwin.service.DownloadService;
import com.example.popwin.util.Common;
import com.example.popwin.util.LogUtil;
import com.legame.eightplatform.R;
import com.legame.np.util.InstallPlugsUtil;
import com.legame.np.util.TaskUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyGridAdapter extends BaseAdapter{    
    private Context mContext;
    		ArrayList<HashMap<String, Object>> arrList;
    		View wininfo;
    		Activity MainAc;
     public MyGridAdapter(Context context, ArrayList<HashMap<String, Object>> alist, View pwininfo, Activity ac){
         mContext=context;
         arrList=(ArrayList<HashMap<String, Object>>) alist.clone();
         wininfo=pwininfo;
         MainAc=ac;
     }
     
 	@Override
 	public int getCount() {
 		// TODO Auto-generated method stub
 		return arrList.size();
 	}

    @Override    
    public Object getItem(int position) {    
        // TODO Auto-generated method stub    
        return arrList.get(position);    
    }    

    @Override
    public long getItemId(int position) {    
        // TODO Auto-generated method stub    
        return position;    
    }    
    
    public void updateGridView(ArrayList<HashMap<String, Object>> list){
    	arrList.clear();
    	arrList=(ArrayList<HashMap<String, Object>>) list.clone();
    	MainActivity.myActivity.ma.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//    	System.out.println(""+position+"--"+convertView+"--"+parent);
//    	System.out.println(arrList);
        Holder holder;
        if(convertView==null){
            convertView=LayoutInflater.from(mContext).inflate(R.layout.popwininfo, null);
            holder=new Holder();
            holder.image=(ImageView) convertView.findViewById(R.id.ImageV);
            holder.name=(TextView) convertView.findViewById(R.id.textV2);
            convertView.setTag(holder);
        }
        else{
            holder=(Holder) convertView.getTag();
        }
        convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=(Intent) arrList.get(position).get("itemIntent");
				System.out.println(intent.getBooleanExtra("default", false));
				if(intent.getBooleanExtra("default", false)==true){
					
		    		final String saveUrl = Common.getSavePath(mContext, 
		    				MainActivity.myActivity.getResources().getString(R.string.defaultFileName));
		    		Boolean b=InstallPlugsUtil.install(mContext, saveUrl);
					System.out.println("click the default :"+saveUrl+" "+b);
//					App app=new App();
//					app.setPackageName(MainActivity.myActivity.getResources().getString(R.string.defaultPackageName));
//					App defaultApp = Util.searchApp(app, MainActivity.arrDownMap_copy);
//					if(defaultApp==null){
//						defaultApp=MainActivity.appjxt;
//						System.out.println(defaultApp.getAppName()+"///"+defaultApp.getFileSize()+";;;"+defaultApp.getUrl());
//					}
//					defaultApp.setStatus(TaskUtil.STATUS_DOWNLOADING);
//
//					Intent intent1 = new Intent(MainActivity.myActivity,
//							DownloadService.class);
//					intent1.putExtra(App.APPID, defaultApp
//							.getAppId());
//					new AdHandler(mContext).putOneToDownload(defaultApp);
//					MainActivity.myActivity.startService(intent1);
					
					return;
				}
				try{
				MainAc.startActivity(intent);
				}
				catch(Exception e){
					Log.e("err", e.toString());
				}
			}
		});
        holder.name.setText((CharSequence) arrList.get(position).get("itemName"));
        holder.image.setImageDrawable((Drawable) arrList.get(position).get("itemImage"));
        return convertView;
    }
    public class Holder{
    	ImageView image;
    	TextView name;
    	Intent intent;
    }
}