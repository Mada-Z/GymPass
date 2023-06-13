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

public class UserType extends AppCompatActivity {


    EditText et_userTypeId;
    EditText et_userType;
    Button btn_delete;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        et_userTypeId = findViewById(R.id.et_userTypeId);
        et_userType = findViewById(R.id.et_userType);
        btn_delete = findViewById(R.id.btn_delete);

        Intent i = getIntent();
        String userTypeId = i.getStringExtra("userTypeId");
        String userType = i.getStringExtra("userType");
        et_userTypeId.setText(userTypeId);
        et_userType.setText(userType);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("USER_TYPE");

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.orderByChild("id").equalTo(Integer.parseInt(userTypeId))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                                dataSnapshot.getRef().removeValue();
                            }
                            Toast.makeText(UserType.this, "Tipul de utilizator a fost sters cu succes!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(UserType.this, UserTypeList.class);
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
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}