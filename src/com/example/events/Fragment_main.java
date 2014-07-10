package com.example.events;

import java.util.ArrayList;
import java.util.List;

import database.DbAdapter;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
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
	
    //TEST DB istanze
		private DbAdapter dbHelper;
		private Cursor cursor;
   //
		
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
		
		// ESPERIMENTI DB
				dbHelper = new DbAdapter(this.getActivity().getApplicationContext());
				dbHelper.open();
				//RECORD DA INSERIRE MANUALMENTE PER PROVE
				String id = "1";
				String description = "Appuntamento a via C n11 alle 19:30";
				String title = "Cena sushi";
				String end_time = "23:00";
				String start_time = "19:30";
				String location = "via C n11";
				String image = "http://image.jpeg";
				String id2 = "2";
				String description2 = "dormita di gruppo a roncobilaccio";
				String title2 = "sleep";
				String end_time2 = "00:00";
				String start_time2 = "19:30";
				String location2 = "prato piazzo";
				String image2 = "http://image2.jpeg";
		        
				//ELIMINO ELEMENTI PER ID PER LE PROVE
				dbHelper.deleteEvents(id);
				dbHelper.deleteEvents(id2);
				//CREO  2 EVENTI 
				dbHelper.createEvents(id, image, title, description, start_time,
						end_time, location);
				dbHelper.createEvents(id2, image2, title2, description2, start_time2,
						end_time2, location2);
		        
				// OK Cursor c=dbHelper.fetchEventsById();
				// OK Cursor c=dbHelper.fetchAllEvents();
				
				//METODO DI RICERCA SU FILTRO 
				Cursor c = dbHelper.fetchEventsByFilter("sushi");
		        //SETTO IL CURSORE SUL PRIMO RECORD 
				System.out.println("Curosor c=" + c.moveToFirst());
				System.out.println("===================Risultato delle query:================ ");
				//STAMPO TUTTI I CAMPI DEL RECORD
				System.out.println("Curosor c=" + c.getString(0));
				System.out.println("Curosor c=" + c.getString(1));
				System.out.println("Curosor c=" + c.getString(2));
				System.out.println("Curosor c=" + c.getString(3));
				System.out.println("Curosor c=" + c.getString(4));
				System.out.println("Curosor c=" + c.getString(5));
				System.out.println("Curosor c=" + c.getString(6));
				System.out.println("==================fine risultato query==================");
				/*
				 * NEL CASO VI FOSSERO PIU RECORD DA VERIFICARE FARE UN WHILE PER FAR 
				 * SCORRERE IL CURSORE
				 * while (!cursor.isAfterLast()) { Person person =
				 * cursorToPerson(cursor); people.add(person); cursor.moveToNext(); }
				 */
				
				
				dbHelper.close();
		        //CHIUDERE IL CURSORE
				//cursor.close();
				
				// Fine DB
		
		
		
		

		return view;
	}
}
/*
 * Bitmap contact_pic; //a picture to show in drawable Drawable drawable=new
 * BitmapDrawable(contact_pic); Drawable d =new
 * BitmapDrawable(getResources(),bitmap);
 */
