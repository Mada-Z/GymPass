package com.example.gympass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReserveClass extends AppCompatActivity {

    EditText et_className;
    EditText et_classDescription;
    EditText et_classDate;
    EditText et_classHour;
    EditText et_classTrainer;
    Button bt_reserveClass;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_class);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        et_className = findViewById(R.id.et_className);
        et_classDescription = findViewById(R.id.et_classDescription);
        et_classDate = findViewById(R.id.et_classDate);
        et_classHour = findViewById(R.id.et_classHour);
        et_classTrainer = findViewById(R.id.et_classTrainer);
        bt_reserveClass = findViewById(R.id.bt_reserveClass);

        Intent i = getIntent();
        String userId = i.getStringExtra("userId");
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

        databaseReference = FirebaseDatabase.getInstance().getReference().child("RESERVATION");

        bt_reserveClass.setOnClickListener(v -> {
            ReservationModel reservationModel = new ReservationModel();
            reservationModel.setClassId(Integer.parseInt(classId));
            reservationModel.setUserId(Integer.parseInt(userId));

            databaseReference.push().setValue(reservationModel);
            Toast.makeText(ReserveClass.this, "Rezervarea s-a facut cu succes!", Toast.LENGTH_SHORT).show();
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