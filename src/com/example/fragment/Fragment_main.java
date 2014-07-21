package com.example.fragment;

import java.io.IOException;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.adapter.AdapterListView;
import com.example.download.DownloadEventsTask;
import com.example.events.MainActivity;
import com.example.events.R;
import com.example.events.R.id;
import com.example.events.R.layout;
import com.example.helper.Communicator;
import com.example.helper.EventsHelper;
import com.example.helper.GPSTracker;

import database.DbAdapter;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Fragment_main extends Fragment {
	Button b1;
	private static List<EventsHelper> events = new ArrayList<EventsHelper>();
	private ProgressDialog dialog;

	public static Boolean flag_loading = true;

	// adapter listView
	private AdapterListView adapter;

	// Name city where the USER request a Events
	String city = "";

	// offset on query facebook and Limit
	private static int offsetQuery = 0;
	Integer limitQuery = 10;

	// latitudine Longitudine
	public static double latitude = 0;
	public static double longitude = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle saveInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main, container, false);
		final Communicator comm;
		comm = (Communicator) getActivity();
		// ViewGroup l=(ViewGroup)view.findViewById(R.id.layoutTest);
		final ListView lv = (ListView) view.findViewById(R.id.listview_events);

		// set adapter
		adapter = new AdapterListView(view.getContext(), events);
		lv.setAdapter(adapter);

		GPSTracker gps = gps = new GPSTracker(view.getContext());
		if (gps.canGetLocation()) {

			latitude = gps.getLatitude();
			longitude = gps.getLongitude();

			// \n is for new line
			// Toast.makeText(view.getContext(), "Your Location is - \nLat: " +
			// latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
		} else {
			// can't get location
			// GPS or Network is not enabled
			// Ask user to enable GPS/network in settings
			gps.showSettingsAlert();
		}

		Geocoder geocoder;
		List<Address> addresses = null;
		geocoder = new Geocoder(view.getContext(), Locale.getDefault());
		try {
			addresses = geocoder.getFromLocation(latitude, longitude, 1);
			// Toast.makeText(view.getContext(),"Your Location is - \nLat: " +
			// latitude + "\nLong: " + longitude +"\n"+
			// addresses.get(0).getAddressLine(1) , Toast.LENGTH_LONG).show();
			String Str = addresses.get(0).getAddressLine(1);

			String[] Res = Str.split("[\\p{Punct}\\s]+");
			Toast.makeText(view.getContext(), "[" + Res[1] + "]",
					Toast.LENGTH_LONG).show();
			if (addresses != null)
				city = Res[1];

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// print on screen events
		if (events.size() == 0) {
			dialog = ProgressDialog.show(view.getContext(), "", "Attendi...",
					false, true);
			DownloadEventsTask taskEvents = new DownloadEventsTask(view, comm,
					lv, dialog, view.getContext(), city, offsetQuery,
					limitQuery, adapter, (ArrayList<EventsHelper>) events);
			taskEvents.execute();
		}
		// else {
		// events = MainActivity.getListEvents();
		// adapter = new AdapterListView(view.getContext(),
		// (ArrayList<EventsHelper>) events);
		// lv.setAdapter(adapter);
		// }

		// Aggiungo l'ascoltatore per aprire maggiori dettagli dall'elenco della
		// listView

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				MainActivity.setidEvents(position);
				comm.respond("fragment_event", position);
			}
		});

		lv.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				final int lastItem = firstVisibleItem + visibleItemCount;
				if (lastItem == totalItemCount) {

					if (flag_loading == false) {
						Log.w("ListView", "End");
						flag_loading = true;

						dialog = ProgressDialog.show(view.getContext(), "",
								"Attendi...", false, true);
						offsetQuery = offsetQuery + 10;
						DownloadEventsTask taskEvents = new DownloadEventsTask(
								view, comm, lv, dialog, view.getContext(),
								city, offsetQuery, limitQuery, adapter,
								(ArrayList<EventsHelper>) events);
						taskEvents.execute();
					}
				}
			}
		});

		return view;
	}

}
