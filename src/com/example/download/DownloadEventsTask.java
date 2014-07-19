package com.example.download;

import com.example.adapter.AdapterListView;
import com.example.events.MainActivity;
import com.example.events.R;
import com.example.fragment.Fragment_main;
import com.example.helper.Communicator;
import com.example.helper.EventsHelper;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.android.Facebook;
import com.facebook.model.GraphObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import database.DbAdapter;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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

	public DownloadEventsTask(View view, Communicator comm, ListView l,
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
		// fqlQuery =
		// "select eid,name,description,start_time, pic_big,venue from event where eid in (SELECT eid FROM event WHERE contains(\""
		// + city
		// + "\") or contains(\""
		// + "Italy"
		// + "\") ) and start_time > now() order by start_time ASC limit "
		// + Integer.toString(limitQuery)
		// + " offset "
		// + Integer.toString(offsetQuery); // order by start_time ASC
		// // fqlQuery =
		// //
		// "q=Trento&type=event&fields=id,name ,start_time,location,venue,cover";
		// Log.w("OnPreExecute", fqlQuery);
		// params.putString("q", fqlQuery);
		session = MainActivity.session;

	}

	@Override
	protected ArrayList<EventsHelper> doInBackground(Void... params) {
		// TODO Auto-generated method stub
		final List<EventsHelper> events = new ArrayList<EventsHelper>();
		Log.w("Async Task", "doInBackground start!");

		String url = "https://graph.facebook.com/search?q=" + city
				+ "&type=event&access_token=" + session.getAccessToken()
				+ "&fields=id,name,start_time,venue,cover,description";
		Log.w("URL REQUEST", url);
		JSONObject json = getRequestFromUrl(url);

		try {
			// final JSONObject json = response.getGraphObject()
			// .getInnerJSONObject();
			JSONArray d = json.getJSONArray("data");
			int l = (d != null ? d.length() : 0);
			for (int i = 0; i < l; i++) {
				JSONObject o = d.getJSONObject(i);
				String id = o.getString("id");
				String title = o.getString("name");
				String start_time = o.getString("start_time");
				// String photoURL = o.getString("source");

				String description = "";
				try {
					description = o.getString("description");
				} catch (Exception e) {
					Log.w("Facebook-Example", e.getCause() + "Description!");
				}

				Double latitude = 0.0;
				Double longitude = 0.0;
				JSONObject venue = null;
				try {
					venue = o.getJSONObject("venue");
					latitude = venue.getDouble("latitude");
					longitude = venue.getDouble("longitude");
				} catch (Exception e) {
					Log.w("Facebook-Example",
							e.getCause() + Integer.toString(i)
									+ "Get Latitude Longitude");
				}

				// cover
				String pathPhoto = null;
				JSONObject cover = null;
				try {
					cover = o.getJSONObject("cover");
					pathPhoto = cover.getString("source");
				} catch (Exception e) {
					Log.w("Facebook-Example",
							e.getCause() + Integer.toString(i)
									+ "Get Path Photo");
				}

				EventsHelper f = new EventsHelper();
				f.setId(id);
				f.setTitle(title);
				f.setDescription(description);
				f.setStart_time(start_time);
				f.setPhotoURL(pathPhoto);
				f.setLatitude(latitude);
				f.setLongitude(longitude);
				if ((f.getLatitude() > 46.0) && (f.getLatitude() < 46.1))
					events.add(f);

				dbHelper.open();
				dbHelper.createEvents(id, pathPhoto, title, description,
				start_time, "0", "0");
				dbHelper.close();
			}

			//
			// //test impstazione call another events
			// //
			// JSONObject jo = json.getJSONObject("paging");
			// String nextPage = jo.getString("next");
			// d Log.w("NEXT_PAGE", nextPage);

		} catch (JSONException e) {
			Log.w("Facebook-Example", "JSON Error in response");
		}

		// // cliclo la lista di elementi scaricare l'immagine relativa
		// all'evento
		for (int i = 0; i < 20; i++) {
			String URLPhoto = events.get(i).getPhotoURL();
			if (URLPhoto != null)
				events.get(i).setPhoto(getBitmapFromURL(URLPhoto));
			else {
				Bitmap bitmap = BitmapFactory.decodeResource(
						context.getResources(), R.drawable.default_event);
				events.get(i).setPhoto(bitmap);
			}
		}

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
			Log.w("DOWNLOAD IMAGE", imageUrl);
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

	public JSONObject getRequestFromUrl(String url) {
		InputStream is = null;
		try {
			AbstractHttpClient httpClient = new DefaultHttpClient();
			HttpGet get = new HttpGet(url);

			HttpResponse httpResponse = httpClient.execute(get);
			HttpEntity responseEntity = httpResponse.getEntity();
			is = responseEntity.getContent();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String json = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;

			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

			is.close();
			json = sb.toString();
			Log.w("JSON", json);
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		JSONObject jObj = null;
		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		return jObj;
	}

}
