package com.example.gympass;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ClientSubscriptionList extends AppCompatActivity {

    ListView lv_subscriptions;
    ArrayList<SubscriptionModel> subscriptionAL;
    DatabaseReference databaseReference;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_subscription_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        lv_subscriptions = findViewById(R.id.lv_subscriptions);
        subscriptionAL = new ArrayList<SubscriptionModel>();
        getSubscriptions();

        Intent i = getIntent();
        userId = i.getStringExtra("userId");

        lv_subscriptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SubscriptionModel clickedSubscription = (SubscriptionModel) parent.getItemAtPosition(position);
                Intent i = new Intent(ClientSubscriptionList.this, ClientGetSubscription.class);
                i.putExtra("userId", userId);
                i.putExtra("subscriptionId", clickedSubscription.getId() + "");
                i.putExtra("subscriptionType", clickedSubscription.getSubscriptionType());
                i.putExtra("subscriptionDescription", clickedSubscription.getDescription());
                i.putExtra("subscriptionPrice", clickedSubscription.getPrice()+"");
                i.putExtra("subscriptionAvailable", clickedSubscription.getAvailable()+"");
                startActivity(i);
            }
        });
    }

    private void getSubscriptions(){
        final ArrayAdapter<SubscriptionModel> adapter = new ArrayAdapter<SubscriptionModel>(this, android.R.layout.simple_dropdown_item_1line, subscriptionAL);
        databaseReference = (DatabaseReference) FirebaseDatabase.getInstance().getReference().child("SUBSCRIPTION");
            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            subscriptionAL.add(snapshot.getValue(SubscriptionModel.class));
                            adapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        adapter.notifyDataSetChanged();
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        subscriptionAL.remove(snapshot.getValue(ClassesModel.class));
                        adapter.notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            lv_subscriptions.setAdapter(adapter);
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