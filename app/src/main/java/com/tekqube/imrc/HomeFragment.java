package com.tekqube.imrc;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.google.android.gms.analytics.HitBuilders;
import com.tekqube.Constants;
import com.tekqube.application.IMRCApp;
import com.tekqube.application.IMRCApp.TrackerName;
import com.tekqube.utils.Util;

public class HomeFragment extends Fragment {
	private CustomAdapter mAdapter;
	private ListView listView;
	private Activity mActivity;
	private List<Schedule> schedules = new ArrayList<Schedule>();
	int day = 1;

	public HomeFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

		boolean enableEventsCall = false;
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mActivity = getActivity();
		String url = Constants.scheduleFileUrl;
        listView = (ListView) rootView.findViewById(R.id.listView);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			TextView textView = (TextView) rootView.findViewById(R.id.txtLabel);

			String currentDate = sdf.format(new Date());
			if (sdf.parse(currentDate).compareTo(sdf.parse("2018-06-30")) < 0) {
				//System.out.println(' Current date is before 29ths..')
				listView.setAlpha(0.2f);
				listView.setEnabled(false);
				enableEventsCall = false;
			} else {
				textView.setVisibility(View.GONE);
				enableEventsCall = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		Calendar cal = Calendar.getInstance();
		Date date = new Date();
		cal.setTime(date);
		int month = cal.get(Calendar.MONTH);
		int dt = cal.get(Calendar.DAY_OF_MONTH);
		int year = cal.get(Calendar.YEAR);

		if (dt == 1 && (month+1) == 7 && year == 2018) {
			day = 2;
		} else if (dt == 2 && (month+1) == 7 && year == 2018) {
			day = 3;
		} else if (dt == 3 && (month+1) == 7 && year == 2018) {
			day = 4;
		}

		if (enableEventsCall) {
			new ExcelURL().execute(url);

//			listView.setOnItemClickListener(new OnItemClickListener() {
//				@Override
//				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//										long arg3) {
//
//					// Detect if the clicked item is row and not a section header
//					if (mAdapter.getItemViewType(arg2) == 0) {
//						Schedule schedule = schedules.get(arg2);
//
//						Intent i = new Intent(getActivity(), ScheduleDetailActivity.class);
//						i.putExtra("schedule", schedule);
//
//						startActivity(i);
//					}
//				}
//			});
		}
        
        // Get tracker.
        com.google.android.gms.analytics.Tracker t = ((IMRCApp) getActivity().getApplication()).getTracker(
            TrackerName.APP_TRACKER);
        t.setScreenName("Home Screen");
        t.send(new HitBuilders.AppViewBuilder().build());

        return rootView;
    }

	private void showData() {
		mAdapter = new CustomAdapter(mActivity);

		List<Schedule> entertainment = new ArrayList<Schedule>();
		List<Schedule> general = new ArrayList<Schedule>();
		List<Schedule> sakhi = new ArrayList<Schedule>();
		List<Schedule> kids = new ArrayList<Schedule>();
		List<Schedule> now = new ArrayList<Schedule>();
		List<Schedule> prayer = new ArrayList<>();
		List<Schedule> fitness = new ArrayList<>();
		List<Schedule> sessions = new ArrayList<>();
		List<Schedule> rays = new ArrayList<>();

		Date currentDateTime = new Date();
		float hour = currentDateTime.getHours();
		float seconds = currentDateTime.getMinutes();
		float currentTime = hour + (seconds/100);

		for (int i =0; i < schedules.size(); i++) {
			System.out.println(" Start Time :"+schedules.get(i).getStart24Time());
			System.out.println(" End Time :"+schedules.get(i).getEnd24Time());
			Schedule schedule = schedules.get(i);
			String targetGroup = "";
			if (schedule.getTargetGroup() != null) {
				targetGroup = schedule.getTargetGroup().toLowerCase();
			}

			float startTime = 0.0f;
			if (schedules.get(i).getStart24Time() != null) {
				startTime = Float.parseFloat(schedules.get(i).getStart24Time());
			}

			float endTime = 0.0f;
			if (schedules.get(i).getEnd24Time() != null) {
				endTime =  Float.parseFloat(schedules.get(i).getEnd24Time() );

				if ((startTime >= 23.0) && endTime == 0.0) {
					endTime = 23.59f;
				}
			}

			if (targetGroup.contains("entertainment") && (currentTime <= endTime)) {
				entertainment.add(schedule);
			}

			if (targetGroup.contains("general") && (currentTime <= endTime)) {
				general.add(schedule);
			}

			if (targetGroup.contains("sakhi") && (currentTime <= endTime)) {
				sakhi.add(schedule);
			}

			if (targetGroup.contains("kids") && (currentTime <= endTime)) {
				kids.add(schedule);
			}

			if (targetGroup.contains("prayer") && (currentTime <= endTime)) {
				prayer.add(schedule);
			}

			if (targetGroup.contains("fitness") && (currentTime <= endTime)) {
				fitness.add(schedule);
			}

			if (targetGroup.contains("session") && (currentTime <= endTime)) {
				sessions.add(schedule);
			}

			if (targetGroup.toLowerCase().contains("rays") && (currentTime <= endTime)) {
				rays.add(schedule);
			}

			if ((currentTime >= startTime) && (currentTime <= endTime)) {
				now.add(schedule);
			}
		}

		if (now.size() > 0) {
			mAdapter.addSectionHeaderItem(new Schedule("What's Happening Now"));
			mAdapter.addItems(now);
		}

        if (rays.size() > 0) {
            mAdapter.addSectionHeaderItem(new Schedule("Rays"));
            mAdapter.addItems(rays);
        }

		if (entertainment.size() > 0) {
			mAdapter.addSectionHeaderItem(new Schedule("Entertainment"));
			mAdapter.addItems(entertainment);
		}

		if (general.size() > 0) {
			mAdapter.addSectionHeaderItem(new Schedule("General"));
			mAdapter.addItems(general);
		}

		if (kids.size() > 0) {
			mAdapter.addSectionHeaderItem(new Schedule("Kids"));
			mAdapter.addItems(kids);
		}

		if (sakhi.size() > 0) {
			mAdapter.addSectionHeaderItem(new Schedule("Sakhi"));
			mAdapter.addItems(sakhi);
		}

		if (prayer.size() > 0) {
			mAdapter.addSectionHeaderItem(new Schedule("Prayer"));
			mAdapter.addItems(prayer);
		}

		if (fitness.size() > 0) {
			mAdapter.addSectionHeaderItem(new Schedule("Fitness"));
			mAdapter.addItems(fitness);
		}

		if (sessions.size() > 0) {
			mAdapter.addSectionHeaderItem(new Schedule("Sessions"));
			mAdapter.addItems(sessions);
		}

		listView.setAdapter(mAdapter);
	}
	
	private class ExcelURL extends AsyncTask<String, Void, String> {
		private String content =  null;
		private ProgressDialog dialog = new ProgressDialog(mActivity);

		protected void onPreExecute() {
			dialog.setMessage("Please wait... Loading...");
			dialog.show();
		}

		protected String doInBackground(String... urls) {

			try {
				schedules.clear();
				// Figure out the DATE..
				
				schedules = (List<Schedule>) (Util.downloadAndParseExcelFile(mActivity, day, urls)).get(day);
			} catch (ClientProtocolException e) {
				Log.w("HTTP2:",e );
				content = e.getMessage();
				cancel(true);
			} catch (IOException e) {
				Log.w("HTTP3:",e );
				content = e.getMessage();
				cancel(true);
			}catch (Exception e) {
				Log.w("HTTP4:",e );
				content = e.getMessage();
				cancel(true);
			}
			return content;
		}

		protected void onCancelled() {
			dialog.dismiss();
			Toast toast = Toast.makeText(mActivity, 
					"Error connecting to Server", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.TOP, 25, 400);
			toast.show();
		}

		protected void onPostExecute(String content) {
			dialog.dismiss();
			showData();
  		}
	}
}
