package com.paopao.weather.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class WeatherDatabaseHelper extends SQLiteOpenHelper{

	public static final String CREATE_WEATHER = "create table WeatherCity (city text primary key)";
	private Context mContext;
	
	public WeatherDatabaseHelper(Context context ,String name,CursorFactory factory,int version) {
		super(context, name, factory, version);
		mContext = context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_WEATHER);
		Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
