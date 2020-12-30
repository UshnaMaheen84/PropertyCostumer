package com.example.propertycostumer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ViewMap extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int Request_Code = 101;
    Location mlocation;
    Marker marker;

    String  name;
    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_map);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        name = getIntent().getStringExtra("name");
        latitude = getIntent().getDoubleExtra("lat",0);
        longitude = getIntent().getDoubleExtra("lng",0);

//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
        Getlastlocation();

    }
    private void Getlastlocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_Code);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location!= null) {

                    mlocation = location;

                    Toast.makeText(getApplicationContext(), mlocation.getLatitude()+ ""+mlocation.getLongitude(), Toast.LENGTH_SHORT).show();

                    SupportMapFragment mapFragment= (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(ViewMap.this);



                }

            }
        });


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        LatLng current_latLng = new LatLng(mlocation.getLatitude(), mlocation.getLongitude());
        LatLng destination_latLng = new LatLng(latitude, longitude);

//        DrawRouteMaps.getInstance(this)
//                .draw(current_latLng, destination_latLng, mMap);
//
//        LatLngBounds bounds = new LatLngBounds.Builder()
//                .include(current_latLng)
//                .include(destination_latLng).build();
//        Point displaySize = new Point();
//        getWindowManager().getDefaultDisplay().getSize(displaySize);

        Log.e("loation", destination_latLng.toString());

//        LatLngBounds bounds = new LatLngBounds.Builder()
//                .include(current_latLng)
//                .include(destination_latLng).build();
//        Point displaySize = new Point();
//
//        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,displaySize.x,
//                25,
//                30));

        mMap.addMarker(new MarkerOptions().position(destination_latLng).title(name));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(destination_latLng));
          mMap.addMarker(new MarkerOptions().position(current_latLng).title("Current Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(current_latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destination_latLng, 10));


//        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=%f,%f(%s)&daddr=%f,%f (%s)", mlocation.getLatitude(), mlocation.getLongitude(), "Home Sweet Home", latitude, longitude, "Where the party is at");
//        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                Uri.parse(uri));
//             intent.setPackage("com.google.android.apps.maps");
////
////        intent.setClassName("com.example.propertycostumer",
////                "com.example.propertycostumer.ViewMap");
//
//        startActivity(intent);
//




    }

}