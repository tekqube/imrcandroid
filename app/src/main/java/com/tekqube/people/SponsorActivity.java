package com.tekqube.people;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.tekqube.Constants;
import com.tekqube.application.IMRCApp;
import com.tekqube.application.IMRCApp.TrackerName;
import com.tekqube.imrc.R;
import com.tekqube.people.sponsors.Sponsor;
import com.tekqube.people.sponsors.SponsorAdapter;

public class SponsorActivity extends Activity implements TabListener {

	ListView listView;
	ArrayList<Sponsor> sponsors = new ArrayList<Sponsor>();
	ArrayList<Sponsor> filterSponsors = new ArrayList<Sponsor>();
	private ActionBar actionBar;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sponsor, menu);
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
		
		// Get tracker.
        com.google.android.gms.analytics.Tracker t = ((IMRCApp) getApplication()).getTracker(
            TrackerName.APP_TRACKER);
        t.setScreenName("Sponsor Activity ");
        t.send(new HitBuilders.AppViewBuilder().build());

		setContentView(R.layout.activity_sponsor);

		actionBar = this.getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#a00000")));

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		// Set Tab Icon and Titles
        Tab all = actionBar.newTab().setText("All");
        Tab diamond = actionBar.newTab().setText("Diamond");
		Tab emerald = actionBar.newTab().setText("Emerald");
		Tab ruby = actionBar.newTab().setText("Ruby");
        Tab platinum  = actionBar.newTab().setText("Platinum");
        Tab gold = actionBar.newTab().setText("Gold");
        Tab silver = actionBar.newTab().setText("Silver");
        Tab bronze= actionBar.newTab().setText("Bronze");
        Tab crystal = actionBar.newTab().setText("Crystal");
        Tab patron = actionBar.newTab().setText("Patron");
        Tab corporate = actionBar.newTab().setText("Corporate");

        // Add tabs to actionbar
        actionBar.addTab(all.setTabListener(this));
        actionBar.addTab(diamond.setTabListener(this));
		actionBar.addTab(emerald.setTabListener(this));
		actionBar.addTab(ruby.setTabListener(this));
        actionBar.addTab(platinum.setTabListener(this));
        actionBar.addTab(gold.setTabListener(this));
        actionBar.addTab(silver.setTabListener(this));
        actionBar.addTab(bronze.setTabListener(this));
        actionBar.addTab(crystal.setTabListener(this));
        actionBar.addTab(patron.setTabListener(this));
        actionBar.addTab(corporate.setTabListener(this));
		
		String url = Constants.sponsorInformationFile;
		new ExcelURL().execute(url);

		//get listview
		listView = (ListView) findViewById(R.id.SponsorListView);
	}

	private void displaySponsors(String type) {
		filterSponsors.clear();
		List<Sponsor> tempArr = new ArrayList<Sponsor>();
        for (int i=0; i<sponsors.size(); i++) {
            if(sponsors.get(i).getSponsorName() != null && sponsors.get(i).getSponsorName().toString().length() > 3){
                    if (type.equals("all") || sponsors.get(i).getCategory().toLowerCase().contains(type)) {
                        tempArr.add(sponsors.get(i));
                }
            }
		}
		filterSponsors.addAll(tempArr);
		System.out.println( "Sponsors Size : "+filterSponsors.size());
		if (filterSponsors.size() != 0) {
			SponsorAdapter dataAdapter = new SponsorAdapter(this, android.R.layout.simple_list_item_1, filterSponsors);
			listView.setAdapter(dataAdapter);
		}
	}

	private class ExcelURL extends AsyncTask<String, Void, String> {
		private static final int REGISTRATION_TIMEOUT = 3 * 1000;
		private static final int WAIT_TIMEOUT = 30 * 1000;
		private final HttpClient httpclient = new DefaultHttpClient();
		final HttpParams params = httpclient.getParams();
		HttpResponse response;
		private String content =  null;
		private ProgressDialog dialog = new ProgressDialog(SponsorActivity.this);

		protected void onPreExecute() {
			dialog.setMessage("Please wait... Loading...");
			dialog.show();
		}

		@SuppressLint("LongLogTag")
		protected String doInBackground(String... urls) {
			String fileName = Constants.sponsorListFileName + Constants.extensionName;
			String URL = null;

			try {

				URL = urls[0];
				HttpConnectionParams.setConnectionTimeout(params, REGISTRATION_TIMEOUT);
				HttpConnectionParams.setSoTimeout(params, WAIT_TIMEOUT);
				ConnManagerParams.setTimeout(params, WAIT_TIMEOUT);
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
						//throw new IOException(statusLine.getReasonPhrase());
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


        public InputStream getTempFile(String url) throws IOException {
            InputStream inputStream = null;

            try {
                // Check if file exists on this url or not..
                inputStream = openFileInput(url);
            } catch (IOException e) {
                Log.e("login activity", "Can not read file: " + e.toString());
            }
            return inputStream;
        }

        protected void onCancelled() {
            dialog.dismiss();
            Toast toast = Toast.makeText(SponsorActivity.this,
                    "Error connecting to Server", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();

        }

		protected void onPostExecute(String content) {
			dialog.dismiss();
			displaySponsors("all");
		}

        private void parseExcel(InputStream fis){

            try{
                // Create a workbook using the Input Stream
                XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);

                // Get the first sheet from workbook
                XSSFSheet mySheet = myWorkBook.getSheetAt(0);

                // We now need something to iterate through the cells
                Iterator<Row> rowIter = mySheet.rowIterator();
                int emptyRow = 0;

                boolean flag = true;
                while(rowIter.hasNext()){

                    XSSFRow myRow = (XSSFRow) rowIter.next();
                    // Skip the first 2 rows
                    if(myRow.getRowNum() < 1) {
                        continue;
                    }

                    if (emptyRow > 10) {
                        break;
                    }

                    Sponsor sponsor = new Sponsor();

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

                        if (cellValue.isEmpty()) {
                            emptyRow++;
                            continue;
                        }

                        // Just some log information
                        //Log.v(LOG_TAG, cellValue);

                        // Push the parsed data in the Java Object
                        // Check for cell index
                        switch (myCell.getColumnIndex()) {
                            case 0:
                                sponsor.setSponsorName(cellValue);
                                break;
                            case 1:
                                sponsor.setCategory(cellValue);
                                break;

                            default:
                                break;
                        }
                    }

                    if (flag == false) {
                        break;
                    }

                    // Add object to list
                    sponsors.add(sponsor);
                }
            } catch (Exception e){
                e.printStackTrace();
            }

        }
    }

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// All

		if  (tab.getPosition() == 1) { // Platinum
			displaySponsors("diamond");
		}
		else if  (tab.getPosition() == 2) { // Platinum
			displaySponsors("emerald");
		} else if (tab.getPosition() == 3) {
            displaySponsors("ruby");
        } else if (tab.getPosition() == 4) { // Gold
            displaySponsors("platinum");
        } else if (tab.getPosition() == 5) { // Gold
			displaySponsors("gold");
		} else if (tab.getPosition() == 6) { // Silver
			displaySponsors("silver");
		} else if (tab.getPosition() == 7) { // Bronze
			displaySponsors("bronze");
		} else if (tab.getPosition() == 8) { // Crystal
			displaySponsors("brass");
		} else {
			displaySponsors("all");
		}
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
}
