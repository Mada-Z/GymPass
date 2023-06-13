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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ClassSchedule extends GymMainActivity {

    final Calendar myCalendar= Calendar.getInstance();

    EditText et_className;
    EditText et_classDescription;
    EditText et_classDate;
    Spinner dd_classHour;
    Spinner dd_classTrainer;
    Button bt_addClass;
    ListView lv_classes;
    int gymId;
    DatabaseReference databaseReference;
    ArrayAdapter hoursAA;
    long maxId;
    ArrayList<ClassesModel> classAL;
    ArrayList<String> trainerAL;
    ArrayList<TrainerModel> trainerData;
    int trainerId;
    String trainerName;
    String classTrainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_schedule);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        et_className = findViewById(R.id.et_className);
        et_classDescription = findViewById(R.id.et_classDescription);
        et_classDate = findViewById(R.id.et_classDate);
        dd_classHour = findViewById(R.id.dd_classHour);
        dd_classTrainer = findViewById(R.id.dd_classTrainer);
        bt_addClass = findViewById(R.id.bt_addClass);
        lv_classes = findViewById(R.id.lv_classes);
        ClassesModel classesModel = new ClassesModel();
        classAL = new ArrayList<ClassesModel>();
        trainerAL = new ArrayList<String>();
        trainerData = new ArrayList<TrainerModel>();


        Intent intent = getIntent();
        gymId = Integer.parseInt(intent.getStringExtra("userId"));

        getClasses();
        getTrainers();
        getAllTrainerData();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("CLASS_SCHEDULE");

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };

        et_classDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ClassSchedule.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

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

        ArrayList<String> hours = new ArrayList<String>(Arrays.asList("00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00",
                "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00",
                "21:00", "22:00", "23:00"));

        hoursAA = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, hours);
        dd_classHour.setAdapter(hoursAA);

        bt_addClass.setOnClickListener(v -> {
            String trainer = dd_classTrainer.getSelectedItem().toString();
            for(int i = 0; i< trainerData.size(); i++){
                if(Objects.equals(trainerData.get(i).getFirst_name() + " " + trainerData.get(i).getLast_name(), trainer)){
                    trainerId = trainerData.get(i).getId();
                    classTrainer = trainerData.get(i).getFirst_name() + " " + trainerData.get(i).getLast_name();
                }
            }

            String className = et_className.getText().toString();
            String classDescription = et_classDescription.getText().toString();
            String classDate = et_classDate.getText().toString();
            String classHour = dd_classHour.getSelectedItem().toString();

            classesModel.setId((int) maxId + 1);
            classesModel.setGymId(gymId);
            classesModel.setTrainerId(trainerId);
            classesModel.setName(className);
            classesModel.setDescription(classDescription);
            classesModel.setDate(classDate);
            classesModel.setHour(classHour);
            classesModel.setTrainerName(classTrainer);

            if(classesModel.getTrainerId() == 0
            || classesModel.getName().isEmpty()
            || classesModel.getDescription().isEmpty()
            || classesModel.getDate().isEmpty()
            || classesModel.getHour().isEmpty()) {
                Toast.makeText(ClassSchedule.this, "Toate campurile sunt obligatorii!" , Toast.LENGTH_SHORT).show();
            }
            else {
                databaseReference.push().setValue(classesModel);
                Toast.makeText(this, "Clasa a fost adaugata cu succes!", Toast.LENGTH_SHORT).show();
            }
            et_className.setText("");
            et_classDescription.setText("");
            et_classDate.setText("");

        });

        lv_classes.setOnItemClickListener((parent, view, position, id) -> {
            ClassesModel clickedClass = (ClassesModel) parent.getItemAtPosition(position);
            Intent i = new Intent(ClassSchedule.this, GymClass.class);
            i.putExtra("classId", clickedClass.getId() + "");
            i.putExtra("classGymId", clickedClass.getGymId() + "");
            i.putExtra("classTrainerId", clickedClass.getTrainerId() + "");
            i.putExtra("className", clickedClass.getName());
            i.putExtra("classDescription", clickedClass.getDescription());
            i.putExtra("classDate", clickedClass.getDate());
            i.putExtra("classHour", clickedClass.getHour());
            i.putExtra("classTrainerName", clickedClass.getTrainerName());
            startActivity(i);
        });

    }

    private void updateLabel(){
        String myFormat="dd.MM.yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        et_classDate.setText(dateFormat.format(myCalendar.getTime()));
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

    private void getTrainers() {
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, trainerAL);
        databaseReference = (DatabaseReference) FirebaseDatabase.getInstance().getReference().child("USER");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue(TrainerModel.class).getGym_id() == gymId) {
                    trainerAL.add(snapshot.getValue(TrainerModel.class).getFirst_name() + " " + snapshot.getValue(TrainerModel.class).getLast_name());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue(TrainerModel.class).getGym_id() == gymId) {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue(TrainerModel.class).getGym_id() == gymId) {
                    trainerAL.remove(snapshot.getValue(TrainerModel.class).getFirst_name() + " " + snapshot.getValue(TrainerModel.class).getLast_name());
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
        dd_classTrainer.setAdapter(adapter);
    }

    private void getAllTrainerData() {
        databaseReference = (DatabaseReference) FirebaseDatabase.getInstance().getReference().child("USER");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getValue(TrainerModel.class).getGym_id() == gymId) {
                    trainerData.add(snapshot.getValue(TrainerModel.class));
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

