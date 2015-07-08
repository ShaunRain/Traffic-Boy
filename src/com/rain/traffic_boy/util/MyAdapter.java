package com.rain.traffic_boy.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.loopj.android.image.SmartImageTask;
import com.loopj.android.image.SmartImageView;
import com.rain.traffic_boy.R;
import com.rain.traffic_boy.activity.DetailActivity;
import com.rain.traffic_boy.model.Incident;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

	private LayoutInflater mInflater;
	private Context mContext;
	private List<Incident> mDatas;
	private List<String> imageURL;
	private List<Integer> mHeights;

	public static Map<Integer, Drawable> drawables;

	public interface OnItemClickListener {
		void onItemClick(View view, int position);

		void onItemLongClick(View v, int position);
	}

	private OnItemClickListener mListener;

	public void setOnItemClickListener(OnItemClickListener listener) {
		mListener = listener;
	}

	public MyAdapter(Context context, List<Incident> data) {
		this.mContext = context;
		this.mDatas = data;
		imageURL = new ArrayList<>();
		for (Incident i : data)
			imageURL.add(i.getImage());
		mInflater = LayoutInflater.from(mContext);
		mHeights = new ArrayList<Integer>();
		for (int i = 0; i < mDatas.size(); i++) {
			mHeights.add((int) (200 + Math.random() * 300));
		}
		drawables = new HashMap<>();

	}

	@Override
	public int getItemCount() {
		return mDatas.size();
	}

	@Override
	public void onBindViewHolder(final MyViewHolder viewHolder, int pos) {
		LayoutParams params = viewHolder.image.getLayoutParams();
		params.height = mHeights.get(pos);
		viewHolder.image.setLayoutParams(params);
		final int position = pos;
		viewHolder.image.setClickable(false);
		viewHolder.image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent detailIntent = new Intent(mContext, DetailActivity.class);
				detailIntent.putExtra("position", position);
				detailIntent.putExtra("incident",
						(Serializable) mDatas.get(position));
				mContext.startActivity(detailIntent);
			}
		});
		viewHolder.image.setImageUrl(imageURL.get(pos), R.drawable.ic_launcher,
				R.drawable.ic_launcher,
				new SmartImageTask.OnCompleteListener() {

					@Override
					public void onComplete() {
						drawables.put(position, viewHolder.image.getDrawable());
						viewHolder.image.setClickable(true);
					}

				});

	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		View view = mInflater.inflate(R.layout.item_single, arg0, false);
		MyViewHolder viewHolder = new MyViewHolder(view);
		return viewHolder;
	}

}

class MyViewHolder extends ViewHolder {

	SmartImageView image;

	public MyViewHolder(View arg0) {
		super(arg0);
		image = (SmartImageView) arg0.findViewById(R.id.item_image);

	}

}
