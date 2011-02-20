package com.triplelands.sd.rest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class HttpClientUtils {
	public static void post(String url, Object obj) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			HttpEntity entity = new StringEntity(JsonUtils.toJson(obj));
			post.setEntity(entity);
			post.setHeader("Accept", "application/json");
			post.setHeader("Content-type", "application/json");
			client.execute(post, new BasicResponseHandler());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static <T> T getList(String url, Type type) {
		String result = null;
		
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(url);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			result = client.execute(get, responseHandler);
			Log.i("public static <T> T get(String url, Class<T> clazz)", cleanJson(result));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return JsonUtils.<T>toListObject(cleanJson(result), type);
	}

	public static <T> T getList2(String url, Type type) {
		String result = null;
		
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(url);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			result = client.execute(get, responseHandler);
			Log.i("public static <T> T get(String url, Class<T> clazz)", cleanJson(result));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return JsonUtils.<T>toListObject2(cleanJson(result), type);
	}
	
	public static <T> T get(String url, Class<T> clazz) {
		String result = null;
		
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(url);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			result = client.execute(get, responseHandler);
			Log.i("public static <T> T get(String url, Class<T> clazz)", cleanJson(result));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return JsonUtils.<T>toObject(cleanJson(result), clazz);
	}
	
	private static String cleanJson(String result) {
		if (result == null || result.trim().equals("")) return null;
		return result.split("\n")[0];
	}
}
