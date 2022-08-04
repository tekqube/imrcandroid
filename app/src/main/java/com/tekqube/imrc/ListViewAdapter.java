package com.tekqube.imrc;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tekqube.utils.Util;

import static com.tekqube.imrc.R.drawable.favorite;
import static com.tekqube.imrc.R.drawable.favoriteselected;

public class ListViewAdapter extends BaseAdapter {
	Context context;
	List<Schedule> schedules;
	LayoutInflater inflater;
	
	public ListViewAdapter(){}
	
	public ListViewAdapter(Context context, List<Schedule> schedules) {
		super();
		this.context = context;
		this.schedules = schedules;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return schedules.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return schedules.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressLint({ "ViewHolder", "DefaultLocale" })
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		// Declare Variables
		TextView txtrank;
		TextView txtcountry;
		TextView time;
		ImageView imgflag;
		final ImageButton starButton;

		if (arg1 == null) {
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			arg1 = inflater.inflate(R.layout.list_column, arg2, false);
		}

		time = (TextView) arg1.findViewById(R.id.time);
		txtrank = (TextView) arg1.findViewById(R.id.rank);
		txtcountry = (TextView) arg1.findViewById(R.id.country);

		// Locate the ImageView in listview_item.xml
		imgflag = (ImageView) arg1.findViewById(R.id.flag);

		Schedule schedule = (Schedule) getItem(arg0);
		
		// Capture position and set to the TextViews
		time.setText(schedule.getTime());
		txtrank.setText(schedule.session);
		txtcountry.setText(schedule.room);

		starButton = (ImageButton) arg1.findViewById(R.id.starButton);
        String key = schedule.getSession()+"-"+ schedule.getTargetGroup() + "-" + schedule.getDay();
        if (Util.getFavorites((Activity) context, key) != null) {
            starButton.setImageResource(favoriteselected);
        } else {
            starButton.setImageResource(favorite);
        }

		starButton.setTag(arg0);
		starButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int tag = (int) v.getTag();
				ImageButton button = (ImageButton) v.findViewById(R.id.starButton);
				System.out.print(" View Tag :"+tag);

				Schedule schedule = (Schedule) getItem(tag);
				String key = schedule.getSession()+"-"+ schedule.getTargetGroup() + "-" + schedule.getDay();
				if (Util.getFavorites((Activity) context, key) == null) {
					Util.setFavorite((Activity) context, key, schedule);
					button.setImageResource(favoriteselected);
				} else {
					Util.setFavorite((Activity) context, key, null);
					button.setImageResource(favorite);
				}
			}
		});

		// Capture position and set to the ImageView
		if (schedule.getTargetGroup() != null ) {
			System.out.println(" Target Group : "+schedule.getTargetGroup().toLowerCase());
			if (schedule.getTargetGroup().toLowerCase().equals("session")) {
				imgflag.setImageResource(R.drawable.sessions);
			} else if (schedule.getTargetGroup().toLowerCase().equals("rays")) {
				imgflag.setImageResource(R.drawable.rays);
			} else if (schedule.getTargetGroup().toLowerCase().equals("sakhi")) {
				imgflag.setImageResource(R.drawable.sakhi);
			} else if (schedule.getTargetGroup().toLowerCase().equals("seniors")) {
				imgflag.setImageResource(R.drawable.seniors);
			} else if (schedule.getTargetGroup().toLowerCase().equals("entertainment")) {
				imgflag.setImageResource(R.drawable.entertainment);
			} else if (schedule.getTargetGroup().toLowerCase().equals("food")) {
				imgflag.setImageResource(R.drawable.food);
			} else if (schedule.getTargetGroup().toLowerCase().equals("kids")) {
				imgflag.setImageResource(R.drawable.kids);
			} else if (schedule.getTargetGroup().toLowerCase().equals("prayer")) {
				imgflag.setImageResource(R.drawable.prayer);
			} else if (schedule.getTargetGroup().toLowerCase().equals("fitness")) {
				imgflag.setImageResource(R.drawable.fitness);
			} else {
				imgflag.setImageResource(R.drawable.general);
			}
		}

		return arg1;
	}

}
