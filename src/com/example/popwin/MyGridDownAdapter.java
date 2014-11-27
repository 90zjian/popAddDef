package com.example.popwin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.example.android.bitmapfun.util.ImageFetcher;
import com.example.popwin.net.sqlite.AdHandler;
import com.example.popwin.net.sqlite.App;
import com.example.popwin.service.DownloadService;
import com.example.popwin.util.LogUtil;
import com.legame.eightplatform.R;
import com.legame.np.util.ImageUtils;
import com.legame.np.util.TaskUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MyGridDownAdapter extends BaseAdapter {

	private Context mContext;
	List<App> arrList = new ArrayList<App>();
	Activity MainAc;
	// TextView tv;
	// View cv;
	int page;
	
	public static ImageFetcher mImageFetcher;
	
	public MyGridDownAdapter() {
	}

	public MyGridDownAdapter(Context context, List<App> list, Activity ac,
			int page) {
		mContext = context;
		this.page = page;
		if (list.size() > 0) {
			LogUtil.e("list.size", list.size());
			if (list.size() < 5)
				arrList.addAll(list);
			else
				arrList = list.subList(0, 4);
			LogUtil.e("arrList", arrList);
		}
		MainAc = ac;
		mImageFetcher = new ImageFetcher(context, 480);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arrList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public void updateGridDownView(List<App> list) {
		arrList.clear();
		if (list.size() < 5)
			arrList.addAll(list);
		else
			arrList = list.subList(0, 4);
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		HolderDown holder;
		// Boolean clickable=true;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.popwininfo, null);
			holder = new HolderDown();
			holder.image = (ImageView) convertView.findViewById(R.id.ImageV);
			holder.name = (TextView) convertView.findViewById(R.id.textV2);
			// tv = (TextView) convertView.findViewById(R.id.textV2);
			// cv = convertView;
			convertView.setTag(holder);
		} else {
			holder = (HolderDown) convertView.getTag();
		}
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String link = (String) arrList.get(position).getUrl();
				System.out.println("click the GridDownButton");
				if (arrList.get(position).getStatus() != TaskUtil.STATUS_DOWNLOADING) {
					showDialog(arrList.get(position));
				}
			}

			@SuppressLint("NewApi") private void showDialog(final App app) {
				View infoView = LayoutInflater.from(mContext).inflate(
						R.layout.infopop, null);
				ImageView imageView = (ImageView) infoView
						.findViewById(R.id.imageView);
				TextView nameText = (TextView) infoView
						.findViewById(R.id.nameView);
				TextView infoText = (TextView) infoView
						.findViewById(R.id.infoView);
				TextView sizeView= (TextView)infoView.findViewById(R.id.sizeView);
				Button button = (Button) infoView.findViewById(R.id.instButton);
				Bitmap bitmap = null;
				try {
					bitmap = BitmapFactory
							.decodeStream(new FileInputStream(new File(
									ImageUtils.getLocalFileName(
											MainActivity.myActivity,
											app.getIconUrl()))));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				imageView.setImageBitmap(bitmap);
				nameText.setText(app.getAppName());
				infoText.setText(app.getDesc());
				sizeView.setText((double) ((app.getFileSize()*100)/1024/1024/100.0)+"M");

				final MyDialog md = new MyDialog(mContext, infoView);
				md.requestWindowFeature(Window.FEATURE_NO_TITLE);

				button.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						arrList.get(position).setStatus(
								TaskUtil.STATUS_DOWNLOADING);

						Intent intent = new Intent(MainActivity.myActivity,
								DownloadService.class);
						intent.putExtra(App.APPID, arrList.get(position)
								.getAppId());
						new AdHandler(mContext).putOneToDownload(arrList
								.get(position));
						MainActivity.myActivity.startService(intent);
						md.dismiss();
						Message msg = new Message();
						msg.what = 1;
						msg.obj = app;
						MainActivity.waitDownHandler.sendMessage(msg);
					}
				});
				md.show();
			}

		});
		holder.name.setText((CharSequence) arrList.get(position).getAppName());
		App app = arrList.get(position);
//		Bitmap bitmap = null;
//		if (!new File(ImageUtils.getLocalFileName(MainActivity.myActivity,
//				app.getIconUrl())).exists()) {
//			bitmap = BitmapFactory.decodeResource(
//					MainActivity.myActivity.getResources(), R.drawable.loading);
//		} else
//			try {
//				bitmap = BitmapFactory.decodeStream(new FileInputStream(
//						new File(ImageUtils.getLocalFileName(
//								MainActivity.myActivity, app.getIconUrl()))));
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		holder.image.setImageBitmap(bitmap);
		mImageFetcher.loadImage(app.getIconUrl().toString(), holder.image);
		return convertView;
	}

	public class HolderDown {
		ImageView image;
		TextView name;
		Intent intent;
	}

	class netRun implements Runnable {

		public String fileName = "default";
		public String Link = "http://apk.r1.market.hiapk.com/data/upload/2014/09_29/13/com.iflytek.inputmethod_135036.apk";

		public netRun(String link, String itemName) {
			if (link != null)
				fileName = itemName;
			if (itemName != null)
				link = Link;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			System.out.println();
			// String image=NetUtil.getApkmapFromServer(Link,fileName);
			Bitmap apk = NetUtil.getBitmapFromServer(Link);
		}
	}
}
