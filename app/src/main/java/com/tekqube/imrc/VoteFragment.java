package com.tekqube.imrc;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.analytics.HitBuilders;
import com.tekqube.application.IMRCApp;
import com.tekqube.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class VoteFragment extends android.app.Fragment {
    List<String> choices = new ArrayList<String>();
    int selectedChoice = 0;
    Integer qid = null;
    VoteFragment voteFragment = this;
    private ListView listView;
    private View currentSelectedView;
    private String phoneNumber;
    private TextView confirmation;
    private TextView question;
    private Button submitVote;
    private com.android.volley.RequestQueue requestQueue;

    Timer timer;
    TimerTask timerTask;

    public VoteFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.vote_layout, container, false);
        phoneNumber = Util.getRandomString(getActivity());
        if (phoneNumber.equals("0")) {
            phoneNumber = UUID.randomUUID().toString();
            Util.setRandomVoteString(getActivity(), phoneNumber);
        }

        // Get tracker.
        final com.google.android.gms.analytics.Tracker t = ((IMRCApp) getActivity().getApplication()).getTracker(
                IMRCApp.TrackerName.APP_TRACKER);

        // Set screen name.
        // Where path is a String representing the screen name.
        t.setScreenName("Vote Fragment");

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());

        question = (TextView) rootView.findViewById(R.id.question);
        confirmation = (TextView) rootView.findViewById(R.id.confirmation);

        listView = (ListView) rootView.findViewById(R.id.choices);

        requestQueue = Volley.newRequestQueue(getActivity());
        getQuestion(requestQueue);

        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedChoice = position;
                if (currentSelectedView != null && currentSelectedView != view) {
                    unhighlightCurrentRow(currentSelectedView);
                }

                currentSelectedView = view;
                highlightCurrentRow(currentSelectedView);
                //other codes
                System.out.print(" Position :"+position);
            }
        });

        submitVote = (Button) rootView.findViewById(R.id.submitVote);
        submitVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String postUrl = "https://imrc.mmna.org/vote/updatevote.php?qid="+qid+"&phonenum="+phoneNumber+"&choice="+selectedChoice;

                StringRequest stringRequest = new StringRequest(Request.Method.POST, postUrl,new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Util.setChoiceInfo(getActivity(), qid, selectedChoice);
                        confirmation.setVisibility(View.VISIBLE);
                        VolleyLog.v("Response:%n %s", response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                    }
                });

                requestQueue.add(stringRequest);
                System.out.print(" SubmitVote Set OnClick... ");
            }
        });

        return rootView;
    }

    @Override
    public void onDestroy() {
        if(timer != null) {
            timer.cancel(); // for CountDownTimer
            timer = null;
        }
        super.onDestroy();
    }

    public void onResume() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        System.out.print(" Getting new question... Timer");
                        getQuestion(requestQueue);
                    }
                });
            }
        } ,15000, 15000);
        super.onResume();
    }

    @Override
    public void onPause() {
        if(timer != null) {
            timer.cancel(); // for CountDownTimer
            timer = null;
        }
        super.onPause();
    }

    private void getQuestion(com.android.volley.RequestQueue requestQueue) {
        confirmation.setVisibility(View.INVISIBLE);
        choices = new ArrayList<>();

        final String url ="https://imrc.mmna.org/vote/activeq.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(" Response :"+response);
                try {
                    question.setText(response.getString("question"));

                    qid = (Integer) response.get("qid");
                    selectedChoice = Util.getChoiceInfo(getActivity(), qid);

                    JSONArray jArray = (JSONArray) response.get("choices");
                    if (jArray != null) {
                        for (int i=0 ; i<jArray.length() ; i++){
                            choices.add(jArray.get(i).toString());
                        }
                    }

                    if (choices.size() <= 1) {
                        submitVote.setVisibility(View.INVISIBLE);
                    } else {
                        submitVote.setVisibility(View.VISIBLE);
                    }

                    ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), R.layout.vote_layout_text, choices);
                    listView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                System.out.print(" Error :"+error);
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void unhighlightCurrentRow(View rowView) {
        rowView.setBackgroundColor(Color.TRANSPARENT);
        TextView textView = (TextView) rowView.findViewById(R.id.vote_text);
    }

    private void highlightCurrentRow(View rowView) {
        rowView.setBackgroundColor(getResources().getColor(
                R.color.dark_gray));
        TextView textView = (TextView) rowView.findViewById(R.id.vote_text);
    }
}
