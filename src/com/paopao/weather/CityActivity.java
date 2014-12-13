package com.paopao.weather;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.paopao.paopaoweather.R;
import com.paopao.weather.adapter.CityListAdapter;
import com.paopao.weather.database.WeatherDatabaseHelper;
import com.thinkland.juheapi.common.JsonCallBack;
import com.thinkland.juheapi.data.weather.WeatherData;

public class CityActivity extends Activity {

	private ListView lv_city;
	private List<String> list;
	private Context mContext;
	private WeatherDatabaseHelper dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city);
		mContext = this;
		initViews();
		createWeatherCityDatabase();
		getCities();

	}

	private void initViews() {
		findViewById(R.id.iv_back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		lv_city = (ListView) findViewById(R.id.lv_city);
	}

	private void getCities() {
		list = new ArrayList<String>();
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		// 查询Book表中所有的数据
		Cursor cursor = db.query("WeatherCity", null, null, null, null, null,
				null);
		if (cursor.moveToFirst()) {
			do {
				// 遍历Cursor对象，取出数据并打印
				String city = cursor.getString(cursor.getColumnIndex("city"));
				list.add(city);
				} while (cursor.moveToNext());
			
			Toast.makeText(CityActivity.this,
					"Downloading Successfully", Toast.LENGTH_SHORT)
					.show();
		}else{
			WeatherData data = WeatherData.getInstance();
			data.getCities(new JsonCallBack() {
				@Override
				public void jsonLoaded(JSONObject json) {
					try {
						int code = json.getInt("resultcode");
						int error_code = json.getInt("error_code");
						Log.d("debug", json.toString());
						if (error_code == 0 && code == 200) {
							JSONArray resultArray = json.getJSONArray("result");
							Set<String> citySet = new HashSet<String>();
							for (int i = 0; i < resultArray.length(); i++) {
								String city = resultArray.getJSONObject(i)
										.getString("city");
								citySet.add(city);
							}
							list.addAll(citySet);
							writeWeatherDatabase(dbHelper, list);
							Toast.makeText(CityActivity.this,
									"Refreshing Successfully", Toast.LENGTH_SHORT)
									.show();
						} else {
							Toast.makeText(CityActivity.this,
									"Refreshing Failurely", Toast.LENGTH_SHORT)
									.show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		}

		CityListAdapter adatper = new CityListAdapter(
				CityActivity.this, list);
		lv_city.setAdapter(adatper);
		lv_city.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0,
					View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("city", list.get(arg2));
				setResult(1, intent);
				finish();
			}
		});
	}

	public void createWeatherCityDatabase() {
		dbHelper = new WeatherDatabaseHelper(mContext, "city.db", null, 1);
	}

	public void writeWeatherDatabase(WeatherDatabaseHelper dbHelper,
			List<String> list) {
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		for (int i = 0; i < list.size(); ++i) {
			values.put("city", list.get(i));
			database.insert("WeatherCity", null, values);
			values.clear();
		}
	}
}
