package com.example.gympass;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GymProfile extends AppCompatActivity {

    EditText et_name;
    EditText et_email;
    Button btn_add;
    ListView lv_address;
    ArrayList<GymDetailsModel> gymDetails;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_profile);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        btn_add = findViewById(R.id.btn_add);
        lv_address = findViewById(R.id.lv_address);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("USER");
        gymDetails = new ArrayList<GymDetailsModel>();

        Intent i = getIntent();
        String userId = i.getStringExtra("userId");

        databaseReference.orderByChild("id").equalTo(Integer.parseInt(userId))
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                et_name.setText(snapshot.child("name").getValue(String.class));
                                et_email.setText(snapshot.child("email").getValue(String.class));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        getDetails(Integer.parseInt(userId));

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GymProfile.this, GymDetails.class);
                i.putExtra("gymId", userId);
                startActivity(i);
            }
        });
    }

    private void getDetails(int userId) {
        final ArrayAdapter<GymDetailsModel> adapter = new ArrayAdapter<GymDetailsModel>(this, android.R.layout.simple_dropdown_item_1line, gymDetails);
        databaseReference = (DatabaseReference) FirebaseDatabase.getInstance().getReference().child("GYM_DETAILS");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue(GymDetailsModel.class).getId_gym() == userId) {
                    gymDetails.add(snapshot.getValue(GymDetailsModel.class));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue(GymDetailsModel.class).getId_gym() == userId) {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue(GymDetailsModel.class).getId_gym() == userId) {
                    gymDetails.add(snapshot.getValue(GymDetailsModel.class));
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
        lv_address.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}