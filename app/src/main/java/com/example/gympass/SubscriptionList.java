package com.example.gympass;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

public class SubscriptionList extends AdminMainActivity {

    Button bt_add_subscription;
    EditText et_subscription_type;
    EditText et_subscription_description;
    EditText et_subscription_price;
    EditText et_available;
    ListView lv_subscriptions;
    ArrayList<SubscriptionModel> subscriptionAL;
    DatabaseReference databaseReference;
    long maxId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        bt_add_subscription = findViewById(R.id.bt_add_subscription);
        et_subscription_type = findViewById(R.id.et_subscription_type);
        et_subscription_description = findViewById(R.id.et_subscription_description);
        et_subscription_price = findViewById(R.id.et_subscription_price);
        et_available = findViewById(R.id.et_available);
        lv_subscriptions = findViewById(R.id.lv_subscriptions);

        SubscriptionModel subscriptionModel = new SubscriptionModel();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("SUBSCRIPTION");
        subscriptionAL = new ArrayList<SubscriptionModel>();

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

        getSubscriptions();

        bt_add_subscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subscriptionType = et_subscription_type.getText().toString();
                String subscriptionDescription = et_subscription_description.getText().toString();
                String subscriptionPrice = et_subscription_price.getText().toString();
                String available = et_available.getText().toString();
                subscriptionModel.setId((int) (maxId + 1));
                subscriptionModel.setSubscriptionType(subscriptionType);
                subscriptionModel.setDescription(subscriptionDescription);
                subscriptionModel.setPrice(Integer.parseInt(subscriptionPrice));
                subscriptionModel.setAvailable(Integer.parseInt(available));

                if(subscriptionModel.getSubscriptionType().isEmpty() || subscriptionModel.getDescription().isEmpty() || subscriptionModel.getPrice() == 0 || subscriptionModel.getAvailable() == 0){
                    Toast.makeText(SubscriptionList.this, "Toate campurile sunt obligatorii!", Toast.LENGTH_SHORT).show();
                }
                else {
                    databaseReference.orderByChild("subscriptionType").equalTo(subscriptionType)
                            .addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        Toast.makeText(SubscriptionList.this, "Exista deja acest tip de abonament!", Toast.LENGTH_SHORT).show();
                                        et_subscription_type.setText("");
                                        et_subscription_description.setText("");
                                        et_subscription_price.setText("");
                                        et_available.setText("");
                                    } else {
                                        databaseReference.push().setValue(subscriptionModel);
                                        Toast.makeText(SubscriptionList.this, "Tipul de abonament a fost adaugat cu succes!", Toast.LENGTH_SHORT).show();
                                        et_subscription_type.setText("");
                                        et_subscription_description.setText("");
                                        et_subscription_price.setText("");
                                        et_available.setText("");
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }

            }
        });

        lv_subscriptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SubscriptionModel clickedSubscription = (SubscriptionModel) parent.getItemAtPosition(position);
                Intent i = new Intent(SubscriptionList.this, Subscription.class);
                i.putExtra("subscriptionId", clickedSubscription.getId()+"");
                i.putExtra("subscriptionType", clickedSubscription.getSubscriptionType());
                i.putExtra("description", clickedSubscription.getDescription());
                i.putExtra("price", clickedSubscription.getPrice()+"");
                i.putExtra("available", clickedSubscription.getAvailable()+"");
                startActivity(i);
            }
        });

    }

    private void getSubscriptions() {
        final ArrayAdapter<SubscriptionModel> adapter = new ArrayAdapter<SubscriptionModel>(this, android.R.layout.simple_dropdown_item_1line, subscriptionAL);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("SUBSCRIPTION");
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
                subscriptionAL.remove(snapshot.getValue(SubscriptionModel.class));
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
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}