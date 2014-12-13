package com.paopao.weather.service;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.json.JSONObject;
import com.paopao.weather.bean.FutureWeatherBean;
import com.paopao.weather.bean.HousWeatherBean;
import com.paopao.weather.bean.PMBean;
import com.paopao.weather.bean.TodayWeatherBean;
import com.paopao.weather.util.FutureWeatherUtil;
import com.paopao.weather.util.HousWeatherUtil;
import com.paopao.weather.util.PMUtil;
import com.paopao.weather.util.TodayWeatherUtil;
import com.thinkland.juheapi.common.JsonCallBack;
import com.thinkland.juheapi.data.air.AirData;
import com.thinkland.juheapi.data.weather.WeatherData;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class WeatherService extends Service {

	private final static int REPEAT_MSG = 0x01;
	private final static int CALLBACK_OK = 0x02;
	private final static int CALLBACK_ERROR = 0x04;

	private boolean isRunning = false;
	private String city;
	private MyBinder binder = new MyBinder();
	private TodayWeatherBean todayWeatherBean;
	private PMBean pmBean;
	private List<HousWeatherBean> list;
	private List<FutureWeatherBean> futureList;
	private OnJsonParserListener onJsonParserListener;

	public interface OnJsonParserListener{
		void OnParserComplete(TodayWeatherBean todayWeatherBean,PMBean pmBean, 
				List<HousWeatherBean> list,List<FutureWeatherBean> futureList);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return binder;
	}

	@Override
	public void onCreate() {
		city = "Œ‰∫∫";
		myHandler.sendEmptyMessage(REPEAT_MSG);
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	public void setOnJsonParserListener(OnJsonParserListener onJsonOnParserListener) {
		this.onJsonParserListener = onJsonOnParserListener;
	}
	

	public void removeOnJsonParserListener() {
		onJsonParserListener = null;
	}

	@SuppressLint("HandlerLeak")
	Handler myHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REPEAT_MSG:
				getWeather();
				sendEmptyMessageDelayed(REPEAT_MSG, 30 * 60 * 1000);
				break;
			case CALLBACK_OK:
				if (onJsonParserListener != null) {
					onJsonParserListener.OnParserComplete(todayWeatherBean, pmBean, list,futureList);
				}
				isRunning = false;
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	
	public void getWeather(String city) {
		this.city = city;
		getWeather();
	}
	
	public void getWeather() {
		
		if (isRunning) {
			return;
		}
		isRunning = true;
		final CountDownLatch countDownLatch = new CountDownLatch(3);
		WeatherData weatherData = WeatherData.getInstance();
		weatherData.getByCitys(city, 2, new JsonCallBack() {
			@Override
			public void jsonLoaded(JSONObject jsonObject) {
				Log.d("debug", jsonObject.toString());
				todayWeatherBean = TodayWeatherUtil.parserTodayWeather(jsonObject);
				futureList = FutureWeatherUtil.parserFutureWeather(jsonObject);
				countDownLatch.countDown();
			}
		});
		
		weatherData.getForecast3h(city,new JsonCallBack() {
			
			@Override
			public void jsonLoaded(JSONObject jsonObject) {
				list = HousWeatherUtil.parserHousWeather(jsonObject);
				Log.d("debug", jsonObject.toString());
				countDownLatch.countDown();
			}
		});
		
		AirData airData = AirData.getInstance();
		airData.cityAir(city, new JsonCallBack() {
			
			@Override
			public void jsonLoaded(JSONObject jsonObject) {
				Log.d("debug", jsonObject.toString());
				pmBean = PMUtil.parserPMWeather(jsonObject);
				countDownLatch.countDown();
			}
		});
		
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					countDownLatch.await();
					myHandler.sendEmptyMessage(CALLBACK_OK);
				} catch (InterruptedException e) {
					myHandler.sendEmptyMessage(CALLBACK_ERROR);
					return;
				}
			}

		}.start();
		return;
	}
	
	public class MyBinder extends Binder {
		public WeatherService getServiceInsetance() {
			return WeatherService.this;
		}
	}
}
