package com.example.gympass;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

public class ClientGymClasses extends AppCompatActivity {

    ListView lv_classes;
    ArrayList<ClassesModel> classAL;
    DatabaseReference databaseReference;
    int gymId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_gym_classes);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        lv_classes = findViewById(R.id.lv_classes);

        Intent i = getIntent();
        gymId = Integer.parseInt(i.getStringExtra("gymId"));
        String userId = i.getStringExtra("userId");
        Toast.makeText(this, "gymId: "+ gymId, Toast.LENGTH_SHORT).show();

        classAL = new ArrayList<ClassesModel>();
        getClasses();

        lv_classes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClassesModel clickedClass = (ClassesModel) parent.getItemAtPosition(position);
                Intent i = new Intent(ClientGymClasses.this, ReserveClass.class);
                i.putExtra("userId", userId);
                i.putExtra("classId", clickedClass.getId() + "");
                i.putExtra("classGymId", clickedClass.getGymId() + "");
                i.putExtra("classTrainerId", clickedClass.getTrainerId() + "");
                i.putExtra("className", clickedClass.getName());
                i.putExtra("classDescription", clickedClass.getDescription());
                i.putExtra("classDate", clickedClass.getDate());
                i.putExtra("classHour", clickedClass.getHour());
                i.putExtra("classTrainerName", clickedClass.getTrainerName());
                startActivity(i);
            }
        });
    }
    private void getClasses(){
        final ArrayAdapter<ClassesModel> adapter = new ArrayAdapter<ClassesModel>(this, android.R.layout.simple_dropdown_item_1line, classAL);
        databaseReference = (DatabaseReference) FirebaseDatabase.getInstance().getReference().child("CLASS_SCHEDULE");
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String date = sdf.format(new Date());
        try {
            Date date1 = sdf.parse(date);
            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    try {
                        Date date2 = sdf.parse(snapshot.getValue(ClassesModel.class).getDate());
                        if(snapshot.getValue(ClassesModel.class).getGymId() == gymId && date2.compareTo(date1) >= 0) {
                            classAL.add(snapshot.getValue(ClassesModel.class));
                            adapter.notifyDataSetChanged();

                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(ClassesModel.class).getGymId() == gymId) {
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue(ClassesModel.class).getGymId() == gymId) {
                        classAL.remove(snapshot.getValue(ClassesModel.class));
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
        } catch (ParseException e) {
            e.printStackTrace();
        }
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