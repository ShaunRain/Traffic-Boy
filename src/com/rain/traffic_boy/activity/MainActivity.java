package com.rain.traffic_boy.activity;

import java.io.File;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.baidu.mapapi.SDKInitializer;
import com.rain.traffic_boy.R;
import com.rain.traffic_boy.util.AnimeMaker;
import com.rain.traffic_boy.util.UploadUtil;

public class MainActivity extends Activity {

	Button bt_upload;
	Button bt_query;

	ProgressDialog progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_main);
		File picDir = new File(Environment.getExternalStorageDirectory(),
				"traffic_pic");
		if (!picDir.exists())
			picDir.mkdirs();

		File logDir = new File(Environment.getExternalStorageDirectory(),
				"traffic_log");
		if (!logDir.exists())
			logDir.mkdirs();

		File jsonDir = new File(Environment.getExternalStorageDirectory(),
				"traffic_json");
		if (!jsonDir.exists())
			jsonDir.mkdirs();

		progress = new ProgressDialog(this);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.setTitle("连接服务器...");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

		bt_upload = (Button) findViewById(R.id.bt_upload);
		bt_query = (Button) findViewById(R.id.bt_query);

		AnimeMaker.scaleXY(bt_upload, bt_query).start();

		bt_upload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent getPic = new Intent(MainActivity.this,
						GetPicActivity.class);
				startActivity(getPic);
			}
		});

		bt_query.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				AsynTaskThread thread = new AsynTaskThread();
				thread.execute("all");
				progress.show();

			}
		});
	}

	class AsynTaskThread extends AsyncTask<String, Integer, Void> {

		@Override
		protected Void doInBackground(String... params) {

			UploadUtil.server2mobile(MainActivity.this, params[0]);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			progress.cancel();
			Intent queryIntent = new Intent(MainActivity.this,
					QueryActivity.class);
			startActivity(queryIntent);
		}

	}
}
