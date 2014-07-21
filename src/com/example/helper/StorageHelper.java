package com.example.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class StorageHelper {
	
	public static String pathStorage="/storage/sdcard0";
	
	public static String saveToInternalSorage(Bitmap bitmapImage, String id) {
		

		String filename = id+".jpg";
		File sd = Environment.getExternalStorageDirectory();
		File dest = new File(sd, filename);

		try {
			FileOutputStream out = new FileOutputStream(dest);
			bitmapImage.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filename;

	}
	public static Bitmap loadImageFromStorage(String path, String id) {

		try {
			
			File f = new File(path, id + ".jpg");
			
			Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
			return b;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;

	}
	public static Bitmap loadImageFromStorage(String path) {

		try {
			
			File f = new File(path);
			
			Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
			return b;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;

	}


}
