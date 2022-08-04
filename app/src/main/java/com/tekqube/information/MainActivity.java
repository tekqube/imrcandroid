package com.tekqube.information;


import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tekqube.imrc.R;

public class MainActivity extends Activity implements OnMapReadyCallback {
	private GoogleMap mMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_layout);

		ActionBar actionBar = this.getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#a00000")));

		((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
		//mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);         

	}

	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		// Changing map type
		mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		// googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		// googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		// googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
		// googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);

		// Showing / hiding your current location
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}

		mMap.setMyLocationEnabled(true);

		// Enable / Disable zooming controls
		mMap.getUiSettings().setZoomControlsEnabled(false);

		// Enable / Disable my location button
		mMap.getUiSettings().setMyLocationButtonEnabled(true);

		// Enable / Disable Compass icon
		mMap.getUiSettings().setCompassEnabled(true);

		// Enable / Disable Rotate gesture
		mMap.getUiSettings().setRotateGesturesEnabled(true);

		// Enable / Disable zooming functionality
		mMap.getUiSettings().setZoomGesturesEnabled(true);

		final LatLng CIU = new LatLng(42.966801,  -85.672190);

		Marker ciu = mMap.addMarker(new MarkerOptions()
				.position(CIU).title("Amway Grand Plaza"));

	}
	
	@Override     
	public boolean onCreateOptionsMenu(Menu menu) {         
	// Inflate the menu; this adds items to the action bar if it is present.         
	getMenuInflater().inflate(R.menu.main, menu);         
	return true;     } 
	} 