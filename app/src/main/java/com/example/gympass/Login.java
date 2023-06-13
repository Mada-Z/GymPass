package com.example.gympass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Login extends MainActivity {

    Button bt_login;
    EditText et_userEmail;
    EditText et_userPassword;
    TextView tv_createUser;
    DatabaseReference databaseReference;
    int userType;
    int userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        bt_login = findViewById(R.id.bt_login);
        et_userEmail = findViewById(R.id.et_userEmail);
        et_userPassword = findViewById(R.id.et_userPassword);
        tv_createUser = findViewById(R.id.tv_createUser);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("USER");

        et_userEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!et_userEmail.getText().toString().isEmpty() && !et_userPassword.getText().toString().isEmpty()){
                    bt_login.setEnabled(true);
                }
                else {
                    bt_login.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        et_userPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!et_userEmail.getText().toString().isEmpty() && !et_userPassword.getText().toString().isEmpty()){
                    bt_login.setEnabled(true);
                }
                else {
                    bt_login.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userEmail = et_userEmail.getText().toString();
                String userPassword = et_userPassword.getText().toString();

                databaseReference.orderByChild("email").equalTo(userEmail)
                        .addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                        if(Objects.equals(snapshot.child("password").getValue(String.class), userPassword)){
                                            userType = snapshot.child("userType").getValue(Integer.class);
                                            userId = snapshot.child("id").getValue(Integer.class);
                                            login(v);
                                        }
                                        else{
                                            Toast.makeText(Login.this, "Datele de autentificare sunt incorecte!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } else {
                                    Toast.makeText(Login.this, "Datele de autentificare sunt incorecte!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

            }
        });

        tv_createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, SignUp.class);
                startActivity(i);
            }
        });

    }

    public void login (View view){

        SessionManagement sessionManagement = new SessionManagement(Login.this);
        sessionManagement.saveSession("email");

        startMainActivity();
    }

    public void startMainActivity(){
        if(userType == 4){
            Intent i = new Intent(Login.this, ClientMainActivity.class);
            i.putExtra("userId", userId+"");
            startActivity(i);
        }
        if(userType == 2){
            Intent i = new Intent(Login.this, GymMainActivity.class);
            i.putExtra("userId", userId+"");
            startActivity(i);
        }
        if(userType == 1){
            Intent i = new Intent(Login.this, AdminMainActivity.class);
            i.putExtra("userId", userId+"");
            startActivity(i);
        }
        Toast.makeText(Login.this, "Logarea s-a facut cu succes!", Toast.LENGTH_SHORT).show();
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