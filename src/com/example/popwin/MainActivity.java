package com.example.popwin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import com.example.popwin.net.sqlite.App;
import com.example.popwin.service.FetchNpService;
import com.example.popwin.util.LogUtil;
import com.legame.eightplatform.R;
import com.legame.np.util.SharedPreferencesUtil;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static View sub_view;
//	private static View info_view;
	public static PopupWindow mPopupWindow;
	// public static PopupWindow infoPop;
	public static DefinedScrollView ds;
	public static MainActivity myActivity;
	public static ArrayList<HashMap<String, Object>> arrMap;
	public static List<App> arrDownMap = new ArrayList<App>();
	public static MyGridAdapter ma;
	public static Handler myDownHandler;
	public static Handler freshViewHandler;
	public static Handler waitDownHandler;
	public static final List<String> ls = new ArrayList<String>();
	public static List<App> listFromSP;
	
//	public static App appjxt;
//	public static App appzx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myActivity=this;
//      			http%3A%2F%2Fyxxzwg.oss-cn-hangzhou.aliyuncs.com%2Fgamebox%2Fdlss%2Fdlss.icon
//     				http%3A%2F%2Fyxxzwg.oss-cn-hangzhou.aliyuncs.com%2Fgamebox%2Fglsc%2Fglsc.icon
//   			    http%3A%2F%2Fyxxzwg.oss-cn-hangzhou.aliyuncs.com%2Fgamebox%2Fhdl3wd%2Fhdl3wd.icon
//    			    http%3A%2F%2Fyxxzwg.oss-cn-hangzhou.aliyuncs.com%2Fgamebox%2Flkr1%2Flkr1.icon
//      			http%3A%2F%2Fyxxzwg.oss-cn-hangzhou.aliyuncs.com%2Fgamebox%2Fmlaxd%2Fmlaxd.icon
//      			http%3A%2F%2Fyxxzwg.oss-cn-hangzhou.aliyuncs.com%2Fgamebox%2Frzsg%2Frzsg.icon
//        			http%3A%2F%2Fyxxzwg.oss-cn-hangzhou.aliyuncs.com%2Fgamebox%2Fyxwd%2Fyxwd.icon
        String str="http%3A%2F%2Fyxxzwg.oss-cn-hangzhou.aliyuncs.com%2Fgamebox%2F";
        ls.add(str+"dlss%2Fdlss.icon");
        ls.add(str+"glsc%2Fglsc.icon");
        ls.add(str+"hdl3wd%2Fhdl3wd.icon");
        ls.add(str+"lkr1%2Flkr1.icon");
        ls.add(str+"mlaxd%2Fmlaxd.icon");
        ls.add(str+"rzsg%2Frzsg.icon");
        ls.add(str+"yxwd%2Fyxwd.icon");
        if(!SharedPreferencesUtil.getValue(myActivity, "shortcut", "N").equals("Y")){
        	Util.addshortcut(this);
        	SharedPreferencesUtil.setValue(myActivity, "shortcut", "Y");
	        try {
				Util.copyBigDataToSD(this);
				Iterator<String> i = ls.iterator();
				while (i.hasNext()){
					Util.copyImageToSD(this, i.next());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        initMap();
		initArrDownMap_copy();

        initPopuWindow(R.layout.popwin);
 	   	ArrayList<HashMap<String , Object>> item=(ArrayList<HashMap<String, Object>>) arrMap.clone();
        GridView gv=(GridView)sub_view.findViewById(R.id.gridview);
 	    ma = new MyGridAdapter(this, item, sub_view, this);
        gv.setAdapter(ma);

        init();
        mPopupWindow.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				MainActivity.this.finish();
			}
		});
        initApp();
        initHandler(); 
    }

    private void initApp() {
		// TODO Auto-generated method stub
//    	appjxt=new App();
//    	appzx=new App();
//		appjxt.setAppName("热血足球");
//		appjxt.setCheckStr("2078164984");
//		appjxt.setFileSize(1292713);
//		appjxt.setIconUrl("http://yxxzwg.oss-cn-hangzhou.aliyuncs.com/gamebox/rxzq/rxzq.icon.png");
//		appjxt.setPackageName("com.legame.rxzq.nes");
//		appjxt.setUrl("http://yxxzwg.oss-cn-hangzhou.aliyuncs.com/gamebox/rxzq/rxzq.10K301.apk");
//		appjxt.setpusherId(1);
//		
//		appzx.setAppName("热血足球");
//		appzx.setCheckStr("-1122055615");
//		appzx.setFileSize(1292714);
//		appzx.setIconUrl("http://yxxzwg.oss-cn-hangzhou.aliyuncs.com/gamebox/rxzq/rxzq.icon.png");
//		appzx.setPackageName("com.legame.rxzq.nes");
//		appzx.setUrl("http://yxxzwg.oss-cn-hangzhou.aliyuncs.com/gamebox/rxzq/rxzq.10L301.apk");
//		appzx.setpusherId(1);
	}

	private void init() {
		// TODO Auto-generated method stub
		Intent intent=new Intent(MainActivity.myActivity,FetchNpService.class);
		MainActivity.myActivity.startService(intent);
		
	}

	private static void initMap() {
		// TODO Auto-generated method stub
    	arrMap=Util.getList(myActivity);
    	if(arrMap.size()==0){
    		HashMap<String, Object> Hmap=new HashMap<String, Object>();
    		Hmap.put("itemName", myActivity.getResources().getString(R.string.defaultName));
    		Drawable da=MainActivity.myActivity.getResources().getDrawable(R.drawable.default_game);
    		Hmap.put("itemImage", da);
 
    		Intent intent=new Intent();
    		intent.putExtra("default", true);
    		Hmap.put("itemIntent", intent);
    		arrMap.add(Hmap);
    	}
	}

	public static List<App> arrDownMap_copy=new ArrayList<App>();
	private static List<App> arrDownMap_copy1=new ArrayList<App>();
	
	private static void initArrDownMap_copy(){
		arrDownMap_copy.clear();
		arrDownMap_copy.addAll(arrDownMap);
	}
	
	private static void initArrDownMap_copy1(){
		arrDownMap_copy1.clear();
		arrDownMap_copy1.addAll(arrDownMap_copy);
	}
	
	public static List<App> get4Data(){
    	List<App> fordata = new ArrayList<App>();
    	for (int i=0; i<4; i++){
    		try{
//	    		if (arrDownMap.get(i+1)!=null){

	  			if (!arrDownMap_copy1.isEmpty())
	  			{
	    			fordata.add(arrDownMap_copy1.get(0));
	        		arrDownMap_copy1.remove(0);
	    		}
    		}
    		catch(Exception e){
    			
    		}
    	}
    	LogUtil.e("result of Get the 4 data", fordata);
    	return fordata;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("menu");// 必须创建一项
		return super.onCreateOptionsMenu(menu);
	}

    /** 
     * 拦截MENU
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if(mPopupWindow != null){
            if(!mPopupWindow.isShowing()){
                /*最重要的一步：弹出显示   在指定的位置(parent)  最后两个参数 是相对于 x / y 轴的坐标*/  
                mPopupWindow.showAtLocation(sub_view, Gravity.CENTER, 0, 0);  
            }
        }
        return false;// 返回为true 则显示系统menu  
    }
  
       private void initPopuWindow(int menuViewID){
        LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        sub_view = mLayoutInflater.inflate(menuViewID, null);
        ds=(DefinedScrollView) sub_view.findViewById(R.id.definedview);
        ImageView iv=(ImageView)sub_view.findViewById(R.id.eightView);
        Bitmap bm=BitmapFactory.decodeResource(getResources(), R.drawable.eightpf);
        iv.setImageBitmap(bm);
        mPopupWindow = new PopupWindow(sub_view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0xaaaaaa));
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        mPopupWindow.update();
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        sub_view.setFocusableInTouchMode(true);
        sub_view.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if ((keyCode == KeyEvent.KEYCODE_MENU)&&(mPopupWindow.isShowing())) {
                    mPopupWindow.dismiss();// 这里写明模拟menu的PopupWindow退出就行
                    MainActivity.this.finish();
                    return true;
                }
                return false;
            }
        });
       }

       public Boolean showed=false;
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if(!SharedPreferencesUtil.getValue(myActivity, "shortcut", "N").equals("Y")){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (!showed){
	        mPopupWindow.showAtLocation(sub_view, Gravity.CENTER, 0, 0);
	        initMap();

	        initArrDownMap_copy();
	        initArrDownMap_copy1();
			ActContentByDefinedView.setupView(ds);
			showed=true;
		}
	}

	public static void initHandler() {
		myDownHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				LogUtil.e("receivedAMessage", msg.what);
				super.handleMessage(msg);
				Thread th;
				switch (msg.what) {
				case 1:
					initMap();
					App app1 = (App) msg.obj;
					arrDownMap.remove(app1);
					initArrDownMap_copy();
					initArrDownMap_copy1();
					LogUtil.e("the arrMap", "refreshUP");
					ma.updateGridView(MainActivity.arrMap);
					freshViewHandler.sendEmptyMessage(1);
					break;

				case 2:
					App app = (App) msg.obj;
					if(MyThread.running==false){
						th=new MyThread(app);
						th.start();
						System.out.println("Start the thread ");
					}
					else{
						System.out.println("wait to start the thread");
						Message msg1=new Message();
						msg1.copyFrom(msg);
						myDownHandler.removeMessages(2);
						myDownHandler.sendMessageDelayed(msg1, 3000);
					}
					break;
				case 3:
					Toast.makeText(MainActivity.myActivity, "网络连接失败。。。", 1)
							.show();
					break;
				default:
					break;
				}
			}
		};
		freshViewHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				initArrDownMap_copy1();
				LogUtil.e("initArrDownMap", "update the downView");
				if(ActContentByDefinedView.listAda!=null){
					for(int i=0;i<ActContentByDefinedView.listAda.size();i++){
						ActContentByDefinedView.listAda.get(i).updateGridDownView(get4Data());
					}
				}
				super.handleMessage(msg);
			}
		};

		waitDownHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				LogUtil.e("waitingDownloadApp", msg.what);

				switch (msg.what) {
				case 1:
					LogUtil.e("waitingDownloadApp", msg.obj);
					App app = Util.searchApp((App) msg.obj,
					MainActivity.arrDownMap_copy);
					if(app!=null){
						app.setAppName("等待下载");
						MainActivity.freshViewHandler.obtainMessage(1).sendToTarget();
					}
					break;

				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
	}

	public static int getarrDownMap() {
		// TODO Auto-generated method stub
		return arrDownMap.size();
	}
}

