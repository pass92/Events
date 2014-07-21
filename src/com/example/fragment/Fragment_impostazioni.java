package com.example.fragment;

import com.example.events.LogActivity;
import com.example.events.MainActivity;
import com.example.events.R;
import com.example.events.Dialogfragment.NoticeDialogLIstener;
import com.example.events.R.id;
import com.example.events.R.layout;
import com.facebook.Session;
import com.facebook.widget.ProfilePictureView;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.*;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Fragment_impostazioni extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle saveInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_impostazioni,
				container, false);
		TextView tx = (TextView) view.findViewById(R.id.text_NomeUtente);
		ImageView img = (ImageView) view.findViewById(R.id.profilePictureView1);
		final EditText defaultCity = (EditText) view
				.findViewById(R.id.default_city);
		final Button btnUpdateCity = (Button) view
				.findViewById(R.id.update_dafault_city);

		tx.setText("" + MainActivity.infoUserLogged.getName());
		img.setImageBitmap(MainActivity.infoUserLogged.getImage());

		SeekBar S = (SeekBar) view.findViewById(R.id.seekBar1);
		final TextView T = (TextView) view.findViewById(R.id.textkm);
		final ViewGroup mContainerView = (ViewGroup) view
				.findViewById(R.id.cont);
		ImageButton Btn1 = (ImageButton) view.findViewById(R.id.additem);
		SharedPreferences userDetails = getActivity().getApplicationContext()
				.getSharedPreferences("userdetails",
						getActivity().getApplicationContext().MODE_PRIVATE);
		int km = userDetails.getInt("km", 10);

		if (km != 0)
			try {
				S.setProgress(km);
				T.setText(km + " km");
			} catch (NumberFormatException ex) {

			}

		S.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			Integer progressChanged = 0;

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				progressChanged = progress;
				T.setText(progressChanged + " km");

			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			public void onStopTrackingTouch(SeekBar seekBar) {

				saveUserDatails(progressChanged, view.getContext());

			}
		});

		Btn1.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				final Context context = getActivity();
				AlertDialog.Builder alert = new AlertDialog.Builder(context);
				alert.setTitle("Add Filter"); // Set Alert dialog title here
				alert.setMessage("Enter TAG"); // Message here
				final ViewGroup newView = (ViewGroup) LayoutInflater.from(
						view.getContext()).inflate(R.layout.list_item_example,
						mContainerView, false);
				newView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						System.out.println("view: " + v.getId());

					}
				});
				
				newView.findViewById(R.id.delete_button).setOnClickListener(
						new View.OnClickListener() {
							@Override
							public void onClick(View view) {
								// Remove the row from its parent (the container
								// view).
								// Because mContainerView has
								// android:animateLayoutChanges set to true,
								// this removal is automatically animated.
								mContainerView.removeView(newView);
							}
						});

				// Set an EditText view to get user input
				final EditText input = new EditText(context);
				alert.setView(input);
				alert.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								if (mContainerView.getChildCount() <= 4) {
									mContainerView.addView(newView, 0);
									String srt = input.getEditableText()
											.toString();
									if (srt.equals("")) {
//										((TextView) newView
//												.findViewById(android.R.id.text1))
//												.setText("default");
										Toast.makeText(context,"Add a Filter..",Toast.LENGTH_LONG).show();
									} else {
										((TextView) newView
												.findViewById(android.R.id.text1))
												.setText(srt);
										//save filter
										saveFilters(srt, context);
										//reset all variable in Fragment_main
										Fragment_main.clearAllVariable();
										// Toast.makeText(context,srt,Toast.LENGTH_LONG).show();
										
									}
								} // End of onClick(DialogInterface dialog, int
									// whichButton)
							}
						}); // End of alert.setPositiveButton
				
				alert.setNegativeButton("CANCEL",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// Canceled.

								dialog.cancel();
							}
						}); // End of alert.setNegativeButton
				AlertDialog alertDialog = alert.create();
				alertDialog.show();

			}

		});

		Button b = (Button) view.findViewById(R.id.button_logout);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("Session.getActiveSession(): "
						+ Session.getActiveSession());
				if (Session.getActiveSession() != null) {
					Session.getActiveSession().closeAndClearTokenInformation();
				}

				Session.setActiveSession(null);

				// definisco l'intenzione
				Intent intent = new Intent(getActivity()
						.getApplicationContext(), LogActivity.class);

				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_NEW_TASK);

				// passo all'attivazione dell'activity Pagina.java
				startActivity(intent);
			}

		});

		btnUpdateCity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String city = defaultCity.getText().toString();
				saveDafaultCity(city, view.getContext());
				defaultCity.clearFocus();

				// Hide Keyboard
				closeKeyboard(getActivity(), defaultCity.getWindowToken());
			}
		});

		return view;
	}

	public static void closeKeyboard(Context c, IBinder windowToken) {
	    InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
	    mgr.hideSoftInputFromWindow(windowToken, 0);
	}
	
	private void saveUserDatails(Integer kmUser, Context context) {

		// Context context;
		// context = getActivity().getApplicationContext();
		SharedPreferences userDetails = context.getSharedPreferences(
				"userdetails", Context.MODE_PRIVATE);
		Editor edit = userDetails.edit();
		edit.clear();
		edit.putInt("km", kmUser);
		edit.commit();
		Toast.makeText(context, "Login details are saved..", 3000).show();

	}

	public static int loadUserDatails(Context context) {
		SharedPreferences userDetails = context.getSharedPreferences(
				"userdetails", Context.MODE_PRIVATE);

		int km = userDetails.getInt("km", 10);
		return km;
	}

	private void saveDafaultCity(String city, Context context) {

		SharedPreferences userDetails = context.getSharedPreferences(
				"dafaultCity", Context.MODE_PRIVATE);
		Editor edit = userDetails.edit();
		edit.clear();
		edit.putString("city", city);
		edit.commit();
		Toast.makeText(context, "Default city is saved..", 3000).show();

	}

	public static String loadDafaultCity(Context context) {
		SharedPreferences userDetails = context.getSharedPreferences(
				"dafaultCity", Context.MODE_PRIVATE);

		String city = userDetails.getString("city", "Trento");
		return city;
	}

	
	private void saveFilters(String filter, Context context) {

		SharedPreferences userDetails = context.getSharedPreferences(
				"filterQuery", Context.MODE_PRIVATE);
		Editor edit = userDetails.edit();
		edit.clear();
		edit.putString("filter", filter);
		edit.commit();
		Toast.makeText(context, "Default city is saved..", 3000).show();

	}

	public static String loadFilters(Context context) {
		SharedPreferences userDetails = context.getSharedPreferences(
				"filterQuery", Context.MODE_PRIVATE);

		String filter = userDetails.getString("filter", "");
		return filter;
	}
}
