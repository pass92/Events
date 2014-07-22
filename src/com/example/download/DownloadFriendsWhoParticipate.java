package com.example.download;

import com.example.adapter.AdapterUser;
import com.example.events.MainActivity;
import com.example.helper.Communicator;
import com.example.helper.UserHelper;
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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

public class DownloadFriendsWhoParticipate extends
		AsyncTask<Void, Void, ArrayList<UserHelper>> {

	Session session;
	View view;
	Communicator comm;
	ListView l;
	private ProgressDialog dialog;
	private AdapterUser adapter;
	private final List<UserHelper> user;
	private Context context;
	private String idEvent;

	public DownloadFriendsWhoParticipate(View view, ListView l,
			Context context, List<UserHelper> user, String idEvent,
			AdapterUser adapter) {
		this.view = view;
		this.l = l;
		this.context = context;
		this.user = user;
		this.idEvent = idEvent;
		this.adapter = adapter;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		session = MainActivity.session;
	}

	@Override
	protected ArrayList<UserHelper> doInBackground(Void... params) {
		// TODO Auto-generated method stub
		final List<UserHelper> user1 = new ArrayList<UserHelper>();
		Log.w("Async Task", "doInBackground start!");

		Request request = new Request(session, "/" + idEvent + "/attending",
				null, HttpMethod.GET, new Request.Callback() {
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

									String name = o.getString("name");
									String id = o.getString("id");

									UserHelper f = new UserHelper();
									f.setName(name);
									f.setId(id);
									user1.add(f);
								}
							}
						} catch (JSONException e) {
							Log.w("Facebook-Example", "JSON Error in response");
						}
					}

				});
		Request.executeBatchAndWait(request);

		// /picture?redirect=false
		// // cliclo la lista di elementi scaricare l'immagine relativa
		// all'evento
		// for (int i = 0; i < events.size(); i++) {
		// String URLPhoto = events.get(i).getPhotoURL();
		// events.get(i).setPhoto(getBitmapFromURL(URLPhoto));
		// Log.w("URLImage", URLPhoto);
		// }

		return (ArrayList<UserHelper>) user1;
	}

	@Override
	protected void onPostExecute(ArrayList<UserHelper> result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		Log.w("Async Task", "on post excute");

		for (int i = 0; i < result.size(); i++) {
			user.add(result.get(i));
			adapter.notifyDataSetChanged();
		}

		// Fragment_main.flag_loading = false;
		// if (dialog.isShowing())
		// dialog.dismiss();
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
