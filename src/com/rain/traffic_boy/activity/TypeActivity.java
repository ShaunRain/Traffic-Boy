package com.rain.traffic_boy.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.rain.traffic_boy.R;
import com.rain.traffic_boy.model.Incident;
import com.rain.traffic_boy.util.ParseJson;

public class TypeActivity extends Activity implements OnClickListener {
	TextView tv_clear;
	TextView tv_trouble;
	TextView tv_crowd;
	TextView tv_control;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_type);
		tv_clear = (TextView) findViewById(R.id.tv_clear);
		tv_control = (TextView) findViewById(R.id.tv_control);
		tv_crowd = (TextView) findViewById(R.id.tv_crowd);
		tv_trouble = (TextView) findViewById(R.id.tv_trouble);

		tv_clear.setOnClickListener(this);
		tv_control.setOnClickListener(this);
		tv_crowd.setOnClickListener(this);
		tv_trouble.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		ArrayList<String> imageURL = new ArrayList<>();
		List<Incident> incidents = null;
		switch (v.getId()) {
		case R.id.tv_clear:
			incidents = ParseJson.readDetail("clear");
			if (incidents == null)
				break;
			break;
		case R.id.tv_control:
			incidents = ParseJson.readDetail("control");
			if (incidents == null)
				break;
			break;
		case R.id.tv_crowd:
			incidents = ParseJson.readDetail("crowd");
			if (incidents == null)
				break;
			break;
		case R.id.tv_trouble:
			incidents = ParseJson.readDetail("trouble");
			if (incidents == null)
				break;
			break;
		}

		if (incidents != null) {
			Intent recIntent = new Intent(TypeActivity.this,
					RecyclerActivity.class);
			recIntent.putExtra("incidents", (Serializable) incidents);
			startActivity(recIntent);
		} else
			Toast.makeText(getApplicationContext(), "该类型还没有",
					Toast.LENGTH_SHORT).show();
	}

}
