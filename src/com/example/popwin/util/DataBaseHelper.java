package com.example.popwin.util;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper{

	public DataBaseHelper(Context context) {
        super(context, "legame_app", null, 1);
    }
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		//鍒涘缓SmsSender琛�
		String sql = "create table if not exists AAB(appId integer PRIMARY KEY autoincrement,pusherId integer not null,iconUrl varchar(500) not null,desc varchar(2000) not null , appName varchar(100) not null,packageName varchar(500) unique not null" +
				",url varchar(500) not null,fileSize integer not null ,checkStr varchar(2000) not null, status integer not null);"; //,primary key (appId)         

        db.execSQL(sql);
    }
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		
		
	}

}
