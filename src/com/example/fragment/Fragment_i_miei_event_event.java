package com.example.fragment;

import java.util.List;
import java.util.Locale;

import com.example.download.DownloadMyEvents;
import com.example.events.MainActivity;
import com.example.events.R;
import com.example.events.R.id;
import com.example.events.R.layout;
import com.example.helper.EventsHelper;
import com.example.helper.StorageHelper;

import database.DbAdapter;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Fragment_i_miei_event_event extends Fragment {

	List<EventsHelper> events;
	private DbAdapter dbHelper;
	private String idEvent;
	private String descrizione;
	private double lat;
	private double lon;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		events=DownloadMyEvents.getmieieeventi();
		
		
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle saveInstanceState) {
		Log.w("Fragment_event", "On Create View");
		View view = inflater.inflate(R.layout.fragment_event, container, false);
		System.out.println("size: "+events.size());
		idEvent = getArguments().getString("id");
		descrizione = getArguments().getString("descrizione");
		lat = getArguments().getDouble("lat");
		lon = getArguments().getDouble("lon");
		
		Bundle bundle = new Bundle();
		bundle.putString("descrizione", descrizione);
		Fragment fragment=new Fragment_i_miei_event_descrizione();
		fragment.setArguments(bundle);
		FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.content_frame2, fragment, "descriptionfragment");
        transaction.commit();
	
		ImageView imageEvent = (ImageView) view.findViewById(R.id.image_event);
		//cosa vecchia			TextView descizione = (TextView) view.findViewById(R.id.descrizione_event);
		
		Bitmap bitmap = StorageHelper.loadImageFromStorage(
				StorageHelper.pathStorage,idEvent);
		if (bitmap != null) {

			BitmapDrawable bdrawable = new BitmapDrawable(bitmap);
			imageEvent.setBackgroundDrawable(bdrawable);
		} else {
			imageEvent.setBackgroundResource(R.drawable.default_event);
		}
		/*
//cosa vecchia			descizione.setText(events.get(MainActivity.getidEvents()).getDescription());
//cosa vecchia			descizione.setMovementMethod(new ScrollingMovementMethod());
//		final Fragment_descritpion fragment = new Fragment_descritpion();
//		final Fragment_partecipant fragment2 = new Fragment_partecipant();
//
//		FragmentManager manager = getFragmentManager();
//		FragmentTransaction transaction = manager.beginTransaction();
//		transaction.add(R.id.fragment_event, fragment, "descriptionfragment");
//		transaction.commit();
*/
		Button b0 = (Button) view.findViewById(R.id.button_map_event);
		b0.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				invokeGoogleMaps();

			}
		});

		Button b1 = (Button) view.findViewById(R.id.button_description_event);
		b1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				bundle.putString("descrizione", descrizione);
				FragmentManager manager = getFragmentManager();
				FragmentTransaction transaction = manager.beginTransaction();
				Fragment fragment = new Fragment_i_miei_event_descrizione();
				fragment.setArguments(bundle);
				transaction.replace(R.id.content_frame2, fragment,
						"description_event");
				transaction.commit();

			}
		});

		Button b2 = (Button) view.findViewById(R.id.button_partecipa_event);
		b2.setText("delete");
		b2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				dbHelper = new DbAdapter(getActivity().getApplicationContext());
				dbHelper.open();

				String id = "" + idEvent;
				Cursor c = dbHelper.fetchEventById(id);
				System.out.println("id: " + id);
				System.out.println("numero di righe: " + c.getCount());
				getActivity().startManagingCursor(c);
				System.out.println("Curosor c=" + c.moveToFirst());

				if(c.moveToFirst()==true){
					Context context = getActivity().getApplicationContext();
					CharSequence text = "eliminato l evento!";
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
				String ID = "" + c.getString(0);
				String IMAGE = "" + c.getString(1);
				String TITLE = "" + c.getString(2);
				String DESCRIPTION = "" + c.getString(3);
				String STARTTIME = "" + c.getString(4);
				String ENDTIME = "" + c.getString(5);
				String LOCATION = "" + c.getString(6);
				String MY = "" + 0;
				String latitude="" + c.getString(8);
				String longitude="" + c.getString(9);


				c.close();
				System.out.println(dbHelper.deleteEvents(ID));
				dbHelper.createEvents(ID, IMAGE, TITLE, DESCRIPTION, STARTTIME,
						ENDTIME, LOCATION, MY,latitude,longitude);


				Cursor c2 = dbHelper.fetchEventById(id);
				System.out.println("id: " + id);
				System.out.println("numero di righe: " + c2.getCount());
				getActivity().startManagingCursor(c);
				

				c2.close();

			}
			else{
				Context context1 = getActivity().getApplicationContext();
				CharSequence text1 = "impossibile eliminare se partecipi!";
				int duration1 = Toast.LENGTH_SHORT;

				Toast toast1 = Toast.makeText(context1, text1, duration1);
				toast1.show();
				
			}
			}
		
	});

		Button b3 = (Button) view.findViewById(R.id.button_partecipant_event);
		b3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Fragment fragment2= new Fragment_partecipant();
				Bundle args = new Bundle();
				args.putString("id", idEvent);
				fragment2.setArguments(args);
				FragmentManager manager = getFragmentManager();
				FragmentTransaction transaction = manager.beginTransaction();
				transaction.replace(R.id.content_frame2, fragment2,
						"partecipa_event");
				transaction.commit();

			}
		});

		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	public void invokeGoogleMaps() {

		// Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
		// Uri.parse("http://maps.google.com/maps?daddr="
		// + events.get(MainActivity.getidEvents()).getLongitude()
		// + "," + events.get(MainActivity.getidEvents()).getLatitude()));
		// this.startActivity(intent);
       if(events.get(MainActivity.getidEvents()).getLatitude()==null){
			//caso db 
    	   System.out.println("KKKKK: " + events.get(MainActivity.getidEvents()).getId());
    	   dbHelper = new DbAdapter(getActivity().getApplicationContext());
		   dbHelper.open();
		   String id = "" + events.get(MainActivity.getidEvents()).getId();
		   Cursor c = dbHelper.fetchEventById(id);
		   
		   if(c.moveToFirst()==true){
				
			
			String latitudemy="" + c.getString(8);
			String longitudmye="" + c.getString(9);

		  
			c.close();
			dbHelper.close();
		   
           String uri="geo:0,0?q="+latitudemy +","+longitudmye;
		   startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
		   
		   }
       
       
       
       
       
       
       
       }
		else{

			String uri="geo:0,0?q="+lat +","+lon;
			startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
	}
	
}





