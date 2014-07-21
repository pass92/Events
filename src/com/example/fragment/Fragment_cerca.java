package com.example.fragment;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.example.adapter.AdapterListViewCercaEvent;
import com.example.events.MainActivity;
import com.example.events.R;
import com.example.events.R.id;
import com.example.events.R.layout;
import com.example.helper.Communicator;
import com.example.helper.EventsHelper;

import database.DbAdapter;
import android.app.Fragment;
import android.content.Context;
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
import android.view.inputmethod.InputMethodSession.EventCallback;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;

public class Fragment_cerca extends Fragment {

	private String filter;
	private DbAdapter dbHelper;
	private Cursor cursor;
	private Communicator comm;
	private int id;
	private static List<EventsHelper> eventsmain;
	private static List<EventsHelper> cercaevents = new ArrayList<EventsHelper>();

	public static List<EventsHelper> getEventcerca() {
		return cercaevents;

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle saveInstanceState) {
		View view = inflater.inflate(R.layout.fragment_cerca, container, false);
		final ListView lv = (ListView) view.findViewById(R.id.listView_cerca);
		final TextView tx = (TextView) view
				.findViewById(R.id.editText_crea_event);
		comm = (Communicator) getActivity();
		eventsmain = MainActivity.getListEvents();
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

		Button b = (Button) view.findViewById(R.id.button_cerca_event);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				filter = new String(tx.getText().toString());
				cercaevents = new ArrayList<EventsHelper>();

				dbHelper = new DbAdapter(getActivity().getApplicationContext());
				dbHelper.open();
				Cursor c = dbHelper.fetchEventsByFilter(filter);

				System.out.println("numero di righe: " + c.getCount());
				getActivity().startManagingCursor(c);

				System.out.println("Curosor c=" + c.moveToFirst());

				if (c.moveToFirst() == true) {
					while (!c.isAfterLast()) {

						EventsHelper eventHelper = new EventsHelper();
						eventHelper.setId(c.getString(0));
						eventHelper.setPhotoURL(c.getString(1));
						eventHelper.setDescription(c.getString(3));
						eventHelper.setTitle(c.getString(2));
						eventHelper.setStart_time(c.getString(4));
						// eventHelper.setPhoto();
						// /test

						for (int i = 0; i < eventsmain.size(); i++) {
							System.out.println("id eventsmain: "
									+ eventsmain.get(i).getId() + "== "
									+ c.getString(0));
							if ((eventsmain.get(i).getId()).equals(c
									.getString(0))) {
								cercaevents.add(eventsmain.get(i));
								System.out.println("trovatoooooo");
							}

						}

						// //testfine

						System.out
								.println("===================Risultato delle query:================ ");
						// STAMPO TUTTI I CAMPI DEL RECORD
						System.out.println("Curosor c=" + c.getString(0));
						System.out.println("Curosor c=" + (c.getString(1)));
						System.out.println("Curosor c=" + c.getString(2));
						System.out.println("Curosor c=" + c.getString(3));
						System.out.println("Curosor start_time="
								+ c.getString(4));
						System.out.println("Curosor end_time=" + c.getString(5));
						System.out.println("Curosor location=" + c.getString(6));
						System.out
								.println("==================fine risultato query==================");
						c.moveToNext();

					}
					System.out.println("grandezza lista: " + cercaevents.size());

					AdapterListViewCercaEvent adapter = new AdapterListViewCercaEvent(
							getActivity().getApplicationContext(),
							(ArrayList<EventsHelper>) cercaevents);

					lv.setAdapter(adapter);
					lv.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							System.out.println("there: " + position);
							MainActivity.setidEvents(position);
							comm.respond("fragment_eventcerca_descrizione",
									position);
						}
					});

				} else {
					Context context = getActivity().getApplicationContext();
					CharSequence text = "Nessun Evento trovato!";
					int duration = Toast.LENGTH_SHORT;
					AdapterListViewCercaEvent adapter = new AdapterListViewCercaEvent(
							getActivity().getApplicationContext(),
							(ArrayList<EventsHelper>) cercaevents);

					lv.setAdapter(adapter);

					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
				}

			}
		});

		return view;

	}
}
