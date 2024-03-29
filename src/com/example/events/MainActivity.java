package com.example.events;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.download.DownloadUserInfo;
import com.example.fragment.Fragment_cerca;
import com.example.fragment.Fragment_cerca_event;
import com.example.fragment.Fragment_crea_event;
import com.example.fragment.Fragment_event;
import com.example.fragment.Fragment_i_miei_event_event;
import com.example.fragment.Fragment_i_miei_eventi;
import com.example.fragment.Fragment_impostazioni;
import com.example.fragment.Fragment_main;
import com.example.helper.Communicator;
import com.example.helper.EventsHelper;
import com.example.helper.UserHelper;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Request.GraphUserListCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.android.Facebook;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
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
	private String[] mPlanetTitles = { "Events", "I miei Eventi",
			"Crea Evento", "Cerca", "Impostazioni" };

	//info logged user
	public static UserHelper infoUserLogged = new UserHelper();

	
	// sessione passata dopo il Login
	public static Session session;

	// Array per contenere eventi scaricati da Facebook
	private static List<EventsHelper> events = new ArrayList<EventsHelper>();
	// id da recuperare nel fragment_event
	private static int id;

	// location manager e variabili
	public static Location currentBestLocation = null;

	LocationManager lm;
	String provider;
	private Object isGPSEnabled;

	//FAcebook4j
	//
	//---------------------
	//Facebook facebook = new FacebookFactory().getInstance();
	
	
	public static int getidEvents() {
		return id;
	}

	public static void setidEvents(int id2) {
		id = id2;
	}

	public static List<EventsHelper> getListEvents() {
		return events;
	}

	public static void setListEvents(List<EventsHelper> listEvents) {
		events.addAll(listEvents);
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
		// menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
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
		// commentoto per futuro utilizzo dell'action
		// case R.id.action_websearch:
		// // create intent to perform web search for this planet
		// Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
		// intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
		// // catch event that there's no activity to handle intent
		// if (intent.resolveActivity(getPackageManager()) != null) {
		// startActivity(intent);
		// } else {
		// Toast.makeText(this, R.string.app_not_available,
		// Toast.LENGTH_LONG).show();
		// }
		// return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* The click listener for ListView in the navigation drawer */
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
		Fragment fragment;
		//fragment = getFragmentManager().findFragmentById(R.id.content_frame);
		// FragmentManager manager = getFragmentManager();
		// FragmentTransaction transaction = manager.beginTransaction();

		switch (position) {
		case 0:
			System.out.println("eventi");

			// if (fragment == null) {
			fragment = new Fragment_main();
			FragmentManager manager = getFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			transaction.replace(R.id.content_frame, fragment, "main");
			//transaction.addToBackStack("myscreen");
			transaction.commit();
			// }
			break;
		case 1:
			System.out.println("i miei eventi");
			fragment = new Fragment_i_miei_eventi();
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction transaction2=fragmentManager.beginTransaction();
			transaction2.replace(R.id.content_frame, fragment, "main");
			transaction2.addToBackStack("myscreen2");
			transaction2.commit();
			break;
		case 2:
			System.out.println("crea evento");
			fragment = new Fragment_crea_event();
			FragmentManager fragmentManager2 = getFragmentManager();
			FragmentTransaction transaction3=fragmentManager2.beginTransaction();
			transaction3.replace(R.id.content_frame, fragment);
			transaction3.addToBackStack("myscreen23");
			transaction3.commit();

			break;
		case 3:
			System.out.println("cerca evento");
			fragment = new Fragment_cerca();
			FragmentManager fragmentManager33 = getFragmentManager();
			FragmentTransaction transaction33=fragmentManager33.beginTransaction();
			transaction33.replace(R.id.content_frame, fragment);
			transaction33.addToBackStack("myscreen234");
			transaction33.commit();


			break;
		case 4:
			System.out.println("impostazioni");
			fragment = new Fragment_impostazioni();
			FragmentManager fragmentManager7 = getFragmentManager();
			FragmentTransaction transaction7=fragmentManager7.beginTransaction();
			transaction7.replace(R.id.content_frame, fragment);
			transaction7.addToBackStack("myscreen2347");
			transaction7.commit();
			break;
		default:
			Log.w("MainActivity", "dafault");
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
//		if (data.equals("fragment_event")) {
//			Log.w("call fragment event", data);
//
//			Fragment fragment = new Fragment_event();
//			FragmentManager manager = getFragmentManager();
//			FragmentTransaction transaction = manager.beginTransaction();
//			transaction.replace(R.id.content_frame, fragment);
//			transaction.addToBackStack("event_bo3");
//			transaction.commit();
//		}
		if (data.equals("fragment_eventcerca_descrizione")) {
			System.out.println(data);
			Fragment fragment3 = new Fragment_cerca_event();
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			transaction.replace(R.id.content_frame, fragment3);
			transaction.addToBackStack("event_bo");
			transaction.commit();
		}
//		if (data.equals("fragment_i_miei_event")) {
//			Log.w("call fragment event", data);
//
//			Fragment fragment = new Fragment_i_miei_event_event();
//			FragmentManager manager = getFragmentManager();
//			FragmentTransaction transaction = manager.beginTransaction();
//			transaction.replace(R.id.content_frame, fragment);
//			transaction.addToBackStack("event_bo");
//			transaction.commit();
//		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Log.v(TAG, "OnCreate");

	
		/* make the API call */
		Intent intent = getIntent();
		session = (Session) intent.getSerializableExtra("session");

		
        DownloadUserInfo infoUset = new DownloadUserInfo();
        infoUset.execute();

        
        
		// get location
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria c = new Criteria();
		provider = lm.getBestProvider(c, false);
		currentBestLocation = lm.getLastKnownLocation(provider);

		// if (!enabled) {
		// Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		// startActivity(intent);
		// }

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

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("justCall", true);
		super.onSaveInstanceState(outState);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	

}
