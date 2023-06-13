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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class Client extends AppCompatActivity {

    final Calendar myCalendar= Calendar.getInstance();

    EditText et_clientId;
    EditText et_clientFirstName;
    EditText et_clientLastName;
    EditText et_clientBirthDate;
    EditText et_clientTelephone;
    EditText et_clientEmail;
    EditText et_clientPassword;
    Button btn_delete;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        et_clientId = findViewById(R.id.et_clientId);
        et_clientFirstName = findViewById(R.id.et_clientFirstName);
        et_clientLastName = findViewById(R.id.et_clientLastName);
        et_clientBirthDate = findViewById(R.id.et_clientBirthDate);
        et_clientTelephone = findViewById(R.id.et_clientTelephone);
        et_clientEmail = findViewById(R.id.et_clientEmail);
        et_clientPassword = findViewById(R.id.et_clientPassword);
        btn_delete = findViewById(R.id.btn_delete);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("USER");

        Intent i = getIntent();
        String clientId = i.getStringExtra("clientId");
        String clientFirstName = i.getStringExtra("clientFirstName");
        String clientLastName = i.getStringExtra("clientLastName");
        String clientBirthDate = i.getStringExtra("clientBirthDate");
        String clientEmail = i.getStringExtra("clientEmail");
        String clientPassword = i.getStringExtra("clientPassword");
        String clientTelephone = i.getStringExtra("clientTelephone");

        et_clientId.setText(clientId);
        et_clientFirstName.setText(clientFirstName);
        et_clientLastName.setText(clientLastName);
        et_clientBirthDate.setText(clientBirthDate);
        et_clientTelephone.setText(clientTelephone);
        et_clientEmail.setText(clientEmail);
        et_clientPassword.setText(clientPassword);

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };

        et_clientBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Client.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.orderByChild("id").equalTo(Integer.parseInt(clientId))
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    snapshot.getKey();
                                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                                        dataSnapshot.getRef().removeValue();
                                    }
                                    Toast.makeText(Client.this, "Clientul a fost sters cu succes!", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(Client.this, ClientList.class);
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
        et_clientBirthDate.setText(dateFormat.format(myCalendar.getTime()));
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