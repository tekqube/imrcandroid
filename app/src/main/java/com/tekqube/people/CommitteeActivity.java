package com.tekqube.people;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.tekqube.Constants;
import com.tekqube.application.IMRCApp;
import com.tekqube.application.IMRCApp.TrackerName;
import com.tekqube.imrc.R;
import com.tekqube.people.committee.Committee;
import com.tekqube.people.committee.CommitteeAdapter;
import com.tekqube.utils.Util;

import static android.support.design.widget.Snackbar.LENGTH_INDEFINITE;

public class CommitteeActivity extends Activity {
    ListView listView;
    ArrayList<Committee> committees = new ArrayList<Committee>();
    private View mLayout;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;

        ActionBar actionBar = this.getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#a00000")));

        setContentView(R.layout.activity_committee);
        mLayout = findViewById(R.id.committee_page);

        // Get tracker.
        com.google.android.gms.analytics.Tracker t = ((IMRCApp) getApplication()).getTracker(
                TrackerName.APP_TRACKER);

        // Set screen name.
        // Where path is a String representing the screen name.
        t.setScreenName("Committee Activity ");

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());

        //get listview
        listView = (ListView) findViewById(R.id.CommitteeListView);

        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                    long arg3) {
                Committee listItem = (Committee) listView.getItemAtPosition(arg2);

                // Get tracker.
                com.google.android.gms.analytics.Tracker t = ((IMRCApp) getApplication()).getTracker(
                        TrackerName.APP_TRACKER);

                // Set screen name.
                // Where path is a String representing the screen name.
                t.setScreenName("Committee Activity ");

                // Send a screen view.
                t.send(new HitBuilders.EventBuilder("Committee Info Selected", "Committee selected"+listItem).build());

                // on selecting a single album
                Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + listItem.getPhone()));
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(i);

                    return;
                }

                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CALL_PHONE)) {
                    Log.i("Attendee",
                            "Displaying camera permission rationale to provide additional context.");
                    Snackbar.make(mLayout, getString(R.string.call_phone_permission),
                            LENGTH_INDEFINITE)
                            .setAction("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ActivityCompat.requestPermissions(CommitteeActivity.this,
                                            new String[]{Manifest.permission.CALL_PHONE}, 1);
                                }
                            })
                            .show();
                }
            }
        });

        String url = Constants.teamPointOfContactList;
        new ExcelURL().execute(url);
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == 1) {
            // BEGIN_INCLUDE(permission_result)
            // Received permission result for camera permission.
            Log.i("Committee Activity", "Received response for Call Phone.");

            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission has been granted, preview can be displayed
                Log.i("Committee Activity", "Call permission has now been granted. Showing preview.");
                Snackbar.make(mLayout, "Call Permission has now been granted.",
                        Snackbar.LENGTH_SHORT).show();
            } else {
                Log.i("Committee Activity", "Call Permission not granted.");
                Snackbar.make(mLayout, "Call Permission not granted.",
                        Snackbar.LENGTH_SHORT).show();

            }
            // END_INCLUDE(permission_result)
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void displaySpeakers() {
        CommitteeAdapter adapter = new CommitteeAdapter(this, R.layout.commiteedetail, committees);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.committee, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class ExcelURL extends AsyncTask<String, Void, String> {
        private static final int REGISTRATION_TIMEOUT = 3 * 1000;
        private static final int WAIT_TIMEOUT = 30 * 1000;
        private final HttpClient httpclient = new DefaultHttpClient();
        final HttpParams params = httpclient.getParams();
        HttpResponse response;
        private String content =  null;
        private ProgressDialog dialog = new ProgressDialog(CommitteeActivity.this);

        protected void onPreExecute() {
            dialog.setMessage("Please wait... Loading...");
            dialog.show();
        }

        @SuppressLint("LongLogTag")
        protected String doInBackground(String... urls) {

            String URL = null;

            try {
                URL = urls[0];
                HttpConnectionParams.setConnectionTimeout(params, REGISTRATION_TIMEOUT);
                HttpConnectionParams.setSoTimeout(params, WAIT_TIMEOUT);
                ConnManagerParams.setTimeout(params, WAIT_TIMEOUT);
                String filename = Constants.teamFileName + Constants.extensionName;

                InputStream temp = getTempFile(filename);

                if (temp == null) {
                    HttpGet httpGet = new HttpGet(URL);
                    response = httpclient.execute(httpGet);
                    InputStream copy = response.getEntity().getContent();

                    StatusLine statusLine = response.getStatusLine();
                    if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                        BufferedWriter writer = null;
                        try {
                            FileOutputStream openFileOutput = openFileOutput(filename, Context.MODE_PRIVATE);
                            byte[] bytes = IOUtils.toByteArray(copy);
                            openFileOutput.write(bytes);
                        } catch (Exception e) {
                            Log.w(" Unable to download/Parse the file..", e.getMessage());
                            parseExcel(getAssets().open(filename));
                            //throw new RuntimeException(e);
                        } finally {
                            if (writer != null) {
                                try {
                                    writer.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        parseExcel(getAssets().open(filename));
                    } else {
                        //Closes the connection.
                        Log.w("HTTP1:",statusLine.getReasonPhrase());
                        response.getEntity().getContent().close();
                        parseExcel(getAssets().open(filename));
                    }
                }
                else {
                    parseExcel(temp);
                }
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
            Toast toast = Toast.makeText(CommitteeActivity.this,
                    "Error connecting to Server", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();
        }

        public InputStream getTempFile(String url) throws IOException {
            InputStream inputStream = null;

            try {
                // Check if file exists on this url or not..
                if (! Util.canDownloadOtherFiles(CommitteeActivity.this, "committeeFile")) {
                    inputStream = openFileInput(url);
                } else {
                    return null;
                }

            } catch (IOException e) {
                Log.e("login activity", "Can not read file: " + e.toString());
            }
            return inputStream;
        }

        protected void onPostExecute(String content) {
            dialog.dismiss();
            displaySpeakers();
        }

        private void parseExcel(InputStream fis){

            try {

                // Create a workbook using the Input Stream
                XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);

                // Get the first sheet from workbook
                XSSFSheet mySheet = myWorkBook.getSheetAt(0);

                // We now need something to iterate through the cells
                Iterator<Row> rowIter = mySheet.rowIterator();
                while(rowIter.hasNext()){

                    XSSFRow myRow = (XSSFRow) rowIter.next();
                    // Skip the first 2 rows
                    if(myRow.getRowNum() < 1) {
                        continue;
                    }

                    Committee committee = new Committee();

                    Iterator<Cell> cellIter = myRow.cellIterator();

                    if (!cellIter.hasNext())
                        break;

                    while(cellIter.hasNext()){

                        XSSFCell myCell = (XSSFCell) cellIter.next();
                        String cellValue = "";

                        // Check for cell Type
                        if(myCell.getCellType() == XSSFCell.CELL_TYPE_STRING){
                            cellValue = myCell.getStringCellValue();
                        } else {
                            cellValue = String.valueOf(myCell.getNumericCellValue());
                        }

                        // Push the parsed data in the Java Object
                        // Check for cell index
                        switch (myCell.getColumnIndex()) {
                            case 0:
                                committee.setPhotoUrl(cellValue);
                                break;
                            case 1:
                                committee.setCommitteeName(cellValue);
                                break;
                            case 2:
                                committee.setName(cellValue);
                                break;
                            case 3:
                                committee.setPhone(cellValue);
                                break;
                            default:
                                break;
                        }

                    }

                    // Add object to list
                    committees.add(committee);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
