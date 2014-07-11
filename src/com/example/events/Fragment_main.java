package com.example.events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import database.DbAdapter;
import android.app.Fragment;
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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class Fragment_main extends Fragment {
	Button b1;
	private static List<EventsHelper> events;
	private ProgressDialog dialog;

	// TEST DB istanze
	private DbAdapter dbHelper;
	private Cursor cursor;
	private Boolean flag_loading = false;

	//Name city where the USER request a Events
	String city;

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
		ListView lv = (ListView) view.findViewById(R.id.listview_events);

		/*
		 * Get location from MainActivity
		 * Print out on a Screen The City
		 */
		if (MainActivity.l != null) {
			// get latitude and longitude of the location
			double lng = MainActivity.l.getLongitude();
			double lat = MainActivity.l.getLatitude();

			Context context = view.getContext();
			int duration = Toast.LENGTH_SHORT;

			Geocoder gcd = new Geocoder(context, Locale.getDefault());
			List<Address> addresses = null;
			try {
				addresses = gcd.getFromLocation(lat, lng, 1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (addresses.size() > 0) {
				city = addresses.get(0).getLocality();
				Toast toast = Toast.makeText(context, "City:["
						+ city +"]"+ Double.toString(lng)
						+ " " + Double.toString(lat), duration);
				toast.show();
			}

		
		if (MainActivity.getListEvents().size() == 0) {
			dialog = ProgressDialog.show(view.getContext(), "", "Attendi...",
					false, true);
			DownloadEventsTask taskEvents = new DownloadEventsTask(view, comm,
					lv, dialog, view.getContext(),city);
			taskEvents.execute();
		} else {
			events = MainActivity.getListEvents();
			AdapterListView adapter = new AdapterListView(view.getContext(),
					(ArrayList<EventsHelper>) events);
			lv.setAdapter(adapter);
		}

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
					Log.w("ListView", "End");
					if (flag_loading == false) {
						flag_loading = true;
						// additems();
						// DownloadEventsTask taskEvents = new
						// DownloadEventsTask(view, comm,
						// lv, dialog);
						// taskEvents.execute();
					}
				}
			}
		});

		
		}

		return view;
	}
}
/*
 * Bitmap contact_pic; //a picture to show in drawable Drawable drawable=new
 * BitmapDrawable(contact_pic); Drawable d =new
 * BitmapDrawable(getResources(),bitmap);
 */
