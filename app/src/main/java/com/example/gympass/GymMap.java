package com.example.gympass;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.gympass.databinding.ActivityGymMapBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GymMap extends MainActivity implements
        OnMapReadyCallback{

    private GoogleMap mMap;
    private ActivityGymMapBinding binding;
    private static final int REQUEST_CODE = 101;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
    ArrayList<GymDetailsModel> gymDetails;
    String name;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        userId = i.getStringExtra("userId");

        binding = ActivityGymMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;

        }

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        LatLngBounds romaniaBounds = new LatLngBounds(
                new LatLng(43.66667, 20.48333), // SW bounds
                new LatLng(48.18333, 28.86667)  // NE bounds
        );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(romaniaBounds.getCenter(), 7));

        databaseReference = (DatabaseReference) FirebaseDatabase.getInstance().getReference().child("GYM_DETAILS");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gymDetails = new ArrayList<GymDetailsModel>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    gymDetails.add(dataSnapshot.getValue(GymDetailsModel.class));
                }

                for(GymDetailsModel gymDetailsModel : gymDetails) {
                    LatLng gym = new LatLng(gymDetailsModel.getLatitude(), gymDetailsModel.getLongitude());

                    mMap.addMarker(new MarkerOptions().position(gym).title(gymDetailsModel.getName()));
                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(@NonNull Marker marker) {
                            openPopUpWindow(gymDetailsModel);
                            return false;
                        }
                    });

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void openPopUpWindow(GymDetailsModel gymDetailsModel) {
        Intent popupwindow = new Intent (GymMap.this,PopupActivity.class);
        popupwindow.putExtra("userId", userId);
        popupwindow.putExtra("gymId", gymDetailsModel.getId_gym() + "");
        popupwindow.putExtra("image", gymDetailsModel.getImage());
        popupwindow.putExtra("name", gymDetailsModel.getName());
        popupwindow.putExtra("street", gymDetailsModel.getStreet());
        popupwindow.putExtra("number", gymDetailsModel.getAdr_nr() + "");
        popupwindow.putExtra("city", gymDetailsModel.getCity());
        popupwindow.putExtra("county", gymDetailsModel.getCounty());
        popupwindow.putExtra("open", gymDetailsModel.getOpen_hours());
        popupwindow.putExtra("close", gymDetailsModel.getClose_hours());
        startActivity(popupwindow);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}