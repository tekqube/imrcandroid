package com.tekqube.food;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.tekqube.Constants;
import com.tekqube.application.IMRCApp;
import com.tekqube.application.IMRCApp.TrackerName;
import com.tekqube.imrc.R;
import com.tekqube.utils.Util;

import org.apache.http.client.ClientProtocolException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FoodFragment extends Fragment{

    private Activity mActivity;
    ActionBar actionBar;
    List<Food> foods = new ArrayList<Food>();
    CustomAdapter mAdapter;
    ListView listView;
    private List<Food> filteredFoods;
    private TextView message;

    public FoodFragment(){}

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
            mAdapter = new CustomAdapter(mActivity);

            for (int i = 0; i < foods.size(); i++) {
                Food food = foods.get(i);

                if (food.getDay() == day) {
                    if ((food.getCategory() != null && food.getTime() != null) && (food.getCategory() != "0.0" && food.getTime() != "0.0")) {
                        System.out.println(">>>>> Category :"+food.getCategory());
                        System.out.println(" Time :"+food.getTime() +" ... i :"+i);

                        food.setCuisine(null);
                        food.setMealName(null);
                        mAdapter.addSectionHeaderItem(new Food(food.getCategory(), food.getTime()));
                    }

                    if ((food.getMealName() != null) && !(food.getMealName().equals("0.0"))) {
                        food.setCategory(null);
                        food.setTime(null);
                        mAdapter.addItem(food);
                    }
                }
            }

            listView.setAdapter(mAdapter);
        } catch (Exception e) {
            Log.w("HTTP4:", e);
        }
    }

    @SuppressLint("NewApi")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        actionBar = mActivity.getActionBar();

        // Get tracker.
        com.google.android.gms.analytics.Tracker t = ((IMRCApp) mActivity.getApplication()).getTracker(
                TrackerName.APP_TRACKER);
        t.setScreenName("Food Fragment");
        t.send(new HitBuilders.AppViewBuilder().build());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final View rootView = inflater.inflate(R.layout.myschedulefragment, container, false);
        listView = (ListView) rootView.findViewById(R.id.tableDataview);
        message = (TextView) rootView.findViewById(R.id.message);

        String url = Constants.foodMenuFile;
        new ExcelURL().execute(url);

        int day = 0;
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        int dt = cal.get(Calendar.DATE);
        int year = cal.get(Calendar.YEAR);

        if (dt == 2 && month == 7 && year == 2018) {
            day = 1;
        } else if (dt == 3 && month == 7 && year == 2018) {
            day = 2;
        } else if (dt == 4 && month == 7 && year == 2018) {
            day = 3;
        }

        setButtonColor(rootView, (day == 0 ? "#a00000" : "#ffffff"),
                (day == 1 ? "#a00000" : "#ffffff"),
                (day == 2 ? "#a00000" : "#ffffff"),
                (day == 3 ? "#a00000" : "#ffffff"));
        //updateTable(rootView, day);

        Button satDayButton = (Button) rootView.findViewById(R.id.sat);
        satDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonColor(rootView, "#a00000","#ffffff", "#ffffff", "#ffffff");
                updateTable(rootView, 0);
            }
        });

        Button sunDayButton = (Button) rootView.findViewById(R.id.sun);

        sunDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonColor(rootView, "#ffffff","#a00000", "#ffffff", "#ffffff");
                updateTable(rootView, 1);
            }
        });

        Button monDayButton = (Button) rootView.findViewById(R.id.mon);

        monDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonColor(rootView, "#ffffff","#ffffff","#a00000", "#ffffff");
                updateTable(rootView, 2);
            }
        });

        Button tueDayButton = (Button) rootView.findViewById(R.id.tue);

        tueDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonColor(rootView, "#ffffff","#ffffff", "#ffffff", "#a00000");
                updateTable(rootView, 3);
            }
        });


        return rootView;
    }

    private class ExcelURL extends AsyncTask<String, Void, String> {
        private String content =  null;
        private ProgressDialog dialog = new ProgressDialog(mActivity);

        protected void onPreExecute() {
            dialog.setMessage("Please wait... Loading...");
            dialog.show();
        }

        protected String doInBackground(String... urls) {
            try {
                foods.clear();
                foods = Util.downloadAndParseFoodExcelFile(mActivity, 2, urls);
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
            System.out.println(" on Post Execute Schedules Row : " + foods.size());

            updateTable(null, 0);
        }
    }
}
