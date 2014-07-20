package com.example.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.adapter.AdapterUser;
import com.example.download.DownloadFriendsWhoParticipate;
import com.example.events.MainActivity;
import com.example.events.R;
import com.example.events.R.id;
import com.example.events.R.layout;
import com.example.helper.EventsHelper;
import com.example.helper.UserHelper;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Fragment_partecipant extends Fragment {

	// adapter listView
	private static List<UserHelper> user = null;
	private AdapterUser adapter;
	private ArrayList<EventsHelper> event;
	private String IdEvent;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		IdEvent = args.getString("id");
		
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle saveInstanceState) {
		View view = inflater.inflate(R.layout.fragment_partecipant, container,
				false);

		// Lista partecipanti evento
		final List<UserHelper> user =new ArrayList();
		final ListView listView = (ListView) view
				.findViewById(R.id.listview_partecipanti);
		adapter = new AdapterUser(view.getContext(), user);
		listView.setAdapter(adapter);
		
		
		
		//String id = event.get(MainActivity.getidEvents()).getId();
		//Log.w("ID EVENT", id);
		DownloadFriendsWhoParticipate taskEvents = new DownloadFriendsWhoParticipate(view, listView, view.getContext(), user,IdEvent,adapter);
		taskEvents.execute();
		

		final EditText tx = (EditText) view.findViewById(R.id.edit_text_email);
		Button b1 = (Button) view.findViewById(R.id.button_send_email);
		b1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println(tx.getText());
				String emailToSend = tx.getText().toString();

				if ((emailToSend.contains("@"))) {// mettere controllo sull
														// email

					Intent intent = new Intent(Intent.ACTION_SENDTO); // it's
																		// not
																		// ACTION_SEND
					intent.setType("text/plain");
					intent.putExtra(Intent.EXTRA_SUBJECT, "INVITO");
					intent.putExtra(Intent.EXTRA_TEXT,
							"Hey! raggiungimi su BestEvents ");
					intent.setData(Uri.parse("mailto:" + emailToSend)); // or
																		// just
																		// "mailto:"
																		// for
																		// blank
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will
																	// make such
																	// that when
																	// user
																	// returns
																	// to your
																	// app, your
																	// app is
																	// displayed,
																	// instead
																	// of the
																	// email
																	// app.
					startActivity(intent);

					intent.setData(Uri.parse("mailto:default@recipient.com")); // or
																				// just
																				// "mailto:"
																				// for
																				// blank

				} else {
					Context context = getActivity().getApplicationContext();
					CharSequence text = "Inserire Email!";
					int duration = Toast.LENGTH_SHORT;

					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
				}
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent facebookIntent = getOpenFacebookIntent(view.getContext(),user.get(position).getId());
				startActivity(facebookIntent);
				
			}
		});
		
		return view;
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		getView().removeCallbacks(null);
	}
	
	public static Intent getOpenFacebookIntent(Context context, String idProfile) {

	    try {
	        //context.getPackageManager().getPackageInfo("com.facebook.katana", 0); //Checks if FB is even installed.
	        return new Intent(Intent.ACTION_VIEW,
	                Uri.parse("fb://profile/"+idProfile)); //Trys to make intent with FB's URI
	    } catch (Exception e) {
	        return new Intent(Intent.ACTION_VIEW,
	                Uri.parse("https://www.facebook.com/"+idProfile)); //catches and opens a url to the desired page
	    }
	}
}
