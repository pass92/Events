package com.example.events;

import com.google.android.maps.GeoPoint;

import android.graphics.Bitmap;

public class EventsHelper {
	private String Id;
	private String Title;
	private String Description;
	private Bitmap Photo;
	private String Location;
	private GeoPoint Point;
	private String start_time;
	private String end_time;
	
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
	public String getId() {
		return Id;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public Bitmap getPhoto() {
		return Photo;
	}
	public void setPhoto(Bitmap photo) {
		Photo = photo;
	}
	public String getLocation() {
		return Location;
	}
	public void setLocation(String location) {
		Location = location;
	}
	public GeoPoint getPoint() {
		return Point;
	}
	public void setPoint(GeoPoint point) {
		Point = point;
	}
	
}
