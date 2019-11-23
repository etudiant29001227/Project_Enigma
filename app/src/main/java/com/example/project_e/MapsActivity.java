package com.example.project_e;


import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.*;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, SensorEventListener {

    private final static long REFRESH_FASTER = 100;
    private final static long REFRESH = 500;
    private final static int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private final static  int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    private final static int VISIBLE = 500;
    private final static int UNVISIBLE = 404;

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
    private TextView dialogBox;
    private ImageView detectiveImage;
    private SensorManager sensorManager;
    private Sensor shakeSensor;
    private float acelVal,acelLast,shake;
    private MediaPlayer playSong;
    private GoogleMapOptions options;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        dialogBox = findViewById(R.id.detectivedialog);
        detectiveImage = findViewById(R.id.detective);

        if (fusedLocationProviderClient == null) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
                    for (Location location : locationResult.getLocations()) {
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
        Intent intent = new Intent("com.example.project_e.ProximityAlert");
        proximityIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        receiver = new ProximityBroadCastReceiver();
        filter = new IntentFilter("com.example.project_e.ProximityAlert");
        registerReceiver(receiver,filter);


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        shakeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);



        playSong();
    }


    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setCompassEnabled(false);
        //mMap.getUiSettings().setZoomGesturesEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
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

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        }

        sensorManager.registerListener(this,shakeSensor,SensorManager.SENSOR_DELAY_NORMAL);
        acelVal = SensorManager.GRAVITY_EARTH;
        acelLast = SensorManager.GRAVITY_EARTH;
        shake = 0.00f;
        locationManager.addProximityAlert(-20.9054529 ,55.500220999999996,100.0f,-1,proximityIntent);

    }

    @Override
    protected void onPause(){
        super.onPause();
        stopLocationUpdates();
        sensorManager.unregisterListener(this);
        try{
            if(locationManager != null){
                locationManager.removeProximityAlert(proximityIntent);
                unregisterReceiver(receiver);
            }
        }catch (SecurityException e){}
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        if(requestCode == MY_PERMISSIONS_REQUEST_CAMERA){
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
        }


    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        acelLast = acelVal;
        acelVal = (float) Math.sqrt((double)(x*x+y*y+z*z));
        float delta = acelVal - acelLast;
        shake = shake * 0.9f + delta;
        if(shake>12) {
            dialogBox.setText("Test 2");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startLocationUpdates();
                } else {

                    updateMark(null);
                }
                return;
            }
        }
    }

    private void playSong(){
        if(playSong == null){
            playSong = MediaPlayer.create(this,R.raw.allisfine);
            playSong.setLooping(true);
            playSong.start();
        }
    }

    private void stopSong(){

        if(playSong!=null){
            playSong.pause();
        }
    }

    private void startLocationUpdates(){
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,null);
    }



    private void stopLocationUpdates(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
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
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            }else {

                marker = mMap.addMarker(new MarkerOptions().position(latLng));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

            }
        }else{
            System.out.println("Can't update location");
        }
    }

    private boolean nearToTarget(Location l1, Location l2, int minDistance){
        return l1.distanceTo(l2)<=minDistance;
    }

    private void cameraEvent(){

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, MY_PERMISSIONS_REQUEST_CAMERA);
            setVisiblyDetective(UNVISIBLE);

        }

    }

    public void setVisiblyDetective(int visible){

        switch (visible){

            case UNVISIBLE:

                detectiveImage.setVisibility(View.GONE);
                dialogBox.setVisibility(View.GONE);

            case VISIBLE:

                detectiveImage.setVisibility(View.VISIBLE);
                dialogBox.setVisibility(View.VISIBLE);
        }

    }

}

