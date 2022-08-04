package com.tekqube.people.sponsors;

import java.util.ArrayList;

import android.R;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SponsorAdapter extends ArrayAdapter<Sponsor> {
	private Context context;
	
	public SponsorAdapter(Context context, int resource, ArrayList<Sponsor> sponsors) {
		super(context, resource, sponsors);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.simple_list_item_1, null);
        }

        Sponsor item = getItem(position);
        if (item!= null) {
            // My layout has only one TextView
            TextView itemView = (TextView) view.findViewById(R.id.text1);
            if (itemView != null) {
                // do whatever you want with your string and long
                itemView.setTextColor(Color.parseColor("#a00000"));
                itemView.setText(String.format("%s", item.getSponsorName()));
            }
         }

        return view;
	}
}
