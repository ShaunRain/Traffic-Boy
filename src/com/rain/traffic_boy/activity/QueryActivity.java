package com.rain.traffic_boy.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.rain.traffic_boy.R;
import com.rain.traffic_boy.model.Incident;
import com.rain.traffic_boy.util.AnimeMaker;
import com.rain.traffic_boy.util.ParseJson;

public class QueryActivity extends Activity {

	Button bt_near;
	Button bt_type;
	List<Incident> allInc;

	String[] allType = { "clear", "crowd", "trouble", "control" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query);

		bt_near = (Button) findViewById(R.id.bt_near);
		bt_type = (Button) findViewById(R.id.bt_type);

		AnimeMaker.scaleXY(bt_near, bt_type).start();

		bt_near.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				allInc = new ArrayList<>();
				for (String type : allType) {
					List<Incident> list = ParseJson.readDetail(type);
					if (list != null) {
						for (Incident i : list)
							allInc.add(i);
					}
				}

				Intent nearIntent = new Intent(QueryActivity.this,
						NearActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("allInc", (Serializable) allInc);
				nearIntent.putExtra("bundle", bundle);
				startActivity(nearIntent);

			}
		});
		bt_type.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intentType = new Intent(QueryActivity.this,
						TypeActivity.class);
				startActivity(intentType);
			}
		});

	}

}
