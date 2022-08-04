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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
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
import com.tekqube.people.speaker.Speaker;
import com.tekqube.people.speaker.SpeakerAdapter;
import com.tekqube.utils.Util;

public class SpeakersActivity extends Activity {

	ListView listView;
	ArrayList<Speaker> speakers = new ArrayList<Speaker>();

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.speakers, menu);
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		setContentView(R.layout.speakersactivity);
		
		// Get tracker.
        com.google.android.gms.analytics.Tracker t = ((IMRCApp) getApplication()).getTracker(
            TrackerName.APP_TRACKER);

        // Set screen name.
        // Where path is a String representing the screen name.
        t.setScreenName("Speakers Activity ");

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());

		String url = Constants.speakerDataFile;
		new ExcelURL().execute(url);

		//get listview
		listView = (ListView) findViewById(R.id.SpeakerListView);

		/**
		 * Listview item click listener
		 * TrackListActivity will be lauched by passing album id
		 * */
		listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				Speaker listItem = (Speaker) listView.getItemAtPosition(arg2);
				// on selecting a single album
				Intent i = new Intent(getApplicationContext(), SpeakersDetailActivity.class);
				i.putExtra("speaker", listItem);
				// i.putParcelableArrayListExtra("speakers", speakers);

				startActivity(i);
			}
		});		

	}

	private void displaySpeakers() {

		SpeakerAdapter dataAdapter = new SpeakerAdapter(this,
				android.R.layout.simple_list_item_1, speakers);
		listView.setAdapter(dataAdapter);
	}

	private class ExcelURL extends AsyncTask<String, Void, String> {
		private static final int REGISTRATION_TIMEOUT = 3 * 1000;
		private static final int WAIT_TIMEOUT = 30 * 1000;
		private final HttpClient httpclient = new DefaultHttpClient();
		final HttpParams params = httpclient.getParams();
		HttpResponse response;
		private String content =  null;
		private ProgressDialog dialog = new ProgressDialog(SpeakersActivity.this);

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
				String fileName = Constants.speakerListFileName + Constants.extensionName;

				InputStream temp = getTempFile(fileName);
				
				if (temp == null) {
					HttpGet httpGet = new HttpGet(URL);
					response = httpclient.execute(httpGet);
					InputStream copy = response.getEntity().getContent();

					StatusLine statusLine = response.getStatusLine();
					if(statusLine.getStatusCode() == HttpStatus.SC_OK){
						 BufferedWriter writer = null;
						  try {
							  FileOutputStream openFileOutput = openFileOutput(fileName, Context.MODE_PRIVATE);
							  byte[] bytes = IOUtils.toByteArray(copy);
							  openFileOutput.write(bytes);
						  } catch (Exception e) {
							  Log.w(" Unable to download/Parse the file..", e.getMessage());
							  parseExcel(getAssets().open(fileName));
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
						  
						parseExcel(getTempFile(fileName));
					} else {
						//Closes the connection.
						Log.w("HTTP1:",statusLine.getReasonPhrase());
						response.getEntity().getContent().close();
						parseExcel(getAssets().open(fileName));
					}
				} else {
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
			Toast toast = Toast.makeText(SpeakersActivity.this, 
					"Error connecting to Server", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.TOP, 25, 400);
			toast.show();

		}
		
		public InputStream getTempFile(String url) throws IOException {
			  InputStream inputStream = null;
			  
			    try {
			    	if (! Util.canDownloadOtherFiles(SpeakersActivity.this, "speakerFile")) {
			    		// 	Check if file exists on this url or not..
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

			try{

				// Create a workbook using the Input Stream 
				XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);

				// Get the first sheet from workbook 
				XSSFSheet mySheet = myWorkBook.getSheetAt(0);
				int emptyRow = 0;

				// We now need something to iterate through the cells
				Iterator<Row> rowIter = mySheet.rowIterator();
				while(rowIter.hasNext()){

					XSSFRow myRow = (XSSFRow) rowIter.next();
					// Skip the first 2 rows
					if(myRow.getRowNum() < 1) {
						continue;
					}

					if (emptyRow > 10) {
						break;
					}

					Speaker speaker = new Speaker();

					Iterator<Cell> cellIter = myRow.cellIterator();

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
							if (cellValue.equals("") || cellValue.equals("0.0") || cellValue.isEmpty()) {
								emptyRow++;
								continue;
							}

							speaker.setImageUrl(cellValue);
							break;
						case 1:
							if (cellValue.equals("") || cellValue.equals("0.0") || cellValue.isEmpty()) {
								emptyRow++;
								continue;
							}

							speaker.setName(cellValue);
							break;
						case 2:
							if (cellValue.equals("") || cellValue.equals("0.0") || cellValue.isEmpty()) {
								emptyRow++;
								continue;
							}

							speaker.setTitle(cellValue);
							break;
						case 3:

							if (cellValue.equals("") || cellValue.equals("0.0") || cellValue.isEmpty()) {
								emptyRow++;
								continue;
							}
							speaker.setLocation(cellValue);
							break;
						case 4:
							if (cellValue.equals("") || cellValue.equals("0.0") || cellValue.isEmpty()) {
								emptyRow++;
								continue;
							}
							speaker.setKeyAchihevements(cellValue);
							break;
						case 5:
							if (cellValue.equals("") || cellValue.equals("0.0") || cellValue.isEmpty()) {
								emptyRow++;
								continue;
							}
							speaker.setCommunity(cellValue);
							break;

						default: 
							break;
						}

					}

					// Add object to list
					if (speaker != null && speaker.getName() != null) {
						speakers.add(speaker);
					}
				}
			}
			catch (Exception e){
				e.printStackTrace(); 
			}
		}
	}
}
