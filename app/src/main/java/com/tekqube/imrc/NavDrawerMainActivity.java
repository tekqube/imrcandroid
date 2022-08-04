package com.tekqube.imrc;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.tekqube.application.IMRCApp;
import com.tekqube.application.IMRCApp.TrackerName;
import com.tekqube.food.FoodFragment;
import com.tekqube.gallery.Gallery;
import com.tekqube.imrc.mySchedule.MyScheduleFragment;
import com.tekqube.presentations.Presentations;
import com.tekqube.videos.Videos;
import com.tekqube.information.Information;

public class NavDrawerMainActivity extends Activity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
    // nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
    Fragment fragment = null;
    Fragment oldFragment = null;

	@Override
	@SuppressWarnings("ResourceType")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_p);


		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
		// Schedule
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
		// My Schedule
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));

		// People
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
		// Venue
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));

        // Food
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));

		// Social
		//navDrawerItems.add(new NavDrawerItem(navMenuTitles[8], navMenuIcons.getResourceId(8, -1)));

		navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));

//		navDrawerItems.add(new NavDrawerItem(navMenuTitles[10], navMenuIcons.getResourceId(10, -1)));


		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#a00000")));

		bar.setDisplayHomeAsUpEnabled(true);

		//getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, //nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility
		) {
			public void onDrawerClosed(View view) {
				ActionBar bar = getActionBar();
				bar.setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				ActionBar bar = getActionBar();
				bar.setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}
		
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

    private void deleteFragment(String fragmentTag) {
        Fragment deleteFragment = getFragmentManager().findFragmentByTag(fragmentTag);
        if (deleteFragment != null) {
            getFragmentManager().beginTransaction().detach(deleteFragment).commit();
        }
    }

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
        // update the main content by replacing fragments
        oldFragment = fragment;

        switch (position) {
			case 0:
				fragment = new HomeFragment();

				com.google.android.gms.analytics.Tracker t = ((IMRCApp) getApplication()).getTracker(
						TrackerName.APP_TRACKER);
				t.setScreenName("Side Bar Activity");
				t.send(new HitBuilders.EventBuilder("Option Selected", "Home option Selected").build());

				break;
			case 1:
				fragment = new ScheduleFragment();
				com.google.android.gms.analytics.Tracker t1 = ((IMRCApp) getApplication()).getTracker(
						TrackerName.APP_TRACKER);
				t1.setScreenName("Side Bar Activity");
				t1.send(new HitBuilders.EventBuilder("Option Selected", "Schedule option Selected").build());

				break;
			case 2:
				fragment = new MyScheduleFragment();
				com.google.android.gms.analytics.Tracker t11 = ((IMRCApp) getApplication()).getTracker(
						TrackerName.APP_TRACKER);
				t11.setScreenName("Side Bar Activity");
				t11.send(new HitBuilders.EventBuilder("Option Selected", "My Schedule option Selected").build());

				break;
			case 3:
				fragment = new FindPeopleFragment();
				com.google.android.gms.analytics.Tracker t2 = ((IMRCApp) getApplication()).getTracker(
						TrackerName.APP_TRACKER);
				t2.setScreenName("Side Bar Activity");
				t2.send(new HitBuilders.EventBuilder("Option Selected", "People option Selected").build());

				break;
			case 4:
				fragment = new Information();
				com.google.android.gms.analytics.Tracker t3 = ((IMRCApp) getApplication()).getTracker(
						TrackerName.APP_TRACKER);
				t3.setScreenName("Side Bar Activity");
				t3.send(new HitBuilders.EventBuilder("Option Selected", "Information option Selected").build());

				break;

            case 5:
                fragment = new FoodFragment();
                com.google.android.gms.analytics.Tracker t4 = ((IMRCApp) getApplication()).getTracker(
                        TrackerName.APP_TRACKER);
                t4.setScreenName("Side Bar Activity");
                t4.send(new HitBuilders.EventBuilder("Option Selected", "Food Fragment option Selected").build());

                break;

			case 7:
				fragment = new Presentations();
				com.google.android.gms.analytics.Tracker t5 = ((IMRCApp) getApplication()).getTracker(
						TrackerName.APP_TRACKER);
				t5.setScreenName("Side Bar Activity");
				t5.send(new HitBuilders.EventBuilder("Option Selected", "Presentations option Selected").build());

				break;
			case 8:
				fragment = new Gallery();
				com.google.android.gms.analytics.Tracker t6 = ((IMRCApp) getApplication()).getTracker(
						TrackerName.APP_TRACKER);
				t6.setScreenName("Side Bar Activity");
				t6.send(new HitBuilders.EventBuilder("Option Selected", "Gallery option Selected").build());

				break;
			case 9:
				fragment = new Videos();
				com.google.android.gms.analytics.Tracker t7 = ((IMRCApp) getApplication()).getTracker(
						TrackerName.APP_TRACKER);
				t7.setScreenName("Side Bar Activity");
				t7.send(new HitBuilders.EventBuilder("Option Selected", "Information option Selected").build());

				break;
			case 6:
				fragment = new AboutFragment();
				com.google.android.gms.analytics.Tracker t9 = ((IMRCApp) getApplication()).getTracker(
						TrackerName.APP_TRACKER);
				t9.setScreenName("Side Bar Activity");
				t9.send(new HitBuilders.EventBuilder("Option Selected", "About option Selected").build());

				break;
//			case 1:
//				fragment = new VoteFragment();
//				com.google.android.gms.analytics.Tracker t10 = ((IMRCApp) getApplication()).getTracker(
//						TrackerName.APP_TRACKER);
//				t10.setScreenName("Side Bar Activity");
//				t10.send(new HitBuilders.EventBuilder("Option Selected", "Vote option Selected").build());
//
//				break;
			default:
				break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction ft = fragmentManager.beginTransaction();
            this.onDestroy();
            if (oldFragment != null) {
                ft.remove(oldFragment);
            }
            ft.replace(R.id.frame_container, fragment);
            ft.addToBackStack(null);
            ft.commit();
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
		
		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		mDrawerList.setSelection(position);
		setTitle(navMenuTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		ActionBar bar = getActionBar();
		bar.setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	/*@Override
    public void onBackPressed() {
        // Write your code here
 
        super.onBackPressed();
    }*/
	
	/*@Override
	public void onBackPressed() {
	  if (this.lastBackPressTime < System.currentTimeMillis() - 4000) {
	    toast = Toast.makeText(this, "Are you sure you want to exit?", 4000);
	    toast.show();
	    this.lastBackPressTime = System.currentTimeMillis();
	  } else {
	    if (toast != null) {
	    toast.cancel();
	  }
	  super.onBackPressed();
	 }
	}*/
	

	
	/*
	@Override
	public void onBackPressed() {
	    new AlertDialog.Builder(this)
	           .setMessage("Are you sure you want to exit?")
	           .setCancelable(false)
	           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	            	   NavDrawerMainActivity.this.finish();
	               }
	           })
	           .setNegativeButton("No", null)
	           .show();
		}*/
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    //Handle the back button
	    if(keyCode == KeyEvent.KEYCODE_BACK) {
	        //Ask the user if they want to quit
	        new AlertDialog.Builder(this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle(R.string.quit)
	        .setMessage(R.string.really_quit)
	        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

	            @Override
	            public void onClick(DialogInterface dialog, int which) {

	                //Stop the activity
	            	NavDrawerMainActivity.this.finish();    
	            }

	        })
	        .setNegativeButton(R.string.no, null)
	        .show();

	        return true;
	    }
	    else {
	        return super.onKeyDown(keyCode, event);
	    }

	}
}
