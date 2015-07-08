package com.rain.traffic_boy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.rain.traffic_boy.R;
import com.rain.traffic_boy.model.Incident;
import com.rain.traffic_boy.util.MyAdapter;

public class DetailActivity extends Activity {
	ImageView detail_resultimage;
	TextView detail_type, detail_location, detail_shoottime, detail_detail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		initViews();
		setViews();

	}

	private void setViews() {
		Intent intent = getIntent();
		detail_resultimage.setImageDrawable(MyAdapter.drawables.get(intent
				.getIntExtra("position", 0)));
		Incident inc = (Incident) intent.getSerializableExtra("incident");
		detail_type.setText(inc.getType());
		detail_location.setText(inc.getLaltitude() + "," + inc.getLongtitude()
				+ "," + inc.getAddr());
		detail_shoottime.setText(inc.getTime());
		detail_detail.setText(inc.getDetail());
	}

	private void initViews() {
		detail_resultimage = (ImageView) findViewById(R.id.detail_resultimage);
		detail_type = (TextView) findViewById(R.id.detail_type);
		detail_location = (TextView) findViewById(R.id.detail_location);
		detail_shoottime = (TextView) findViewById(R.id.detail_shoottime);
		detail_detail = (TextView) findViewById(R.id.detail_detail);
	}

}
