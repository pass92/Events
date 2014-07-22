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
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Fragment_i_miei_eventi extends Fragment {
	private ProgressDialog dialog;
	private ListView listView;
	private Communicator comm; 
	private AdapterListView adapter;
	private boolean start = true;

	private List<EventsHelper> events = new ArrayList<EventsHelper>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle saveInstanceState) {
		View view = inflater.inflate(R.layout.fragment_i_miei_eventi,
				container, false);
 
		comm = (Communicator) getActivity();
		listView = (ListView) view.findViewById(R.id.listview_my_events);

		adapter = new AdapterListView(view.getContext(),
				(ArrayList<EventsHelper>) events);
		listView.setAdapter(adapter);
		// if (saveInstanceState != null) {
		// events = (List<EventsHelper>) saveInstanceState
		// .getSerializable("events");
		// adapter = new AdapterListView(view.getContext(),
		// (ArrayList<EventsHelper>) events);
		//
		// listView.setAdapter(adapter);
		// } else {
		// if(start){
		// start=false;

		dialog = ProgressDialog.show(view.getContext(), "", "Attendi...",
				false, true);

		DownloadMyEvents taskEvents = new DownloadMyEvents(view, listView,
				dialog, view.getContext(), events, adapter);
		taskEvents.execute();

		// }

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// MainActivity.setidEvents(position);
				// comm.respond("fragment_i_miei_event", position);
				Bundle bundle = new Bundle();
				bundle.putString("id", events.get(position).getId());
				bundle.putString("descrizione", events.get(position)
						.getDescription());
				bundle.putDouble("lat", events.get(position).getLatitude());
				bundle.putDouble("lon", events.get(position).getLongitude());

				Fragment fragment = new Fragment_i_miei_event_event();
				fragment.setArguments(bundle);
				FragmentManager manager = getFragmentManager();
				FragmentTransaction transaction = manager.beginTransaction();
				transaction.replace(R.id.content_frame, fragment);
				transaction.addToBackStack("event_bo");
				transaction.commit();
			}
		});

		return view;
	}

}