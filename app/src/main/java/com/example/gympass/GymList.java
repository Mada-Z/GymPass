package com.example.gympass;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GymList extends AdminMainActivity {

    Button bt_add_gym;
    EditText et_gym_name;
    EditText et_gym_email;
    EditText et_gym_password;
    ListView lv_gyms;
    ArrayList<GymModel> gymAL;
    DatabaseReference databaseReference;
    long maxId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        bt_add_gym = findViewById(R.id.bt_add_gym);
        et_gym_name = findViewById(R.id.et_gym_name);
        et_gym_email = findViewById(R.id.et_gym_email);
        et_gym_password = findViewById(R.id.et_gym_password);
        lv_gyms = findViewById(R.id.lv_gyms);

        GymModel gymModel = new GymModel();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("USER");
        gymAL = new ArrayList<GymModel>();

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

        getGyms();

        bt_add_gym.setOnClickListener(v -> {
           String gymName = et_gym_name.getText().toString();
           String gymEmail = et_gym_email.getText().toString();
           String gymPassword = et_gym_password.getText().toString();

           gymModel.setId((int) maxId + 1);
           gymModel.setUserType(2);
           gymModel.setName(gymName);
           gymModel.setEmail(gymEmail);
           gymModel.setPassword(gymPassword);

                if(gymModel.getName().isEmpty() || gymModel.getEmail().isEmpty() || gymModel.getPassword().isEmpty()) {
                    Toast.makeText(GymList.this, "Toate campurile sunt obligatorii", Toast.LENGTH_SHORT).show();}
                else {
                    databaseReference.orderByChild("name").equalTo(gymName)
                            .addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        Toast.makeText(GymList.this, "Exista deja aceasta sala!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        databaseReference.push().setValue(gymModel);
                                        Toast.makeText(GymList.this, "Sala a fost adaugata cu succes!", Toast.LENGTH_SHORT).show();
                                    }
                                    et_gym_name.setText("");
                                    et_gym_email.setText("");
                                    et_gym_password.setText("");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }


        });

        lv_gyms.setOnItemClickListener((parent, view, position, id) -> {
            GymModel clickedGym = (GymModel) parent.getItemAtPosition(position);
            Intent i = new Intent(GymList.this, Gym.class);
            i.putExtra("gymId", clickedGym.getId()+"");
            i.putExtra("gymName", clickedGym.getName());
            i.putExtra("gymEmail", clickedGym.getEmail());
            i.putExtra("gymPassword", clickedGym.getPassword());
            startActivity(i);
        });
    }

    private void getGyms() {
        final ArrayAdapter<GymModel> adapter = new ArrayAdapter<GymModel>(this, android.R.layout.simple_dropdown_item_1line, gymAL);
        databaseReference = (DatabaseReference) FirebaseDatabase.getInstance().getReference().child("USER");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue(GymModel.class).getUserType() == 2) {
                    gymAL.add(snapshot.getValue(GymModel.class));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue(GymModel.class).getUserType() == 2) {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue(GymModel.class).getUserType() == 2) {
                    gymAL.remove(snapshot.getValue(GymModel.class));
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
        lv_gyms.setAdapter(adapter);
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