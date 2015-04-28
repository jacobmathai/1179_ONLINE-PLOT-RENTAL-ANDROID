

package com.opr.gcm_utils;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.opr.utils.AppUtils;


public class GCMRegistrationTools {

	private Context context;
	private Activity activity;
	String register_id = "";
	String status = "";
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	GoogleCloudMessaging gcm = null;

	public GCMRegistrationTools(Context context, Activity activity) {

		this.context = context;
		this.activity = activity;
	}

	public boolean isPlayServicesSupport() {

		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(context);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, activity,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i("TAG", "This device is not supported.");

			}
			return false;
		}
		return true;
	}

	public String getGCMAppKey() {

		String value = PreferenceManager.getDefaultSharedPreferences(context)
				.getString(AppUtils.GCM_KEY_FROM_PREFERNCES, "");
		if (value.equals("") || value.equals("error")) {

			new AsyncTask<Void, Void, String>() {

				ProgressDialog dialog = new ProgressDialog(context);

				@Override
				protected void onPreExecute() {

					dialog.setMessage("Loading...!");
					dialog.setCanceledOnTouchOutside(false);
					dialog.show();
				}

				@Override
				protected String doInBackground(Void... params) {

					if (gcm == null) {
						try {
							gcm = GoogleCloudMessaging.getInstance(context);
							while (register_id.equals("")) {
								register_id = gcm
										.register(AppUtils.GCM_PROJECT_ID);
							}
						} catch (IOException e) {
							e.printStackTrace();
							return "error";
						}
					}
					return register_id;
				}

				@Override
				protected void onPostExecute(String result) {

					dialog.dismiss();
					if (register_id.equals("error")) {
						status = "error";
					} else if (result.equals("")) {
						status = "error";
					} else {
						status = result;
						PreferenceManager
								.getDefaultSharedPreferences(context)
								.edit()
								.putString(AppUtils.GCM_KEY_FROM_PREFERNCES,
										result).commit();
						if (result.equals("error")) {
							showDialoge("");
						}
					}
				}

			}.execute();
		} else {
			status = PreferenceManager.getDefaultSharedPreferences(context)
					.getString(AppUtils.GCM_KEY_FROM_PREFERNCES, "");
		}
		return status;
	}

	protected void showDialoge(String title) {

		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle(title);
		builder.setCancelable(false);
		builder.setMessage("Can't reach google at the moment. Please try later");
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				// TODO Auto-generated method stub
				activity.finish();
			}
		});
		builder.create().show();

	}
}
