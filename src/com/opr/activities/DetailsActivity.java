package com.opr.activities;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.opr.services.WebServiceManager;
import com.opr.utils.Appconstants;
import com.opr.utils.URLDownloadUtil;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends Activity implements OnClickListener {
	TextView typetexTextView = null;
	TextView priceTextView = null;
	TextView propNameTextView = null;
	TextView addressteTextView = null;
	TextView ownertTextView = null;
	TextView areatTextView = null;
	TextView bedroomstTextView = null;
	TextView bathroomstTextView = null;
	TextView contractTextView = null;
	TextView propIdtTextView = null;
	ImageView propImageView = null;
	Button bookButton = null;
	Button cancelbButton = null;
	Button videoButton = null;
	Button mapbButton = null;
	SharedPreferences preferences = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_deatils);
		super.onCreate(savedInstanceState);
		String data = getIntent().getExtras().getString("data");
		setComponents();
		getvalues(data);
	}

	private void setComponents() {
		
		typetexTextView = (TextView) findViewById(R.id.propTypetextView);
		priceTextView = (TextView) findViewById(R.id.propPricetextView);
		propNameTextView = (TextView) findViewById(R.id.propNameTextView);
		addressteTextView = (TextView) findViewById(R.id.addressTextView);
		propImageView = (ImageView) findViewById(R.id.propImageView);
		ownertTextView = (TextView) findViewById(R.id.propOwnerTextView);
		areatTextView = (TextView) findViewById(R.id.propAreaTextView);
		bedroomstTextView = (TextView) findViewById(R.id.bedroomsTextView);
		bathroomstTextView = (TextView) findViewById(R.id.bathroomsTextView);
		contractTextView = (TextView) findViewById(R.id.contractTextView);
		propIdtTextView = (TextView) findViewById(R.id.propIdTextView);
		bookButton = (Button) findViewById(R.id.bookingButton);
		cancelbButton = (Button) findViewById(R.id.searchButton);
		videoButton = (Button) findViewById(R.id.videoButton);
		mapbButton = (Button) findViewById(R.id.mapButton);
		
		
		
		bookButton.setOnClickListener(this);
		cancelbButton.setOnClickListener(this);
		videoButton.setOnClickListener(this);
		mapbButton.setOnClickListener(this);

	}

	private void getvalues(String data) {
		try {
			JSONObject object = new JSONObject(data);
			typetexTextView.setText(object.getString("prop_type"));
			propIdtTextView.setText(object.getString("prop_id"));
			priceTextView.setText(object.getString("price"));
			propNameTextView.setText(object.getString("prop_title"));
			addressteTextView.setText(object.getString("address"));
			areatTextView.setText(object.getString("area"));
			ownertTextView.setText(object.getString("owner_name"));
			bedroomstTextView.setText(object.getString("bedrooms"));
			bathroomstTextView.setText(object.getString("bathrooms"));
			contractTextView.setText(object.getString("contract_type"));
			StringBuilder builder = new StringBuilder();
			builder.append(Appconstants.IMAGE_FOLDER);
			builder.append(object.getString("image"));
			Picasso.with(getApplicationContext()).load(builder.toString())
					.placeholder(R.drawable.kfm_home).into(propImageView);
			preferences = PreferenceManager.getDefaultSharedPreferences(DetailsActivity.this);
			Editor edit = preferences.edit();
			edit.putString(Appconstants.PROPERTY_TITLE, object.getString("prop_title"));
			edit.putString(Appconstants.LOCATTION, object.getString("location"));
			edit.commit();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == bookButton.getId()) {
			preferences = PreferenceManager
					.getDefaultSharedPreferences(DetailsActivity.this);
			String username = preferences.getString(Appconstants.USERNAME, "");
			String propid = (String) propIdtTextView.getText();
			Toast.makeText(getApplicationContext(),
					username + " " + propIdtTextView.getText(),
					Toast.LENGTH_SHORT).show();

			new Bookingtask().execute(username, propid);

		} else if (v.getId() == cancelbButton.getId()) {
			finish();
		} else if (v.getId() == videoButton.getId()) {
			Intent intent = new Intent(DetailsActivity.this, VideoViewActivity.class);
			intent.putExtra(Appconstants.PROPERTY_ID, propIdtTextView.getText().toString());
			startActivity(intent);

		} else if (v.getId() == mapbButton.getId()) {
			Intent intent = new Intent(DetailsActivity.this, MapActivity.class);
			startActivity(intent);

		}

	}

	public class VideoTask extends AsyncTask<String, String, String> {
		ProgressDialog dialog = new ProgressDialog(getApplicationContext());
		private String url;

		@Override
		protected String doInBackground(String... params) {
			try {
				url = URLDownloadUtil.downloadUrl(Appconstants.VIDEO_SERVLET,
						getApplicationContext());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return url;
		}

		@Override
		protected void onPreExecute() {
			dialog.setMessage("Loading.....!");
			dialog.show();
		}

		@Override
		protected void onPostExecute(String result) {

			dialog.dismiss();
			Toast.makeText(getApplicationContext(), "video", Toast.LENGTH_SHORT)
					.show();
		}

	}

	public class Bookingtask extends AsyncTask<String, String, String> {
		ProgressDialog dialog = new ProgressDialog(DetailsActivity.this);

		@Override
		protected String doInBackground(String... params) {
			WebServiceManager manager = new WebServiceManager();
			String response = manager.bookproperty(params[0], params[1]);
			return response;
		}

		@Override
		protected void onPreExecute() {
			dialog.setMessage("Loading.....!");
			dialog.show();
		}

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			if (!result.equals("error")) {
				if (result.equals("true")) {
					Toast.makeText(getApplicationContext(), "Property Booked",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), "Booking Failed!",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getApplicationContext(), "Network Error!",
						Toast.LENGTH_SHORT).show();
			}

		}
	}

}
