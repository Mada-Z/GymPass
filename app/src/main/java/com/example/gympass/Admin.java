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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Admin extends AppCompatActivity {

    EditText et_adminId;
    EditText et_adminEmail;
    EditText et_adminPassword;
    Button btn_delete;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        et_adminId = findViewById(R.id.et_adminId);
        et_adminEmail = findViewById(R.id.et_adminEmail);
        et_adminPassword = findViewById(R.id.et_adminPassword);
        btn_delete = findViewById(R.id.btn_delete);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("USER");

        Intent i = getIntent();
        String adminId = i.getStringExtra("adminId");
        String adminEmail = i.getStringExtra("adminEmail");
        String adminPassword = i.getStringExtra("adminPassword");
        et_adminId.setText(adminId);
        et_adminEmail.setText(adminEmail);
        et_adminPassword.setText(adminPassword);

        btn_delete.setOnClickListener(v -> databaseReference.orderByChild("id").equalTo(Integer.parseInt(adminId))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            snapshot.getKey();
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                                dataSnapshot.getRef().removeValue();
                            }
                            Toast.makeText(Admin.this, "Adminul a fost sters cu succes!", Toast.LENGTH_SHORT).show();
                            Intent i1 = new Intent(Admin.this, AdminList.class);
                            startActivity(i1);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }));
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