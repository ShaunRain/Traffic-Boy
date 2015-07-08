package com.rain.traffic_boy.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;

import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rain.traffic_boy.model.Incident;

public class ParseJson {

	public static List<Incident> readDetail(String type) {
		Gson gson = new Gson();
		File jsonFile = new File(Environment.getExternalStorageDirectory()
				+ "/traffic_json/" + type + ".json");
		if (!jsonFile.exists())
			return null;
		StringBuffer sb = new StringBuffer();
		sb.append('[');
		try (InputStreamReader isr = new InputStreamReader(new FileInputStream(
				jsonFile));
				BufferedReader reader = new BufferedReader(isr)) {
			String line = null;
			while ((line = reader.readLine()) != null)
				sb.append(line);

			String jsonStr = sb.toString().trim() + ']';
			System.out.println("jsonSTR: " + jsonStr);
			Type tp = new TypeToken<List<Incident>>() {
			}.getType();
			List<Incident> incidents = gson.fromJson(jsonStr, tp);

			return incidents;

		} catch (UnsupportedEncodingException e) {
			Log.d("Excep", e.toString());
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			Log.d("Excep", e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			Log.d("Excep", e.toString());
			e.printStackTrace();

		}
		return null;
	}

}
