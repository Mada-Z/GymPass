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

public class Subscription extends AppCompatActivity {

    EditText et_subscriptionId;
    EditText et_subscriptionType;
    EditText et_description;
    EditText et_subscriptionPrice;
    EditText et_available;
    Button btn_delete;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        et_subscriptionId = findViewById(R.id.et_subscriptionId);
        et_subscriptionType = findViewById(R.id.et_subscriptionType);
        et_description = findViewById(R.id.et_subscriptionDescription);
        et_subscriptionPrice = findViewById(R.id.et_subscriptionPrice);
        et_available = findViewById(R.id.et_available);
        btn_delete = findViewById(R.id.btn_delete);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("SUBSCRIPTION");

        Intent i = getIntent();
        String subscriptionId = i.getStringExtra("subscriptionId");
        String subscriptionType = i.getStringExtra("subscriptionType");
        String description = i.getStringExtra("description");
        String price = i.getStringExtra("price");
        String available = i.getStringExtra("available");
        et_subscriptionId.setText(subscriptionId);
        et_subscriptionType.setText(subscriptionType);
        et_description.setText(description);
        et_subscriptionPrice.setText(price);
        et_available.setText(available);

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.orderByChild("id").equalTo(Integer.parseInt(subscriptionId))
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    snapshot.getKey();
                                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                                        dataSnapshot.getRef().removeValue();
                                    }
                                    Toast.makeText(Subscription.this, "Tipul de abonament a fost sters cu succes!", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(Subscription.this, SubscriptionList.class);
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