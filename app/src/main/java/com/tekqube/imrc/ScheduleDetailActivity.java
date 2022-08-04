package com.tekqube.imrc;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tekqube.people.attendee.MainActivity;
import com.tekqube.utils.Util;

import org.w3c.dom.Text;

import static android.support.design.widget.Snackbar.LENGTH_INDEFINITE;
import static com.tekqube.imrc.R.drawable.favorite;
import static com.tekqube.imrc.R.drawable.favoriteselected;

public class ScheduleDetailActivity extends Activity implements OnTouchListener {

    private static final String TAG = "Touch";
    @SuppressWarnings("unused")
    private static final float MIN_ZOOM = 1f,MAX_ZOOM = 1f;

    // These matrices will be used to scale points of the image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    // The 3 states (events) which the user is trying to perform
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // these PointF objects are used to record the point(s) the user is touching
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Activity activity = this;

		setContentView(R.layout.schedule_detail);

		ActionBar actionBar = this.getActionBar();

		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#a00000")));
		
		// get intent data
		Intent i = getIntent();
		
		final Schedule schedule = (Schedule) getIntent().getSerializableExtra("schedule");

		TextView sessionNameView = (TextView) findViewById(R.id.sessionName);
		sessionNameView.setText(schedule.getSession());

		String day = "Saturday";
		if (schedule.getDay() == 2) {
			day = "Sunday";
		} else if (schedule.getDay() == 3) {
			day = "Monday";
		} else if (schedule.getDay() == 4) {
			day = "Tuesday";
		}
		
		TextView timeView = (TextView) findViewById(R.id.timeView);
		timeView.setText(day + ", " + schedule.getTime());
		
		TextView venueView = (TextView) findViewById(R.id.venueView);
		venueView.setText(schedule.getRoom());

        ImageView imageView = (ImageView) findViewById(R.id.map);
        imageView.setOnTouchListener(this);
		if (schedule.getImageName() != null && schedule.getImageName() != "0.0") {
            String name = schedule.getImageName();
            try {
                int id = getResources().getIdentifier(name, "drawable", getPackageName());
                Drawable drawable = getResources().getDrawable(id);
                imageView.setBackground(drawable);
            }catch (Exception e) {
                System.out.print(e);
            }
        }

		TextView volunteerView = (TextView) findViewById(R.id.volunteerView);
		volunteerView.setText(schedule.getCoordinators());

		TextView volunteerPhone = (TextView) findViewById(R.id.volunteerPhone);
		volunteerPhone.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + schedule.getVolunteerPhone()));
				if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
					startActivity(i);
					return;
				}

				if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CALL_PHONE)) {
					Log.i("ScheduleVolunteer",
							"Displaying camera permission rationale to provide additional context.");
					Snackbar.make(findViewById(R.id.desc), getString(R.string.call_phone_permission),
							LENGTH_INDEFINITE)
							.setAction("Ok", new View.OnClickListener() {
								@Override
								public void onClick(View view) {
									ActivityCompat.requestPermissions(ScheduleDetailActivity.this,
											new String[]{Manifest.permission.CALL_PHONE}, 1);
								}
							})
							.show();
				}
			}
		});

		TextView descriptionView = (TextView) findViewById(R.id.desc);
		descriptionView.setText(schedule.getBriefDescription().equals("0.0") ? "N/A" : schedule.getBriefDescription());
		descriptionView.setMovementMethod(new ScrollingMovementMethod());


        ImageView starButton = (ImageButton) findViewById(R.id.starButton);
        String key = schedule.getSession()+"-"+ schedule.getTargetGroup() + "-" + schedule.getDay();
        if (Util.getFavorites(activity, key) != null) {
            starButton.setImageResource(favoriteselected);
        } else {
            starButton.setImageResource(favorite);
        }

        starButton.setTag(0);
        starButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (int) v.getTag();
                ImageButton button = (ImageButton) v.findViewById(R.id.starButton);
                System.out.print(" View Tag :"+tag);

                String key = schedule.getSession()+"-"+ schedule.getTargetGroup() + "-" + schedule.getDay();
                if (Util.getFavorites(activity, key) == null) {
                    Util.setFavorite(activity, key, schedule);
                    button.setImageResource(favoriteselected);
                } else {
                    Util.setFavorite(activity, key, null);
                    button.setImageResource(favorite);
                }
            }
        });
		
//		ImageView imageView = (ImageView) findViewById(R.id.targetGroupImage);
//		if (schedule.getTargetGroup() != null ) {
//			System.out.println(" Target Group : "+schedule.getTargetGroup().toLowerCase());
//			if (schedule.getTargetGroup().toLowerCase().equals("session")) {
//				imageView.setImageResource(R.drawable.sessions);
//			} else if (schedule.getTargetGroup().toLowerCase().equals("rays")) {
//				imageView.setImageResource(R.drawable.rays);
//			} else if (schedule.getTargetGroup().toLowerCase().equals("sakhi")) {
//				imageView.setImageResource(R.drawable.sakhi);
//			} else if (schedule.getTargetGroup().toLowerCase().equals("seniors")) {
//				imageView.setImageResource(R.drawable.seniors);
//			} else if (schedule.getTargetGroup().toLowerCase().equals("entertainment")) {
//				imageView.setImageResource(R.drawable.entertainment);
//			} else if (schedule.getTargetGroup().toLowerCase().equals("food")) {
//				imageView.setImageResource(R.drawable.food);
//			} else if (schedule.getTargetGroup().toLowerCase().equals("kids")) {
//				imageView.setImageResource(R.drawable.kids);
//			} else {
//
//				imageView.setImageResource(R.drawable.general);
//			}
//		}
//
//		TextView coordinatorsView = (TextView) findViewById(R.id.coordinators);
//		coordinatorsView.setText(schedule.getCoordinators().equals("0.0") ? "N/A" : schedule.getCoordinators());
//
//		TextView panelistView = (TextView) findViewById(R.id.panelists);
//		panelistView.setText(schedule.getPanel().equals("0.0") ? "N/A" : schedule.getPanel());
			
	}

	// Create an anonymous implementation of OnClickListener

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        ImageView view = (ImageView) v;
        view.setScaleType(ImageView.ScaleType.MATRIX);
        float scale;

        dumpEvent(event);

        // Handle touch events here...

        switch (event.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:   // first finger down only
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                Log.d(TAG, "mode=DRAG"); // write to LogCat
                mode = DRAG;
                break;

            case MotionEvent.ACTION_UP: // first finger lifted

            case MotionEvent.ACTION_POINTER_UP: // second finger lifted

                mode = NONE;
                Log.d(TAG, "mode=NONE");
                break;

            case MotionEvent.ACTION_POINTER_DOWN: // first and second finger down

                oldDist = spacing(event);
                Log.d(TAG, "oldDist=" + oldDist);
                if (oldDist > 5f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                    Log.d(TAG, "mode=ZOOM");
                }
                break;

            case MotionEvent.ACTION_MOVE:

                if (mode == DRAG)
                {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y); // create the transformation in the matrix  of points
                }
                else if (mode == ZOOM)
                {
                    // pinch zooming
                    float newDist = spacing(event);
                    Log.d(TAG, "newDist=" + newDist);
                    if (newDist > 5f)
                    {
                        matrix.set(savedMatrix);
                        scale = newDist / oldDist; // setting the scaling of the
                        // matrix...if scale > 1 means
                        // zoom in...if scale < 1 means
                        // zoom out
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;
        }

        view.setImageMatrix(matrix); // display the transformation on screen

        return true; // indicate event was handled
    }

    /*
     * --------------------------------------------------------------------------
     * Method: spacing Parameters: MotionEvent Returns: float Description:
     * checks the spacing between the two fingers on touch
     * ----------------------------------------------------
     */

    private float spacing(MotionEvent event)
    {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /*
     * --------------------------------------------------------------------------
     * Method: midPoint Parameters: PointF object, MotionEvent Returns: void
     * Description: calculates the midpoint between the two fingers
     * ------------------------------------------------------------
     */

    private void midPoint(PointF point, MotionEvent event)
    {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /** Show an event in the LogCat view, for debugging */
    private void dumpEvent(MotionEvent event)
    {
        String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE","POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);

        if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP)
        {
            sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")");
        }

        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++)
        {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";");
        }

        sb.append("]");
        Log.d("Touch Events ---------", sb.toString());
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
