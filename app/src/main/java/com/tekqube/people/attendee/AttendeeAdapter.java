package com.tekqube.people.attendee;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.tekqube.imrc.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import static com.tekqube.imrc.R.layout.attendee;

public class AttendeeAdapter extends ArrayAdapter<Attendee>{

	private Context context;
	private List<Attendee> worldpopulationlist = new ArrayList<Attendee>();
	private ArrayList<Attendee> arraylist = new ArrayList<Attendee>();
    public AttendeeAdapter(Context context, int resource, List<Attendee> objects) {
		super(context, resource, objects);
		this.context = context;
		  this.worldpopulationlist = objects;
	      this.arraylist = new ArrayList<Attendee>();
	      this.arraylist.addAll(objects);
	}

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.attendee, null, false);
        }

        Attendee item = getItem(position);
        if (item!= null) {
            // My layout has only one TextView
            TextView itemView = (TextView) view.findViewById(R.id.fullName);
            itemView.setText("");
            if (itemView != null) {
                // do whatever you want with your string and long
                itemView.setText(String.format("%s %s", item.getFirstName(), item.getLastName()));
            }

            TextView subTextView = (TextView) view.findViewById(R.id.city);
            if (subTextView != null && item.getCityName() != null && item.getStateName() != null) {
                if(item.getCityName().equals("") && item.getStateName().equals("")) {
                }else {
                    subTextView.setText(String.format("%s,  %s", item.getCityName(), item.getStateName()));
                }
            }

            TextView chapterName = (TextView) view.findViewById(R.id.chapterName);
            if (chapterName != null && item.getChapterName() != null) {
                if(item.getChapterName().equals("") || item.getChapterName().equals("0.0")) {
                }else {
                    chapterName.setText(String.format("%s", item.getChapterName()));
                }
            }
         }

        return view;
    }
    
    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        worldpopulationlist.clear();
        if (charText.length() == 0) {
            worldpopulationlist.addAll(arraylist);
        } 
        else 
        {
            for (Attendee wp : arraylist) {
                if (wp != null && wp.getFirstName().length() > 0 && wp.getLastName().length() > 0) {
//                    System.out.println(" LastName --> " + wp.getLastName().toString() + "" + wp.getCityName().toString());
                    if (wp.getFirstName().toLowerCase(Locale.getDefault()).contains(charText) ||
                            wp.getLastName().toLowerCase(Locale.getDefault()).contains(charText)||
                            wp.getCityName().toLowerCase(Locale.getDefault()).contains(charText)
                            ) {
                        worldpopulationlist.add(wp);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }
 
}
