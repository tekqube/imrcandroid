package com.tekqube.imrc;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.analytics.HitBuilders;
import com.tekqube.application.IMRCApp;
import com.tekqube.application.IMRCApp.TrackerName;
import com.tekqube.schedule.FragmentTab1;
import com.tekqube.schedule.FragmentTab2;
import com.tekqube.schedule.FragmentTab3;
import com.tekqube.schedule.FragmentTab4;

public class ScheduleFragment extends Fragment{
	
	private Activity mActivity;
	ActionBar actionBar;

	public ScheduleFragment(){}
	
    @SuppressLint("NewApi")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
    	actionBar = mActivity.getActionBar();
		
		 // Get tracker.
       com.google.android.gms.analytics.Tracker t = ((IMRCApp) mActivity.getApplication()).getTracker(
           TrackerName.APP_TRACKER);
       t.setScreenName("Schedule Fragment");
       t.send(new HitBuilders.AppViewBuilder().build());
		
		if (actionBar.getTabCount() != 4) {
			// Set up the action bar.
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			
			// For each of the sections in the app, add a tab to the action bar.
			Tab friTab = actionBar.newTab();
			RelativeLayout view = (RelativeLayout) mActivity.getLayoutInflater().inflate(R.layout.frilabel, null);
			friTab.setCustomView(view);
			actionBar.addTab(friTab.setTabListener(new ScheduleTabListener<FragmentTab1>(mActivity, "simple", FragmentTab1.class)));
                    
			Tab satTab = actionBar.newTab();
			RelativeLayout satView = (RelativeLayout) mActivity.getLayoutInflater().inflate(R.layout.satlabel, null);
			satTab.setCustomView(satView);
			actionBar.addTab(satTab.setTabListener(new ScheduleTabListener<FragmentTab2>(mActivity, "fragment2", FragmentTab2.class)));
						
			Tab sunTab = actionBar.newTab();
			RelativeLayout sunView = (RelativeLayout) mActivity.getLayoutInflater().inflate(R.layout.sunlabel, null);
			sunTab.setCustomView(sunView);
			actionBar.addTab(sunTab.setTabListener(new ScheduleTabListener<FragmentTab3>(mActivity, "fragment3", FragmentTab3.class)));
			
			Tab monTab = actionBar.newTab();
			RelativeLayout monView = (RelativeLayout) mActivity.getLayoutInflater().inflate(R.layout.monlabel, null);
			monTab.setCustomView(monView);
			actionBar.addTab(monTab.setTabListener(new ScheduleTabListener<FragmentTab4>(mActivity, "fragment4", FragmentTab4.class)));
		} else {
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			actionBar.show();			
		}
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		View rootView = inflater.inflate(R.layout.schedulefragment, container, false);

		return rootView;
	}
	
	public void onResume () {
		super.onResume();

	   actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	}
	
	public void onPause() {
	    super.onPause();

	   actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
	}	
}
