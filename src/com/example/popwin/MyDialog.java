package com.example.popwin;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

public class MyDialog extends Dialog {

    Context context;
    View infoView;
    public MyDialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
    }
    public MyDialog(Context context, int theme){
        super(context, theme);
        this.context = context;
    }
    public MyDialog(Context context, View infoView) {
		// TODO Auto-generated constructor stub
    	super(context);
    	this.infoView=infoView;
	}
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//        this.setContentView(R.layout.infopop);
        this.setContentView(infoView);
//        this.set
        
    }

}