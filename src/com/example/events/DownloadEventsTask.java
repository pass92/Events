package com.example.events;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.android.Facebook;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import database.DbAdapter;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class DownloadEventsTask extends
		AsyncTask<Void, Void, ArrayList<EventsHelper>> {

	String fqlQuery;
	Bundle params = new Bundle();
	Session session;
	View view;
	Communicator comm;
	ListView l;
	private ProgressDialog dialog;
	private DbAdapter dbHelper;
	private Cursor cursor;
	private Context context;
	private String city;
	private Integer offsetQuery;
	private Integer limitQuery;
	private AdapterListView adapter;
	private static Integer start = 0;
	private List<EventsHelper> events;

	
	
	
	DownloadEventsTask(View view, Communicator comm, ListView l,
			ProgressDialog dialog, Context context, String city,
			Integer offsetQuery, Integer limitQuery, AdapterListView adapter,
			List<EventsHelper> events) {
		this.view = view;
		this.comm = comm;
		this.l = l;
		this.dialog = dialog;
		this.context = context;
		this.city = city;
		dbHelper = new DbAdapter(context);
		this.offsetQuery = offsetQuery;
		this.limitQuery = limitQuery;
		this.adapter = adapter;
		this.events = events;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		fqlQuery = "select eid,name,description,start_time, pic_big,venue from event where eid in (SELECT eid FROM event WHERE contains(\""
				+ city
				+ "\") or contains(\""
				+ "Italy"
				+ "\") ) and start_time > now() order by start_time ASC limit "
				+ Integer.toString(limitQuery)
				+ " offset "
				+ Integer.toString(offsetQuery); // order by start_time ASC
		//fqlQuery = "q=Trento&type=event&fields=id,name ,start_time,location,venue,cover";
		Log.w("OnPreExecute", fqlQuery);
		params.putString("q", fqlQuery);
		session = MainActivity.session;

	}

	@Override
	protected ArrayList<EventsHelper> doInBackground(Void... params) {
		// TODO Auto-generated method stub
		final List<EventsHelper> events = new ArrayList<EventsHelper>();
		Log.w("Async Task", "doInBackground start!");

		 Request request = new Request(
		 session,
		 "/fql",
		 this.params, HttpMethod.GET, new Request.Callback() {
		 @Override
		 public void onCompleted(Response response) {
		 // Log.i(TAG, "Got results: " + response.toString());
		 try {
		 if (response != null) {
		 final JSONObject json = response
		 .getGraphObject().getInnerJSONObject();
		 JSONArray d = json.getJSONArray("data");
		 int l = (d != null ? d.length() : 0);
		 for (int i = 0; i < l; i++) {
		 JSONObject o = d.getJSONObject(i);
		 String id = o.getString("eid");
		 String title = o.getString("name");
		 String description = o
		 .getString("description");
		 String start_time = o
		 .getString("start_time");
		 String photoURL = o.getString("pic_big");
		
		 Double latitude = null;
		 Double longitude = null;
		 try {
		 JSONObject venue = o
		 .getJSONObject("venue");
		 latitude = venue.getDouble("latitude");
		 longitude = venue
		 .getDouble("longitude");
		 } catch (Exception e) {
		 Log.w("Facebook-Example", e.getCause()
		 + "JSON Error in response");
		 }
		
		 EventsHelper f = new EventsHelper();
		 f.setId(id);
		 f.setTitle(title);
		 f.setDescription(description);
		 f.setStart_time(start_time);
		 f.setPhotoURL(photoURL);
		 f.setLatitude(latitude);
		 f.setLongitude(longitude);
		 events.add(f);
		
		 dbHelper.open();
		 dbHelper.createEvents(id, photoURL, title,
		 description, start_time, "0", "0");
		 dbHelper.close();
		 }
		
		 //
		 // //test impstazione call another events
		 // //
		 // JSONObject jo = json.getJSONObject("paging");
		 // String nextPage = jo.getString("next");
		 // d Log.w("NEXT_PAGE", nextPage);
		
		 }
		 } catch (JSONException e) {
		 Log.w("Facebook-Example", "JSON Error in response");
		 }
		 }
		
		 });
		 Request.executeBatchAndWait(request);
//
//		 Request request = new Request(
//		 session,
//		 "search?q=Trento&type=event&fields=id,name ,start_time,location,venue,cover",
//		 null,
//		 HttpMethod.GET,
//		 new Request.Callback() {
//		 public void onCompleted(Response response) {
//		 Log.w("RESPONSE", response.toString());
//		 // try {
//		 // if (response != null) {
//		 // final JSONObject json = response
//		 // .getGraphObject().getInnerJSONObject();
//		 // JSONArray d = json.getJSONArray("data");
//		 // int l = (d != null ? d.length() : 0);
//		 // for (int i = 0; i < l; i++) {
//		 // JSONObject o = d.getJSONObject(i);
//		 // String id = o.getString("id");
//		 // String title = o.getString("name");
//		 //// String description = o
//		 //// .getString("description");
//		 // String start_time = o
//		 // .getString("start_time");
//		 // //String photoURL = o.getString("pic_big");
//		 //
//		 //// Double latitude = null;
//		 //// Double longitude = null;
//		 //// try {
//		 //// JSONObject venue = o
//		 //// .getJSONObject("venue");
//		 //// latitude = venue.getDouble("latitude");
//		 //// longitude = venue
//		 //// .getDouble("longitude");
//		 //// } catch (Exception e) {
//		 //// Log.w("Facebook-Example", e.getCause()
//		 //// + "JSON Error in response");
//		 //// }
//		 //
//		 // EventsHelper f = new EventsHelper();
//		 // f.setId(id);
//		 // f.setTitle(title);
//		 // //f.setDescription(description);
//		 // f.setStart_time(start_time);
//		 //// f.setPhotoURL(photoURL);
//		 //// f.setLatitude(latitude);
//		 //// f.setLongitude(longitude);
//		 // events.add(f);
//		 //
//		 //// dbHelper.open();
//		 //// dbHelper.createEvents(id, photoURL, title,
//		 //// description, start_time, "0", "0");
//		 // //dbHelper.close();
//		 // }
//		 //
//		 // //
//		 // // //test impstazione call another events
//		 // // //
//		 // // JSONObject jo = json.getJSONObject("paging");
//		 // // String nextPage = jo.getString("next");
//		 // // d Log.w("NEXT_PAGE", nextPage);
//		 //
//		 // }
//		 // } catch (JSONException e) {
//		 // Log.w("Facebook-Example", "JSON Error in response");
//		 // }
//		 }
//		 }
//		 );
//		 Request.executeBatchAndWait(request);
		
		
		 // cliclo la lista di elementi scaricare l'immagine relativa all'evento
		 for (int i = 0; i < events.size(); i++) {
		 String URLPhoto = events.get(i).getPhotoURL();
		 events.get(i).setPhoto(getBitmapFromURL(URLPhoto));
		 Log.w("URLImage", URLPhoto);
		 }
		
		
		
//		URL request = null;
//		try {
//			request = new URL(
//					"https://graph.facebook.com/v2.0/search?q=Trento&type=event&fields=id,name ,start_time,location,venue,cover?access_token="
//							+ session.getAccessToken()
//							+ "&format=json&sdk=android");
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		HttpURLConnection conn = null;
//		int code = 0;
//		try {
//			conn = (HttpURLConnection) request.openConnection();
//			conn.connect();
//			
//			code = conn.getResponseCode();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		Log.w("code", Integer.toString(code));
		return (ArrayList) events;
	}

	@Override
	protected void onPostExecute(ArrayList<EventsHelper> result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		Log.w("Async Task", "on post excute");

		for (int i = 0; i < result.size(); i++) {
			events.add(result.get(i));
			adapter.notifyDataSetChanged();
		}
		MainActivity.setListEvents(result);
		Fragment_main.flag_loading = false;
		if (dialog.isShowing())
			dialog.dismiss();
	}

	public Bitmap getBitmapFromURL(String imageUrl) {
		try {
			URL url = new URL(imageUrl);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
