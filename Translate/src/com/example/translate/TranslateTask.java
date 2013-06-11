package com.example.translate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class TranslateTask implements Runnable {
	private static final String TAG = "TanslateTask";
	private final Translate translate;
	private final String original, from, to;
	
	public TranslateTask(Translate translate, String original, String from, String to) {
		this.translate = translate;
		this.original = original;
		this.from = from;
		this.to = to;
	}
	
	public void run() {
		String trans = doTranslate(original,from, to);
		translate.setTranslated(trans);
		String retrans = doTranslate(trans, to, from);
		translate.setTranslated(retrans);
	}
	
	private String doTranslate(String original, String from, String to) {
		String result = translate.getResources().getString(R.string.translation_error);
		HttpsURLConnection con = null;
		Log.d(TAG, "doTranslate("+original+", "+from+ ", "+to+")");
		try {
			if (Thread.interrupted())
				throw new InterruptedException();
			String q = URLEncoder.encode(original, "UTF-8");
			URL url = new URL(
					"http://ajax.googleapis.com/ajax/services/language/translate"+
							"?v=1.0" + "&q=" + q + "&langpair=" + from + "%7C" + to);
			con = (HttpsURLConnection) url.openConnection();
			con.setReadTimeout(10000);
			con.setConnectTimeout(15000);
			con.setRequestMethod("GET");
			con.addRequestProperty("Referer", "http://www.pragprog.com/titles/eband3/hello-android");
			con.setDoInput(true);
			con.connect();
			
			if (Thread.interrupted())
				throw new InterruptedException();
			
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(con.getInputStream(), "UTF-8"));
			String payload = reader.readLine();
			reader.close();
			
			JSONObject jsonObject = new JSONObject(payload);
			result = jsonObject.getJSONObject("responseData")
					.getString("translatedText")
					.replace("&#39;", "'")
					.replace("&amp;", "&");
			
			if (Thread.interrupted())
				throw new InterruptedException();
			
		} catch (IOException e) {
			Log.e(TAG, "IOException", e);
		} catch (JSONException e) {
			Log.e(TAG, "JSONException", e);
		} catch (InterruptedException e) {
			Log.e(TAG, "InterruptedException", e);
			result = translate.getResources().getString(R.string.translation_interrupted);
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
		Log.d(TAG, " -> returned"+result);
		return result;
	}
}
