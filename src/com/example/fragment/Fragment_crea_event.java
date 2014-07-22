package com.example.fragment;

import java.io.File;
import java.text.DateFormat;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import com.example.events.R;
import com.example.events.R.id;
import com.example.events.R.layout;
import com.example.helper.StorageHelper;

import database.DbAdapter;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Fragment_crea_event extends Fragment {

	private static int RESULT_ACTIVITY = 1;
	private static int RESULT_CODE = -1;
	private DbAdapter dbHelper;
	private Cursor cursor;
	Button b1 = null;
	ImageView image = null;
	private String description;
	private String title;
	private String pathimg;
	private String location;
	private String date;
	private String id;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle saveInstanceState) {
		View view = inflater.inflate(R.layout.fragment_crea_event, container,
				false);

		Button buttonPosition = (Button) view.findViewById(R.id.button_addPosition);
		Button buttonSave = (Button) view.findViewById(R.id.button_save);
		final EditText t1 = (EditText) view
				.findViewById(R.id.editText_addTitle);
		final EditText t2 = (EditText) view
				.findViewById(R.id.editText_addDescription);
		image = (ImageView) view.findViewById(R.id.imageView_addImg);

		image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("IMG");
				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				i.setType("image/*");
				image.setImageBitmap(null);
				startActivityForResult(i, RESULT_ACTIVITY);

			}
		});
		buttonPosition.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method
				// stub/Users/specter987/Documents/kris/Events/src/com/example/events/Fragment_crea_event.java
				System.out.println("MAp");

				final Context context = getActivity();
				AlertDialog.Builder alert = new AlertDialog.Builder(context);
				alert.setTitle("POSIZIONE PER MAPPA"); // Set Alert dialog title here
				alert.setMessage("Enter Location"); // Message here

				// Set an EditText view to get user input
				final EditText input = new EditText(context);
				alert.setView(input);
				alert.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

								String srt = input.getEditableText().toString();
								Toast.makeText(context, srt, Toast.LENGTH_LONG)
										.show();
								location=srt;
							} // End of onClick(DialogInterface dialog, int
								// whichButton)
						}); // End of alert.setPositiveButton
				alert.setNegativeButton("CANCEL",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// Canceled.
								location=null;
								dialog.cancel();
							}
						}); // End of alert.setNegativeButton
				AlertDialog alertDialog = alert.create();
				alertDialog.show();

			}
		});
		
		Button buttonData=(Button)view.findViewById(R.id.button_data);
		buttonData.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("here");
				
				final Context context = getActivity();
				AlertDialog.Builder alert = new AlertDialog.Builder(context);
				alert.setTitle("DATA DELL EVENTO"); // Set Alert dialog title here
				alert.setMessage("Enter Data"); // Message here

				// Set an EditText view to get user input
				final EditText input = new EditText(context);
				alert.setView(input);
				alert.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

								String srt = input.getEditableText().toString();
								Toast.makeText(context, srt, Toast.LENGTH_LONG)
										.show();
								date=srt;
							} // End of onClick(DialogInterface dialog, int
								// whichButton)
						}); // End of alert.setPositiveButton
				alert.setNegativeButton("CANCEL",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// Canceled.
								date=null;
								dialog.cancel();
							}
						}); // End of alert.setNegativeButton
				AlertDialog alertDialog = alert.create();
				alertDialog.show();

			}
	
		});
		
		
		
		
		
		
		buttonSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("SAVE");

				if (t1.getText().toString().contentEquals("")) {
					Context context = getActivity().getApplicationContext();
					CharSequence text = "Inserire Titolo!";
					int duration = Toast.LENGTH_SHORT;

					Toast toast = Toast.makeText(context, text, duration);
					toast.show();

				}
				if (t2.getText().toString().contentEquals("")) {
					Context context = getActivity().getApplicationContext();
					CharSequence text = "Inserire Descrizione";
					int duration = Toast.LENGTH_SHORT;

					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
				}

				else {
					title=t1.getText().toString();
					description=t2.getText().toString();
					// inserire le risorse all' interno di qualche struttura
					Random rand = new Random();
					int  n = rand.nextInt(1000) + 1;
					System.out.println("id: "+n+", img: "+pathimg+", title: "+title+", descrizione: "+description+", locazione: "+location+", data: "+date);
					 dbHelper = new DbAdapter(getActivity().getApplicationContext());
					 Bitmap b=StorageHelper.loadImageFromStorage(pathimg);
					 StorageHelper.saveToInternalSorage(b, ""+n);
					 
					 
					 dbHelper.open();
					 dbHelper.createEvents(""+n, pathimg, title, description, date, "0", location, "1", "46.8", "11.1");
				dbHelper.close();
				Context context = getActivity().getApplicationContext();
				CharSequence text = "salvato";
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
				}
			}
		});

		return view;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RESULT_ACTIVITY && resultCode == RESULT_CODE) {
			Uri contentUri = data.getData();

			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = this.getActivity().managedQuery(contentUri, proj, // Which
																				// columns
																				// to
																				// return
					null, // WHERE clause; which rows to return (all rows)
					null, // WHERE clause selection arguments (none)
					null); // Order-by clause (ascending by name)
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			String filePath = cursor.getString(column_index);
			System.out.println("file: " + filePath);
            pathimg=filePath;
			Bitmap bm = StorageHelper.loadImageFromStorage(filePath);
			BitmapDrawable bdrawable = new BitmapDrawable(bm);
			image.setBackgroundDrawable(bdrawable);
            cursor.close();
			System.gc();
			System.runFinalization();

		}
		super.onActivityResult(requestCode, resultCode, data);

	}

	
	
	
	private Object getPath(Uri uri) {
		// TODO Auto-generated method stub

		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = this.getActivity().managedQuery(uri, projection, null,
				null, null);
		if (cursor == null)
			return null;
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String s = cursor.getString(column_index);
		cursor.close();
		return s;
	}
	
}
