/*
 * Copyright (C) 2011 The Android Open Source Project
 * Copyright (C) 2014 Zhenghong Wang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tekqube.information;

import zh.wang.android.apis.yweathergetter4a.WeatherInfo;
import zh.wang.android.apis.yweathergetter4a.WeatherInfo.ForecastInfo;
import zh.wang.android.apis.yweathergetter4a.YahooWeather;
import zh.wang.android.apis.yweathergetter4a.YahooWeather.SEARCH_MODE;
import zh.wang.android.apis.yweathergetter4a.YahooWeatherExceptionListener;
import zh.wang.android.apis.yweathergetter4a.YahooWeatherInfoListener;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tekqube.imrc.R;

public class WeatherMainActivity extends Activity implements YahooWeatherInfoListener,
    YahooWeatherExceptionListener {
	
	private ImageView mIvWeather0;
	private TextView mTvWeather0;
	private TextView mTvErrorMessage;
	private TextView mTvTitle;
	private LinearLayout mWeatherInfosLayout;

	private YahooWeather mYahooWeather = YahooWeather.getInstance(5000, 5000, true);

    private ProgressDialog mProgressDialog;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_activity_layout);
        
        mYahooWeather.setExceptionListener(this);
        
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();
        
    	mTvTitle = (TextView) findViewById(R.id.textview_title);
		mTvWeather0 = (TextView) findViewById(R.id.textview_weather_info_0);
		mTvErrorMessage = (TextView) findViewById(R.id.textview_error_message);
		mIvWeather0 = (ImageView) findViewById(R.id.imageview_weather_info_0);
                
        mWeatherInfosLayout = (LinearLayout) findViewById(R.id.weather_infos);

        searchByPlaceName("95054");
    }
    
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		hideProgressDialog();
		mProgressDialog = null;
		super.onDestroy();
	}

	@Override
	public void gotWeatherInfo(WeatherInfo weatherInfo) {
		// TODO Auto-generated method stub
		hideProgressDialog();
        if (weatherInfo != null) {
        	setNormalLayout();
        	mWeatherInfosLayout.removeAllViews();
        	String weather = weatherInfo.getTitle().replace("Yahoo! Weather -", "");
			mTvTitle.setText(
					weather + ","
					/*+ weatherInfo.getWOEIDneighborhood() + ", "
					+ weatherInfo.getWOEIDCounty() + ", "
					+ weatherInfo.getWOEIDState() + ", " */
					+ weatherInfo.getWOEIDCountry());
			mTvWeather0.setText("====== CURRENT Conditions: ======" + "\n" +
					           "Date: " + weatherInfo.getCurrentConditionDate() + "\n" +
							   "Weather: " + weatherInfo.getCurrentText() + "\n" +
						       "Current Temperature in ºC/ºF: " + weatherInfo.getCurrentTempC() + "/" + + weatherInfo.getCurrentTempF() + "\n" 
					           /*+ "Current Temperature in F: " + weatherInfo.getCurrentTempF() + "\n"*/ 
						       /*+ "Wind chill in ÂºF: " + weatherInfo.getWindChill() + "\n" +
					           "Wind direction: " + weatherInfo.getWindDirection() + "\n" +
						       "Wind speed: " + weatherInfo.getWindSpeed() + "\n" +
					           "Humidity: " + weatherInfo.getAtmosphereHumidity() + "\n" +
						       "Pressure: " + weatherInfo.getAtmospherePressure() + "\n" +
					           "Visibility: " + weatherInfo.getAtmosphereVisibility()*/
					           );
			if (weatherInfo.getCurrentConditionIcon() != null) {
				mIvWeather0.setImageBitmap(weatherInfo.getCurrentConditionIcon());
			}
			
			for (int i = 0; i < YahooWeather.FORECAST_INFO_MAX_SIZE; i++) {
				final LinearLayout forecastInfoLayout = (LinearLayout) 
						getLayoutInflater().inflate(R.layout.forecastinfo, null);
				final TextView tvWeather = (TextView) forecastInfoLayout.findViewById(R.id.textview_forecast_info);
				final ForecastInfo forecastInfo = weatherInfo.getForecastInfoList().get(i);
				tvWeather.setText("====== FORECAST " + (i+1) + " ======" + "\n" +
				                   "Date: " + forecastInfo.getForecastDay() + " , "+ forecastInfo.getForecastDate() + "\n" +
				                   "Weather: " + forecastInfo.getForecastText() + "\n" +
						           "Low  temperature in ºC/ºF : " + forecastInfo.getForecastTempLowC() + "/" +forecastInfo.getForecastTempLowF()+ "\n" +
				                   "High temperature in ºC/ºF : " + forecastInfo.getForecastTempHighC() + "/" +forecastInfo.getForecastTempHighF() + "\n" 
						           /* + "low  temperature in ÂºF: " + forecastInfo.getForecastTempLowF() + "\n" +
				                   "high temperature in ÂºF: " + forecastInfo.getForecastTempHighF() + "\n"*/
						           );
				final ImageView ivForecast = (ImageView) forecastInfoLayout.findViewById(R.id.imageview_forecast_info);
				if (forecastInfo.getForecastConditionIcon() != null) {
					ivForecast.setImageBitmap(forecastInfo.getForecastConditionIcon());
				}
				mWeatherInfosLayout.addView(forecastInfoLayout);
			}
        } else {
        	setNoResultLayout();
        }
	}

    @Override
    public void onFailConnection(final Exception e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onFailParsing(final Exception e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onFailFindLocation(final Exception e) {
        // TODO Auto-generated method stub
        
    }

	private void setNormalLayout() {
		mWeatherInfosLayout.setVisibility(View.VISIBLE);
		mTvTitle.setVisibility(View.VISIBLE);
		mTvErrorMessage.setVisibility(View.INVISIBLE);
	}
	
	private void setNoResultLayout() {
		mTvTitle.setVisibility(View.INVISIBLE);
		mWeatherInfosLayout.setVisibility(View.INVISIBLE);
		mTvErrorMessage.setVisibility(View.VISIBLE);
		mTvErrorMessage.setText("Sorry, no result returned");
	    mProgressDialog.cancel();
	}
	
	private void searchByPlaceName(String location) {
		mYahooWeather.setNeedDownloadIcons(true);
		mYahooWeather.setSearchMode(SEARCH_MODE.PLACE_NAME);
		mYahooWeather.queryYahooWeatherByPlaceName(getApplicationContext(), location, WeatherMainActivity.this);
	}
	
	private void showProgressDialog() {
      	if (mProgressDialog != null && mProgressDialog.isShowing()) {
      		mProgressDialog.cancel();
      	}
        mProgressDialog = new ProgressDialog(WeatherMainActivity.this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();
	}
	
	private void hideProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.cancel();
		}
	}
}
