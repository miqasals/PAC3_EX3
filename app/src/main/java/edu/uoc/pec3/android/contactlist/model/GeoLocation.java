package edu.uoc.pec3.android.contactlist.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by mgarcia on 23/03/2016.
 */
public class GeoLocation {

    private Double longitude;
    private Double latitude;

    public GeoLocation() {
    }

    public GeoLocation(LatLng latLng) {
        this.longitude = latLng.longitude;
        this.latitude = latLng.latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    @Override
    public String toString() {
        return longitude + "," + latitude;
    }
}
