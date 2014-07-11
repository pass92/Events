package com.example.events;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import database.DbAdapter;
import android.app.Fragment;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;

public class Fragment_Cerca extends Fragment {
	
	private String filter;
	private DbAdapter dbHelper;
	private Cursor cursor;
	private Communicator comm;
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle saveInstanceState) {
	   View view = inflater.inflate(R.layout.fragment_cerca, container, false);
	   final ListView lv=(ListView)view.findViewById(R.id.listView_cerca);
	   final TextView tx=(TextView)view.findViewById(R.id.editText_crea_event);
	   comm = (Communicator) getActivity();
	   tx.setOnFocusChangeListener(new OnFocusChangeListener() {
		   
		
	@Override
		public void onFocusChange(View v, boolean hasFocus) {
			// TODO Auto-generated method stub
		    if (!hasFocus) {
		           System.out.println("finish");
		          }
		     
			tx.setText("");
			
		}
	});
	   
	    
			 
	  Button b=(Button)view.findViewById(R.id.button_cerca_event);
	  b.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			filter=new String(tx.getText().toString());
			ArrayList <EventsHelper> events=new ArrayList();	
			
			
		    dbHelper = new DbAdapter(getActivity().getApplicationContext());
			dbHelper.open();
			Cursor c=dbHelper.fetchEventsByFilter(filter);
			
			System.out.println("numero di righe: "+c.getCount());
			getActivity().startManagingCursor(c);
			
			
			System.out.println("Curosor c=" + c.moveToFirst());
					
			
			if(c.moveToFirst()==true){
				while(!c.isAfterLast()){
					
					
					EventsHelper eventHelper=new EventsHelper();
					eventHelper.setId(c.getString(0));
					eventHelper.setPhotoURL(c.getString(1));
					eventHelper.setDescription( c.getString(3));
					eventHelper.setTitle(c.getString(2));
					eventHelper.setStart_time(c.getString(4));
					//eventHelper.setPhoto();
					events.add(eventHelper);
					
					
					System.out.println("===================Risultato delle query:================ ");
					//STAMPO TUTTI I CAMPI DEL RECORD
					System.out.println("Curosor c=" + c.getString(0));
					System.out.println("Curosor c=" + (c.getString(1)));
					System.out.println("Curosor c=" + c.getString(2));
					System.out.println("Curosor c=" + c.getString(3));
					System.out.println("Curosor start_time=" + c.getString(4));
					System.out.println("Curosor end_time=" + c.getString(5));
					System.out.println("Curosor location=" + c.getString(6));
					System.out.println("==================fine risultato query==================");
					c.moveToNext();
					

				
				}
				System.out.println("grandezza lista: "+events.size());
				
				AdapterListViewCercaEvent adapter = new AdapterListViewCercaEvent(getActivity().getApplicationContext(),
						(ArrayList<EventsHelper>) events);
				lv.setAdapter(adapter);
				lv.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						MainActivity.setidEvents(position);
						comm.respond("fragment_event", position);
					}
				});

                //manca aggiungere photo quindi convertire photoUrL con task asincrono vedi pass
/*
	public Bitmap getBitmapFromURL(String imageUrl) {
		try {
			URL url = new URL(imageUrl);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	events.get(i).setPhoto(getBitmapFromURL(URLPhoto));
	
	*/
				
			
			}
			
			
			
			
			
		}
	});
	   
	   return view;
       
	
		
	}	
}
