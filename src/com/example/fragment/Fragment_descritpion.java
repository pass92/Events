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
	
	@Override 
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle saveInstanceState){
	 View view= inflater.inflate(R.layout.fragment_description,container,false);
	 events=MainActivity.getListEvents();
	 TextView tx=(TextView)view.findViewById(R.id.textView_eventdescription);
		tx.setText(events.get(MainActivity.getidEvents()).getDescription());
		
		
	return view;
	
/*COSE VECCHIE 
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
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		getView().removeCallbacks(null);
	}
*/
	}
}
