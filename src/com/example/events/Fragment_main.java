package com.example.events;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class Fragment_main extends Fragment {
	Button b1;
	private static List<EventsHelper> events;
	private ProgressDialog dialog;

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

		if (MainActivity.getListEvents().size() == 0) {
			dialog = ProgressDialog.show(view.getContext(), "", "Attendi...",
					false, true);
			DownloadEventsTask taskEvents = new DownloadEventsTask(view, comm,
					lv, dialog);
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

		return view;
	}
}
/*
 * Bitmap contact_pic; //a picture to show in drawable Drawable drawable=new
 * BitmapDrawable(contact_pic); Drawable d =new
 * BitmapDrawable(getResources(),bitmap);
 */
