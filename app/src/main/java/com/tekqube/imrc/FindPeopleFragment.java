package com.tekqube.imrc;

//import com.example.hello.R;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.analytics.HitBuilders;
import com.tekqube.application.IMRCApp;
import com.tekqube.application.IMRCApp.TrackerName;
import com.tekqube.people.CommitteeActivity;
import com.tekqube.people.attendee.MainActivity;
import com.tekqube.people.SpeakersActivity;
import com.tekqube.people.SponsorActivity;

public class FindPeopleFragment extends Fragment 
implements OnClickListener, android.view.View.OnClickListener 
{
Button btn1;
Button btn2;
Button btn3;
Button btn4;

	@Override
	public View onCreateView(LayoutInflater inflater,
	ViewGroup container, Bundle savedInstanceState)
	{
	    View view = inflater.inflate(
	        R.layout.peoplefragment, container, false);


	    btn1 = (Button) view.findViewById(R.id.button1);
//	    btn2 = (Button) view.findViewById(R.id.button2);
	    btn3 = (Button) view.findViewById(R.id.button3);
	    btn4 = (Button) view.findViewById(R.id.button4);
	    
	    btn1.setOnClickListener(this);
//	    btn2.setOnClickListener(this);
	    btn3.setOnClickListener(this);
	    btn4.setOnClickListener(this);
	   
	    // Get tracker.
        com.google.android.gms.analytics.Tracker t = ((IMRCApp) getActivity().getApplication()).getTracker(
            TrackerName.APP_TRACKER);

        // Set screen name.
        // Where path is a String representing the screen name.
        t.setScreenName("People Fragment");

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
        
	    return view;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId()){
		case R.id.button1:
			Intent i1 = new Intent (getActivity(), MainActivity.class);
			
			// Get tracker.
	        com.google.android.gms.analytics.Tracker t = ((IMRCApp) getActivity().getApplication()).getTracker(
	            TrackerName.APP_TRACKER);

	        // Set screen name.
	        // Where path is a String representing the screen name.
	        t.setScreenName("People Fragment");

	        // Send a screen view.
	        t.send(new HitBuilders.EventBuilder("Option Selected", "Attendees option Selected").build());
			
			startActivity(i1);
			break;
			
//		case R.id.button2:
//			Intent i2 = new Intent (getActivity(), SpeakersActivity.class);
//			// Get tracker.
//	        com.google.android.gms.analytics.Tracker t1 = ((IMRCApp) getActivity().getApplication()).getTracker(
//	            TrackerName.APP_TRACKER);
//
//	        // Set screen name.
//	        // Where path is a String representing the screen name.
//	        t1.setScreenName("People Fragment");
//
//	        // Send a screen view.
//	        t1.send(new HitBuilders.EventBuilder("Option Selected", "Speakers option Selected").build());
//
//			startActivity(i2);
//			break;
			
		case R.id.button3:
			Intent i3 = new Intent (getActivity(), CommitteeActivity.class);
			
			// Get tracker.
	        com.google.android.gms.analytics.Tracker t2 = ((IMRCApp) getActivity().getApplication()).getTracker(
	            TrackerName.APP_TRACKER);

	        // Set screen name.
	        // Where path is a String representing the screen name.
	        t2.setScreenName("People Fragment");

	        // Send a screen view.
	        t2.send(new HitBuilders.EventBuilder("Option Selected", "Committee option Selected").build());
	        
			startActivity(i3);
			break;
			
		case R.id.button4:
			Intent i4 = new Intent (getActivity(), SponsorActivity.class);
			
			// Get tracker.
	        com.google.android.gms.analytics.Tracker t3 = ((IMRCApp) getActivity().getApplication()).getTracker(
	            TrackerName.APP_TRACKER);

	        // Set screen name.
	        // Where path is a String representing the screen name.
	        t3.setScreenName("People Fragment");

	        // Send a screen view.
	        t3.send(new HitBuilders.EventBuilder("Option Selected", "Sponsors option Selected").build());
	        
			startActivity(i4);
			break;
		}
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
	}
	
}