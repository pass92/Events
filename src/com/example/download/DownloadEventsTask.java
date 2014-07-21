package com.example.download;

import com.example.adapter.AdapterListView;
import com.example.events.MainActivity;
import com.example.events.R;
import com.example.fragment.Fragment_impostazioni;
import com.example.fragment.Fragment_main;
import com.example.fragment.Fragment_partecipant;
import com.example.helper.Communicator;
import com.example.helper.DistanceBeetwenPoint;
import com.example.helper.EventsHelper;
import com.example.helper.LatLng;
import com.example.helper.StorageHelper;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.android.Facebook;
import com.facebook.model.GraphObject;
import com.google.android.maps.GeoPoint;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import android.content.ContextWrapper;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
	private int offsetQuery;
	private Integer limitQuery;
	private AdapterListView adapter;
	private List<EventsHelper> events;

	private int raggio;
	private double latitude;
	private double longitude;
	private double difLAt;
	private double difLong;
	private String filter;
	private String filterCityQuery;

	Geocoder geocoder;
	public static boolean start = true;

	public DownloadEventsTask(View view, Communicator comm, ListView l,
			ProgressDialog dialog, Context context, String city,
			int offsetQuery, Integer limitQuery, AdapterListView adapter,
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

		session = MainActivity.session;
		if (city == "") {
			city = Fragment_impostazioni.loadDafaultCity(context);

			if(Geocoder.isPresent()){
			    try {
			        String location = city;
			        Geocoder gc = new Geocoder(context);
			        List<Address> addresses= gc.getFromLocationName(location, 5); // get the found Address Objects

			        List<LatLng> ll = new ArrayList<LatLng>(addresses.size()); // A list to save the coordinates if they are available
			        for(Address a : addresses){
			            if(a.hasLatitude() && a.hasLongitude()){
			                ll.add(new LatLng(a.getLatitude(), a.getLongitude()));
			            }  
			        }  
			        
					latitude = ll.get(0).getLat();
					longitude = ll.get(0).getLon();
			    } catch (IOException e) {
			         // handle the exception
			    }
			}
			else{
				latitude = 46;
				longitude = 11;
			}
			
			

			raggio = Fragment_impostazioni.loadUserDatails(view.getContext());
			difLAt = (1 * raggio) / (111.22263);
			difLong = (1 * raggio) / (772.1345);
		} else {
			raggio = Fragment_impostazioni.loadUserDatails(view.getContext());
			longitude = Fragment_main.longitude;
			latitude = Fragment_main.latitude;
			difLAt = (1 * raggio) / (111.22263);
			difLong = (1 * raggio) / (772.1345);
		}

		filter = Fragment_impostazioni.loadFilters(context);
		if (filter != "")
			filterCityQuery = city + "%20" + filter;
		else
			filterCityQuery = city;

	}

	@Override
	protected ArrayList<EventsHelper> doInBackground(Void... params) {
		// TODO Auto-generated method stub
		final List<EventsHelper> events = new ArrayList<EventsHelper>();
		Log.w("Async Task", "doInBackground start!");

		String url = "https://graph.facebook.com/search?q=(" + filterCityQuery
				+ ")&type=event&access_token=" + session.getAccessToken()
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

				if (latitude > (this.latitude - difLAt)
						&& latitude < (this.latitude + difLAt)
						&& longitude > (this.longitude - difLong)
						&& longitude < (this.longitude + difLong)) {
					events.add(f);
				
					dbHelper.open();
					dbHelper.createEvents(id, pathPhoto, title, description,
						start_time, "0", "0","0",""+this.latitude,""+this.longitude);
					dbHelper.close();

				}

				// Log.w("LOAD PREFERENCES	",
				// Integer.toString(Fragment_impostazioni.loadUserDatails(context)));


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
		if (offsetQuery < events.size() && events.size() - offsetQuery >= 10) {
			for (int i = offsetQuery; i < 10 + offsetQuery; i++) {
				String URLPhoto = events.get(i).getPhotoURL();
				if (URLPhoto != null) {// salvo nell internal

					StorageHelper.saveToInternalSorage(
							getBitmapFromURL(URLPhoto), events.get(i).getId());
					// System.out.println("result: "+
					// loadImageFromStorage("/storage/sdcard0"));

					// events.get(i).setPhoto(loadImageFromStorage("/storage/sdcard0",events.get(i).getId()));

				} else {

					Bitmap bitmap = BitmapFactory.decodeResource(
							context.getResources(), R.drawable.default_event);
					events.get(i).setPhoto(bitmap);
				}
			}
		} else {
			if ((events.size() - offsetQuery) > 0 || start) {
				for (int i = offsetQuery; i < offsetQuery
						+ (events.size() - offsetQuery); i++) {
					String URLPhoto = events.get(i).getPhotoURL();
					if (URLPhoto != null) {// salvo nell internal

						StorageHelper.saveToInternalSorage(
								getBitmapFromURL(URLPhoto), events.get(i)
										.getId());

					} else {

						Bitmap bitmap = BitmapFactory.decodeResource(
								context.getResources(),
								R.drawable.default_event);
						events.get(i).setPhoto(bitmap);
					}
				}
			}
		}

		return (ArrayList<EventsHelper>) events;
	}

	@Override
	protected void onPostExecute(ArrayList<EventsHelper> result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		Log.w("Async Task", "on post excute");

		if (offsetQuery < result.size() && result.size() - offsetQuery >= 10) {

			List<EventsHelper> list = new ArrayList<EventsHelper>();
			for (int i = offsetQuery; i < 10 + offsetQuery; i++) {
				events.add(result.get(i));
				adapter.notifyDataSetChanged();
				list.add(result.get(i));
			}

			MainActivity.setListEvents(list);

			Fragment_main.flag_loading = false;
		} else {
			if ((events.size() - offsetQuery) > 0 || start) {
				List<EventsHelper> list = new ArrayList<EventsHelper>();
				for (int i = offsetQuery; i < offsetQuery
						+ (result.size() - offsetQuery); i++) {
					events.add(result.get(i));
					adapter.notifyDataSetChanged();
					list.add(result.get(i));
				}

				MainActivity.setListEvents(list);

				Fragment_main.flag_loading = false;
			}
		}

		if (dialog.isShowing())
			dialog.dismiss();
		start = false;
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

	private Bitmap codec(Bitmap src, Bitmap.CompressFormat format, int quality) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		src.compress(format, quality, os);

		byte[] array = os.toByteArray();
		return BitmapFactory.decodeByteArray(array, 0, array.length);
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

	
	
	// AGGIUNTA////////////////////////////////////////////////////
	/*
	 * private String saveToInternalSorage(Bitmap bitmapImage, String id) {
	 * 
	 * String filename = id+".jpg"; File sd =
	 * Environment.getExternalStorageDirectory(); File dest = new File(sd,
	 * filename);
	 * 
	 * try { FileOutputStream out = new FileOutputStream(dest);
	 * bitmapImage.compress(Bitmap.CompressFormat.PNG, 90, out); out.flush();
	 * out.close(); } catch (Exception e) { e.printStackTrace(); } return
	 * filename;
	 * 
	 * }
	 */

	// ////////////////////////////////////////////

}
