package com.example.events;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

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

		ImageView user_picture;
		URL img_value = null;
		try {
			img_value = new URL("http://graph.facebook.com/"
					+ MainActivity.infoUserLogged.getId() + "/picture?type=large");
			Bitmap mIcon1 = BitmapFactory.decodeStream(img_value
					.openConnection().getInputStream());
			MainActivity.infoUserLogged.setImage(mIcon1);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
	}

	@Override
	protected void onPostExecute(UserHelper result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		Log.w("INFO", "INFO"+ MainActivity.infoUserLogged.getName()+MainActivity.infoUserLogged.getId());
	}

}
