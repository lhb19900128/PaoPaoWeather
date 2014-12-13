package com.paopao.weather;

import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.paopao.paopaoweather.R;
import com.paopao.weather.bean.FutureWeatherBean;
import com.paopao.weather.bean.HousWeatherBean;
import com.paopao.weather.bean.PMBean;
import com.paopao.weather.bean.TodayWeatherBean;
import com.paopao.weather.service.WeatherService;
import com.paopao.weather.service.WeatherService.OnJsonParserListener;

public class MainActivity extends Activity {

	private WeatherService weatherService;
	private WeatherService.MyBinder parserBinder;
	private SwipeRefreshLayout swipeRefreshLayout;
	private TextView cityTextView;
	private TextView releaseTimeTextView;
	private ImageView weatherNowImageView;
	private TextView weatherNowTextView;
	private TextView temperatureTextView;
	private TextView currentTmpTextView;
	private TextView airIndexTextView;
	private TextView airQualityTextView;
	private TextView nextThreeTextView;
	private TextView nextSixTextView;
	private TextView nextNineTextView;
	private TextView nextTwelveTextView;
	private TextView nextFifteenTextView;
	private ImageView nextThreeTmpImageView;
	private ImageView nextSixWeatherImageView;
	private ImageView nextNineTmpWeatherImageView;
	private ImageView nextTwelveWeatherImageView;
	private ImageView nextFiftheenTmpWeatherImageView;
	private TextView nextThreeTmpTextView;
	private TextView nextSixTmpTextView;
	private TextView nextNineTmpTextView;
	private TextView nextTwelveTmpTextView;
	private TextView nextFifteenTmpTextView;
	private TextView futureOneTextView;
	private TextView futureTwoTextView;
	private TextView futureThreeTextView;
	private ImageView futureOneWeatherImageView;
	private ImageView futureTwoWeatherImageView;
	private ImageView futureThreeWeatherImageView;
	private TextView futureOneTmpTextView;
	private TextView futureTwoTmpTextView;
	private TextView futureThreeTmpTextView;
	private TextView feltTmpTextView;
	private TextView humidityTextView;
	private TextView windDirectionTextView;
	private TextView uvTextView;
	private TextView dressingTextView;
	private RelativeLayout rl_city;
	private Context mContext;

	private ServiceConnection connection = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			parserBinder = (WeatherService.MyBinder) service;
			weatherService = parserBinder.getServiceInsetance();
			weatherService.setOnJsonParserListener(new OnJsonParserListener() {

				@Override
				public void OnParserComplete(TodayWeatherBean todayWeatherBean,
						PMBean pmBean, List<HousWeatherBean> list,
						List<FutureWeatherBean> futureList) {
					refreshUI(todayWeatherBean, pmBean, list, futureList);
					swipeRefreshLayout.setRefreshing(false);
					swipeRefreshLayout.setBackgroundColor( getResources().getColor(R.color.background));
				}
			});
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_layout);
		init();
		mContext = this;
		Intent intentService = new Intent(MainActivity.this,
				WeatherService.class);
		startService(intentService);
		bindService(intentService, connection, BIND_AUTO_CREATE);
		initSharedPreferences();
		
		swipeRefreshLayout.setColorScheme(R.color.blue, R.color.rude,
				R.color.green, R.color.light_mid);
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {
						weatherService.getWeather(String.valueOf(cityTextView.getText()));
						Log.d("debug",String.valueOf(cityTextView.getText()));
					}
				});
	}

	public void init() {
		rl_city = (RelativeLayout) findViewById(R.id.rl_city);
		rl_city.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivityForResult(
						new Intent(mContext, CityActivity.class), 1);

			}
		});

		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
		cityTextView = (TextView) findViewById(R.id.tv_city);
		releaseTimeTextView = (TextView) findViewById(R.id.tv_release_time);
		weatherNowImageView = (ImageView) findViewById(R.id.iv_current_weather);
		weatherNowTextView = (TextView) findViewById(R.id.tv_weather_now);
		temperatureTextView = (TextView) findViewById(R.id.tv_temperature);
		currentTmpTextView = (TextView) findViewById(R.id.tv_current_tmp);
		airIndexTextView = (TextView) findViewById(R.id.tv_air_index);
		airQualityTextView = (TextView) findViewById(R.id.tv_air_quality);
		nextThreeTextView = (TextView) findViewById(R.id.tv_next_three);
		nextSixTextView = (TextView) findViewById(R.id.tv_next_six);
		nextNineTextView = (TextView) findViewById(R.id.tv_next_nine);
		nextTwelveTextView = (TextView) findViewById(R.id.tv_next_twelve);
		nextFifteenTextView = (TextView) findViewById(R.id.tv_next_fifteen);
		nextThreeTmpImageView = (ImageView) findViewById(R.id.iv_next_three);
		nextSixWeatherImageView = (ImageView) findViewById(R.id.iv_next_six);
		nextNineTmpWeatherImageView = (ImageView) findViewById(R.id.iv_next_nine);
		nextTwelveWeatherImageView = (ImageView) findViewById(R.id.iv_next_twelve);
		nextFiftheenTmpWeatherImageView = (ImageView) findViewById(R.id.iv_next_fifteen);
		nextThreeTmpTextView = (TextView) findViewById(R.id.tv_next_three_tmp);
		nextSixTmpTextView = (TextView) findViewById(R.id.tv_next_six_tmp);
		nextNineTmpTextView = (TextView) findViewById(R.id.tv_next_nine_tmp);
		nextTwelveTmpTextView = (TextView) findViewById(R.id.tv_next_twelve_tmp);
		nextFifteenTmpTextView = (TextView) findViewById(R.id.tv_next_fifteen_tmp);
		futureOneTextView = (TextView) findViewById(R.id.tv_future_one);
		futureTwoTextView = (TextView) findViewById(R.id.tv_future_two);
		futureThreeTextView = (TextView) findViewById(R.id.tv_future_three);
		futureOneWeatherImageView = (ImageView) findViewById(R.id.iv_future_one_weather);
		futureTwoWeatherImageView = (ImageView) findViewById(R.id.iv_future_two_weather);
		futureThreeWeatherImageView = (ImageView) findViewById(R.id.iv_future_three_weather);
		futureOneTmpTextView = (TextView) findViewById(R.id.tv_future_one_temperature);
		futureTwoTmpTextView = (TextView) findViewById(R.id.tv_future_two_temperature);
		futureThreeTmpTextView = (TextView) findViewById(R.id.tv_future_three_temperature);
		feltTmpTextView = (TextView) findViewById(R.id.tv_felt_temp);
		humidityTextView = (TextView) findViewById(R.id.tv_humidity);
		windDirectionTextView = (TextView) findViewById(R.id.tv_wind_direction);
		uvTextView = (TextView) findViewById(R.id.tv_uv_index);
		dressingTextView = (TextView) findViewById(R.id.tv_dressing_index);
	}

	void refreshUI(TodayWeatherBean todayWeatherBean, PMBean pmBean,
			List<HousWeatherBean> list, List<FutureWeatherBean> futureList) {

		if (todayWeatherBean.isResult() == true) {
			cityTextView.setText(todayWeatherBean.getCity());
			releaseTimeTextView.setText(todayWeatherBean.getTime());
			weatherNowImageView.setImageResource(getResources().getIdentifier(
					"d" + todayWeatherBean.getWeather_id_fa(), "drawable", "com.paopao.paopaoweather"));
			weatherNowTextView.setText(todayWeatherBean.getWeather());
			temperatureTextView.setText(todayWeatherBean.getTemperature());
			currentTmpTextView.setText(todayWeatherBean.getTemp());
			// feltTmpTextView.setText(weatherBean.getFeltTmpString());
			humidityTextView.setText(todayWeatherBean.getHumidity());
			windDirectionTextView.setText(todayWeatherBean
					.getWind_direction_wind_strength());
			uvTextView.setText(todayWeatherBean.getUv_index());
			dressingTextView.setText(todayWeatherBean.getDressing_index());
		}

		if (pmBean.isResult() == true) {
			airIndexTextView.setText(pmBean.getAqi());
			airQualityTextView.setText(pmBean.getQuality());
		}

		if (list.size() == 5) {
			nextThreeTextView.setText(list.get(0).getTime()+"ʱ");
			nextSixTextView.setText(list.get(1).getTime()+"ʱ");
			nextNineTextView.setText(list.get(2).getTime()+"ʱ");
			nextTwelveTextView.setText(list.get(3).getTime()+"ʱ");
			nextFifteenTextView.setText(list.get(4).getTime()+"ʱ");
			nextThreeTmpTextView.setText(list.get(0).getTemp());
			nextSixTmpTextView.setText(list.get(1).getTemp());
			nextNineTmpTextView.setText(list.get(2).getTemp());
			nextTwelveTmpTextView.setText(list.get(3).getTemp());
			nextFifteenTmpTextView.setText(list.get(4).getTemp());
			nextThreeTmpImageView.setImageResource(getResources().getIdentifier(
					"d" + list.get(0).getWeather_id(), "drawable", "com.paopao.paopaoweather"));
			nextSixWeatherImageView.setImageResource(getResources().getIdentifier(
					"d" + list.get(1).getWeather_id(), "drawable", "com.paopao.paopaoweather"));
			nextNineTmpWeatherImageView.setImageResource(getResources().getIdentifier(
					"d" + list.get(2).getWeather_id(), "drawable", "com.paopao.paopaoweather"));
			nextTwelveWeatherImageView.setImageResource(getResources().getIdentifier(
					"d" + list.get(3).getWeather_id(), "drawable", "com.paopao.paopaoweather"));
			nextFiftheenTmpWeatherImageView.setImageResource(getResources().getIdentifier(
					"d" + list.get(4).getWeather_id(), "drawable", "com.paopao.paopaoweather"));
		}

		if (futureList.size() == 3) {
			futureOneTextView.setText(futureList.get(0).getWeek());
			futureTwoTextView.setText(futureList.get(1).getWeek());
			futureThreeTextView.setText(futureList.get(2).getWeek());
			futureOneTmpTextView.setText(futureList.get(0).getTemp());
			futureTwoTmpTextView.setText(futureList.get(1).getTemp());
			futureThreeTmpTextView.setText(futureList.get(2).getTemp());
			futureOneWeatherImageView.setImageResource(getResources().getIdentifier(
					"d" + futureList.get(0).getWeather_id(), "drawable", "com.paopao.paopaoweather"));
			futureTwoWeatherImageView.setImageResource(getResources().getIdentifier(
					"d" + futureList.get(1).getWeather_id(), "drawable", "com.paopao.paopaoweather"));
			futureThreeWeatherImageView.setImageResource(getResources().getIdentifier(
					"d" + futureList.get(2).getWeather_id(), "drawable", "com.paopao.paopaoweather"));
		}
		if((todayWeatherBean.isResult() == true) && (pmBean.isResult() == true)
				&&(list.size() == 5) && (futureList.size() == 3))
		{
			Toast.makeText(MainActivity.this, "Refreshing Successfully", Toast.LENGTH_SHORT).show();
			setSharedPreferences(todayWeatherBean, pmBean, list, futureList);
		}else {
			Toast.makeText(MainActivity.this, "Refreshing Failurely", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		if (requestCode == 1 && resultCode == 1) {
			String city = data.getStringExtra("city");
			weatherService.getWeather(city);
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		unbindService(connection);
		super.onDestroy();
	}

	public void setSharedPreferences(TodayWeatherBean todayWeatherBean,
			PMBean pmBean, List<HousWeatherBean> list,
			List<FutureWeatherBean> futureList) {
		SharedPreferences.Editor editor = getSharedPreferences("weatherData",
				MODE_PRIVATE).edit();

		editor.putString("cityTextView", String.valueOf(cityTextView.getText()));
		editor.putString("releaseTimeTextView",
				String.valueOf(releaseTimeTextView.getText()));
		editor.putInt("weatherNowImageView", weatherNowImageView.getId());
		editor.putString("weatherNowTextView",
				String.valueOf(weatherNowTextView.getText()));
		editor.putString("temperatureTextView",
				String.valueOf(temperatureTextView.getText()));
		editor.putString("currentTmpTextView",
				String.valueOf(currentTmpTextView.getText()));
		editor.putString("airIndexTextView",
				String.valueOf(airIndexTextView.getText()));
		editor.putString("airQualityTextView",
				String.valueOf(airQualityTextView.getText()));
		editor.putString("nextThreeTextView",
				String.valueOf(nextThreeTextView.getText()));
		editor.putString("nextSixTextView",
				String.valueOf(nextSixTextView.getText()));
		editor.putString("nextNineTextView",
				String.valueOf(nextNineTextView.getText()));
		editor.putString("nextTwelveTextView",
				String.valueOf(nextTwelveTextView.getText()));
		editor.putString("nextFifteenTextView",
				String.valueOf(nextFifteenTextView.getText()));
		editor.putInt("nextThreeTmpImageView", nextThreeTmpImageView.getId());
		editor.putInt("nextSixWeatherImageView",
				nextSixWeatherImageView.getId());
		editor.putInt("nextNineTmpWeatherImageView",
				nextNineTmpWeatherImageView.getId());
		editor.putInt("nextTwelveWeatherImageView",
				nextTwelveWeatherImageView.getId());
		editor.putInt("nextFiftheenTmpWeatherImageView",
				nextFiftheenTmpWeatherImageView.getId());
		editor.putString("nextThreeTmpTextView",
				String.valueOf(nextThreeTmpTextView.getText()));
		editor.putString("nextSixTmpTextView",
				String.valueOf(nextSixTmpTextView.getText()));
		editor.putString("nextNineTmpTextView",
				String.valueOf(nextNineTmpTextView.getText()));
		editor.putString("nextTwelveTmpTextView",
				String.valueOf(nextTwelveTmpTextView.getText()));
		editor.putString("nextFifteenTmpTextView",
				String.valueOf(nextFifteenTmpTextView.getText()));
		editor.putString("futureOneTextView",
				String.valueOf(futureOneTextView.getText()));
		editor.putString("futureTwoTextView",
				String.valueOf(futureTwoTextView.getText()));
		editor.putString("futureThreeTextView",
				String.valueOf(futureThreeTextView.getText()));
		editor.putInt("futureOneWeatherImageView",
				futureOneWeatherImageView.getId());
		editor.putInt("futureTwoWeatherImageView",
				futureTwoWeatherImageView.getId());
		editor.putInt("futureThreeWeatherImageView",
				futureThreeWeatherImageView.getId());
		editor.putString("futureOneTmpTextView",
				String.valueOf(futureOneTmpTextView.getText()));
		editor.putString("futureTwoTmpTextView",
				String.valueOf(futureTwoTmpTextView.getText()));
		editor.putString("futureThreeTmpTextView",
				String.valueOf(futureThreeTmpTextView.getText()));
		editor.putString("feltTmpTextView",
				String.valueOf(feltTmpTextView.getText()));
		editor.putString("humidityTextView",
				String.valueOf(humidityTextView.getText()));
		editor.putString("windDirectionTextView",
				String.valueOf(windDirectionTextView.getText()));
		editor.putString("uvTextView", String.valueOf(uvTextView.getText()));
		editor.putString("dressingTextView",
				String.valueOf(dressingTextView.getText()));
		editor.commit();
	}

	public void initSharedPreferences() {
		SharedPreferences pref = getSharedPreferences("weatherData",
				MODE_PRIVATE);
		cityTextView.setText(pref.getString("cityTextView", null));
		releaseTimeTextView.setText(pref.getString("releaseTimeTextView", null));
		weatherNowImageView.setId(pref.getInt("weatherNowImageView", 0));
		weatherNowTextView.setText(pref.getString("weatherNowTextView", null));
		temperatureTextView
				.setText(pref.getString("temperatureTextView", null));
		currentTmpTextView.setText(pref.getString("currentTmpTextView", null));
		feltTmpTextView.setText(pref.getString("feltTmpTextView", null));
		humidityTextView.setText(pref.getString("humidityTextView", null));
		windDirectionTextView.setText(pref.getString("windDirectionTextView",
				null));
		uvTextView.setText(pref.getString("uvTextView", null));
		dressingTextView.setText(pref.getString("dressingTextView", null));

		airIndexTextView.setText(pref.getString("airIndexTextView", null));
		airQualityTextView.setText(pref.getString("airQualityTextView", null));

		nextThreeTextView.setText(pref.getString("nextThreeTextView", null));
		nextSixTextView.setText(pref.getString("nextSixTextView", null));
		nextNineTextView.setText(pref.getString("nextNineTextView", null));
		nextTwelveTextView.setText(pref.getString("nextTwelveTextView", null));
		nextFifteenTextView
				.setText(pref.getString("nextFifteenTextView", null));
		nextThreeTmpImageView.setId(pref.getInt("nextThreeTmpImageView", 0));
		nextSixWeatherImageView.setId(pref.getInt("nextSixWeatherImageView", 0));
		nextNineTmpWeatherImageView.setId(pref.getInt("nextNineTmpWeatherImageView", 0));
		nextTwelveWeatherImageView.setId(pref.getInt("nextTwelveWeatherImageView", 0));
		nextFiftheenTmpWeatherImageView.setId(pref.getInt("nextFiftheenTmpWeatherImageView", 0));
		nextThreeTmpTextView.setText(pref.getString("nextThreeTmpTextView",
				null));
		nextSixTmpTextView.setText(pref.getString("nextSixTmpTextView", null));
		nextNineTmpTextView
				.setText(pref.getString("nextNineTmpTextView", null));
		nextTwelveTmpTextView.setText(pref.getString("nextTwelveTmpTextView",
				null));
		nextFifteenTmpTextView.setText(pref.getString("nextFifteenTmpTextView",
				null));

		futureOneTextView.setText(pref.getString("futureOneTextView", null));
		futureTwoTextView.setText(pref.getString("futureTwoTextView", null));
		futureThreeTextView
				.setText(pref.getString("futureThreeTextView", null));
		futureOneWeatherImageView.setId(pref.getInt("futureOneWeatherImageView", 0));
		futureTwoWeatherImageView.setId(pref.getInt("futureTwoWeatherImageView", 0));
		futureThreeWeatherImageView.setId(pref.getInt("futureThreeWeatherImageView", 0));
		futureOneTmpTextView.setText(pref.getString("futureOneTmpTextView",
				null));
		futureTwoTmpTextView.setText(pref.getString("futureTwoTmpTextView",
				null));
		futureThreeTmpTextView.setText(pref.getString("futureThreeTmpTextView",
				null));
	}
	

}
