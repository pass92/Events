package com.example.download;

import com.example.adapter.AdapterListView;
import com.example.events.MainActivity;
import com.example.fragment.Fragment_main;
import com.example.helper.Communicator;
import com.example.helper.EventsHelper;
import com.example.helper.StorageHelper;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Request.GraphUserListCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.callback.Callback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;

import database.DbAdapter;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

public class DownloadMyEvents extends
		AsyncTask<Void, Void, ArrayList<EventsHelper>> {

	String fqlQuery;
	Bundle params = new Bundle();
	Session session;
	View view;
	Communicator comm;
	ListView l;
	private ProgressDialog dialog;
	private AdapterListView adapter;
	private static List<EventsHelper> events;
	private Context context;
	private DbAdapter dbHelper = new DbAdapter(context);

	public static List<EventsHelper> getmieieeventi() {
		return events;
	}

	public DownloadMyEvents(View view, ListView l, ProgressDialog dialog,
			Context context, List<EventsHelper> events, AdapterListView adapter) {
		this.view = view;
		this.l = l;
		this.dialog = dialog;
		this.context = context;
		this.events = events;
		this.adapter = adapter;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		fqlQuery = "select eid,name,description,start_time, pic_big,venue from event where eid in (SELECT eid,rsvp_status FROM event_member WHERE uid=me() and rsvp_status=\""
				+ "attending" + "\")";
		Log.w("OnPreExecute", fqlQuery);
		params.putString("q", fqlQuery);
		session = MainActivity.session;
	}

	@Override
	protected ArrayList<EventsHelper> doInBackground(Void... params) {
		// TODO Auto-generated method stub
		final List<EventsHelper> events = new ArrayList<EventsHelper>();
		Log.w("Async Task", "doInBackground start!");

		Request request = new Request(session, "/fql", this.params,
				HttpMethod.GET, new Request.Callback() {
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
									EventsHelper f = new EventsHelper();
									f.setId(id);
									f.setTitle(title);
									f.setDescription(description);
									f.setStart_time(start_time);
									f.setPhotoURL(photoURL);
									f.setLatitude(latitude);
									f.setLongitude(longitude);
									events.add(f);
									/*
									 * dbHelper.open();
									 * dbHelper.createEvents(id,photoURL, title,
									 * description,start_time, "0",
									 * "0","0","0","0"); dbHelper.close();
									 */
								}
							}
						} catch (JSONException e) {
							Log.w("Facebook-Example", "JSON Error in response");
						}
					}

				});
		Request.executeBatchAndWait(request);

		// cliclo la lista di elementi scaricare l'immagine relativa all'evento
		for (int i = 0; i < events.size(); i++) {
			String URLPhoto = events.get(i).getPhotoURL();

			StorageHelper.saveToInternalSorage(getBitmapFromURL(URLPhoto),
					events.get(i).getId());
			events.get(i).setPhoto(getBitmapFromURL(URLPhoto));
			Log.w("URLImage", URLPhoto);
		}

		return (ArrayList) events;
	}

	@Override
	protected void onPostExecute(ArrayList<EventsHelper> result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		Log.w("Async Task", "on post excute");
		// query

		dbHelper = new DbAdapter(context);
		dbHelper.open();
		Cursor c = dbHelper.fetchEventByMy("1");
		System.out.println("numero di righe: " + c.getCount());

		System.out.println("Curosor c=" + c.moveToFirst());

		if (c.moveToFirst() == true) {
			while (!c.isAfterLast()) {
				EventsHelper myevent = new EventsHelper();

				System.out
						.println("===================Risultato delle query:================ ");
				// STAMPO TUTTI I CAMPI DEL RECORD
				System.out.println("Curosor c=" + c.getString(0));
				System.out.println("Curosor c=" + (c.getString(1)));
				System.out.println("Curosor c=" + c.getString(2));
				System.out.println("Curosor c=" + c.getString(3));
				System.out.println("Curosor start_time=" + c.getString(4));
				System.out.println("Curosor end_time=" + c.getString(5));
				System.out.println("Curosor location=" + c.getString(6));

				System.out.println("Curosor latitude=" + c.getString(8));
				System.out.println("Curosor longitude=" + c.getString(9));

				System.out
						.println("==================fine risultato query==================");

				myevent.setId(c.getString(0));
				myevent.setPhotoURL(c.getString(1));
				myevent.setTitle(c.getString(2));
				myevent.setDescription(c.getString(3));
				myevent.setStart_time(c.getString(4));
				myevent.setEnd_time(c.getString(5));

				myevent.setMy_status(c.getString(7));

				result.add(myevent);
				c.moveToNext();

			}
		}
		c.close();

		for (int i = 0; i < result.size(); i++) {
			events.add(result.get(i));
			adapter.notifyDataSetChanged();

		}

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
