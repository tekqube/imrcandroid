package com.tekqube.people.speaker;

import java.util.List;

import android.R;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
//import com.tekqube.imrc.R;

public class SpeakerAdapter extends ArrayAdapter<Speaker>{
	private Context context;

    public SpeakerAdapter(Context context, int resource, List<Speaker> objects) {
		super(context, resource, objects);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.simple_list_item_2, null);
        }

        Speaker item = getItem(position);
        if (item!= null) {
            // My layout has only one TextView
            TextView itemView = (TextView) view.findViewById(R.id.text1);
            if (itemView != null) {
                // do whatever you want with your string and long
                itemView.setTextColor(Color.parseColor("#183875"));
                itemView.setText(String.format("%s", item.getName()));
            }
            
            TextView subTextView = (TextView) view.findViewById(R.id.text2);
            if (subTextView != null) {
                subTextView.setTextColor(Color.parseColor("#183875"));
            	subTextView.setText(item.getQualification());
            }
         }

        return view;
    }
}
