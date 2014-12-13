package com.paopao.weather.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import com.paopao.weather.bean.HousWeatherBean;

@SuppressLint("SimpleDateFormat")
public class HousWeatherUtil {
	public static List<HousWeatherBean> parserHousWeather(JSONObject jsonObject){
		List<HousWeatherBean> list = new ArrayList<HousWeatherBean>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		Date date = new Date(System.currentTimeMillis());
			int code;
			try {
				code = jsonObject.getInt("resultcode");
				int error_code = jsonObject.getInt("error_code");
				if (error_code == 0 && code == 200) {
					JSONArray resultArray = jsonObject.getJSONArray("result");
					for (int i = 0; i < resultArray.length(); i++) {
						HousWeatherBean bean = new HousWeatherBean();
						JSONObject hourJson = resultArray.getJSONObject(i);
						Date hDate = null;
						try {
							hDate = sdf.parse(hourJson.getString("sfdate"));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (!hDate.after(date)) {
							continue;
						}
						bean.setWeather_id(hourJson.getString("weatherid"));
						bean.setTemp(hourJson.getString("temp1"));
						Calendar c = Calendar.getInstance();
						c.setTime(hDate);
						bean.setTime(c.get(Calendar.HOUR_OF_DAY) + "");
						bean.setResult(true);
						list.add(bean);
						if (list.size() == 5) {
							break;
						}
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return list;
	}
}
