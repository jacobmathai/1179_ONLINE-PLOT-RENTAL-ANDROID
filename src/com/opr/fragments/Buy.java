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
import com.opr.utils.AppUtils;
import com.opr.utils.Appconstants;
import com.opr.utils.URLDownloadUtil;

public class Buy extends Fragment implements OnClickListener {

	Button search = null;
	AutoCompleteTextView city = null;
	CheckBox houseCheckBox = null;
	CheckBox appartmentCheckBox = null;
	CheckBox villaCheckBox = null;
	CheckBox bungalowCheckBox = null;
	CheckBox officeCheckBox = null;
	EditText amount = null;
	EditText bedrooms = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View buy = inflater.inflate(R.layout.buy, container, false);
		setcomponents(buy);
		return buy;
	}

	private void setcomponents(View buy) {

		search = (Button) buy.findViewById(R.id.searchButton);
		city = (AutoCompleteTextView) buy.findViewById(R.id.cityeditText1);
		city.setAdapter(new PlaceAdaptor(getActivity(),
				android.R.layout.simple_list_item_1));
		houseCheckBox = (CheckBox) buy.findViewById(R.id.housecheckBox);
		appartmentCheckBox = (CheckBox) buy
				.findViewById(R.id.appartmentcheckBox);
		villaCheckBox = (CheckBox) buy.findViewById(R.id.villacheckBox);
		bungalowCheckBox = (CheckBox) buy.findViewById(R.id.bungalowcheckBox);
		amount = (EditText) buy.findViewById(R.id.amounteditText);
		bedrooms = (EditText) buy.findViewById(R.id.bedroomeditText);
		officeCheckBox = (CheckBox) buy.findViewById(R.id.officecheckBox);
		search.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		if (v.getId() == search.getId()) {
			writeJSON();
		}

	}

	private void writeJSON() {
		JSONObject object = new JSONObject();
		try {
			if (city.getText().toString().equals("")) {
				object.put(Appconstants.CITY, "Any");
			} else {
				object.put(Appconstants.CITY, city.getText().toString());
			}
			
			if (amount.getText().toString().equals("")) {
				object.put(Appconstants.MAX_AMOUNT, "Any");
			} else {
				object.put(Appconstants.MAX_AMOUNT, amount.getText().toString());
			}

			if (bedrooms.getText().toString().equals("")) {
				object.put(Appconstants.BEDROOMS, "Any");
			} else {
				object.put(Appconstants.BEDROOMS, bedrooms.getText().toString());
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
			object.put(Appconstants.CONTRACT_TYPE, Appconstants.SALE);
			object.put(AppUtils.TYPE, AppUtils.BUILDING);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String data = object.toString();
		Log.d("TAG", "property_data:" + data);
		new Searchtask().execute(data);

	}

	
		

	public class Searchtask extends AsyncTask<String, String, String> {
		ProgressDialog dialog = new ProgressDialog(getActivity());
		StringBuilder type = new StringBuilder();

		@Override
		protected String doInBackground(String... params) {
			// WebServiceManager manager = new WebServiceManager();
			// String result = manager.SearchForProperty(params[0]);
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
			Log.d("TAG", "result: " + result);
			Intent intent = new Intent(getActivity(), ResultsActivity.class);
			intent.putExtra(Appconstants.RESULT, result);
			startActivity(intent);
		}

	}
	

}
