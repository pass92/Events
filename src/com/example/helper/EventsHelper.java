package com.example.helper;


import android.graphics.Bitmap;

public class EventsHelper {
	private String id;
	private String title;
	private String description;
	private String photoURL=null;
	private Bitmap photo=null;
	private String start_time;
	private String end_time;
	private String rsvp_status;
	private Double latitude;
	private Double longitude;
	private String pathPhotoLocal;
	private String my;
	
	public String getRsvp_status() {
		return rsvp_status;
	}
	public void setRsvp_status(String rsvp_status) {
		this.rsvp_status = rsvp_status;
	}

	public String my() {
		return my;
	}
	public void setMy_status(String my) {
		this.my = my;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Bitmap getPhoto() {
		return photo;
	}
	public void setPhoto(Bitmap photo) {
		this.photo = photo;
	}

	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getPhotoURL() {
		return photoURL;
	}
	public void setPhotoURL(String photoURL) {
		this.photoURL = photoURL;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public String getPathPhotoLocal() {
		return pathPhotoLocal;
	}
	public void setPathPhotoLocal(String pathPhotoLocal) {
		this.pathPhotoLocal = pathPhotoLocal;
	}
	
	
	
}
