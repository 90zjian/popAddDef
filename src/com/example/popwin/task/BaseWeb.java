package com.example.popwin.task;

import com.example.popwin.util.LogUtil;
import com.legame.np.util.NetWorkUtils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class BaseWeb extends Base {
	private static final String TAG = "BaseWeb";

    public BaseWeb(Context context, Handler handler) {
        super(context, handler);
    }

    public boolean startRequest(int what) {
       //LogUtil.e(TAG,"startRequest");
        if (!NetWorkUtils.checkNetWork(context)
                && !NetWorkUtils.checkWifiNetWork(context)) {
            Message m = handler.obtainMessage(what);
            m.obj = "无法请求网络，请�?查当前的网络情况�?";
            handler.sendMessage(m);
            return false;
        }
        return true;
    }
}
