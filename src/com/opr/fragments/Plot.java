package com.opr.fragments;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.opr.activities.PlotListActivity;
import com.opr.activities.R;
import com.opr.utils.AppUtils;
import com.opr.utils.Appconstants;
import com.opr.utils.URLDownloadUtil;

public class Plot extends Fragment implements OnClickListener, OnItemSelectedListener {
	private class SearchPlotTask extends AsyncTask<String, Void, String> {
		ProgressDialog dialog = new ProgressDialog(getActivity());

		@Override
		protected String doInBackground(String... params) {
			String url = "";
			StringBuilder builder = new StringBuilder();
			builder.append(Appconstants.PLOT_FETCH);
			builder.append(params[0]);
			try {
				url = URLDownloadUtil.downloadUrl(builder.toString(), getActivity()
						.getApplicationContext());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return url;
		}

		@Override
		protected void onPreExecute() {
			dialog.setMessage("Loading..!");
			dialog.show();
		}

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			Log.d("TAG", result);
			Intent intent = new Intent(getActivity(), PlotListActivity.class);
			intent.putExtra(AppUtils.PLOT_DETAILS, result);
			startActivity(intent);
		}

	}

	Button searchButton = null;
	Spinner landSpinner = null;
	Spinner contractSpinner = null;
	EditText amountEditText = null;
	EditText cityEditText = null;
	String[] landType = { "Select", "FARM_LAND", "ASSOCIATED_LAND", "NON_URBAN_LAND",
			"MULTY_STORY_BUILDINGS" };
	String[] contractType = { "Select", "For_Sale", "For_Rent" };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View plot = inflater.inflate(R.layout.plot, container, false);
		setcomponents(plot);
		return plot;
	}

	private void setcomponents(View plot) {
		landSpinner = (Spinner) plot.findViewById(R.id.landTypeSpinner);
		contractSpinner = (Spinner) plot.findViewById(R.id.contractTypeSpinner);
		amountEditText = (EditText) plot.findViewById(R.id.amountEditText);
		cityEditText = (EditText) plot.findViewById(R.id.cityEditText);
		ArrayAdapter<String> landAdapter = new ArrayAdapter<String>(
				getActivity().getApplicationContext(),
				android.R.layout.simple_spinner_dropdown_item, landType);
		ArrayAdapter<String> contractAdapter = new ArrayAdapter<String>(
				getActivity().getApplicationContext(),
				android.R.layout.simple_spinner_dropdown_item, contractType);
		landSpinner.setAdapter(landAdapter);
		contractSpinner.setAdapter(contractAdapter);
		landSpinner.setOnItemSelectedListener(this);
		contractSpinner.setOnItemSelectedListener(this);
		searchButton = (Button) plot.findViewById(R.id.searchPlot);
		searchButton.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == searchButton.getId()) {
			writeJSON();
		}
	}

	private void writeJSON() {
		JSONObject object = new JSONObject();
		try {
			JSONArray array = new JSONArray();
			if (landSpinner.getSelectedItem().toString().equals("Select")) {
				array.put("Any");
			} else {
				array.put(landSpinner.getSelectedItem().toString());
			}
				
			object.put(AppUtils.LAND_TYPE, array);
			if (contractSpinner.getSelectedItem().toString().equals("Select")) {
				object.put(AppUtils.COTRACT_TYPE, "Any");
			} else {
				object.put(AppUtils.COTRACT_TYPE, contractSpinner.getSelectedItem()
						.toString());
			}
			
			if (amountEditText.getText().toString().equals("")) {
				object.put(AppUtils.MAX_AMOUNT, "Any");
			} else {
				object.put(AppUtils.MAX_AMOUNT, amountEditText.getText().toString());
			}
			if (cityEditText.getText().equals("")) {
				object.put(AppUtils.CITY, "Any");
			} else {
				object.put(AppUtils.CITY, cityEditText.getText().toString());
			}
			
			object.put(AppUtils.TYPE, AppUtils.PLOT);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String data = object.toString();
		Log.d("TAG", "data : " + data);
		new SearchPlotTask().execute(data);

	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		 ((TextView)arg0.getChildAt(0)).setTextColor(Color.BLACK);
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

}
