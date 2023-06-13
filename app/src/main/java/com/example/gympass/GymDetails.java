package com.example.gympass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class GymDetails extends MainActivity {

    EditText et_gymId;
    EditText et_name;
    Spinner dd_gymOpen;
    Spinner dd_gymClose;
    EditText et_gymStreet;
    EditText et_gymNumber;
    EditText et_gymCity;
    EditText et_gymCounty;
    EditText et_gymLatitude;
    EditText et_gymLongitude;
    EditText et_url;
    Button btn_add;
    ArrayAdapter openHoursAA;
    ArrayAdapter closeHoursAA;
    DatabaseReference databaseReference;
    long maxId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_details);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        et_gymId = findViewById(R.id.et_gymId);
        et_name = findViewById(R.id.et_name);
        dd_gymOpen = findViewById(R.id.dd_gymOpen);
        dd_gymClose = findViewById(R.id.dd_gymClose);
        et_gymStreet = findViewById(R.id.et_gymStreet);
        et_gymNumber = findViewById(R.id.et_gymNumber);
        et_gymCity = findViewById(R.id.et_gymCity);
        et_gymCounty = findViewById(R.id.et_gymCounty);
        et_gymLatitude = findViewById(R.id.et_gymLatitude);
        et_gymLongitude = findViewById(R.id.et_gymLongitude);
        et_url = findViewById(R.id.et_url);
        btn_add = findViewById(R.id.btn_add);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("GYM_DETAILS");
        GymDetailsModel gymDetailsModel = new GymDetailsModel();

        Intent i = getIntent();
        String gymId = i.getStringExtra("gymId");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    maxId = (snapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ArrayList<String> open = new ArrayList<String>(Arrays.asList("00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00",
                "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00",
                "21:00", "22:00", "23:00", "Non-Stop"));

        ArrayList<String> close = new ArrayList<String>(Arrays.asList("00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00",
                "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00",
                "21:00", "22:00", "23:00"));

        openHoursAA = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, open);
        closeHoursAA = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, close);

        dd_gymOpen.setAdapter(openHoursAA);
        dd_gymClose.setAdapter(closeHoursAA);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = et_name.getText().toString();
                String openHours = dd_gymOpen.getSelectedItem().toString();
                String closeHours = dd_gymClose.getSelectedItem().toString();
                String street = et_gymStreet.getText().toString();
                String number = et_gymNumber.getText().toString();
                String city = et_gymCity.getText().toString();
                String county = et_gymCounty.getText().toString();
                String latitude = et_gymLatitude.getText().toString();
                String longitude = et_gymLongitude.getText().toString();
                String image = et_url.getText().toString();
                gymDetailsModel.setId((int) maxId + 1);
                gymDetailsModel.setId_gym(Integer.parseInt(gymId));
                gymDetailsModel.setName(name);
                gymDetailsModel.setOpen_hours(openHours);
                gymDetailsModel.setClose_hours(closeHours);
                gymDetailsModel.setStreet(street);
                gymDetailsModel.setAdr_nr(Integer.parseInt(number));
                gymDetailsModel.setCity(city);
                gymDetailsModel.setCounty(county);
                gymDetailsModel.setLatitude(Double.parseDouble(latitude));
                gymDetailsModel.setLongitude(Double.parseDouble(longitude));
                gymDetailsModel.setImage(image);

                if (gymDetailsModel.getName().isEmpty()
                        || gymDetailsModel.getOpen_hours().isEmpty()
                        || gymDetailsModel.getClose_hours().isEmpty()
                || gymDetailsModel.getStreet().isEmpty()
                || gymDetailsModel.getAdr_nr() == 0
                || gymDetailsModel.getCity().isEmpty()
                || gymDetailsModel.getCounty().isEmpty()
                || gymDetailsModel.getLatitude() == 0
                || gymDetailsModel.getLongitude() == 0
                || gymDetailsModel.getImage().isEmpty()) {
                    Toast.makeText(GymDetails.this, "Toate campurile sunt obligatorii!", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.push().setValue(gymDetailsModel);
                    Toast.makeText(GymDetails.this, "Adresa a fost adaugata cu succes!", Toast.LENGTH_SHORT).show();
                }
            }});

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