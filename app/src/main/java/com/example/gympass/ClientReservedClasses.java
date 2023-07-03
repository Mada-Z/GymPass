package com.example.gympass;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ClientReservedClasses extends ClientMainActivity{

    ListView lv_classes;
    ArrayList<ReservationModel> reservationAL;
    DatabaseReference databaseReference;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_reserved_classes);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        lv_classes = findViewById(R.id.lv_classes);
        reservationAL = new ArrayList<>();

        Intent i = getIntent();
        userId = Integer.parseInt(i.getStringExtra("userId"));

        getReservations();

    }

    private void getReservations() {
        final ArrayAdapter<ReservationModel> adapter = new ArrayAdapter<ReservationModel>(this, android.R.layout.simple_dropdown_item_1line, reservationAL);
        databaseReference = (DatabaseReference) FirebaseDatabase.getInstance().getReference().child("RESERVATION");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue(UserSubscriptionModel.class).getUserId() == userId) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                    try {
                        if (!new Date().after(sdf.parse(snapshot.getValue(ReservationModel.class).getDate()))) {
                            reservationAL.add(snapshot.getValue(ReservationModel.class));
                            adapter.notifyDataSetChanged();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue(UserSubscriptionModel.class).getUserId() == userId) {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue(UserSubscriptionModel.class).getUserId() == userId) {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        lv_classes.setAdapter(adapter);
    }
}