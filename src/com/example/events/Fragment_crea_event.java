package com.example.events;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Fragment_crea_event extends Fragment {
	
	private static int RESULT_LOAD_IMAGE = 1;
	private static int RESULT_CODE = -1;
	Button b1=null;
	ImageView image = null;
	@Override 
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle saveInstanceState){
	 View view= inflater.inflate(R.layout.fragment_crea_event,container,false);
	 
     Button b2=(Button)view.findViewById(R.id.button_addPosition);
	 Button b3=(Button)view.findViewById(R.id.button_save);
	 final EditText t1=(EditText)view.findViewById(R.id.editText_addTitle);
	 final EditText t2=(EditText)view.findViewById(R.id.editText_addDescription);
	 image=(ImageView)view.findViewById(R.id.imageView_addImg);
	 //ciao pass
	 image.setOnClickListener(new OnClickListener() {
       
		 @Override
	     public void onClick(View v) {
			// TODO Auto-generated method stub
			 System.out.println("IMG");
			 Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			 i.setType("image/*");
			 startActivityForResult(i, RESULT_LOAD_IMAGE);
			
	 	 }
	 });
	 b2.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			System.out.println("DESC");
			
		}
	});
	 b3.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			System.out.println("SAVE");
			
			if(t1.getText().toString().contentEquals("")){
				Context context = getActivity().getApplicationContext();
				CharSequence text = "Inserire Titolo!";
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
				
			}
			if(t2.getText().toString().contentEquals("")){
				Context context = getActivity().getApplicationContext();
				CharSequence text = "Inserire Descrizione";
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}
			
			else{
            //inserire le risorse all' interno di qualche struttura
			}
		}
	});
	
	 
	 
	 
	 return view;
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	     super.onActivityResult(requestCode, resultCode, data);
	     
	     if((requestCode==RESULT_LOAD_IMAGE)&&(resultCode==RESULT_CODE)&&(data!=null)){
			     System.out.println(data);
			     Uri selectedImageUri = data.getData();           
	             Object selectedImagePath = getPath(selectedImageUri);
	             image.setAdjustViewBounds(true);
	             image.setImageURI(selectedImageUri);
	         
	             image.setAdjustViewBounds(true);
	             //image.setScaleType(ImageView.ScaleType.FIT_CENTER);
	             image.setScaleType(ImageView.ScaleType.CENTER_CROP);
	             //image.setScaleType(ImageView.ScaleType.MATRIX);+
            
		}    	
	}
	private Object getPath(Uri uri) {
		// TODO Auto-generated method stub
		String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor =this.getActivity().managedQuery(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
	}
}
 
    	 
	    	 
	    	 /*
	    	 
	    	 pathName=pathName.substring(8);
	    	 System.out.println(pathName);
	    	 System.out.println(data);
	    	 

             File imgFile = new  File(pathName);
             //if(imgFile.exists()){
                       Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                       image.setImageBitmap(myBitmap);
                       
             //}

	    	// Drawable d = Drawable.createFromPath(pathName);
	    	 
	    	// Resources res = getResources(); 
	        /// Drawable drawable = res.getDrawable(R.drawable.schermo1);
	    	 
	        */
	
