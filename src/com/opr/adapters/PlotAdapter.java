package com.opr.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.opr.activities.R;
import com.opr.beans.PlotBean;
import com.opr.utils.Appconstants;
import com.squareup.picasso.Picasso;

public class PlotAdapter extends ArrayAdapter<PlotBean> {
	Context context = null;
	ArrayList<PlotBean> list = null;

	public PlotAdapter(Context context, ArrayList<PlotBean> list) {
		super(context, R.layout.plotrow, list);
		this.context = context;
		this.list = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.plotrow, parent, false);
			holder = new ViewHolder();
			holder.propTypeTextView = (TextView) convertView
					.findViewById(R.id.propTypeTextView);
			holder.propIdTextView = (TextView) convertView
					.findViewById(R.id.propIdextView);
			holder.addressTextView = (TextView) convertView
					.findViewById(R.id.addressextView);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.plotImageView);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.propTypeTextView.setText(list.get(position).getPropType());
		holder.propIdTextView.setText(list.get(position).getPropId());
		holder.addressTextView.setText(list.get(position).getAddress());
		StringBuilder builder = new StringBuilder();
		builder.append(Appconstants.IMAGE_FOLDER);
		builder.append(list.get(position).getImage());
		Picasso.with(context)
		.load(builder.toString())
		.placeholder(
				context.getResources().getDrawable(
						R.drawable.imageplaceholder)).fit()
		.into(holder.imageView);
		return convertView;
	}

	private static class ViewHolder {
		ImageView imageView;
		TextView propTypeTextView;
		TextView propIdTextView;
		TextView addressTextView;

	}

}
