package com.example.propertycostumer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;
import me.relex.circleindicator.CircleIndicator;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.propertycostumer.adapter.SlidingImage_Adapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class PropertyDetail extends AppCompatActivity {
    TextView plotName, price;
    ImageButton mapImage;
    TextView plotNameAgain, address;
    TextView precinct, road, properyType, plotNo, sqyrds, isConstructed, tv_stories, tv_rooms, sellrDetails, roomsHeading, storyHeading;
    String plot_name, plot_no, road_no, priceRange, sqyrd, constructed, rooms, stories, priceFrom, priceTo, agentNum;
    String key;
    Button sell_property;
    double lat, lng;

    SlidingImage_Adapter adapter;
    DatabaseReference databaseReference;
    Query query;
    private static ViewPager mPager;
    private static int currentPage = 0;
    ArrayList<String> imageUrl = new ArrayList<>();

    Toolbar toolbar;
    FusedLocationProviderClient fusedLocationProviderClient;
    Location mlocation;
    TextView location, key_detail, seller, textView;
    Typeface myfonts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_detail);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        toolbar = findViewById(R.id.toolbar6);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(false);
        textView = findViewById(R.id.toolbar_title);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Italic.ttf");
        textView.setTypeface(typeface);


        location = findViewById(R.id.location_tv);
        myfonts = Typeface.createFromAsset(this.getAssets(), "fonts/Heading-Pro-Bold-trial.ttf");
        location.setTypeface(myfonts);

        key_detail = findViewById(R.id.key_tv);
        key_detail.setTypeface(myfonts);
        Getlastlocation();

        seller = findViewById(R.id.seller_tv);
        seller.setTypeface(myfonts);


        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);

        Ui();

        imageUrl = getIntent().getExtras().getStringArrayList("imageUrl");
        Log.e("array", String.valueOf(imageUrl));
        key = getIntent().getExtras().getString("key");
        Log.e("key", key);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Plots");
        query = databaseReference.orderByKey().equalTo(key);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {

                    try {
                        Plots model = data.getValue(Plots.class);


                        String prprty_type_id = "";

                        if (model.getProperty_type_id().equals("1")) {
                            prprty_type_id = "Residential";
                        }
                        if (model.getProperty_type_id().equals("2")) {
                            prprty_type_id = "Commercial";
                        }
                        plotName.setText(model.getName());

                        plot_name = model.getName();
                        priceRange = "PKR." + model.getPlot_price_range_from();
                        price.setText(priceRange);
                        priceFrom = model.getPlot_price_range_from();
                        plotNameAgain.setText(model.getName());
                        address.setText("Lat: " + model.getLatitude() + "\nLng: " + model.getLongitude());
                        lat = model.getLatitude();
                        lng = model.getLongitude();
                        isConstructed.setText(model.getConstructed());
                        constructed = model.getConstructed();
                        precinct.setText(model.getPrecinct_id());
                        road.setText(model.getRoad_id());
                        road_no = model.getRoad_id();
                        properyType.setText(prprty_type_id);
                        plotNo.setText(model.getplot_no());
                        plot_no = model.getplot_no();
                        sqyrds.setText(model.getSq_yrds());
                        sqyrd = model.getSq_yrds();
                        tv_stories.setText(model.getStories());
                        stories = model.getStories();
                        tv_rooms.setText(model.getRooms());
                        rooms = model.getRooms();

                        sellrDetails.setText("Company Id: " + model.getCompany_id()
                                + "\nAgent Name: " + model.getAgent_name()
                                + "\nAgent Id: " + model.getAgent_id());

                        agentNum = model.getAgent_number();

                        if (constructed.equals("Yes")) {
                            tv_stories.setVisibility(View.VISIBLE);
                            storyHeading.setVisibility(View.VISIBLE);

                            tv_rooms.setVisibility(View.VISIBLE);
                            roomsHeading.setVisibility(View.VISIBLE);
                        }
                        if (constructed.equals("No")) {
                            tv_stories.setVisibility(View.GONE);
                            storyHeading.setVisibility(View.GONE);

                            tv_rooms.setVisibility(View.GONE);
                            roomsHeading.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        Log.e("exception", e.getLocalizedMessage());

                    }

                }

                if (imageUrl != null && imageUrl.size() > 0) {
                    adapter = new SlidingImage_Adapter(PropertyDetail.this, imageUrl);
                    mPager.setAdapter(adapter);
                } else {
                    mPager.setVisibility(View.GONE);
                }


                CircleIndicator indicator = findViewById(R.id.indicator);

                indicator.setViewPager(mPager);

                indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageSelected(int position) {
                        currentPage = position;

                    }

                    @Override
                    public void onPageScrolled(int pos, float arg1, int arg2) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int pos) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mapImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
//                Intent intent = new Intent(PropertyDetail.this, ViewMap.class);
//                intent.putExtra("lat", lat);
//                intent.putExtra("lng", lng);
//                intent.putExtra("name", plot_name);
//
//                                startActivity(intent);
//
//

                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=%f,%f(%s)&daddr=%f,%f (%s)",
                        mlocation.getLatitude(), mlocation.getLongitude(), "Home Sweet Home", lat, lng, "Where the party is at");

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse(uri));
                //    intent.setPackage("com.google.android.apps.maps");
                Log.e("latlng", lat + " " + lng);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                intent.putExtra("name", plot_name);

                intent.setClassName("com.example.propertycostumer",
                        "com.example.propertycostumer.ViewMap");

                startActivity(intent);

            }
        });

        sell_property.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + agentNum));
                startActivity(intent);
            }
        });
    }

    private void Ui() {

        mPager = findViewById(R.id.pager);
        plotName = findViewById(R.id.tv_plot_name);
        price = findViewById(R.id.price);
        mapImage = findViewById(R.id.viewmap);
        plotNameAgain = findViewById(R.id.goldenhouse);
        address = findViewById(R.id.tv_address_text);
        precinct = findViewById(R.id.tv_precinct);
        road = findViewById(R.id.tv_road);
        properyType = findViewById(R.id.tv_property_type);
        plotNo = findViewById(R.id.tv_plot_no);
        sqyrds = findViewById(R.id.tv_square_yard);
        isConstructed = findViewById(R.id.is_constructed);
        tv_stories = findViewById(R.id.stories_no);
        tv_rooms = findViewById(R.id.rooms);
        sellrDetails = findViewById(R.id.tv_addedBy);
        roomsHeading = findViewById(R.id.room_heading);
        storyHeading = findViewById(R.id.story_heading);
        sell_property = findViewById(R.id.sell_property);

    }

    private void Getlastlocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {

                    mlocation = location;


                }

            }
        });


    }

}