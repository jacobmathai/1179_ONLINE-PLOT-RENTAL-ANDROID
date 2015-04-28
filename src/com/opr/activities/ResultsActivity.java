package com.opr.activities;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.opr.adapters.PropertyAdapter;
import com.opr.beans.Propertybean;
import com.opr.utils.Appconstants;
import com.opr.utils.URLDownloadUtil;

public class ResultsActivity extends ListActivity {
	String result = "";
	ListView propertyListView = null;
	ArrayList<Propertybean> list = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		result = getIntent().getExtras().getString(Appconstants.RESULT);
		propertyListView = getListView();
		propertyListView.setDividerHeight(5);
		list = getvalues(result);
		if (list.isEmpty()) {
			Toast.makeText(getApplicationContext(), "Sorry no results!",
					Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(ResultsActivity.this, NoResultsActivity.class);
			startActivity(intent);
			finish();
		}

		PropertyAdapter propertyAdapter = new PropertyAdapter(
				ResultsActivity.this, list);
		propertyListView.setAdapter(propertyAdapter);
		propertyListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {

				Object object = arg0.getItemAtPosition(pos);
				if (object instanceof Propertybean) {
					Propertybean bean = (Propertybean) object;
					String propId = bean.getPropId();
					Toast.makeText(getApplicationContext(),
							"Property " + propId + " clicked",
							Toast.LENGTH_SHORT).show();
					new DetailsTask().execute(propId);
				}

			}
		});

	}

	private ArrayList<Propertybean> getvalues(String result) {
		ArrayList<Propertybean> list = new ArrayList<Propertybean>();
		try {
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				Propertybean bean = new Propertybean();
				JSONObject object = new JSONObject(array.getString(i));
				bean.setPropertyType(object.getString("prop_type"));
				Toast.makeText(getApplicationContext(),
						object.getString("prop_type"), Toast.LENGTH_LONG)
						.show();
				bean.setPropTitle(object.getString("prop_title"));
				bean.setPrice(object.getString("price"));
				bean.setBedrooms(object.getString("bedrooms"));
				bean.setPropId(object.getString("prop_id"));
				bean.setImage(object.getString("image"));
				list.add(bean);

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}

	public class DetailsTask extends AsyncTask<String, String, String> {
		ProgressDialog dialog = new ProgressDialog(ResultsActivity.this);
		String url = "";

		@Override
		protected String doInBackground(String... params) {

			try {
				StringBuilder builder = new StringBuilder();
				builder.append(Appconstants.DETAILS_SERVLET);
				builder.append(params[0]);
				url = URLDownloadUtil.downloadUrl(builder.toString(),
						getApplicationContext());

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return url;
		}

		@Override
		protected void onPreExecute() {
			dialog.setMessage("loading.....");
			dialog.show();
		}

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			Log.d("TAG", "details : " + result);
			Intent intent = new Intent(ResultsActivity.this,
					DetailsActivity.class);
			intent.putExtra("data", result);
			startActivity(intent);
		}

	}

}
