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
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GymClass extends AppCompatActivity {

    EditText et_className;
    EditText et_classDescription;
    EditText et_classDate;
    EditText et_classHour;
    EditText et_classTrainer;
    Button bt_deleteClass;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_class);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        et_className = findViewById(R.id.et_className);
        et_classDescription = findViewById(R.id.et_classDescription);
        et_classDate = findViewById(R.id.et_classDate);
        et_classHour = findViewById(R.id.et_classHour);
        et_classTrainer = findViewById(R.id.et_classTrainer);
        bt_deleteClass = findViewById(R.id.bt_deleteClass);

        Intent i = getIntent();
        String classId = i.getStringExtra("classId");
        String classGymId = i.getStringExtra("classGymId");
        String classTrainerId = i.getStringExtra("classTrainerId");
        String className = i.getStringExtra("className");
        String classDescription = i.getStringExtra("classDescription");
        String classDate = i.getStringExtra("classDate");
        String classHour = i.getStringExtra("classHour");
        String trainerName = i.getStringExtra("classTrainerName");


        et_className.setText(className);
        et_classDescription.setText(classDescription);
        et_classDate.setText(classDate);
        et_classHour.setText(classHour);
        et_classTrainer.setText(trainerName);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("CLASS_SCHEDULE");


        bt_deleteClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.orderByChild("id").equalTo(Integer.parseInt(classId))
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    snapshot.getKey();
                                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                                        dataSnapshot.getRef().removeValue();
                                    }
                                    Toast.makeText(GymClass.this, "Clasa a fost stearsa cu succes!", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(GymClass.this, ClassSchedule.class);
                                    startActivity(i);
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        });

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