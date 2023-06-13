package com.example.gympass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class Trainer extends AppCompatActivity {

    final Calendar myCalendar= Calendar.getInstance();

    EditText et_trainerId;
    EditText et_trainerFirstName;
    EditText et_trainerLastName;
    EditText et_trainerBirthDate;
    EditText et_trainerTelephone;
    EditText et_trainerEmail;
    EditText et_trainerPassword;
    ImageView iv_trainer;
    Button btn_delete;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        et_trainerId = findViewById(R.id.et_trainerId);
        et_trainerFirstName = findViewById(R.id.et_trainerFirstName);
        et_trainerLastName = findViewById(R.id.et_trainerLastName);
        et_trainerBirthDate = findViewById(R.id.et_trainerBirthDate);
        et_trainerTelephone = findViewById(R.id.et_trainerTelephone);
        et_trainerEmail = findViewById(R.id.et_trainerEmail);
        et_trainerPassword = findViewById(R.id.et_trainerPassword);
        iv_trainer = findViewById(R.id.iv_trainer);
        btn_delete = findViewById(R.id.btn_delete);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("USER");

        Intent i = getIntent();
        String trainerId = i.getStringExtra("trainerId");
        String trainerFirstName = i.getStringExtra("trainerFirstName");
        String trainerLastName = i.getStringExtra("trainerLastName");
        String trainerBirthDate = i.getStringExtra("trainerBirthDate");
        String trainerEmail = i.getStringExtra("trainerEmail");
        String trainerPassword = i.getStringExtra("trainerPassword");
        String trainerTelephone = i.getStringExtra("trainerTelephone");
        String trainerGymId = i.getStringExtra("trainerGymId");
        String trainerPicture = i.getStringExtra("trainerPicture");


        Glide.with(this).load(trainerPicture).into(iv_trainer);
        et_trainerId.setText(trainerId);
        et_trainerFirstName.setText(trainerFirstName);
        et_trainerLastName.setText(trainerLastName);
        et_trainerBirthDate.setText(trainerBirthDate);
        et_trainerTelephone.setText(trainerTelephone);
        et_trainerEmail.setText(trainerEmail);
        et_trainerPassword.setText(trainerPassword);

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };

        et_trainerBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Trainer.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.orderByChild("id").equalTo(Integer.parseInt(trainerId))
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    snapshot.getKey();
                                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                                        dataSnapshot.getRef().removeValue();
                                    }
                                    Toast.makeText(Trainer.this, "Antrenorul a fost sters cu succes!", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(Trainer.this, TrainerList.class);
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

    private void updateLabel(){
        String myFormat="dd.MM.yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        et_trainerBirthDate.setText(dateFormat.format(myCalendar.getTime()));
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