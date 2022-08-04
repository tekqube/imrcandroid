package com.tekqube.people;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.tekqube.application.IMRCApp;
import com.tekqube.application.IMRCApp.TrackerName;
import com.tekqube.imrc.R;
import com.tekqube.loadwebimage.imageutils.ImageLoader;
import com.tekqube.people.speaker.Speaker;

public class SpeakersDetailActivity extends Activity {

	private ImageView imgView;
	private ImageLoader imgLoader;
	
	Speaker selectedSpeaker = new Speaker();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get tracker.
        com.google.android.gms.analytics.Tracker t = ((IMRCApp) getApplication()).getTracker(
            TrackerName.APP_TRACKER);

        // Set screen name.
        // Where path is a String representing the screen name.
        t.setScreenName("Speakers Detail Activity ");

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());

		setContentView(R.layout.speakersactivitydetail);
		selectedSpeaker = this.getIntent().getExtras().getParcelable("speaker");
		
		 URL url;
		try {
			url = new URL(selectedSpeaker.getImageUrl());
			 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		     connection.setDoInput(true);
		     connection.connect();
		     InputStream input = connection.getInputStream();
		     Bitmap myBitmap = BitmapFactory.decodeStream(input);
			ImageView imageView = (ImageView) findViewById(R.id.speakerPhoto);
			imageView.setImageBitmap(myBitmap);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		
//		imgView = (ImageView) findViewById(R.id.speakerPhoto);
//    	imgLoader = new ImageLoader(this);
//
    	System.out.println(modifyDropboxUrl(selectedSpeaker.getImageUrl()));
//
//		imgLoader.DisplayImage(modifyDropboxUrl(selectedSpeaker.getImageUrl()), imgView);
//
		TextView name = (TextView) findViewById(R.id.name);
		name.setText(selectedSpeaker.getName());
		
		TextView title = (TextView) findViewById(R.id.speakerTitle);
		title.setText(selectedSpeaker.getTitle());

		TextView location = (TextView) findViewById(R.id.speakerLocation);
		location.setText(selectedSpeaker.getLocation());
		
		TextView achievements = (TextView) findViewById(R.id.speakerAchievements);
		achievements.setText(selectedSpeaker.getKeyAchihevements());

		TextView community = (TextView) findViewById(R.id.communityInfo);
		community.setText(selectedSpeaker.getCommunity());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.speakers_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public static String modifyDropboxUrl(String originalUrl)
	{
	    String newUrl = originalUrl.replace("www.dropbox." ,"dl.dropboxusercontent.");

	    //just for sure for case if www is missing in url string
	    newUrl = newUrl.replace("dropbox.", "dl.dropboxusercontent.");

	    return newUrl;
	}
	
	
}


