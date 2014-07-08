package com.example.events;

import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Fragment_event extends Fragment {
	
	List<EventsHelper> events;

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
		
		
		ImageView imageEvent = (ImageView) view.findViewById(R.id.image_event);
		TextView descizione = (TextView) view.findViewById(R.id.descrizione_event);
		
		BitmapDrawable bdrawable = new BitmapDrawable(events.get(MainActivity.getidEvents()).getPhoto());
		imageEvent.setBackgroundDrawable(bdrawable);
		descizione.setText(events.get(MainActivity.getidEvents()).getDescription());
		descizione.setMovementMethod(new ScrollingMovementMethod());
//		final Fragment_descritpion fragment = new Fragment_descritpion();
//		final Fragment_partecipant fragment2 = new Fragment_partecipant();
//
//		FragmentManager manager = getFragmentManager();
//		FragmentTransaction transaction = manager.beginTransaction();
//		transaction.add(R.id.fragment_event, fragment, "descriptionfragment");
//		transaction.commit();

//		Button b0 = (Button) view.findViewById(R.id.button_map_event);
//		b0.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//
//				invokeGoogleMaps();
//
//			}
//		});

//		Button b1 = (Button) view.findViewById(R.id.button_description_event);
//		b1.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				FragmentManager manager = getFragmentManager();
//				FragmentTransaction transaction = manager.beginTransaction();
//				Fragment_descritpion fragment = new Fragment_descritpion();
//				transaction.replace(R.id.fragment_event, fragment,
//						"description_event");
//				transaction.commit();
//
//			}
//		});

//		Button b2 = (Button) view.findViewById(R.id.button_partecipa_event);
//		b2.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//
//				Context context = getActivity().getApplicationContext();
//				CharSequence text = "Salvato in i miei eventi!";
//				int duration = Toast.LENGTH_SHORT;
//
//				Toast toast = Toast.makeText(context, text, duration);
//				toast.show();
//
//			}
//		});

//		Button b3 = (Button) view.findViewById(R.id.button_partecipant_event);
//		b3.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				FragmentManager manager = getFragmentManager();
//				FragmentTransaction transaction = manager.beginTransaction();
//				transaction.replace(R.id.fragment_event, fragment2,
//						"partecipa_event");
//				transaction.commit();
//
//			}
//		});

		return view;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	public void invokeGoogleMaps() {
		Intent intent = new Intent(
				android.content.Intent.ACTION_VIEW,
				Uri.parse("http://maps.google.com/maps? saaddr==Bolzano&daddr=Trento"));
		this.startActivity(intent);
	}
	
}
