package com.example.gympass;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ClientMainActivity extends AppCompatActivity {

    String userType;
    String userId;
    ArrayList<UserSubscriptionModel> clientSubscription;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_main);

        Intent i = getIntent();
        userId = i.getStringExtra("userId");

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.client_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case (R.id.profile): {
                Intent i = new Intent(ClientMainActivity.this, ClientProfile.class);
                i.putExtra("userId", userId);
                startActivity(i);
                return true;
            }
            case (R.id.gymMap): {
                Intent i = new Intent(ClientMainActivity.this, GymMap.class);
                i.putExtra("userId", userId);
                startActivity(i);
                return true;
            }
            case (R.id.getSubscription): {
                Intent i = new Intent(ClientMainActivity.this, ClientSubscriptionList.class);
                i.putExtra("userId", userId);
                startActivity(i);
                return true;
            }
            case (R.id.subscriptions): {
                Intent i = new Intent(ClientMainActivity.this, ActiveSubscription.class);
                i.putExtra("userId", userId);
                startActivity(i);
                return true;
            }
            case (R.id.reservation): {
                Intent i = new Intent(ClientMainActivity.this, ClientReservedClasses.class);
                i.putExtra("userId", userId);
                startActivity(i);
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

}