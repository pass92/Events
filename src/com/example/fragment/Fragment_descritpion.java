package com.example.fragment;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.RemoteViews;
import android.widget.TextView;

public class Fragment_descritpion extends Fragment{
	
	
	List<EventsHelper> events;
	private String descrizione;
	@Override 
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle saveInstanceState){
	 View view= inflater.inflate(R.layout.fragment_description,container,false);

	 descrizione= getArguments().getString("descrizione");
	 TextView tx=(TextView)view.findViewById(R.id.textView_eventdescription);
		tx.setText(""+descrizione);
		
		
	return view;

	}
}
