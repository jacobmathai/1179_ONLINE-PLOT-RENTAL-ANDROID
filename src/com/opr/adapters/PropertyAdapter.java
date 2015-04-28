package com.opr.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.opr.activities.R;
import com.opr.beans.Propertybean;
import com.opr.utils.Appconstants;
import com.squareup.picasso.Picasso;

public class PropertyAdapter extends ArrayAdapter<Propertybean> {

	Context context = null;
	ArrayList<Propertybean> list = null;
	

	public PropertyAdapter(Context context, ArrayList<Propertybean> list) {
		super(context, R.layout.row, list);
		this.context = context;
		this.list = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.row, parent, false);
			holder = new ViewHolder();
			holder.titleTextView = (TextView) convertView
					.findViewById(R.id.titleTextView);
			holder.typeTextView = (TextView) convertView
					.findViewById(R.id.typeTextView);
			holder.priceTextView = (TextView) convertView
					.findViewById(R.id.priceTextView);
			holder.bedroomsTextView = (TextView) convertView
					.findViewById(R.id.bedroomstextView);
			holder.idtextview = (TextView) convertView
					.findViewById(R.id.propertyidtextView);
			holder.prop_image = (ImageView) convertView
					.findViewById(R.id.propImageView);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.titleTextView.setText(list.get(position).getPropTitle());
		holder.typeTextView.setText(list.get(position).getPropertyType());
		holder.priceTextView.setText("Price :" + list.get(position).getPrice());
		holder.bedroomsTextView.setText("Bed rooms :"
				+ list.get(position).getBedrooms());
		holder.idtextview.setText("Property id : " + list.get(position).getPropId());
		StringBuilder builder = new StringBuilder();
		builder.append(Appconstants.IMAGE_FOLDER);
		builder.append(list.get(position).getImage());
		Log.d("TAG", "image : " + builder.toString());
		Picasso.with(context)
				.load(builder.toString())
				.placeholder(
						context.getResources().getDrawable(
								R.drawable.kfm_home)).fit()
				.into(holder.prop_image);

		return convertView;
	}

	private static class ViewHolder {
		TextView titleTextView;
		TextView typeTextView;
		TextView priceTextView;
		TextView bedroomsTextView;
		TextView idtextview;
		ImageView prop_image;
	}
}
