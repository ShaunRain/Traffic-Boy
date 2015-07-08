package com.rain.traffic_boy.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class UploadUtil {

	public static void up2Server(Resources resource, final Context context,
			File image, File detail) {
		try {

			if (image.exists() && image.length() > 0 && detail.exists()
					&& detail.length() > 0) {
				Toast.makeText(context, "找到文件", Toast.LENGTH_SHORT).show();
				AsyncHttpClient client = new AsyncHttpClient();
				RequestParams params = new RequestParams();
				params.put("profile_picture", image);
				params.put("detail_txt", detail);
				client.setTimeout(40000);

				final ProgressDialog progress = new ProgressDialog(context);
				progress.setCancelable(false);
				progress.setCanceledOnTouchOutside(false);
				progress.setTitle("上传中..");
				progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progress.show();

				client.post("http://shaunrain.oicp.net/FileUp/UploadServlet",
						params, new AsyncHttpResponseHandler() {

							@Override
							public void onFailure(int arg0, Header[] arg1,
									byte[] arg2, Throwable arg3) {
								progress.cancel();
								Toast.makeText(context, "上传失败",
										Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onSuccess(int arg0, Header[] arg1,
									byte[] arg2) {
								// TODO Auto-generated method stub
								progress.cancel();
								Toast.makeText(context, "上传成功",
										Toast.LENGTH_SHORT).show();
							}
						});
			} else {
				Toast.makeText(context, "找不到文件", Toast.LENGTH_SHORT).show();
			}

		} catch (FileNotFoundException e) {
			Toast.makeText(context, "找不到文件", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}

	public static void server2mobile(Context context, String type) {

		String serverPath = "http://shaunrain.oicp.net/FileUp/QueryServlet?type="
				+ type;
		try {
			URL url = new URL(serverPath);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(10000);
			conn.setRequestMethod("GET");

			int code = conn.getResponseCode();
			Log.d("code", code + "");
			if (code == 200) {
				InputStream is = conn.getInputStream();

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int len = 0;
				byte[] buffer = new byte[1024];

				while ((len = is.read(buffer)) != -1) {
					baos.write(buffer, 0, len);
				}
				is.close();
				conn.disconnect();

				String result = new String(baos.toByteArray());
				// System.out.println("result: " + result);
				String[] results = result.split("[$]");
				for (String r : results) {
					if (r.length() != 0) {
						Log.d("jsonPath", r);
						URL json = new URL(r);
						HttpURLConnection con = (HttpURLConnection) json
								.openConnection();
						con.setConnectTimeout(5000);
						con.setRequestMethod("GET");
						int co = con.getResponseCode();
						Log.d("jsonCode", co + "");
						if (co == 200) {
							File jsonFile;
							if (r.endsWith("clear.json")) {
								jsonFile = new File(
										Environment
												.getExternalStorageDirectory()
												+ "/traffic_json/clear.json");
							} else if (r.endsWith("crowd.json")) {
								jsonFile = new File(
										Environment
												.getExternalStorageDirectory()
												+ "/traffic_json/crowd.json");
							} else if (r.endsWith("trouble.json")) {
								jsonFile = new File(
										Environment
												.getExternalStorageDirectory()
												+ "/traffic_json/trouble.json");
							} else {
								jsonFile = new File(
										Environment
												.getExternalStorageDirectory()
												+ "/traffic_json/control.json");
							}

							if (jsonFile.exists())
								jsonFile.delete();
							jsonFile.createNewFile();
							try (BufferedReader reader = new BufferedReader(
									new InputStreamReader(con.getInputStream(),
											"gb2312"));
									BufferedWriter writer = new BufferedWriter(
											new FileWriter(jsonFile));) {
								String line = null;
								while ((line = reader.readLine()) != null) {
									writer.write(line);
								}
							}

						}
						con.disconnect();
					}
				}

			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
