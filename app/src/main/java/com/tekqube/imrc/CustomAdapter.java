package com.tekqube.imrc;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
 
class CustomAdapter extends BaseAdapter {
 
	private static final int TYPE_ITEM = 0;
	private static final int TYPE_SEPARATOR = 1;
    private boolean hideStartButton = false;
 
	private ArrayList<Schedule> mData = new ArrayList<Schedule>();
	private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();
 
	private LayoutInflater mInflater;
 
	public CustomAdapter(Context context) {
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

    public CustomAdapter(Context context, boolean hideStartButton) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.hideStartButton = hideStartButton;
    }
 
	public void addItem(final Schedule item) {
		mData.add(item);
		notifyDataSetChanged();
	}
	
	public void addItems(final List<Schedule> items) {
		mData.addAll(items);
		notifyDataSetChanged();
	}
 
	public void addSectionHeaderItem(final Schedule item) {
		mData.add(item);
		sectionHeader.add(mData.size() - 1);
		notifyDataSetChanged();
	}
 
	@Override
	public int getItemViewType(int position) {
		return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
	}
 
	@Override
	public int getViewTypeCount() {
		return 2;
	}
 
	@Override
	public int getCount() {
		return mData.size();
	}
 
	@Override
	public Schedule getItem(int position) {
		return mData.get(position);
	}
 
	@Override
	public long getItemId(int position) {
		return position;
	}
 
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		int rowType = getItemViewType(position);

		if (convertView == null) {
			holder = new ViewHolder();
			switch (rowType) {
			case TYPE_ITEM:
				convertView = mInflater.inflate(R.layout.list_column, null);
				holder.time = (TextView) convertView.findViewById(R.id.time);
				holder.txtrank = (TextView) convertView.findViewById(R.id.rank);
				holder.txtcountry = (TextView) convertView.findViewById(R.id.country);
				holder.imgflag = (ImageView) convertView.findViewById(R.id.flag);
                holder.starButton = (ImageButton) convertView.findViewById(R.id.starButton);
                holder.starButton.setVisibility(View.INVISIBLE);
                break;
			case TYPE_SEPARATOR:
				convertView = mInflater.inflate(R.layout.schedule_section_header, null);
				holder.textView = (TextView) convertView.findViewById(R.id.textSeparator);
				break;
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Schedule schedule = mData.get(position);
		if (schedule.getTime() != null) {
			holder.time.setText(schedule.getTime());
		}
		
		if (schedule.getSession() != null && rowType == TYPE_ITEM) {
			holder.txtrank.setText(schedule.getSession());
		} else {
			holder.textView.setText(schedule.getSession());
		}
		
		if (schedule.getRoom() != null) {
			holder.txtcountry.setText(schedule.getRoom());
		}

		// Capture position and set to the ImageView
		if (schedule.getTargetGroup() != null ) {
			System.out.println(" Target Group : "+schedule.getTargetGroup().toLowerCase());
			if (schedule.getTargetGroup().toLowerCase().equals("session")) {
				holder.imgflag.setImageResource(R.drawable.sessions);
			} else if (schedule.getTargetGroup().toLowerCase().equals("rays")) {
				holder.imgflag.setImageResource(R.drawable.rays);
			} else if (schedule.getTargetGroup().toLowerCase().equals("sakhi")) {
				holder.imgflag.setImageResource(R.drawable.sakhi);
			} else if (schedule.getTargetGroup().toLowerCase().equals("seniors")) {
				holder.imgflag.setImageResource(R.drawable.seniors);
			} else if (schedule.getTargetGroup().toLowerCase().equals("entertainment")) {
				holder.imgflag.setImageResource(R.drawable.entertainment);
			} else if (schedule.getTargetGroup().toLowerCase().equals("food")) {
				holder.imgflag.setImageResource(R.drawable.food);
			} else if (schedule.getTargetGroup().toLowerCase().equals("kids")) {
				holder.imgflag.setImageResource(R.drawable.kids);
			} else if (schedule.getTargetGroup().toLowerCase().equals("prayer")) {
				holder.imgflag.setImageResource(R.drawable.prayer);
			} else if (schedule.getTargetGroup().toLowerCase().equals("fitness")) {
                holder.imgflag.setImageResource(R.drawable.fitness);
            } else {
				holder.imgflag.setImageResource(R.drawable.general);
			}
		}
		
		return convertView;
	}
 
	public static class ViewHolder {
		public TextView textView;
		public TextView time;
		public TextView txtrank;
		public TextView txtcountry;
		public ImageView imgflag;
        public ImageButton starButton;
	}
 
}