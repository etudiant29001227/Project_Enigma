package com.example.project_e;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.*;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private final static long REFRESH_FASTER = 100;
    private final static long REFRESH = 500;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private GoogleMap mMap;
    private Marker marker;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private  Location gLocation;
    private LocationManager locationManager;
    private PendingIntent proximityIntent;
    private ProximityBroadCastReceiver receiver;
    private IntentFilter filter;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //addContentView(this,R.layout.character_dialog);

        if(fusedLocationProviderClient == null){
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            locationCallback = new LocationCallback(){
                @Override
                public void onLocationResult(LocationResult locationResult){
                    if(locationResult == null){
                        return;
                    }
                    for(Location location : locationResult.getLocations()){
                        updateMark(location);
                        return;
                    }
                }
            };
        }

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(REFRESH);
        locationRequest.setFastestInterval(REFRESH_FASTER);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Intent intent = new Intent("com.example.position.ProximityAlert");
        proximityIntent = PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        receiver = new ProximityBroadCastReceiver();
        filter = new IntentFilter("com.example.position.ProximityAlert");

    }


    public void updateMark(Location location){

        gLocation = location;

         if(location != null){

             double latitude = location.getLatitude();
             double longitude = location.getLongitude();
             LatLng latLng = new LatLng(latitude, longitude);

            if(marker != null) {

                marker.remove();


                marker = mMap.addMarker(new MarkerOptions().position(latLng));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                System.out.println("Location : "+latLng);

            }else {

                marker = mMap.addMarker(new MarkerOptions().position(latLng));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

            }
         }else{
             System.out.println("Can't update location");
         }
    }

    @Override
    public  void onResume(){
        super.onResume();

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                                            MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }else{
            startLocationUpdates();
        }

        locationManager.addProximityAlert(0,0,1000.0f,-1,proximityIntent);
    }

    private void startLocationUpdates(){
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,null);
    }

    @Override
    protected void onPause(){
        super.onPause();
        stopLocationUpdates();
        locationManager.removeProximityAlert(proximityIntent);
        unregisterReceiver(receiver);
    }

    private void stopLocationUpdates(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    startLocationUpdates();
                } else {
                    // permission denied
                    updateMark(null);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }


}

