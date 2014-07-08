package com.example.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Request.GraphUserListCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements Communicator {

	private static final String TAG = "MainActivity";
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mPlanetTitles = { "Nome utente", "I miei Eventi",
			"Crea Evento", "Cerca Evento", "Impostazioni" };

	// sessione passata dopo il Login
	static Session session;

	// Array per contenere eventi scaricati da Facebook
	private static List<EventsHelper> events = new ArrayList<EventsHelper>();
	// id da recuperare nel fragment_event
	private static int id;

	static int getidEvents() {
		return id;
	}

	static void setidEvents(int id2) {
		id = id2;
	}

	public static List<EventsHelper> getListEvents() {
		return events;
	}

	public static void setListEvents(List<EventsHelper> listEvents) {
		events = listEvents;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	// menu
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch (item.getItemId()) {
		case R.id.action_websearch:
			// create intent to perform web search for this planet
			Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
			intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
			// catch event that there's no activity to handle intent
			if (intent.resolveActivity(getPackageManager()) != null) {
				startActivity(intent);
			} else {
				Toast.makeText(this, R.string.app_not_available,
						Toast.LENGTH_LONG).show();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		// update the main content by replacing fragments
		switch (position) {
		case 0:
			System.out.println("eventi");
			Fragment fragment5 = new Fragment_main();
			FragmentManager fragmentManager4 = getFragmentManager();
			fragmentManager4.beginTransaction()
					.replace(R.id.content_frame, fragment5).commit();

			break;
		case 1:
			System.out.println("i miei eventi");
			Fragment fragment3 = new Fragment_i_miei_eventi();
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, fragment3).commit();

			break;
		case 2:
			System.out.println("crea evento");
			Fragment fragment4 = new Fragment_crea_event();
			FragmentManager fragmentManager2 = getFragmentManager();// levare e
																	// mettere
																	// all
																	// inizio
			fragmentManager2.beginTransaction()
					.replace(R.id.content_frame, fragment4).commit();

			break;
		case 3:
			System.out.println("cerca evento");

			break;
		case 4:
			System.out.println("impostazioni");
			Fragment fragmentImp = new Fragment_impostazioni();
			FragmentManager fragmentManager3 = getFragmentManager();// levare e
																	// mettere
																	// all
																	// inizio
			fragmentManager3.beginTransaction()
					.replace(R.id.content_frame, fragmentImp).commit();
			break;
		default:
			System.out.println("i???");
			break;
		}
		Bundle args = new Bundle();
		args.putInt("position", position);

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(mPlanetTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void respond(String data, int id) {
		// TODO Auto-generated method stub
		if (data.equals("fragment_event")) {
			System.out.println(data);
			Fragment fragment2 = new Fragment_event();
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, fragment2).commit();
		}
		if (data.equals("fragment_i_miei_eventi")) {
			System.out.println(data);
			Fragment fragment3 = new Fragment_i_miei_eventi();
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, fragment3).commit();
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Log.v(TAG, "OnCreate");

		/* make the API call */
		Intent intent = getIntent();
		session = (Session) intent.getSerializableExtra("session");

//		final ProgressDialog pausingDialog = ProgressDialog.show(
//				MainActivity.this, "", "Loading..", true);
//		Thread boh = new Thread() {
//			public void run() {
//				try {
//					Thread.sleep(5000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} // The length to 'pause' for
//				pausingDialog.dismiss();
//			}
//		};
//		boh.start();

		Fragment fragment = new Fragment_main();
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.add(R.id.content_frame, fragment, "basefragment");
		transaction.addToBackStack(null);
		transaction.commit();



		mTitle = mDrawerTitle = getTitle();
		// mPlanetTitles = getResources().getStringArray(R.array.planets_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mPlanetTitles));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(0);
		}
	}

	/**
	 * Fragment that appears in the "content_frame", shows a planet
	 */
	public static class PositionFragment extends Fragment {

		public PositionFragment() {
			// Empty constructor required for fragment subclasses
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_planet,
					container, false);
			int i = getArguments().getInt("position");

			TextView vista = (TextView) rootView.findViewById(R.id.image);
			String textForPrinting = "START: ";
			for (int j = 0; j < events.size(); j++) {
				String newString = "START: id: " + events.get(j).getId()
						+ "Title: " + events.get(j).getTitle();// +"description"+
																// events.get(j).getDescription()+"\n");
				textForPrinting = textForPrinting + newString;
			}
			vista.setText(textForPrinting);
			return rootView;
		}
	}
}
