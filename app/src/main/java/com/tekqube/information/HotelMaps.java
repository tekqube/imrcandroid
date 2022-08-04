package com.tekqube.information;


import android.Manifest;
import android.app.Activity;

import android.content.pm.PackageManager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tekqube.imrc.R;

import static android.support.design.widget.Snackbar.LENGTH_INDEFINITE;

public class HotelMaps extends Activity
        implements OnMapReadyCallback {

	private View mLayout;
	Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		activity = this;
        setContentView(R.layout.map_layout);
		mLayout = findViewById(R.id.map_layout);

		try {
            // Loading map
            initilizeMap();

//            final Button phonebutton = (Button) findViewById(R.id.buttonCall);
//
//            phonebutton.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View v) {
//                    Intent callIntent = new Intent(Intent.ACTION_CALL);
//                    callIntent.setData(Uri.parse("tel:6167766450"));
//                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                        // TODO: Consider calling
//                        //    ActivityCompat#requestPermissions
//                        // here to request the missing permissions, and then overriding
//                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                        //                                          int[] grantResults)
//                        // to handle the case where the user grants the permission. See the documentation
//                        // for ActivityCompat#requestPermissions for more details.
//                        return;
//                    }
//                    startActivity(callIntent);
//
//
//				}
//			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		initilizeMap();
	}

	/**
	 * function to load map If map is not created it will create it for you
	 * */
	private void initilizeMap() {
		MapFragment mapFragment = (MapFragment) getFragmentManager()
					.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
	}

	@Override
	public void onMapReady(GoogleMap map) {
		LatLng santaClaraMarriot = new LatLng(37.390595, -121.973844);

		// Here, thisActivity is the current activity
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

			Log.i("Map",
					"Map Permission required..");
			ActivityCompat.requestPermissions(HotelMaps.this,
									new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

		} else {
			map.setMyLocationEnabled(true);
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(santaClaraMarriot, 13));

			map.addMarker(new MarkerOptions()
					.title("IMRC 2018")
					.snippet("IMRC 2018")
					.position(santaClaraMarriot));
		}
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
			Log.i("Maps ", "Maps Permission...");

			// Check if the only required permission has been granted
			if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				// Camera permission has been granted, preview can be displayed
				Log.i("HotelMaps Activity", "Maps has now been granted. Showing preview.");
                initilizeMap();
			} else {
				Log.i("HotelMaps Activity", "Maps has not been granted. Showing preview.");
			}
		} else {
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}

}
