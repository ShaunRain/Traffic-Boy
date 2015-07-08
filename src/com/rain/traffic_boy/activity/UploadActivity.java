package com.rain.traffic_boy.activity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.rain.traffic_boy.R;
import com.rain.traffic_boy.util.UploadUtil;

public class UploadActivity extends Activity {
	public static UploadActivity uploadActivity;

	static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	ImageView iv_resultimage;
	RadioGroup rg_type;
	TextView tv_location;
	TextView tv_shoottime;
	EditText et_detail;
	Button bt_uptoserver;

	String imagePath;
	String type;

	LocationClient mLocationClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload);
		uploadActivity = this;
		mLocationClient = new LocationClient(getApplicationContext());
		mLocationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation arg0) {
				Log.d("position",
						arg0.getLatitude() + "," + arg0.getLongitude() + ":"
								+ arg0.getAddrStr());
				tv_location.setText(arg0.getLatitude() + ","
						+ arg0.getLongitude() + "," + arg0.getAddrStr());
			}
		});

		iv_resultimage = (ImageView) findViewById(R.id.iv_resultimage);
		Intent intent = getIntent();
		imagePath = intent.getStringExtra("imagepath");
		Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
		iv_resultimage.setImageBitmap(bitmap);
		iv_resultimage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(UploadActivity.this)
						.setTitle("重选？")
						.setPositiveButton("是的",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										finish();
									}
								})
						.setNegativeButton("不了",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub

									}
								}).show();
			}
		});

		rg_type = (RadioGroup) findViewById(R.id.rg_type);
		rg_type.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_trouble:
					type = "trouble";
					break;
				case R.id.rb_crowd:
					type = "crowd";
					break;
				case R.id.rb_clear:
					type = "clear";
					break;
				case R.id.rb_control:
					type = "control";
					break;
				}
			}
		});
		tv_location = (TextView) findViewById(R.id.tv_location);
		getLocation();
		mLocationClient.start();
		mLocationClient.requestLocation();

		tv_shoottime = (TextView) findViewById(R.id.tv_shoottime);
		tv_shoottime.setText(df.format(new Date(System.currentTimeMillis())));

		et_detail = (EditText) findViewById(R.id.et_detail);
		bt_uptoserver = (Button) findViewById(R.id.bt_uptoserver);

		bt_uptoserver.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				try {
					File pic = new File(imagePath);
					File detail = new File(Environment
							.getExternalStorageDirectory()
							+ "/traffic_log/"
							+ pic.getName() + ".txt");
					detail.createNewFile();

					FileWriter writer = new FileWriter(detail);

					String[] loc = tv_location.getText().toString().split(",");
					writer.write(type + "$" + loc[0] + "$" + loc[1] + "$" + loc[2] + "$"
							+ tv_shoottime.getText().toString() + "$"
							+ et_detail.getText().toString());
					writer.close();

					UploadUtil.up2Server(getResources(), UploadActivity.this,
							pic, detail);

				} catch (IOException e) {

					e.printStackTrace();
				} finally {
					mLocationClient.stop();
				}
			}
		});

	}

	private void getLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);
		option.setCoorType("gcj02");
		int span = 1000;
		option.setScanSpan(span);
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);

	}

	@Override
	protected void onDestroy() {
		mLocationClient.stop();
		super.onDestroy();

	}
}
