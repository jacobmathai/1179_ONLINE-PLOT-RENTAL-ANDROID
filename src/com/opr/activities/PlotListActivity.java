package com.opr.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.opr.adapters.PlotAdapter;
import com.opr.beans.PlotBean;
import com.opr.utils.AppUtils;
import com.opr.utils.PlotUtils;

public class PlotListActivity extends ListActivity implements
		OnItemClickListener {
	ListView plotListView = null;
	String result = "";
	ArrayList<PlotBean> list = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_plotlist);
		// plotListView = (ListView) findViewById(R.id.plotlistView);
		plotListView = getListView();
		result = getIntent().getExtras().getString(AppUtils.PLOT_DETAILS);
		list = getValues(result);
		if (list.isEmpty()) {
			Toast.makeText(getApplicationContext(), "Sorry no results!",
					Toast.LENGTH_SHORT).show();
		}
		PlotAdapter adapter = new PlotAdapter(PlotListActivity.this, list);
		plotListView.setAdapter(adapter);
		plotListView.setOnItemClickListener(this);

	}

	private ArrayList<PlotBean> getValues(String data) {
		ArrayList<PlotBean> list = new ArrayList<PlotBean>();
		try {
			JSONArray array = new JSONArray(data);
			for (int i = 0; i < array.length(); i++) {
				PlotBean bean = new PlotBean();
				JSONObject object = new JSONObject(array.getString(i));
				bean.setPropType(object.getString(PlotUtils.PROP_TYPE));
				bean.setPubLocation(object.getString(PlotUtils.PUB_LOC));
				bean.setOwnerName(object.getString(PlotUtils.OWNER));
				bean.setStatus(object.getString(PlotUtils.STATUS));
				bean.setLocation(object.getString(PlotUtils.LOCATION));
				bean.setImage(object.getString(PlotUtils.IMAGE));
				bean.setPropTitle(object.getString(PlotUtils.AREA));
				bean.setArea(object.getString(PlotUtils.AREA));
				bean.setPrice(object.getString(PlotUtils.PRICE));
				bean.setContractType(object.getString(PlotUtils.CONTRACT_TYPE));
				bean.setQualityType(object.getString(PlotUtils.QUALITY_TYPE));
				bean.setAddress(object.getString(PlotUtils.ADDRESS));
				bean.setDescription(object.getString(PlotUtils.DESCRIPTION));
				bean.setPropId(object.getString(PlotUtils.PRP_ID));
				bean.setDistPubLocation(object
						.getString(PlotUtils.DIST_PUB_LOC));
				bean.setPostDate(object.getString(PlotUtils.POST_DATE));
				list.add(bean);

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		Object object = arg0.getItemAtPosition(position);
		if (object instanceof PlotBean) {
			PlotBean bean = (PlotBean) object;
			Intent intent = new Intent(PlotListActivity.this,
					PlotDetailsActivity.class);
			intent.putExtra(PlotUtils.PLOT, bean);
			startActivity(intent);

		}

	}
}
