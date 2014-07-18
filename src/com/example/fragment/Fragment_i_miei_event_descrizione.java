package com.example.fragment;

import java.util.List;

import com.example.download.DownloadMyEvents;
import com.example.events.MainActivity;
import com.example.events.R;
import com.example.events.R.id;
import com.example.events.R.layout;
import com.example.helper.EventsHelper;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Fragment_i_miei_event_descrizione extends Fragment {
	List<EventsHelper> events;
	
	@Override 
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle saveInstanceState){
	 View view= inflater.inflate(R.layout.fragment_description,container,false);
	 events=DownloadMyEvents.getmieieeventi();
		System.out.println("here: "+MainActivity.getidEvents());
	 TextView tx=(TextView)view.findViewById(R.id.textView_eventdescription);
		tx.setText(events.get(MainActivity.getidEvents()).getDescription());
		
		
	return view;
	}

}
