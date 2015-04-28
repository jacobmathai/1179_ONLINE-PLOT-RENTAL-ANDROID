package com.opr.activities;

import com.opr.beans.PlotBean;
import com.opr.services.WebServiceManager;
import com.opr.utils.Appconstants;
import com.opr.utils.PlotUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PlotDetailsActivity extends Activity implements OnClickListener {

	private class BookPlotTask extends AsyncTask<String, Void, String> {
		ProgressDialog dialog = new ProgressDialog(PlotDetailsActivity.this);

		@Override
		protected String doInBackground(String... params) {
			WebServiceManager manager = new WebServiceManager();
			String response = manager.bookPlot(params[0], params[1]);
			return response;
		}

		@Override
		protected void onPreExecute() {
			dialog.setMessage("Loading....!");
			dialog.show();
		}

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			if (!result.equals("error")) {
				if (result.equals("true")) {
					Toast.makeText(getApplicationContext(),
							"Property Booked Successfully!", Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(getApplicationContext(), "Booking failed!",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getApplicationContext(), "Network Error!",
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	TextView plotTypeTextView = null;
	TextView plotIdTextView = null;
	TextView plotTitleTextView = null;
	TextView descriptionTextView = null;
	TextView ownerTextView = null;
	TextView addressTextView = null;
	TextView areaTextView = null;
	TextView amountTextView = null;
	Button bookPlotButton = null;
	Button cancelButton = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plotdetails);
		PlotBean bean =  (PlotBean) getIntent().getSerializableExtra(
				PlotUtils.PLOT);
		setcomponents(bean);

	}

	private void setcomponents(PlotBean bean) {
		plotTypeTextView = (TextView) findViewById(R.id.plotTypeTextView);
		plotIdTextView = (TextView) findViewById(R.id.plotIdTextView);
		plotTitleTextView = (TextView) findViewById(R.id.plotTitleTextView);
		descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
		ownerTextView = (TextView) findViewById(R.id.ownerTextView);
		addressTextView = (TextView) findViewById(R.id.plotAddressTextView);
		areaTextView = (TextView) findViewById(R.id.plotareaTextView);
		amountTextView = (TextView) findViewById(R.id.plotPriceTextView);

		plotTypeTextView.setText(bean.getPropType());
		plotIdTextView.setText(bean.getPropId());
		plotTitleTextView.setText(bean.getPropTitle());
		descriptionTextView.setText(bean.getDescription());
		ownerTextView.setText(bean.getOwnerName());
		addressTextView.setText(bean.getAddress());
		areaTextView.setText(bean.getArea());
		amountTextView.setText(bean.getPrice());

		bookPlotButton = (Button) findViewById(R.id.plotBookButton);
		cancelButton = (Button) findViewById(R.id.cancelButton);
		bookPlotButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == bookPlotButton.getId()) {
			new BookPlotTask().execute(PreferenceManager
					.getDefaultSharedPreferences(PlotDetailsActivity.this)
					.getString(Appconstants.USERNAME, ""), plotIdTextView
					.getText().toString());
		} else if (v.getId() == cancelButton.getId()) {
			
			finish();
		}

	}

}
