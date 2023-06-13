package com.example.gympass;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Objects;

public class AdminList extends MainActivity {

    Button bt_add_admin;
    EditText et_admin_email;
    EditText et_admin_password;
    ListView lv_admins;
    ArrayList<AdminModel> adminAL;
    DatabaseReference databaseReference;
    long maxId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_list);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        bt_add_admin = findViewById(R.id.bt_add_admin);
        et_admin_email = findViewById(R.id.et_admin_email);
        et_admin_password = findViewById(R.id.et_admin_password);
        lv_admins = findViewById(R.id.lv_admins);
        AdminModel adminModel = new AdminModel();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("USER");
        adminAL = new ArrayList<>();

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

        getAdmins();

        bt_add_admin.setOnClickListener(v -> {

            String adminEmail = et_admin_email.getText().toString();
            String adminPassword = et_admin_password.getText().toString();
            adminModel.setId((int) (maxId + 1));
            adminModel.setUserType(1);
            adminModel.setEmail(adminEmail);
            adminModel.setPassword(adminPassword);

            if (adminModel.getEmail().isEmpty() || adminModel.getPassword().isEmpty()) {
                Toast.makeText(AdminList.this, "Toate campurile sunt obligatorii!", Toast.LENGTH_SHORT).show();
            } else {
                databaseReference.orderByChild("email").equalTo(adminEmail)
                        .addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    Toast.makeText(AdminList.this, "Exista deja acest admin!", Toast.LENGTH_SHORT).show();
                                    et_admin_email.setText("");
                                    et_admin_password.setText("");
                                } else {
                                    databaseReference.push().setValue(adminModel);
                                    Toast.makeText(AdminList.this, "Adminul a fost adaugat cu succes!", Toast.LENGTH_SHORT).show();
                                    et_admin_email.setText("");
                                    et_admin_password.setText("");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        });
        lv_admins.setOnItemClickListener((parent, view, position, id) -> {
            AdminModel clickedAdmin = (AdminModel) parent.getItemAtPosition(position);
            Intent i = new Intent(AdminList.this, Admin.class);
            i.putExtra("adminId", clickedAdmin.getId()+"");
            i.putExtra("adminEmail", clickedAdmin.getEmail());
            i.putExtra("adminPassword", clickedAdmin.getPassword());
            startActivity(i);
        });
    }

    private void getAdmins() {
        final ArrayAdapter<AdminModel> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, adminAL);
        databaseReference = (DatabaseReference) FirebaseDatabase.getInstance().getReference().child("USER");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(Objects.requireNonNull(snapshot.getValue(AdminModel.class)).getUserType() == 1) {
                    adminAL.add(snapshot.getValue(AdminModel.class));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(Objects.requireNonNull(snapshot.getValue(AdminModel.class)).getUserType() == 1) {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                if(Objects.requireNonNull(snapshot.getValue(AdminModel.class)).getUserType() == 1) {
                    adminAL.remove(snapshot.getValue(AdminModel.class));
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
        lv_admins.setAdapter(adapter);
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
