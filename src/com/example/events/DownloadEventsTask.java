package com.example.events;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Request.GraphUserListCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class DownloadEventsTask extends
		AsyncTask<Void, Void, ArrayList<EventsHelper>> {

	String fqlQuery = "select eid,name,description,start_time, pic_big from event where eid in (SELECT eid FROM event WHERE contains("
			+ "'{Trento}'" + ")) order by start_time ASC";
	Bundle params = new Bundle();
	Session session;

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
									String photo = o.getString("pic_big");

									EventsHelper f = new EventsHelper();
									f.setId(id);
									f.setTitle(title);
									f.setDescription(description);
									f.setStart_time(start_time);
									f.setPhoto(photo);
									events.add(f);
								}
							}
						} catch (JSONException e) {
							Log.w("Facebook-Example", "JSON Error in response");
						}
					}

				});
		Request.executeBatchAndWait(request);
				
		return (ArrayList) events;
	}

	@Override
	protected void onPostExecute(ArrayList<EventsHelper> result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		Log.w("Async Task", "on post excute");
		for (int i = 0; i < result.size(); i++)
			Log.w("Name: ", result.get(i).getTitle());
	}
}
