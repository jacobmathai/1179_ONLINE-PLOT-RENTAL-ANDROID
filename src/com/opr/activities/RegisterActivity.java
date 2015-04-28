package com.opr.activities;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.opr.services.WebServiceManager;
import com.opr.utils.Appconstants;

public class RegisterActivity extends Activity implements OnClickListener {
	EditText name = null;
	EditText city = null;
	EditText phone = null;
	EditText email = null;
	EditText username = null;
	EditText password = null;
	EditText confpassword = null;
	Button reg = null;
	String data = "";

	public class RegisterTask extends AsyncTask<String, String, String> {
		ProgressDialog dialog = new ProgressDialog(RegisterActivity.this);

		@Override
		protected String doInBackground(String... params) {
			WebServiceManager manager = new WebServiceManager();
			String response = manager.registerUser(params[0]);
			Log.d("TAG", response);
			return response;
		
		}

		@Override
		protected void onPreExecute() {
			dialog.setMessage("Registering....!");
			dialog.show();
		}

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			if (!result.equals("error")) {
				if (result.equals("true")) {
					Toast.makeText(getApplicationContext(),
							Appconstants.REGISTER_MESSAGE, Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(getApplicationContext(),
							Appconstants.REGISTER_FAILED, Toast.LENGTH_SHORT)
							.show();
				}
			} else {
				Toast.makeText(getApplicationContext(),
						Appconstants.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
			}
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		setUI();
	}

	private void setUI() {

		name = (EditText) findViewById(R.id.nameeditText);
		city = (EditText) findViewById(R.id.cityeditText);
		phone = (EditText) findViewById(R.id.phoneditText);
		email = (EditText) findViewById(R.id.emaileditText);
		username = (EditText) findViewById(R.id.usereditText);
		password = (EditText) findViewById(R.id.passwordeditText);
		confpassword = (EditText) findViewById(R.id.confpasswdeditText);
		reg = (Button) findViewById(R.id.searchButton);
		reg.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		//if (checkFields()) {
			String data = writeJSON();
			new RegisterTask().execute(data);
		//}

	}

	private String writeJSON() {
		JSONObject object = new JSONObject();
		try {
			object.put(Appconstants.NAME, name.getText().toString());
			object.put(Appconstants.CITY, city.getText().toString());
			object.put(Appconstants.PHONE, phone.getText().toString());
			object.put(Appconstants.EMAIL, email.getText().toString());
			object.put(Appconstants.USERNAME, username.getText().toString());
			object.put(Appconstants.PASSWORD, password.getText().toString());
			data = object.toString();
			Log.d("TAG", data);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;

	}

	private boolean checkFields() {
		boolean status = true;
		if (name.getText().toString().equals("")) {
			name.setError("Enter name");
			status = false;
		} else if (city.getText().toString().equals("")) {
			city.setError("Enter city name");
			status = false;
		} else if (phone.getText().toString().equals("")) {
			phone.setError("Enter phone number");
			status = false;
		} else if (email.getText().toString().equals("")) {
			email.setError("Enter email");
			status = false;
		} else if (username.getText().toString().equals("")) {
			username.setError("Enter username");
			status = false;
		} else if (password.getText().toString().equals("")) {
			password.setError("Enter password");
		} else if (!password.getText().toString()
				.equals(confpassword.getText().toString())) {
			Toast.makeText(getApplicationContext(),
					"Password are not matching!", Toast.LENGTH_SHORT).show();
			status = false;
		}
		return status;
	}

}
