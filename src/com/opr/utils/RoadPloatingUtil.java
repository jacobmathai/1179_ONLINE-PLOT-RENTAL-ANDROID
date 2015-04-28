package com.opr.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class RoadPloatingUtil {

	private GoogleMap map;
	private Context context;
	ArrayList<LatLng> points = null;
	private String destinName;
	private ProgressDialog dialog;

	public RoadPloatingUtil(GoogleMap map, Context context, LatLng orgin,
			LatLng dest, String destinName, ProgressDialog dialog) {

		this.map = map;
		this.context = context;
		this.destinName = destinName;
		this.dialog = dialog;

		String directionsUrl = getDirectionsUrl(orgin, dest);
		// call asynchTask
		new DownloadTask().execute(directionsUrl);
	}

	private String getDirectionsUrl(LatLng origin, LatLng dest) {

		// Origin of route
		String str_origin = "origin=" + origin.latitude + ","
				+ origin.longitude;

		// Destination of route
		String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

		// Sensor enabled
		String sensor = "sensor=false";

		// Building the parameters to the web service
		String parameters = str_origin + "&" + str_dest + "&" + sensor;

		// Output format
		String output = "json";

		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + parameters;
		return url;
	}

	private String downloadUrl(String strUrl) throws IOException {

		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());

		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	public class DownloadTask extends AsyncTask<String, Void, String> {

		// Downloading data in non-ui thread
		@Override
		protected String doInBackground(String... url) {

			// For storing data from web service
			String data = "";

			try {
				// Fetching the data from web service
				data = downloadUrl(url[0]);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		// Executes in UI thread, after the execution of
		// doInBackground()
		@Override
		protected void onPostExecute(String result) {

			ParserTask parserTask = new ParserTask();

			// Invokes the thread for parsing the JSON data
			parserTask.execute(result);

		}

		@Override
		protected void onPreExecute() {

			// TODO Auto-generated method stub

		}
	}

	private class ParserTask extends
			AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

		public static final String FIRST_TIME_ENTRY = "FIRST_TIME_ENTRY";

		@Override
		protected void onPreExecute() {

			// TODO Auto-generated method stub

		}

		// Parsing the data in non-ui thread
		@Override
		protected List<List<HashMap<String, String>>> doInBackground(
				String... jsonData) {

			JSONObject jObject;
			List<List<HashMap<String, String>>> routes = null;

			try {
				jObject = new JSONObject(jsonData[0]);
				DirectionsJSONParser parser = new DirectionsJSONParser();

				// Starts parsing data
				routes = parser.parse(jObject);
			} catch (Exception e) {
				e.printStackTrace();

			}
			return routes;
		}

		// Executes in UI thread, after the parsing
		// process======================================
		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> result) {

			PolylineOptions lineOptions = null;
			// Traversing through all the routes
			for (int i = 0; i < result.size(); i++) {
				points = new ArrayList<LatLng>();
				lineOptions = new PolylineOptions();

				// Fetching i-th route
				List<HashMap<String, String>> path = result.get(i);

				// Fetching all the points in i-th route
				for (int j = 0; j < path.size(); j++) {
					HashMap<String, String> point = path.get(j);
					if (j == 0) { // Get distance from the list
						// distance = point.get("distance");

						/*
						 * if (!distance.equals("")) { String[] kilo_meter =
						 * distance.split("k"); //
						 * distanceCalc.add(Float.parseFloat(kilo_meter[0]));
						 * Toast.makeText(getApplicationContext(), "" +
						 * kilo_meter[0], Toast.LENGTH_SHORT) .show(); }
						 */
						continue;
					} else if (j == 1) { // Get duration from the list
						// duration = point.get("duration");
						// continue;
					}
					try {
						double lat = Double.parseDouble(point.get("lat"));
						double lng = Double.parseDouble(point.get("lng"));
						LatLng position = new LatLng(lat, lng);
						points.add(position);
					} catch (NullPointerException e) {

					}

				}

				// Adding all the points in the route to LineOptions
				lineOptions.addAll(points);
				lineOptions.width(8);
				lineOptions.geodesic(true);
				lineOptions.color(Color.RED);

			}
			// Drawing polyline in the Google Map for the i-th route
			try {
				map.addPolyline(lineOptions);
				RoadPloatingUtil.this.dialog.dismiss();

				map.addMarker(new MarkerOptions()
						.position(points.get(points.size() - 1))
						.title(destinName)
						.icon(BitmapDescriptorFactory
								.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
				PreferenceManager.getDefaultSharedPreferences(context).edit()
						.putBoolean(FIRST_TIME_ENTRY, false).commit();

			} catch (NullPointerException e) {

			}

		}
	}
}
