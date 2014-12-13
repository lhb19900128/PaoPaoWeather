package com.paopao.weather.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import com.paopao.weather.bean.FutureWeatherBean;

@SuppressLint("SimpleDateFormat")
public class FutureWeatherUtil {
	public static List<FutureWeatherBean> parserFutureWeather(JSONObject jsonObject){
		List<FutureWeatherBean> futureList = new ArrayList<FutureWeatherBean>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			JSONArray futureArray = jsonObject.getJSONObject("result").getJSONArray("future");
			Date date = new Date(System.currentTimeMillis());
			for (int i = 0; i < futureArray.length(); i++) {
				JSONObject futureJson = futureArray.getJSONObject(i);
				FutureWeatherBean futureBean = new FutureWeatherBean();
				Date datef = null;
				try {
					datef = sdf.parse(futureJson.getString("date"));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (!datef.after(date)) {
					continue;
				}
				futureBean.setTemp(futureJson.getString("temperature"));
				futureBean.setWeek(futureJson.getString("week"));
				futureBean.setWeather_id(futureJson.getJSONObject("weather_id").getString("fa"));
				futureList.add(futureBean);
				if (futureList.size() == 3) {
					break;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return futureList;
	}
}
