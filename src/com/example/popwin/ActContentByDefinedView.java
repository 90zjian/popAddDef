package com.example.popwin; 
  
import java.util.ArrayList;
import java.util.List;

import com.example.popwin.net.sqlite.App;
import com.legame.eightplatform.R;

import android.widget.GridView;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
/** 
* 利用自定义的ScrollView加载视图来实现翻页效果，下面用页码和总页数标识当前的视图是第几屏
*  
* @author WANGXIAOHONG
*
*/
public class ActContentByDefinedView extends Activity {
	private static LinearLayout mLinearLayout;
	private static LinearLayout.LayoutParams param;
//	private static DefinedScrollView scrollView;
	private static LayoutInflater inflater;
//	private TextView mTextView;
//	private ImageView mImageViewMagaPic;
	public static int pageCount = 0;
 //	private static Activity ma;
	public static List<App> la;
	public static MyGridDownAdapter md;
	public static GridView gv_down;
	public static List<MyGridDownAdapter> listAda;
	public static List<List<App>> lList;
	public static ActContentByDefinedView act;
	public static void setupView(DefinedScrollView DS) {
		pageCount = (MainActivity.arrDownMap.size()+3)/4 ;
//		md=new MyGridDownAdapter[pageCount];
//		for(int i=0;i<pageCount;i++){
//			md[i]=new MyGridDownAdapter();
//		}
		listAda=new ArrayList<MyGridDownAdapter>();
		inflater = MainActivity.myActivity.getLayoutInflater();
//		View v=inflater.inflate(R.layout.activity_main, null);
//		scrollView = (DefinedScrollView) findViewById(R.id.definedview);
		System.out.println("pageCount="+pageCount);
		// delete the views showed in every page;
		DS.removeAllViews();
		for (int i = 0; i < pageCount; i++) {
			param = new LinearLayout.LayoutParams(
			android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
			android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			inflater = MainActivity.myActivity.getLayoutInflater();
		  
			if (i == 0) {
				final View addview = inflater.inflate(R.layout.popwininfo1, null);
				mLinearLayout = new LinearLayout(MainActivity.myActivity);
				gv_down= (GridView)addview.findViewById(R.id.scrollGV);
				la=MainActivity.get4Data();
//				LogUtil.e("ActContent-nullpointer", la);
		        md = new MyGridDownAdapter(MainActivity.myActivity, la,
		        												MainActivity.myActivity,i);
		        listAda.add(md);
		        gv_down.setAdapter(md);
				mLinearLayout.addView(addview, param);
				DS.addView(mLinearLayout);
				
			} else {
				View addview = inflater.inflate(R.layout.popwininfo1, null);
				mLinearLayout = new LinearLayout(MainActivity.myActivity);
				gv_down= (GridView)addview.findViewById(R.id.scrollGV);
				la=MainActivity.get4Data();
		        md = new MyGridDownAdapter(MainActivity.myActivity, la,
		        												MainActivity.myActivity,i);
		        listAda.add(md);
		        gv_down.setAdapter(md);
				mLinearLayout.addView(addview, param);
				DS.addView(mLinearLayout);
				System.out.println(i+"==i");
			}
	}	
}
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		// TODO Auto-generated method stub 
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.activity_main);
		act=this;
		////loader = new ImageLoader(this); 
//		setupView(); 
//		setrefresh();
	}
}