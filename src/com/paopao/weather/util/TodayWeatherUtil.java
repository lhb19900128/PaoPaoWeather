package com.paopao.weather.util;

import org.json.JSONException;
import org.json.JSONObject;

import com.paopao.weather.bean.TodayWeatherBean;

public class TodayWeatherUtil {
	public static TodayWeatherBean parserTodayWeather(JSONObject jsonObject){
		TodayWeatherBean todayWeatherBean = new TodayWeatherBean();
		try {
			JSONObject todayJsonObject = jsonObject.getJSONObject("result").getJSONObject("today");
			JSONObject skJsonObject = jsonObject.getJSONObject("result").getJSONObject("sk");
			if(jsonObject.getInt("resultcode") == 200 && jsonObject.getInt("error_code") == 0)
			{
				todayWeatherBean.setResult(true);
				todayWeatherBean.setCity(todayJsonObject.getString("city"));
				todayWeatherBean.setTime(skJsonObject.getString("time")+"·¢²¼");
				todayWeatherBean.setWeather(todayJsonObject.getString("weather"));
				todayWeatherBean.setTemperature(todayJsonObject.getString("temperature"));
				todayWeatherBean.setWeather_id_fa(todayJsonObject.getJSONObject("weather_id").getString("fa"));
				todayWeatherBean.setWeather_id_fb(todayJsonObject.getJSONObject("weather_id").getString("fb"));
				todayWeatherBean.setTemp(skJsonObject.getString("temp")+"¡æ");
				todayWeatherBean.setHumidity(skJsonObject.getString("humidity"));
				todayWeatherBean.setUv_index(todayJsonObject.getString("uv_index"));
				todayWeatherBean.setWind_direction_wind_strength(skJsonObject.getString("wind_direction")+skJsonObject.getString("wind_strength"));
				todayWeatherBean.setDressing_index(todayJsonObject.getString("dressing_index"));
			}else {
				todayWeatherBean.setResult(false);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return todayWeatherBean;
	}
}
