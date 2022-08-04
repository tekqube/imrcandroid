package com.tekqube.utils;

import android.app.Activity;
import android.content.Context;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tekqube.Constants;
import com.tekqube.food.Food;
import com.tekqube.imrc.Schedule;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.util.Log;

public class Util {

    public static Map getScheduleInfos(Activity activity) {

        SharedPreferences pref = activity.getSharedPreferences("scheduleInfos", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = pref.edit();
        Gson gson = new Gson();
        String json = pref.getString("scheduleJson", "");
        Type stringStringMap = new TypeToken<Map<Integer, List<Schedule>>>(){}.getType();
        Map<Integer, List<Schedule>> scheduleMap = gson.fromJson(json, stringStringMap);

        return scheduleMap;
    }

    public static void setScheduleInfos(Activity activity, Map<Integer, List<Schedule>> scheduleMap) {
        SharedPreferences pref = activity.getSharedPreferences("scheduleInfos", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = pref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(scheduleMap);
        ed.putString("scheduleJson", json);
        ed.commit();
    }

    private static void resetScheduleInfos(Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences("scheduleInfos", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = pref.edit();
        ed.remove("scheduleJson");
        ed.commit();
    }

	// Check to see if schedule file can be downloaded now or not..
    public static boolean canDownloadScheduleFile(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("imrc.app", Context.MODE_PRIVATE);
        String dateStart = sharedPreferences.getString("scheduleFile", "0");

        //HH converts hour in 24 hours format (0-23), day calculation
        SimpleDateFormat format = new SimpleDateFormat("HH");

        if (dateStart.equals("0")) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            dateStart = dateFormat.format(new Date());
            //HH converts hour in 24 hours format (0-23), day calculation
            System.out.println("Schedule File Download DATE Saved :"+dateStart);
            activity.getSharedPreferences("imrc.app", Context.MODE_PRIVATE).edit()
                    .putString("scheduleFile", dateStart).commit();

            return true;
        }

        String d1 = format.format(new Date(dateStart));
        String d2 = format.format(new Date());

       if (!d1.equals(d2)) {
           return true;
       }

        return false;
    }

	public static void setRandomVoteString(Activity activity, String id) {
		SharedPreferences sharedPreferences = activity.getSharedPreferences("imrc.app", Context.MODE_PRIVATE);
		activity.getSharedPreferences("imrc.app", Context.MODE_PRIVATE).edit()
				.putString("voteId", id).commit();
	}

	public static String getRandomString(Activity activity) {
		SharedPreferences sharedPreferences = activity.getSharedPreferences("imrc.app", Context.MODE_PRIVATE);
		return sharedPreferences.getString("voteId", "0");
	}

	public static int getChoiceInfo(Activity activity, int qid) {
		SharedPreferences pref = activity.getSharedPreferences("choice", Context.MODE_PRIVATE);
		Map map = pref.getAll();
		Object val = map.get(String.valueOf(qid));
		if (val == null) {
			return -1;
		}
		return (int) val;
	}

	public static void setChoiceInfo(Activity activity, int qid, int choice) {
        SharedPreferences pref = activity.getSharedPreferences("choice", Context.MODE_PRIVATE);
		SharedPreferences.Editor ed = pref.edit();
		ed.putInt(String.valueOf(qid), choice);
		ed.commit();
	}

	public static Schedule getFavorites(Activity activity, String key) {
		SharedPreferences pref = activity.getSharedPreferences("choice", Context.MODE_PRIVATE);
		SharedPreferences.Editor ed = pref.edit();
		Gson gson = new Gson();
		String json = pref.getString(key, "");
		Schedule schedule = gson.fromJson(json, Schedule.class);
		return schedule;
	}

	public static void setFavorite(Activity activity, String key, Schedule schedule) {
		SharedPreferences pref = activity.getSharedPreferences("choice", Context.MODE_PRIVATE);
		SharedPreferences.Editor ed = pref.edit();
		Gson gson = new Gson();
		String json = gson.toJson(schedule);
		ed.putString(key, json);
		ed.commit();
	}

	public static List<Schedule> getAllFavorites(Activity activity) {
		SharedPreferences pref = activity.getSharedPreferences("choice", Context.MODE_PRIVATE);
		List<Schedule> schedules = new ArrayList<>();
        Gson gson = new Gson();
        for (Object entry : pref.getAll().values()) {
            if (entry != null || !entry.equals("null")) {
                try {
                    schedules.add(gson.fromJson(String.valueOf(entry), Schedule.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

		return schedules;
	}

	// Check to see if schedule file can be downloaded now or not..
    public static boolean canDownloadOtherFiles(Activity activity, String fileType) {
		SharedPreferences sharedPreferences = activity.getSharedPreferences("imrc.app", Context.MODE_PRIVATE);
        String dateStart = sharedPreferences.getString(fileType, "0");

        //HH converts hour in 24 hours format (0-23), day calculation
        SimpleDateFormat format = new SimpleDateFormat("dd");

        if (dateStart.equals("0")) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            dateStart = dateFormat.format(new Date());
            //HH converts hour in 24 hours format (0-23), day calculation
            System.out.println("Other File Download DATE Saved :"+dateStart);

            activity.getSharedPreferences("imrc.app", Context.MODE_PRIVATE).edit()
                    .putString(fileType, dateStart).commit();

            return true;
        }

		String d1 = format.format((new Date(dateStart)));
		String d2 = format.format(new Date());

		if (!d1.equals(d2)) {
			return true;
		}

        return false;
    }

	// Parse Excel file for schedules...
	public static Map parseExcel(InputStream fis, int day){
        Map<Integer, List<Schedule>> scheduleMap = new HashMap<Integer, List<Schedule>>();
		try{
			// Create a workbook using the Input Stream
			XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);

			if (myWorkBook != null) {
				// Loop this for 4 days..
				int i = day;
				// Get the first sheet from workbook
				XSSFSheet mySheet = myWorkBook.getSheetAt(0);

				// We now need something to iterate through the cells
				Iterator<Row> rowIter = mySheet.rowIterator();
				String time = "";
				int emptyRows = 0;

				while(rowIter.hasNext()) {
					XSSFRow myRow = (XSSFRow) rowIter.next();
					Schedule schedule = new Schedule();
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

						if (myCell.getColumnIndex() == 0) {
							if (!cellValue.equals("0.0") && !cellValue.equals("")
									&& !cellValue.equals("day") && cellValue.length() > 0) {

								float cellVal = Float.valueOf(cellValue);
								schedule.setDay((int) cellVal);

							} else {
								break;
							}
						} else if (myCell.getColumnIndex() == 1) {
							schedule.setTime(cellValue);
						} else if (myCell.getColumnIndex() == 2) {
							schedule.setStart24Time(cellValue);
						} else if (myCell.getColumnIndex() == 3) {
							if (cellValue.equals("0.0") || cellValue.equals("")) {
								cellValue = "24.00";
							}

							schedule.setEnd24Time(cellValue);
						} else if (myCell.getColumnIndex() == 4) {
							schedule.setSession(cellValue);
						} else if (myCell.getColumnIndex() == 5) {
							schedule.setRoom(cellValue);
						} else if (myCell.getColumnIndex() == 6) {
							schedule.setBriefDescription(cellValue);
						} else if (myCell.getColumnIndex() == 7) {
							schedule.setTeamResponsibility(cellValue);
						} else if (myCell.getColumnIndex() == 8) {
							schedule.setCoordinators(cellValue);
						} else if (myCell.getColumnIndex() == 9) {
							schedule.setVolunteerPhone(cellValue);
						} else if (myCell.getColumnIndex() == 10) {
							schedule.setPanel(cellValue);
						} else if (myCell.getColumnIndex() == 11) {
							schedule.setTargetGroup(cellValue);
						}else if (myCell.getColumnIndex() == 12) {
							// by pass
						}else if (myCell.getColumnIndex() == 13) {
							// by pass
						} else if (myCell.getColumnIndex() == 14) {
							schedule.setSpeakerDescription(cellValue);
						} else {
							break;
						}
					}

					if (schedule.getSession() != null && schedule.getSession() != "" && !schedule.getSession().equals("0.0")) {
                        List<Schedule> schedules = scheduleMap.get(schedule.getDay());

                        if ((schedule.getEnd24Time() != null && !schedule.getEnd24Time().equals("0.0"))) {
                            if (schedules != null) {
                                schedules.add(schedule);
                            } else {
                                schedules = new ArrayList<Schedule>();
                            }
                            scheduleMap.put(schedule.getDay(), schedules);
						} else {
							schedule.setTime(time);
                            if (schedules != null) {
                                schedules.add(schedule);
                            } else {
                                schedules = new ArrayList<Schedule>();
                            }
                            scheduleMap.put(schedule.getDay(), schedules);
						}
					} else {
						if (schedule.getSession() == "" || schedule.getSession() == null || schedule.getSession().equals("0.0")) {
							emptyRows++;
							if (emptyRows >= 11) {
								break;
							}
						}
					}
				}
			} else {
				System.out.println(" Excel file is not available...");
				throw new Exception();
			}
		} catch (Exception e){
			e.printStackTrace(); 
		}

		return scheduleMap;
	}
	
	public static InputStream getScheduleTempFile(String url, Activity activity) throws IOException {
		InputStream inputStream = null;

		try {
			// Check if file exists on this url or not..
			if (!Util.canDownloadScheduleFile(activity)) {
				inputStream = activity.openFileInput(url);
			} else {
                Util.resetScheduleInfos(activity);
				return null;
			}
		} catch (Exception e) {
			Log.e("login activity", "Can not read file: " + e.toString());
		}
		return inputStream;
	}

    public static InputStream getFoodTempFile(String url, Activity activity) throws IOException {
        InputStream inputStream = null;

        try {
            // Check if file exists on this url or not..
            if (!Util.canDownloadOtherFiles(activity, "foodFile")) {
                inputStream = activity.openFileInput(url);
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        return inputStream;
    }

	@SuppressLint("LongLogTag")
	public static List<Food> downloadAndParseFoodExcelFile(Activity mActivity, int day, String... urls) throws IOException {
		List<Food> foods = new ArrayList<Food>();
		final int REGISTRATION_TIMEOUT = 3 * 1000;
		final int WAIT_TIMEOUT = 30 * 1000;
		final HttpClient httpclient = new DefaultHttpClient();
		final HttpParams params = httpclient.getParams();
		HttpResponse response;

		String fileName = Constants.foodMenuFileName + Constants.extensionName;
		String URL = urls[0];

		HttpConnectionParams.setConnectionTimeout(params, REGISTRATION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, WAIT_TIMEOUT);
		ConnManagerParams.setTimeout(params, WAIT_TIMEOUT);

		InputStream temp = Util.getFoodTempFile(fileName, mActivity);
		if (temp == null) {
			HttpGet httpGet = new HttpGet(URL);
			try {
				response = httpclient.execute(httpGet);
				InputStream copy = response.getEntity().getContent();

				StatusLine statusLine = response.getStatusLine();
				if(statusLine.getStatusCode() == HttpStatus.SC_OK) {
					BufferedWriter writer = null;
					try {
						FileOutputStream openFileOutput = mActivity.openFileOutput(fileName, Context.MODE_PRIVATE);
						byte[] bytes = IOUtils.toByteArray(copy);
						openFileOutput.write(bytes);

						SharedPreferences sharedPreferences = mActivity.getSharedPreferences("imrc.app", Context.MODE_PRIVATE);
						//HH converts hour in 24 hours format (0-23), day calculation
						String dateSaved = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss").format(new Date());
						System.out.println("Download Parse food excel file DATE Saved :"+dateSaved);
						sharedPreferences.edit().putString("foodFile", dateSaved).commit();
					} catch (Exception e) {
						Log.w(" Unable to download/Parse the file..", e.getMessage());
                        foods = Util.parseFoodExcel(mActivity.getAssets().open(fileName));
						//	throw new RuntimeException(e);
					} finally {
						if (writer != null) {
							try {
								writer.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}

                    foods = Util.parseFoodExcel(mActivity.openFileInput(fileName));
				} else {
                    foods = Util.parseFoodExcel(mActivity.openFileInput(fileName));
				}
			} catch (Exception e){
				e.printStackTrace();
                foods = Util.parseFoodExcel(mActivity.openFileInput(fileName));
			}
		} else {
            foods = Util.parseFoodExcel(temp);
		}
		return foods;
	}

    private static List<Food> parseFoodExcel(InputStream fis) {
        List<Food> foodList = new ArrayList<Food>();
        try{
            // Create a workbook using the Input Stream
            XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);

            if (myWorkBook != null) {
				for (int i = 0; i < 4; i++) {
					// Get the first sheet from workbook
					XSSFSheet mySheet = myWorkBook.getSheetAt(i);

					// We now need something to iterate through the cells
					Iterator<Row> rowIter = mySheet.rowIterator();
					String time = "";
					int emptyRows = 0;

					while(rowIter.hasNext()) {
						XSSFRow myRow = (XSSFRow) rowIter.next();
						// Skip the first 2 rows

						Food food = new Food();
						Iterator<Cell> cellIter = myRow.cellIterator();

						while(cellIter.hasNext()){
							XSSFCell myCell = (XSSFCell) cellIter.next();
							String cellValue = "";

							// Skip the first 2 rows
							if(myRow.getRowNum() < 2) {
								continue;
							}

							// Check for cell Type
							if(myCell.getCellType() == XSSFCell.CELL_TYPE_STRING){
								cellValue = myCell.getStringCellValue();
							} else {
								cellValue = String.valueOf(myCell.getNumericCellValue());
							}

							if (myCell.getColumnIndex() == 0) {
								// No.
							} else if (myCell.getColumnIndex() == 1) {
								// Time..
								food.setTime(cellValue);
							} else if (myCell.getColumnIndex() == 2) {
								// Type..
								food.setCategory(cellValue);
							} else if (myCell.getColumnIndex() == 3) {
								// Cuisine
								food.setCuisine(cellValue);
							} else if (myCell.getColumnIndex() == 4) {
								food.setMealName(cellValue);
							} else {
								break;
							}
						}

						if (emptyRows > 15) {
							System.out.println(" More than 10 rows available.. ");
							emptyRows = 0;
							break;
						}

						boolean addedToList = false;

						if (((food.getTime() != null && food.getCategory() != null) && (food.getTime() != "0.0" && food.getCategory() != "0.0"))) {
							// Food list is available
							food.setDay(i);
							foodList.add(food);
							addedToList = true;
						}

						if ((food.getCuisine() == null && food.getMealName() == null) || (food.getMealName() == "0.0" && food.getCuisine() == "0.0")) {
							emptyRows++;
							continue;
						} else {
							// Food list is available
							if (!addedToList) {
								food.setDay(i);
								foodList.add(food);
							}
						}
					}
				}
            } else {
                System.out.println(" Excel file is not available...");
                throw new Exception();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return foodList;
    }

	private static Map getScheduleMapFromFile(Activity mActivity, String fileName, int day) throws IOException {
		// Get the data from the FileInput as the file there is new...
		Map<Integer, List<Schedule>> scheduleMap = new HashMap<Integer, List<Schedule>>();

		try {
			FileInputStream is = mActivity.openFileInput(fileName);

			if (is != null) {
				scheduleMap = Util.parseExcel(is, day);
			}

			Log.i("ScheduleMapFromFile", "File found at the downloaded location..");
			Util.setScheduleInfos(mActivity, scheduleMap);
		} catch (FileNotFoundException e) {
			e.printStackTrace();

			// If file not found, check cache and then revert back to old one which came with app..
			scheduleMap = Util.getScheduleInfos(mActivity);

			if (scheduleMap == null || scheduleMap.size() == 0) {
				Log.i("ScheduleMapFromFile", "Cache not found revert to assets file..");
				InputStream is = null;
				is = mActivity.getAssets().open(fileName);
				scheduleMap = Util.parseExcel(is, day);

				Util.setScheduleInfos(mActivity, scheduleMap);
			} else {
				Log.i("ScheduleMapFromFile", "Cache found for the data..");
			}

		}

		return scheduleMap;
	}

    @SuppressLint("LongLogTag")
	public static Map downloadAndParseExcelFile(Activity mActivity, int day, String... urls) throws IOException {
        Map<Integer, List<Schedule>> scheduleMap = new HashMap<Integer, List<Schedule>>();
		final int REGISTRATION_TIMEOUT = 3 * 1000;
		final int WAIT_TIMEOUT = 30 * 1000;
		final HttpClient httpclient = new DefaultHttpClient();
		final HttpParams params = httpclient.getParams();
		HttpResponse response;
		
		String fileName = Constants.scheduleFileName + Constants.extensionName;
		String URL = urls[0];
		
		HttpConnectionParams.setConnectionTimeout(params, REGISTRATION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, WAIT_TIMEOUT);
		ConnManagerParams.setTimeout(params, WAIT_TIMEOUT);

		InputStream temp = Util.getScheduleTempFile(fileName, mActivity);
		if (temp == null) {
            System.out.print(" Temp file not Available.. Download new file..");
			HttpGet httpGet = new HttpGet(URL);
			try {
				response = httpclient.execute(httpGet);
				InputStream copy = response.getEntity().getContent();

				StatusLine statusLine = response.getStatusLine();
				if(statusLine.getStatusCode() == HttpStatus.SC_OK) {
					BufferedWriter writer = null;
					try {
						FileOutputStream openFileOutput = mActivity.openFileOutput(fileName, Context.MODE_PRIVATE);
						byte[] bytes = IOUtils.toByteArray(copy);
						openFileOutput.write(bytes);
						Log.i(" DownloadFile", "File write bytes..");
						
						SharedPreferences sharedPreferences = mActivity.getSharedPreferences("imrc.app", Context.MODE_PRIVATE);
						//HH converts hour in 24 hours format (0-23), day calculation
						String dateSaved = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date());
						System.out.println(" Download & Parse  Schedule excel DATE Saved :"+dateSaved);
						sharedPreferences.edit().putString("scheduleFile", dateSaved).commit();								
					} catch (Exception e) {
						Log.w(" Unable to download/Parse the file..", e.getMessage());
                        File f = new File(fileName);
                        FileInputStream fis = new FileInputStream(f);
                        scheduleMap = Util.parseExcel(fis, day);
						//	throw new RuntimeException(e);
					} finally {
						if (writer != null) {
							try {
								writer.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}

				scheduleMap = getScheduleMapFromFile(mActivity, fileName, day);
			} catch (Exception e){
				e.printStackTrace();
				scheduleMap = getScheduleMapFromFile(mActivity, fileName, day);
			}
		} else {
            System.out.print(" Temp file available... Use existing file");
			// Every time file is available get the info from cache and use it
            scheduleMap = Util.getScheduleInfos(mActivity);
            if (scheduleMap == null || scheduleMap.size() == 0) {
				scheduleMap = getScheduleMapFromFile(mActivity, fileName, day);
				Util.setScheduleInfos(mActivity, scheduleMap);
            }
        }

		return scheduleMap;
	}
}
