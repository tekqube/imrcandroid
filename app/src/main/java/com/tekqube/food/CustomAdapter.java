package com.tekqube.food;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tekqube.imrc.R;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class CustomAdapter extends BaseAdapter {
 
	private static final int TYPE_ITEM = 0;
	private static final int TYPE_SEPARATOR = 1;
 
	private ArrayList<Food> mData = new ArrayList<Food>();
	private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();
 
	private LayoutInflater mInflater;
 
	public CustomAdapter(Context context) {
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
 
	public void addItem(final Food item) {
		mData.add(item);
		notifyDataSetChanged();
	}
	
	public void addItems(final List<Food> items) {
		mData.addAll(items);
		notifyDataSetChanged();
	}
 
	public void addSectionHeaderItem(final Food item) {
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
	public Food getItem(int position) {
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
                    convertView = mInflater.inflate(R.layout.list_single, null);
                    holder.meal = (TextView) convertView.findViewById(R.id.mealName);
					holder.meal.setTextSize((float) 18.0);
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.food_section_header, null);
                    holder.categoryName = (TextView) convertView.findViewById(R.id.textSeparator);
                    holder.time = (TextView) convertView.findViewById(R.id.mealTime);
                    break;
            }
            convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Food food = mData.get(position);

        if (food.getCategory() != null && food.getTime() != null) {
            holder.categoryName.setText(food.getCategory());
            holder.time.setText(food.getTime());
        }

		if (food.getMealName() != null) {
			holder.meal.setText(food.getMealName());
		}
		
		return convertView;
	}
 
	public static class ViewHolder {
		public TextView meal;
		public TextView categoryName;
		public TextView time;
	}
 
}