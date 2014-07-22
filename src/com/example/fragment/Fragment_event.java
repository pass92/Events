package com.example.fragment;

import java.util.List;
import java.util.Locale;

import com.example.events.MainActivity;
import com.example.events.R;
import com.example.events.R.id;
import com.example.events.R.layout;
import com.example.helper.EventsHelper;
import com.example.helper.StorageHelper;

import database.DbAdapter;
import database.DbHelper;
import android.app.Activity;
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
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Fragment_event extends Fragment {

	List<EventsHelper> events;
	private DbAdapter dbHelper;
	private String idEvent;
	private String descrizione;
	private Double lon;
	private Double lat;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		events = MainActivity.getListEvents();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle saveInstanceState) {
		Log.w("Fragment_event", "On Create View");
		View view = inflater.inflate(R.layout.fragment_event, container, false);

		//get IdEvent from bundle
		idEvent = getArguments().getString("id");
		descrizione = getArguments().getString("descrizione");
		lat = getArguments().getDouble("lat");
		lon = getArguments().getDouble("lon");
		
		Bundle bundle = new Bundle();
		bundle.putString("descrizione", descrizione);
		Fragment fragment = new Fragment_descritpion();
		fragment.setArguments(bundle);
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(R.id.content_frame2, fragment,
				"descriptionfragment");
		transaction.commit();

		ImageView imageEvent = (ImageView) view.findViewById(R.id.image_event);
		// cosa vecchia TextView descizione = (TextView)
		// view.findViewById(R.id.descrizione_event);
		Bitmap bitmap = StorageHelper.loadImageFromStorage(
				StorageHelper.pathStorage,
				idEvent);//events.get(MainActivity.getidEvents()).getId());
		if (bitmap != null) {

			BitmapDrawable bdrawable = new BitmapDrawable(bitmap);
			imageEvent.setBackgroundDrawable(bdrawable);
		} else {
			imageEvent.setBackgroundResource(R.drawable.default_event);
		}

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
				Fragment_descritpion fragment = new Fragment_descritpion();
				fragment.setArguments(bundle);
				transaction.replace(R.id.content_frame2, fragment,
						"description_event");
				transaction.commit();

			}
		});

		Button b2 = (Button) view.findViewById(R.id.button_partecipa_event);
		b2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Context context = getActivity().getApplicationContext();
				CharSequence text = "Salvato in i miei eventi!";
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
				//mieie eventi
				dbHelper = new DbAdapter(getActivity().getApplicationContext());
				dbHelper.open();

				String id = "" + idEvent; //events.get(MainActivity.getidEvents()).getId();
				Cursor c = dbHelper.fetchEventById(id);
				System.out.println("id: " + id);
				System.out.println("numero di righe: " + c.getCount());
				getActivity().startManagingCursor(c);
				System.out.println("Curosor c=" + c.moveToFirst());

				String ID = "" + c.getString(0);
				String IMAGE = "" + c.getString(1);
				String TITLE = "" + c.getString(2);
				String DESCRIPTION = "" + c.getString(3);
				String STARTTIME = "" + c.getString(4);
				String ENDTIME = "" + c.getString(5);
				String LOCATION = "" + c.getString(6);
				String MY = "" + 1;
				String latitude="" + c.getString(8);
				String longitude="" + c.getString(9);

				
				System.out.println(dbHelper.deleteEvents(ID));
				dbHelper.createEvents(ID, IMAGE, TITLE, DESCRIPTION, STARTTIME,
						ENDTIME, LOCATION, MY,latitude,longitude);


				Cursor c2 = dbHelper.fetchEventById(id);
				System.out.println("id: " + id);
				System.out.println("numero di righe: " + c2.getCount());
				getActivity().startManagingCursor(c);
				

				

			}
			
//fine mieie eventi 
		});

		Button b3 = (Button) view.findViewById(R.id.button_partecipant_event);
		b3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Fragment fragment2 = new Fragment_partecipant();
				Bundle args = new Bundle();
				args.putString("id", idEvent);//events.get(MainActivity.getidEvents()).getId());
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
			String uri = "geo:0,0?q="+ lat+ ","+ lon;
			startActivity(new Intent(android.content.Intent.ACTION_VIEW,Uri.parse(uri)));
		

	}

}
