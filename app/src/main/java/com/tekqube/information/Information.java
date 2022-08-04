package com.tekqube.information;

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
import com.tekqube.imrc.R;

	public class Information extends Fragment 
	implements OnClickListener, android.view.View.OnClickListener 
	{
	Button btn1;
	Button btn2;

		@Override
		public View onCreateView(LayoutInflater inflater,
		ViewGroup container, Bundle savedInstanceState)
		{
		    View view = inflater.inflate(
		        R.layout.information_layout, container, false);

		    btn1 = (Button) view.findViewById(R.id.button1);
		    btn2 = (Button) view.findViewById(R.id.button2);
//		    btn3 = (Button) view.findViewById(R.id.button3);
		    
		    btn1.setOnClickListener(this);
		    btn2.setOnClickListener(this);
//		    btn3.setOnClickListener(this);

		    return view;
		}
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			switch(v.getId()){

				case R.id.button1:
					Intent i3 = new Intent (getActivity(), HotelMapConfRoomActivity.class);

					com.google.android.gms.analytics.Tracker t3 = ((IMRCApp) getActivity().getApplication()).getTracker(
							TrackerName.APP_TRACKER);
					t3.setScreenName("Information Fragment");
					t3.send(new HitBuilders.EventBuilder("Option Selected", "Nearby option Selected").build());

					startActivity(i3);
					break;
//
			case R.id.button2:
				Intent i1 = new Intent (getActivity(), HotelMaps.class);

				// Get tracker.
		        com.google.android.gms.analytics.Tracker t = ((IMRCApp) getActivity().getApplication()).getTracker(
		            TrackerName.APP_TRACKER);

		        // Set screen name.
		        // Where path is a String representing the screen name.
		        t.setScreenName("Information Fragment");

		        // Send a screen view.
		        t.send(new HitBuilders.EventBuilder("Option Selected", "hotel maps option Selected").build());
				startActivity(i1);
				break;
				
//			case R.id.button3:
//				Intent i2 = new Intent (getActivity(), com.tekqube.about.weatherapp.MainActivity.class);
//
//				com.google.android.gms.analytics.Tracker t2 = ((IMRCApp) getActivity().getApplication()).getTracker(
//		            TrackerName.APP_TRACKER);
//		        t2.setScreenName("Information Fragment");
//		        t2.send(new HitBuilders.EventBuilder("Option Selected", "Weather option Selected").build());
//
//				startActivity(i2);
//				break;
			}
		}
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
		}
		
	}
