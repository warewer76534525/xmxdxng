package com.triplelands.sd.rest;

import java.lang.reflect.Type;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.myjson.Gson;

public class JsonUtils {
	
	public static String toJson(Object object) {
		Gson gson = new Gson();
		return gson.toJson(object);
	}
	
	public static <T> T toObject(String json, Class<T> clazz) {
		Gson gson = new Gson();
		return gson.fromJson(json, clazz);
	}
	
	public static <T> T toListObject(String json, Type type) {
		Gson gson = new Gson();
		T result = null;
		
		try {
			JSONObject obj = new JSONObject(json);
			String lst = obj.names().getString(0);
			
			if (obj.names().length() != 1) 
				throw new RuntimeException("Bukan tipe list");
			
			String content = obj.getString(lst);
			Log.i("isi_list", content);
			
			result = gson.fromJson(obj.getString(lst), type);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static <T> T toListObject2(String json, Type type) {
		Gson gson = new Gson();		
		return gson.fromJson(json, type);
	}
	
	public static String toListJson(Object listObject, Type listType) {
		Gson gson = new Gson();
		return gson.toJson(listObject, listType);
	}
}
