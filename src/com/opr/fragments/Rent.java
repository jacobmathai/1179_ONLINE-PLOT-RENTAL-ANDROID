package com.opr.fragments;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.opr.activities.R;
import com.opr.activities.ResultsActivity;
import com.opr.adapters.PlaceAdaptor;
import com.opr.utils.Appconstants;
import com.opr.utils.URLDownloadUtil;

public class Rent extends Fragment implements OnClickListener {

	AutoCompleteTextView cityTextView = null;

	EditText amounteEditText = null;
	EditText bedroomsEditText = null;
	CheckBox houseCheckBox = null;
	CheckBox appartmentCheckBox = null;
	CheckBox villaCheckBox = null;
	CheckBox bungalowCheckBox = null;
	CheckBox officeCheckBox = null;
	Button searchButton = null;

	// RadioGroup propTypRadioGroup = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rent = inflater.inflate(R.layout.rent, container, false);
		setComponents(rent);
		return rent;
	}

	private void setComponents(View rent) {
		cityTextView = (AutoCompleteTextView) rent
				.findViewById(R.id.cityeditText1);
		cityTextView.setAdapter(new PlaceAdaptor(getActivity(),
				android.R.layout.simple_expandable_list_item_1));
		amounteEditText = (EditText) rent.findViewById(R.id.maxamounteditText);
		bedroomsEditText = (EditText) rent.findViewById(R.id.bedroomseditText);
		houseCheckBox = (CheckBox) rent.findViewById(R.id.housecheckBox);
		appartmentCheckBox = (CheckBox) rent
				.findViewById(R.id.appartmentcheckBox);
		villaCheckBox = (CheckBox) rent.findViewById(R.id.villacheckBox);
		bungalowCheckBox = (CheckBox) rent.findViewById(R.id.bungalowcheckBox);
		officeCheckBox = (CheckBox) rent.findViewById(R.id.officecheckBox);
		searchButton = (Button) rent.findViewById(R.id.searchButton);
		searchButton.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == searchButton.getId()) {

			writeJSON();
		}

	}

	private void writeJSON() {
		JSONObject object = new JSONObject();
		try {
			if (cityTextView.getText().toString().equals("")) {
				object.put(Appconstants.CITY, "Any");
			} else {
				object.put(Appconstants.CITY, cityTextView.getText().toString());
			}
			if (amounteEditText.getText().toString().equals("")) {
				object.put(Appconstants.MAX_AMOUNT, "Any");
			} else {
				object.put(Appconstants.MAX_AMOUNT, amounteEditText.getText()
						.toString());
			}
			if (bedroomsEditText.getText().toString().equals("")) {
				object.put(Appconstants.BEDROOMS, "Any");
			} else {
				object.put(Appconstants.BEDROOMS, bedroomsEditText.getText()
						.toString());
			}
			
			JSONArray array = new JSONArray();
			if (houseCheckBox.isChecked()) {
				array.put(Appconstants.HOUSE);
			}
			if (appartmentCheckBox.isChecked()) {
				array.put(Appconstants.APPARTMENT);
			}
			if (villaCheckBox.isChecked()) {
				array.put(Appconstants.VILLA);
			}
			if (bungalowCheckBox.isChecked()) {
				array.put(Appconstants.BUNGALOW);
			}
			if (officeCheckBox.isChecked()) {
				array.put(Appconstants.OFFICE);
			}
			if (array.length() == 0) {
				array.put("Any");
			}
			object.put(Appconstants.PROPERTY_TYPE, array);
			object.put(Appconstants.CONTRACT_TYPE, Appconstants.RENT);
			String data = object.toString();
			Log.d("TAG", data);
			new SearchPropertyTask().execute(data);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private class SearchPropertyTask extends AsyncTask<String, Void, String> {
		ProgressDialog dialog = new ProgressDialog(getActivity());

		@Override
		protected String doInBackground(String... params) {
			String url = "";

			try {
				StringBuilder builder = new StringBuilder();
				builder.append(Appconstants.SERVLET);
				builder.append(params[0]);
				url = URLDownloadUtil.downloadUrl(builder.toString(),
						getActivity().getApplicationContext());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return url;
		}

		@Override
		protected void onPreExecute() {
			dialog.setMessage("Searching......!");
			dialog.show();
		}

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			Log.d("TAG", "Property : " + result);
			Intent intent = new Intent(getActivity(), ResultsActivity.class);
			intent.putExtra(Appconstants.RESULT, result);
			startActivity(intent);
		}

	}

}
