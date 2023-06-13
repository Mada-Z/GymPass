package com.example.gympass;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class GymTrainer extends AppCompatActivity {

    final Calendar myCalendar= Calendar.getInstance();

    Button bt_add_trainer;
    EditText et_trainer_first_name;
    EditText et_trainer_last_name;
    EditText et_trainer_birth_date;
    EditText et_trainer_telephone;
    EditText et_trainer_email;
    EditText et_trainer_password;
    EditText et_picture;
    ListView lv_trainers;
    ArrayList<TrainerModel> trainerAL;
    DatabaseReference databaseReference;
    int userId;
    long maxId;
    int gymId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_trainer);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        bt_add_trainer = findViewById(R.id.bt_add_trainer);
        et_trainer_first_name = findViewById(R.id.et_trainer_first_name);
        et_trainer_last_name = findViewById(R.id.et_trainer_last_name);
        et_trainer_birth_date = findViewById(R.id.et_trainer_birth_date);
        et_trainer_telephone = findViewById(R.id.et_trainer_telephone);
        et_trainer_email = findViewById(R.id.et_trainer_email);
        et_trainer_password = findViewById(R.id.et_trainer_password);
        et_picture = findViewById(R.id.et_picture);
        lv_trainers = findViewById(R.id.lv_trainers);

        Intent intent = getIntent();
        userId = Integer.parseInt(intent.getStringExtra("userId"));


        TrainerModel trainerModel = new TrainerModel();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("USER");
        trainerAL = new ArrayList<TrainerModel>();
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

        getTrainers();

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };

        et_trainer_birth_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(GymTrainer.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        bt_add_trainer.setOnClickListener(v -> {
            String trainerFirstName = et_trainer_first_name.getText().toString();
            String trainerLastName = et_trainer_last_name.getText().toString();
            String trainerBirthDate = et_trainer_birth_date.getText().toString();
            String trainerTelephone = et_trainer_telephone.getText().toString();
            String trainerEmail = et_trainer_email.getText().toString();
            String trainerPassword = et_trainer_password.getText().toString();
            String trainerPicture = et_picture.getText().toString();

            trainerModel.setId((int) maxId + 1);
            trainerModel.setUserType(3);
            trainerModel.setFirst_name(trainerFirstName);
            trainerModel.setLast_name(trainerLastName);
            trainerModel.setBirth_date(trainerBirthDate);
            trainerModel.setTelephone(trainerTelephone);
            trainerModel.setEmail(trainerEmail);
            trainerModel.setPassword(trainerPassword);
            trainerModel.setGym_id(gymId);
            trainerModel.setPicture(trainerPicture);


            if(trainerModel.getFirst_name().isEmpty() || trainerModel.getLast_name().isEmpty() || trainerModel.getBirth_date().isEmpty() || trainerModel.getEmail().isEmpty() || trainerModel.getPassword().isEmpty() || trainerModel.getTelephone().isEmpty()) {
                Toast.makeText(GymTrainer.this, "Toate campurile sunt obligatorii", Toast.LENGTH_SHORT).show();
            }
            else {
                databaseReference.orderByChild("email").equalTo(trainerEmail)
                        .addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    Toast.makeText(GymTrainer.this, "Exista deja acest antrenor!", Toast.LENGTH_SHORT).show();
                                } else {
                                    databaseReference.push().setValue(trainerModel);
                                    Toast.makeText(GymTrainer.this, "Antrenorul a fost adaugat cu succes!", Toast.LENGTH_SHORT).show();
                                }
                                et_trainer_first_name.setText("");
                                et_trainer_last_name.setText("");
                                et_trainer_birth_date.setText("");
                                et_trainer_telephone.setText("");
                                et_trainer_email.setText("");
                                et_trainer_password.setText("");
                                et_picture.setText("");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }

        });

        lv_trainers.setOnItemClickListener((parent, view, position, id) -> {
            TrainerModel clickedTrainer = (TrainerModel) parent.getItemAtPosition(position);
            Intent i = new Intent(GymTrainer.this, Trainer.class);
            i.putExtra("trainerId", clickedTrainer.getId()+"");
            i.putExtra("trainerFirstName", clickedTrainer.getFirst_name());
            i.putExtra("trainerLastName", clickedTrainer.getLast_name());
            i.putExtra("trainerBirthDate", clickedTrainer.getBirth_date());
            i.putExtra("trainerEmail", clickedTrainer.getEmail());
            i.putExtra("trainerPassword", clickedTrainer.getPassword());
            i.putExtra("trainerTelephone", clickedTrainer.getTelephone());
            i.putExtra("trainerGymId", clickedTrainer.getGym_id()+"");
            i.putExtra("trainerPicture", clickedTrainer.getPicture());
            startActivity(i);
        });

    }

    private void getTrainers() {
        final ArrayAdapter<TrainerModel> adapter = new ArrayAdapter<TrainerModel>(this, android.R.layout.simple_dropdown_item_1line, trainerAL);
        databaseReference = (DatabaseReference) FirebaseDatabase.getInstance().getReference().child("USER");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue(TrainerModel.class).getUserType() == 3) {
                    trainerAL.add(snapshot.getValue(TrainerModel.class));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue(TrainerModel.class).getUserType() == 3) {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue(TrainerModel.class).getUserType() == 3) {
                    trainerAL.remove(snapshot.getValue(TrainerModel.class));
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
        lv_trainers.setAdapter(adapter);
    }

    private void updateLabel(){
        String myFormat="dd.MM.yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        et_trainer_birth_date.setText(dateFormat.format(myCalendar.getTime()));
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