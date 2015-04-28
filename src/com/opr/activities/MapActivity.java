package com.opr.activities;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.model.LatLng;
import com.opr.utils.Appconstants;
import com.opr.utils.GeoLocationDetailsJson;
import com.opr.utils.RoadPloatingUtil;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

public class MapActivity extends FragmentActivity implements
		OnMyLocationChangeListener {

	GoogleMap map = null;
	private LatLng currentLatLng = null;
	SharedPreferences preferences;
	String address = "";
	String propertyName = "";

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_map);
		SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.mapFragment);
		map = fragment.getMap();
		map.setMyLocationEnabled(true);
		map.setOnMyLocationChangeListener(this);

		getValues();

		new SearchPlacetask().execute(address);

	}

	private void getValues() {
		preferences = PreferenceManager
				.getDefaultSharedPreferences(MapActivity.this);
		propertyName = preferences.getString(Appconstants.PROPERTY_TITLE, "");
		address = preferences.getString(Appconstants.LOCATTION, "");
		Toast.makeText(getApplicationContext(),
				"Location : " + address + " Property Name : " + propertyName,
				Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onMyLocationChange(Location arg0) {
		// TODO Auto-generated method stub
		currentLatLng = new LatLng(arg0.getLatitude(), arg0.getLongitude());
	}

	public class SearchPlacetask extends AsyncTask<String, Void, Location> {
		ProgressDialog dialog = new ProgressDialog(MapActivity.this);

		@Override
		protected Location doInBackground(String... params) {
			Location latitudeLongitude = new GeoLocationDetailsJson()
					.getLatitudeLongitude(params[0]);
			Log.d("LOC", latitudeLongitude.toString());
			return latitudeLongitude;
		}

		@Override
		protected void onPreExecute() {
			dialog.setMessage("Searching....!");
			dialog.show();
		}

		@Override
		protected void onPostExecute(Location result) {

			drawPath(result);
		}

		private void drawPath(Location location) {
			LatLng currentposition = new LatLng(location.getLatitude(),
					location.getLongitude());
			if (currentLatLng != null) {
				new RoadPloatingUtil(map, getApplicationContext(),
						currentLatLng, currentposition, propertyName, dialog);
			}
		}

	}

}
