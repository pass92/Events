package com.example.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.example.adapter.AdapterListView;
import com.example.download.DownloadMyEvents;
import com.example.events.MainActivity;
import com.example.events.R;
import com.example.events.R.id;
import com.example.events.R.layout;
import com.example.helper.Communicator;
import com.example.helper.EventsHelper;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Fragment_i_miei_eventi extends Fragment {
	private ProgressDialog dialog;
	private ListView listView;
	private Communicator comm;
	private List<EventsHelper> events;
	private AdapterListView adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle saveInstanceState) {
		View view = inflater.inflate(R.layout.fragment_i_miei_eventi,
				container, false);
      
		comm = (Communicator) getActivity();
		listView = (ListView) view.findViewById(R.id.listview_my_events);
		if (saveInstanceState != null) {
			events = (List<EventsHelper>) saveInstanceState
					.getSerializable("events");
			adapter = new AdapterListView(view.getContext(),
					(ArrayList<EventsHelper>) events);
			
			listView.setAdapter(adapter);
		} else {
			dialog = ProgressDialog.show(view.getContext(), "", "Attendi...",
					false, true);
			
			DownloadMyEvents taskEvents = new DownloadMyEvents(view, listView,
					dialog, view.getContext(), events);
			taskEvents.execute();
			
		}

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MainActivity.setidEvents(position);
				comm.respond("fragment_i_miei_event", position);
			}
		});
	
		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		Bundle args = new Bundle();
		args.putSerializable("events", (Serializable) events);
		outState.putAll(args);
	}
}