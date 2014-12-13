package com.paopao.weather.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.paopao.weather.bean.PMBean;

public class PMUtil {
	public static PMBean parserPMWeather(JSONObject jsonObject) {
		PMBean pmBean = new PMBean();
		JSONArray pmJsonObjectArray;
		try {
			if (jsonObject.getInt("resultcode") == 200
					&& jsonObject.getInt("error_code") == 0) {
				pmBean.setResult(true);
				pmJsonObjectArray = jsonObject.getJSONArray("result");
				pmBean.setAqi(pmJsonObjectArray.getJSONObject(0).getJSONObject("citynow").getString("AQI"));
				pmBean.setQuality(pmJsonObjectArray.getJSONObject(0).getJSONObject("citynow").getString("quality"));
			} else {
				pmBean.setResult(false);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pmBean;
	}
}
