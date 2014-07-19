package com.example.helper;


    public class DistanceBeetwenPoint  
    {  
        public static double RadToDeg(double radians)  
        {  
            return radians * (180 / Math.PI);  
        }  
  
        public static double DegToRad(double degrees)  
        {  
            return degrees * (Math.PI / 180);  
        }  
  
  
        //Calculate distance between two coordinates useing the Haversine formula  
        public static double DistanceBetweenCoords(double lat1, double long1, double lat2, double long2)  
        {  
            //Convert input values to radians  
            lat1 = DistanceBeetwenPoint.DegToRad(lat1);  
            long1 = DistanceBeetwenPoint.DegToRad(long1);  
            lat2 = DistanceBeetwenPoint.DegToRad(lat2);  
            long2 = DistanceBeetwenPoint.DegToRad(long2);  
  
            double earthRadius = 6371; // km  
            double deltaLat = lat2 - lat1;  
            double deltaLong = long2 - long1;  
            double a = Math.sin(deltaLat / 2.0) * Math.sin(deltaLat / 2.0) +  
                    Math.cos(lat1) * Math.cos(lat2) *  
                    Math.sin(deltaLong / 2.0) * Math.sin(deltaLong / 2.0);  
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));  
            double distance = earthRadius * c;  
  
            return distance;  
        }  
  
}  