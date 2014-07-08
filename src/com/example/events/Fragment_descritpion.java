package com.example.events;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Fragment_descritpion extends Fragment{
	// Array per contenere eventi scaricati da Facebook
		private static List<EventsHelper> events;
		private int id;
	@Override 
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle saveInstanceState){
	 View view= inflater.inflate(R.layout.fragment_description,container,false);
	 ViewGroup l=(ViewGroup)view.findViewById(R.id.linearLayout2_description);
	 l.removeAllViews();
	 events=MainActivity.getListEvents();
	 id=MainActivity.getidEvents();
	 System.out.println("ID :"+id);
	 TextView t=new TextView(view.getContext());
	 t.setText(""+events.get(id).getDescription());
	 l.addView(t);
	 return view;
	}
}
