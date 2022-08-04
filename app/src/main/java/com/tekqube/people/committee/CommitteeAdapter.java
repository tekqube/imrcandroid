package com.tekqube.people.committee;

import java.util.List;

import com.tekqube.imrc.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import static com.tekqube.imrc.R.layout.commiteedetail;

public class CommitteeAdapter extends ArrayAdapter<Committee> {
	private Context context;

	public CommitteeAdapter(Context context, int resource,List<Committee> objects) {
		super(context, resource, objects);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.commiteedetail, null, false);
        }

        Committee item = getItem(position);
    //      if (item!= null) {
            if (item!= null && item.getCommitteeName().length() > 0) {
            // My layout has only one TextView
            TextView itemView = (TextView) view.findViewById(R.id.text1);
            if (itemView != null) {
                // do whatever you want with your string and long
                itemView.setText(item.getCommitteeName());
            }
            
            TextView subTextView = (TextView) view.findViewById(R.id.text2);
            if (subTextView != null) {
            	subTextView.setText(item.getName());
            }
         }

        return view;
    }
}
