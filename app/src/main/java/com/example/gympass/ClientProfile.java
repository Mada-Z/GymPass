package com.example.gympass;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Switch;
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
import java.util.Date;
import java.util.Objects;

public class ClientProfile extends ClientMainActivity {

    EditText et_firstName;
    EditText et_lastName;
    EditText et_birthDate;
    EditText et_telephone;
    EditText et_email;
    ArrayList<UserSubscriptionModel> clientSubscription;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_profile);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        et_firstName = findViewById(R.id.et_firstName);
        et_lastName = findViewById(R.id.et_lastName);
        et_birthDate = findViewById(R.id.et_birthDate);
        et_telephone = findViewById(R.id.et_telephone);
        et_email = findViewById(R.id.et_email);
        clientSubscription = new ArrayList<UserSubscriptionModel>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("USER");

        Intent i = getIntent();
        String userId = i.getStringExtra("userId");

        databaseReference.orderByChild("id").equalTo(Integer.parseInt(userId))
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                et_firstName.setText(snapshot.child("first_name").getValue(String.class));
                                et_lastName.setText(snapshot.child("last_name").getValue(String.class));
                                et_birthDate.setText(snapshot.child("birth_date").getValue(String.class));
                                et_telephone.setText(snapshot.child("telephone").getValue(String.class));
                                et_email.setText(snapshot.child("email").getValue(String.class));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        getClientSubscription(Integer.parseInt(userId));

        boolean active = false;

        for(int j = 0; j< clientSubscription.size(); j++){

            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            Date strDate = null;
            try {
                strDate = sdf.parse(clientSubscription.get(j).getDateEnd());
                if (!new Date().after(strDate)) {
                    active = true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }

    private void getClientSubscription(int id){
        databaseReference = (DatabaseReference) FirebaseDatabase.getInstance().getReference().child("USER_SUBSCRIPTION");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue(UserSubscriptionModel.class).getUserId() == id) {
                    clientSubscription.add(snapshot.getValue(UserSubscriptionModel.class));
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