package com.example.gympass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Gym extends AppCompatActivity {

    EditText et_gymId;
    EditText et_gymName;
    EditText et_gymEmail;
    EditText et_gymPassword;
    Button btn_delete;
    Button btn_details;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        et_gymId= findViewById(R.id.et_gymId);
        et_gymName = findViewById(R.id.et_gymName);
        et_gymEmail = findViewById(R.id.et_gymEmail);
        et_gymPassword = findViewById(R.id.et_gymPassword);
        btn_delete = findViewById(R.id.btn_delete);
        btn_details = findViewById(R.id.btn_details);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("USER");

        Intent i = getIntent();
        String gymId = i.getStringExtra("gymId");
        String gymName = i.getStringExtra("gymName");
        String gymEmail = i.getStringExtra("gymEmail");
        String gymPassword = i.getStringExtra("gymPassword");
        et_gymId.setText(gymId);
        et_gymName.setText(gymName);
        et_gymEmail.setText(gymEmail);
        et_gymPassword.setText(gymPassword);

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.orderByChild("id").equalTo(Integer.parseInt(gymId))
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    snapshot.getKey();
                                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                                        dataSnapshot.getRef().removeValue();
                                    }
                                    Toast.makeText(Gym.this, "Sala a fost stearsa cu succes!", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(Gym.this, GymList.class);
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

        btn_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Gym.this, GymDetails.class);
                i.putExtra("gymId", gymId);
                startActivity(i);
            }
            });
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