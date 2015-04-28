
package com.opr.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;

public class GeoLocationDetailsJson {

	JSONObject jsonObject = null;

	/**
	 * Getting current latitude and longitude
	 * 
	 * @param address
	 *            Enter your currnt location ex: Ernakulam
	 * @return it will return current latitude longitude else null
	 */
	public Location getLatitudeLongitude(String address) {

		// TODO Auto-generated method stub
		Location location = null;
		String replace = address.replace(" ", "");
		JSONObject jsonObject = getJson(replace.trim());

		try {
			double lng = ((JSONArray) jsonObject.get("results"))
					.getJSONObject(0).getJSONObject("geometry")
					.getJSONObject("location").getDouble("lng");

			double lat = ((JSONArray) jsonObject.get("results"))
					.getJSONObject(0).getJSONObject("geometry")
					.getJSONObject("location").getDouble("lat");
			location = new Location("");
			location.setLatitude(lat);
			location.setLongitude(lng);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			e.printStackTrace();

			return null;
		}
		return location;
	}

	/**
	 * Getting address
	 * 
	 * @param address
	 *            Enter latitude longitude include comma ex: 9.1225,7.1253
	 * @return it will return the address based on the latitude longitude values
	 */
	public String getAddress(String address) {

		JSONObject json = getJson(address);
		String locationName = null;
		try {
			JSONArray jsonArray = (JSONArray) json.get("results");

			locationName = jsonArray.getJSONObject(0).getString(
					"formatted_address");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return locationName;

	}

	private JSONObject getJson(String address) {

		// TODO Auto-generated method stub
		String API = "http://maps.googleapis.com/maps/api/geocode/json?address="
				+ address + "&sensor=true;";

		URL url;
		try {
			url = new URL(API);

			URLConnection openConnection = url.openConnection();
			openConnection.connect();
			InputStream inputStream = openConnection.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String line = "";
			StringBuilder builder = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			return new JSONObject(builder.toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
