package com.example.oscar.travelagent2;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;


public class GPSTracker extends Service implements LocationListener {

    private final Context context;
    boolean isGPSenable = false;
    boolean canGetLocation = false;
    boolean isNetWorkenable = false;
    Location location;
    double longtitude;
    double latitude;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATE = 10;
    private static final long MIN_TIME_BW_UPDATE = 1000*60*1;

    protected LocationManager locationManager;

    public GPSTracker(Context context){
        this.context = context;
        getLocation();
    }
    public Location getLocation() {
        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            isGPSenable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetWorkenable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSenable && !isNetWorkenable) {

            } else {
                this.canGetLocation = true;
                if (checkLocationPermission()) {
                    if (isNetWorkenable) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATE, MIN_DISTANCE_CHANGE_FOR_UPDATE, this);

                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longtitude = location.getLongitude();
                            }
                        }
                    }
                    if (isGPSenable) {
                        if (location == null) {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATE, MIN_DISTANCE_CHANGE_FOR_UPDATE, this);
                            if (locationManager != null) {
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longtitude = location.getLongitude();
                                }
                            }
                        }
                    }
                }
            }
        }catch (Exception e){e.printStackTrace();}
        return location;
    }
    @TargetApi(Build.VERSION_CODES.M)
    private boolean checkLocationPermission() {
        // 如果使用者的 Android 版本低於 6.0 ，直接回傳 True (在安裝時已授權)
        int api_version = Build.VERSION.SDK_INT;    //API版本
        String android_version = Build.VERSION.RELEASE;    //Android版本
        if(api_version < Build.VERSION_CODES.M && !android_version.matches("(6)\\..+")) return true;

        return (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    public void stopUsingGPS(){
        if(locationManager!=null){
            if (checkLocationPermission()) {
                locationManager.removeUpdates(GPSTracker.this);
            }

        }
    }

    public double getLatitude(){
        if(location!=null){
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongtitude(){
        if(location!=null){
            longtitude = location.getLongitude();
        }
        return longtitude;
    }
    public boolean canGetLocation(){
        return this.canGetLocation;
    }



    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
