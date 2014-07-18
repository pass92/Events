package com.example.events;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.android.Util;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class DownloadUserInfo extends AsyncTask<Void, Void, UserHelper> {

	private Session session;

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		session = MainActivity.session;
	}
	@Override
	protected UserHelper doInBackground(Void... params) {
		// get info User
		Request request = Request.newMeRequest(session, 
		        new Request.GraphUserCallback() {
		    @Override
		    public void onCompleted(GraphUser user, Response response) {
		        // If the response is successful
		            if (user != null) {
		                // Set the id for the ProfilePictureView
		                // view that in turn displays the profile picture.
		                MainActivity.infoUserLogged.setId(user.getId());
		                // Set the Textview's text to the user's name.
		                MainActivity.infoUserLogged.setName(user.getName());
		            }
		        
		        if (response.getError() != null) {
		            // Handle errors, will do so later.
		        }
		    }
		});
		request.executeBatchAndWait(request);


//		Request request1 = new Request(session, "/"+MainActivity.infoUserLogged.getId()+"/picture?redirect=false",
//				null, HttpMethod.GET, new Request.Callback() {
//					@Override
//					public void onCompleted(Response response) {
//						// Log.i(TAG, "Got results: " + response.toString());
//						try {
//							if (response != null) {
//								final JSONObject json = response.getGraphObject()
//										.getInnerJSONObject();
//								JSONObject d = (JSONObject) json.get("data");
//								//int l = (d != null ? d.length() : 0);
//								//for (int i = 0; i < l; i++) {
//									//JSONObject o = d.getJSONObject(i);
//
//									String url = d.getString("url");
//
//									MainActivity.infoUserLogged.setUrlImage(url);
//							//}
//							}
//						} catch (JSONException e) {
//							Log.w("Facebook-Example", "JSON Error in response");
//						}
//					}
//
//				});
//		Request.executeBatchAndWait(request1);
		
		MainActivity.infoUserLogged.setUrlImage("https://graph.facebook.com/"+MainActivity.infoUserLogged.getId()  +"/picture");
		String URLPhoto = MainActivity.infoUserLogged.getUrlImage();
		Log.w("INFO", "INFO"+ URLPhoto);
		MainActivity.infoUserLogged.setImage(getBitmapFromURL(URLPhoto));

		
		
		return null;
	}

	@Override
	protected void onPostExecute(UserHelper result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
	}
	
	public Bitmap getBitmapFromURL(String imageUrl) {
		try {
			URL url = new URL(imageUrl);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
