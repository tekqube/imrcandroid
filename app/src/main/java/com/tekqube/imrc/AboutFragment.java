package com.tekqube.imrc;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.analytics.HitBuilders;
import com.tekqube.application.IMRCApp;
import com.tekqube.application.IMRCApp.TrackerName;

public class AboutFragment  extends Fragment{

	public AboutFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.social_layout, container, false);
         
        // Get tracker.
        com.google.android.gms.analytics.Tracker t = ((IMRCApp) getActivity().getApplication()).getTracker(
            TrackerName.APP_TRACKER);

        // Set screen name.
        // Where path is a String representing the screen name.
        t.setScreenName("About Fragment");

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());

        Button appFeedback = (Button) rootView.findViewById(R.id.button1);
        appFeedback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				final Intent urlIntent = new Intent( Intent.ACTION_VIEW);
				String url = "https://www.facebook.com/imrcsanfrancisco";
				urlIntent.setData(Uri.parse(url));
				startActivity(urlIntent);
			}
		});
        
        Button tellYourFriend= (Button) rootView.findViewById(R.id.button2);
        tellYourFriend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final Intent urlIntent = new Intent( Intent.ACTION_VIEW);
				String url = "https://imrc.mmna.org";
				urlIntent.setData(Uri.parse(url));
				startActivity(urlIntent);
			}
		});

        return rootView;
    }
}
