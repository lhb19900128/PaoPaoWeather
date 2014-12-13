package com.paopao.weather.bean;

public class FutureWeatherBean {
	private String week;
	private String Weather_id;
	private String temp;
	private String date;
	private boolean result;
	
	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}
	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public String getWeather_id() {
		return Weather_id;
	}

	public void setWeather_id(String weather_id) {
		Weather_id = weather_id;
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
