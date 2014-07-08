package com.example.events;

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

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

public class DownloadEventsTask extends
		AsyncTask<Void, Void, ArrayList<EventsHelper>> {

	String fqlQuery = "select eid,name,description,start_time, pic_big from event where eid in (SELECT eid FROM event WHERE contains("
			+ "'{Trento}'" + ")) order by start_time ASC";
	Bundle params = new Bundle();
	Session session;
	View view;
	Communicator comm;
	ListView l;

	DownloadEventsTask(View view, Communicator comm, ListView l) {
		this.view = view;
		this.comm = comm;
		this.l = l;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
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
								Log.d("Facebook-Example-events Request",
										"d.length(): " + l);

								for (int i = 0; i < l; i++) {
									JSONObject o = d.getJSONObject(i);
									String id = o.getString("eid");
									String title = o.getString("name");
									String description = o
											.getString("description");
									String start_time = o
											.getString("start_time");
									String photoURL = o.getString("pic_big");

									EventsHelper f = new EventsHelper();
									f.setId(id);
									f.setTitle(title);
									f.setDescription(description);
									f.setStart_time(start_time);
									f.setPhotoURL(photoURL);
									events.add(f);
								}
							}
						} catch (JSONException e) {
							Log.w("Facebook-Example", "JSON Error in response");
						}
					}

				});
		Request.executeBatchAndWait(request);

		// cliclo la lista di elementi scaricare l'immagine relativa all'evento
		for (int i = 0; i < 10; i++) {
			String URLPhoto = events.get(i).getPhotoURL();
			events.get(i).setPhoto(getBitmapFromURL(URLPhoto));
			Log.w("URLImage", URLPhoto);
		}

		return (ArrayList) events;
	}

	@Override
	protected void onPostExecute(ArrayList<EventsHelper> events) {
		// TODO Auto-generated method stub
		super.onPostExecute(events);
		Log.w("Async Task", "on post excute");
		// for (int i = 0; i < result.size(); i++)
		// Log.w("Name: ", result.get(i).getTitle());
		MainActivity.setListEvents(events);
		AdapterListView adapter = new AdapterListView(view.getContext(), events);
		l.setAdapter(adapter);
		
//		for (int i = 0; i < events.size(); i++) {
//			
//			Button b11 = new Button(view.getContext());
//			b11.setId(i);
//			// b11.setId(events.get(i).getId().toString());
//			b11.setText("" + events.get(i).getDescription());
//			b11.setMaxHeight(70);
//			BitmapDrawable bdrawable = new BitmapDrawable(events.get(i).getPhoto());
//			
//			b11.setBackgroundDrawable(bdrawable);
//			
//			b11.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// System.out.println(v.getId());
//					MainActivity.setidEvents(v.getId());
//
//					comm.respond("fragment_event", v.getId());
//					// TODO Auto-generated method stub
//
//				}
//			});
//
//			l.addView(b11);
////			Button b10 = new Button(view.getContext());
////			b10.setText("" + events.get(i).getStart_time());
////			b10.setHeight(75);
////			l.addView(b10);
//		}

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
