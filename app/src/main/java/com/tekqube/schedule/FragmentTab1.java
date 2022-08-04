package com.tekqube.schedule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import com.tekqube.Constants;
import com.tekqube.imrc.ListViewAdapter;
import com.tekqube.imrc.R;
import com.tekqube.imrc.Schedule;
import com.tekqube.imrc.ScheduleDetailActivity;
import com.tekqube.utils.Util;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
 
public class FragmentTab1 extends Fragment {
	List<Schedule> schedules = new ArrayList<Schedule>();
	ListViewAdapter adapter;
	ListView listView;
	private Activity mActivity;
    private ImageButton starButton;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragmenttab1, container, false);
        mActivity = getActivity();
		String url = Constants.scheduleFileUrl;
        listView = (ListView) rootView.findViewById(R.id.fridayScheduleListView);

        new ExcelURL().execute(url);

        listView.setOnItemClickListener(new OnItemClickListener() {
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

        return rootView;
    }
     
    
    private class ExcelURL extends AsyncTask<String, Void, String> {
		private String content =  null;
		private ProgressDialog dialog;

		protected void onPreExecute() {
            if (dialog != null) {
                dialog = null;
            }
            dialog = new ProgressDialog(mActivity);
			dialog.setMessage("Please wait... Loading...");
			dialog.show();
		}

		protected String doInBackground(String... urls) {

			try {
				schedules.clear();
				schedules = (List<Schedule>) (Util.downloadAndParseExcelFile(mActivity, 1, urls)).get(1);
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
  			System.out.println( " on Post Execute Schedules Row : "+schedules.size());
			adapter = new ListViewAdapter(mActivity, schedules);
			listView.setAdapter(adapter);
  		}

		public void onDestroy() {
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
		}
	}
}