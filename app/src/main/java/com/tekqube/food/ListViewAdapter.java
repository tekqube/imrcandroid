package com.tekqube.food;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tekqube.imrc.R;
import com.tekqube.imrc.Schedule;

import java.util.List;

public class ListViewAdapter extends BaseAdapter {
	Context context;
	List<Food> foods;
	LayoutInflater inflater;
	
	public ListViewAdapter(){}
	
	public ListViewAdapter(Context context, List<Food> foods) {
		super();
		this.context = context;
		this.foods = foods;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return foods.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return foods.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressLint({ "ViewHolder", "DefaultLocale" })
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		TextView meal;

		if (arg1 == null) {
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			arg1 = inflater.inflate(R.layout.list_single, arg2, false);
		}

		meal = (TextView) arg1.findViewById(R.id.mealName);

		Food food = (Food) getItem(arg0);
		
		if (food.getMealName() != null) {
			meal.setText(food.getMealName());
		}

		return arg1;
	}

}
