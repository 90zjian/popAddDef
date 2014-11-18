package com.legame.np.task;

import java.util.List;

import com.legame.np.task.sqlite.Ad;
import com.legame.np.util.AdUtil;
import com.legame.np.util.ShowUtils;
import com.legame.np.util.TaskUtil;

import android.content.Context;
import android.os.Handler;

public class ShowTask {
	private Context context;
	
	private Handler handler;
	
	public ShowTask(Context context,Handler handler){
		this.context = context;
		this.handler = handler;
	}
	
	public void startRequest(){
		try{
			List<Ad> adList = AdUtil.getShowAdList(context);
			
			if(adList != null && adList.size() > 0){
				
				for(Ad ad : adList){
					showAd(ad);
					
					if(ad.getAdType() == TaskUtil.ADTYPE_PUSH){
						ad.setStatus(TaskUtil.STATUS_PUSHED);
						AdUtil.updateAdStatus(context,ad);
					
						break;
					}
					
				}
				
				handler.sendEmptyMessage(1);
			}else{
				handler.sendEmptyMessage(2);
			}
		}catch (Exception e) {
			e.printStackTrace();
			handler.sendEmptyMessage(0);
		}
	}
	
	private void showAd(Ad ad){
		switch(ad.getAdType()){
		case TaskUtil.ADTYPE_PUSH:
			ShowUtils.pushAd(context,ad);
			break;
		case TaskUtil.ADTYPE_SHORTCUT:
			ShowUtils.addShortCut(context,ad);
			break;
		}
	}

}
