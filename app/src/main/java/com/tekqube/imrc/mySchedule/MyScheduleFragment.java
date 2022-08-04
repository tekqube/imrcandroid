package com.tekqube.imrc.mySchedule;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.tekqube.application.IMRCApp;
import com.tekqube.imrc.R;
import com.tekqube.imrc.Schedule;
import com.tekqube.imrc.ScheduleDetailActivity;
import com.tekqube.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class MyScheduleFragment extends Fragment {
	private Activity mActivity;
	private List<Schedule> schedules;
    private ListViewAdapter adapter;
    private ListView listView;
    private List<Schedule> list;
    private TextView message;
	
	public MyScheduleFragment(){}

	@SuppressLint("NewApi")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
		// Get tracker.
		com.google.android.gms.analytics.Tracker t = ((IMRCApp) mActivity.getApplication()).getTracker(
				IMRCApp.TrackerName.APP_TRACKER);
		t.setScreenName("My Schedule Fragment");
		t.send(new HitBuilders.AppViewBuilder().build());
	}

    public void setButtonColor(View rootView, String friColor, String satColor, String sunColor, String monColor) {
        Button friDayButton = (Button) rootView.findViewById(R.id.sat);
        Button sat= (Button) rootView.findViewById(R.id.sun);
        Button sun= (Button) rootView.findViewById(R.id.mon);
        Button mon= (Button) rootView.findViewById(R.id.tue);

        if (friColor == "#ffffff") {
            friDayButton.setTextColor(Color.parseColor("#a00000"));
        } else {
            friDayButton.setTextColor(Color.parseColor("#ffffff"));
        }
        friDayButton.setBackgroundColor(Color.parseColor(friColor));

        if (satColor == "#ffffff") {
            sat.setTextColor(Color.parseColor("#a00000"));
        } else {
            sat.setTextColor(Color.parseColor("#ffffff"));
        }
        sat.setBackgroundColor(Color.parseColor(satColor));

        if (sunColor == "#ffffff") {
            sun.setTextColor(Color.parseColor("#a00000"));
        } else {
            sun.setTextColor(Color.parseColor("#ffffff"));
        }
        sun.setBackgroundColor(Color.parseColor(sunColor));

        if (monColor == "#ffffff") {
            mon.setTextColor(Color.parseColor("#a00000"));
        } else {
            mon.setTextColor(Color.parseColor("#ffffff"));
        }
        mon.setBackgroundColor(Color.parseColor(monColor));
    }

    public void updateTable(View rootView, int day) {
        try {
            schedules.clear();
            for(int i=0 ; i < list.size(); i++) {
                Schedule schedule = list.get(i);

                if (schedule != null && schedule.getDay() == day) {
                    schedules.add(schedule);
                }
            }

            if (schedules.size() > 0) {
                listView.setVisibility(View.VISIBLE);
                message.setVisibility(View.GONE);
                adapter = new ListViewAdapter(mActivity, schedules);
                listView.setAdapter(adapter);
            } else {
                listView.setVisibility(View.GONE);
                message.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Log.w("HTTP4:",e );
        }
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.myschedulefragment, container, false);
        list = Util.getAllFavorites(getActivity());
        listView = (ListView) rootView.findViewById(R.id.tableDataview);
        message = (TextView) rootView.findViewById(R.id.message);

        schedules = new ArrayList<>();
        setButtonColor(rootView, "#a00000","#ffffff", "#ffffff", "#ffffff");
        updateTable(rootView, 1);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                Schedule schedule = schedules.get(arg2);

                Intent i = new Intent(getActivity(), ScheduleDetailActivity.class);
                i.putExtra("schedule", schedule);

                startActivity(i);
            }
        });

        Button friDayButton = (Button) rootView.findViewById(R.id.sat);
        friDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonColor(rootView, "#a00000","#ffffff", "#ffffff", "#ffffff");
                updateTable(rootView, 1);
            }
        });

        Button satDayButton = (Button) rootView.findViewById(R.id.sun);

        satDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonColor(rootView, "#ffffff","#a00000", "#ffffff", "#ffffff");
                updateTable(rootView, 2);
            }
        });

        Button sunDayButton = (Button) rootView.findViewById(R.id.mon);

        sunDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonColor(rootView, "#ffffff","#ffffff","#a00000", "#ffffff");
                updateTable(rootView, 3);
            }
        });

        Button monDayButton = (Button) rootView.findViewById(R.id.tue);

        monDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonColor(rootView, "#ffffff","#ffffff", "#ffffff", "#a00000");
                updateTable(rootView, 4);
            }
        });

		return rootView;
	}

}
