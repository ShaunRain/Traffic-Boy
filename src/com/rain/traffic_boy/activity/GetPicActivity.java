package com.rain.traffic_boy.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.rain.traffic_boy.R;
import com.rain.traffic_boy.util.AnimeMaker;

public class GetPicActivity extends Activity {
	Button bt_camera;
	Button bt_gallery;

	public static final int TAKE_PHOTO = 0;
	public static final int CROP_PHOTO = 1;
	public static final int PICK_PHOTO = 3;

	File outputImage;
	Uri imageUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_getpic);

		bt_camera = (Button) findViewById(R.id.bt_camera);
		bt_gallery = (Button) findViewById(R.id.bt_gallery);

		AnimeMaker.scaleXY(bt_camera, bt_gallery).start();

		bt_camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				outputImage = new File(Environment
						.getExternalStorageDirectory() + "/traffic_pic", System
						.currentTimeMillis() + ".jpg");
				try {
					outputImage.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}

				imageUri = Uri.fromFile(outputImage);
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, CROP_PHOTO);

			}
		});

		bt_gallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				outputImage = new File(Environment
						.getExternalStorageDirectory() + "/traffic_pic", System
						.currentTimeMillis() + ".jpg");
				try {
					if (outputImage.exists()) {
						outputImage.delete();
					}
					outputImage.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				imageUri = Uri.fromFile(outputImage);
				Intent intent2 = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				intent2.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				intent2.putExtra("crop", true);
				intent2.putExtra("scale", true);
				// intent2.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent2, PICK_PHOTO);

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PHOTO:
			if (resultCode == RESULT_OK) {
				Intent crop = new Intent("com.android.camera.action.CROP");
				crop.setDataAndType(imageUri, "image/*");
				crop.putExtra("scale", true);
				crop.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(crop, CROP_PHOTO);
			}
			break;
		case CROP_PHOTO:
			if (resultCode == RESULT_OK) {
				Bitmap bm = BitmapFactory.decodeFile(outputImage
						.getAbsolutePath());
				try {
					compressBmp(bm);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				Intent upload = new Intent(GetPicActivity.this,
						UploadActivity.class);
				upload.putExtra("imagepath", outputImage.getAbsolutePath());
				startActivity(upload);

			}
			break;
		case PICK_PHOTO:
			if (resultCode == RESULT_OK) {
				ContentResolver resolver = this.getContentResolver();
				Uri uri = data.getData();
				try {
					Bitmap bm = MediaStore.Images.Media
							.getBitmap(resolver, uri);
					compressBmp(bm);

					Intent upload = new Intent(GetPicActivity.this,
							UploadActivity.class);
					upload.putExtra("imagepath", outputImage.getAbsolutePath());
					startActivity(upload);

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

			break;
		default:
			break;
		}
	}

	public void compressBmp(Bitmap bm) throws FileNotFoundException,
			IOException {
		FileOutputStream fos = new FileOutputStream(outputImage);
		bm.compress(CompressFormat.JPEG, 100, fos);
		fos.flush();
		fos.close();
		int option = 100;
		while (outputImage.length() / 1024 > 400) {
			option -= 10;
			Log.d("option", option + ";" + (outputImage.length() / 1024));
			outputImage.delete();
			outputImage.createNewFile();
			fos = new FileOutputStream(outputImage);
			bm.compress(CompressFormat.JPEG, option, fos);
			fos.flush();
			fos.close();
		}
	}

}
