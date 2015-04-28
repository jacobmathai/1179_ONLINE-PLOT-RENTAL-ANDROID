package com.opr.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.opr.gcm_utils.GCMRegistrationTools;
import com.opr.services.WebServiceManager;
import com.opr.utils.Appconstants;

public class LoginActivity extends Activity implements OnClickListener {

	EditText username = null;
	EditText passwd = null;
	Button login = null;
	Button register = null;
	SharedPreferences preferences = null;
	String gcmAppkey = "";

	public class LoginTask extends AsyncTask<String, String, String> {
		ProgressDialog dialog = new ProgressDialog(LoginActivity.this);

		@Override
		protected String doInBackground(String... params) {
			WebServiceManager manager = new WebServiceManager();
			String response = manager.userLogin(params[0], params[1]);
//			response = "true";
			if (response.equals("true")) {
				manager.registerGcm(params[0], gcmAppkey);
				return response;
			} else {
				return response;
			}
//			Log.d("TAG", response);
//			Log.d("TAG", "username :" + params[0] + ", passwd : " + params[1]);
//			return response;
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
					preferences = PreferenceManager
							.getDefaultSharedPreferences(LoginActivity.this);
					Editor editor = preferences.edit();
					editor.putString(Appconstants.USERNAME, username.getText()
							.toString());
					editor.putString(Appconstants.PASSWORD, passwd.getText()
							.toString());
					editor.commit();
					startActivity(new Intent(LoginActivity.this,
							HomeActivity.class));
				} else {
					Toast.makeText(getApplicationContext(),
							"Invalid username or password!", Toast.LENGTH_SHORT)
							.show();
				}

			} else {
				Toast.makeText(getApplicationContext(), "network Error",
						Toast.LENGTH_SHORT).show();
			}

		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setUI();
		GCMRegistrationTools tools = new GCMRegistrationTools(this, this);
		if (tools.isPlayServicesSupport()) {
			gcmAppkey = tools.getGCMAppKey();
			Log.d("GCM", gcmAppkey);
		}
	}

	private void setUI() {
		username = (EditText) findViewById(R.id.usernameeditText);
		passwd = (EditText) findViewById(R.id.passwdeditText);
		login = (Button) findViewById(R.id.loginButton);
		register = (Button) findViewById(R.id.registerButton);
		login.setOnClickListener(this);
		register.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == login.getId()) {
			if (username.getText().toString().equals("")) {
				username.setError("Enter username!"); 
			} else if (passwd.getText().toString().equals("")) {
				passwd.setError("Enter pasword!");
			} else {
				new LoginTask().execute(username.getText().toString(), passwd
						.getText().toString());

			}

		} else {
			startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
		}

	}

}
