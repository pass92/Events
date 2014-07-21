package com.example.helper;

public class LatLng {

	private double lat;
	private double lon;
	
	
	public LatLng(double latitude, double longitude) {
		this.lat=latitude;
		this.lon=longitude;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	
}
