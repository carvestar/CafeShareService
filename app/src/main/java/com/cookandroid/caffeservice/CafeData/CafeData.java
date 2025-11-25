package com.cookandroid.caffeservice.CafeData;

import java.io.Serializable;
public class CafeData implements Serializable {
    private String name;
    private String address;
    private double rating;
    private double distance;
    private double lat;
    private double lng;

    public CafeData(String name, String address, double rating, double distance, double lat, double lng) {
        this.name = name;
        this.address = address;
        this.rating = rating;
        this.distance = distance;
        this.lat = lat;
        this.lng = lng;
    }

    public String getName() { return name; }
    public String getAddress() { return address; }
    public double getRating() { return rating; }
    public double getDistance() { return distance; }
    public double getLat() { return lat; }
    public double getLng() { return lng; }
}