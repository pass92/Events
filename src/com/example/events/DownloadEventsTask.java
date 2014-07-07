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
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class DownloadEventsTask extends
		AsyncTask<Void, Void, ArrayList<EventsHelper>> {

	String fqlQuery = "select eid,name,description,start_time, pic_big from event where eid in (SELECT eid FROM event WHERE contains("
			+ "'{Trento}'" + ")) order by start_time ASC";
	Bundle params = new Bundle();
	Session session;
	View view;
	Communicator comm;
	ViewGroup l;
	DownloadEventsTask(View view,Communicator comm, ViewGroup l){
		this.view = view;
		this.comm = comm;
		this.l=l;
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
	protected void onPostExecute(ArrayList<EventsHelper> events) {
		// TODO Auto-generated method stub
		super.onPostExecute(events);
		Log.w("Async Task", "on post excute");
		//for (int i = 0; i < result.size(); i++)
		//	Log.w("Name: ", result.get(i).getTitle());
		MainActivity.setListEvents(events);
		int count=0;
		
        
		
		for(int i=99;i>89;i--){
			Button b11= new Button(view.getContext());
			b11.setId(i);
			//b11.setId(events.get(i).getId().toString());
			b11.setText(""+events.get(i).getDescription());
			b11.setMaxHeight(70);
			if(count%2==0){
		        b11.setBackgroundResource(R.drawable.festa);
			}
			else{
				b11.setBackgroundResource((R.drawable.sfondo));
			}
		    b11.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//System.out.println(v.getId());
					MainActivity.setidEvents(v.getId());
					
					
					comm.respond("fragment_event",v.getId());
					// TODO Auto-generated method stub
					
				}
			});
		   
			l.addView(b11);
			Button b10= new Button(view.getContext());
			b10.setText(""+events.get(i).getStart_time());
		    b10.setHeight(75);
		    l.addView(b10);
		    count++;
		}

	}  
}
