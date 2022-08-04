/*
 * Copyright (C) 2013 Surviving with Android (http://www.survivingwithandroid.com)
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

package com.tekqube.about.weatherapp;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Francesco
 *
 */
public class WeatherHttpClient {

	private static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
	private static String IMG_URL = "http://openweathermap.org/img/w/";

	
	private static String BASE_FORECAST_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?mode=json&q=";

	public String getWeatherData(String location, String lang) {
		HttpURLConnection con = null ;
		InputStream is = null;
		
		try {
			String url = BASE_URL + location;
			if (lang != null)
				url = url + "&lang=" + lang+"&APPID=21f5d21573e46f060fbdc697fe6cea0b";
            url = "";
            url = "http://api.openweathermap.org/data/2.5/forecast?id=5750516&APPID=21f5d21573e46f060fbdc697fe6cea0b";

            con = (HttpURLConnection) ( new URL(url)).openConnection();
			con.setRequestMethod("GET");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.connect();
			
			// Let's read the response
			StringBuffer buffer = new StringBuffer();
			is = con.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while (  (line = br.readLine()) != null )
				buffer.append(line + "\r\n");
			
			is.close();
			con.disconnect();
			
			return buffer.toString();
	    }
		catch(Throwable t) {
			t.printStackTrace();
		}
		finally {
			try { is.close(); } catch(Throwable t) {}
			try { con.disconnect(); } catch(Throwable t) {}
		}

		return null;
				
	}
	
	
	public String getForecastWeatherData(String location, String lang, String sForecastDayNum) {
		HttpURLConnection con = null ;
		InputStream is = null;
		int forecastDayNum = Integer.parseInt(sForecastDayNum);
				
		try {
				
			// Forecast
			String url = BASE_FORECAST_URL + location;
			if (lang != null)
				url = url + "&lang=" + lang +"&APPID=21f5d21573e46f060fbdc697fe6cea0b";
			url = url + "&cnt=" + forecastDayNum;
			url = "";
			url = "http://api.openweathermap.org/data/2.5/forecast?id=5750516&APPID=21f5d21573e46f060fbdc697fe6cea0b";
			con = (HttpURLConnection) ( new URL(url)).openConnection();
			con.setRequestMethod("GET");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.connect();
			
			// Let's read the response
			StringBuffer buffer1 = new StringBuffer();
			is = con.getInputStream();
			BufferedReader br1 = new BufferedReader(new InputStreamReader(is));
			String line1 = null;
			while (  (line1 = br1.readLine()) != null )
				buffer1.append(line1 + "\r\n");
			
			is.close();
			con.disconnect();
			
			System.out.println("Buffer ["+buffer1.toString()+"]");
			return buffer1.toString();
	    }
		catch(Throwable t) {
			t.printStackTrace();
		}
		finally {
			try { is.close(); } catch(Throwable t) {}
			try { con.disconnect(); } catch(Throwable t) {}
		}

		return null;
				
	}
	
	public byte[] getImage(String code) {
		HttpURLConnection con = null ;
		InputStream is = null;
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(String.valueOf(new URL(IMG_URL + code + ".png")));
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			int imageLength = (int)(entity.getContentLength());
			is = entity.getContent();

			byte[] buffer = new byte[1024];
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			while ( is.read(buffer) != -1)
				baos.write(buffer);

			return baos.toByteArray();
		} catch(Throwable t) {
			t.printStackTrace();
		} finally {
			try { is.close(); } catch(Throwable t) {}
			try { con.disconnect(); } catch(Throwable t) {}
		}

		return null;
		
	}
}
