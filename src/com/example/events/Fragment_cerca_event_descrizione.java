package com.example.events;

	import java.util.List;

	import com.example.events.EventsHelper;
	import com.example.events.Fragment_Cerca;
	import com.example.events.MainActivity;
	import com.example.events.R;

	import android.app.Fragment;
	import android.os.Bundle;
	import android.view.LayoutInflater;
	import android.view.View;
	import android.view.ViewGroup;
	import android.widget.TextView;


	public class Fragment_cerca_event_descrizione extends Fragment{

		List<EventsHelper> events;
		
		@Override 
		 public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle saveInstanceState){
		 View view= inflater.inflate(R.layout.fragment_description,container,false);
		 events=Fragment_Cerca.getEventcerca();
			
		 TextView tx=(TextView)view.findViewById(R.id.textView_eventdescription);
			tx.setText(events.get(MainActivity.getidEvents()).getDescription());
			
			
		return view;
		}

	}

