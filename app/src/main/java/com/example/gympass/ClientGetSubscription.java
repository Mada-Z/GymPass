package com.example.gympass;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ClientGetSubscription extends AppCompatActivity {

    EditText et_subscriptionType;
    EditText et_description;
    EditText et_subscriptionPrice;
    EditText et_available;
    Button btn_buy;
    long maxId;
    DatabaseReference databaseReference;
    int userId;
    ArrayList<UserSubscriptionModel> clientSubscription;
    ArrayAdapter<UserSubscriptionModel> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_get_subscription);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        et_subscriptionType = findViewById(R.id.et_subscriptionType);
        et_description = findViewById(R.id.et_subscriptionDescription);
        et_subscriptionPrice = findViewById(R.id.et_subscriptionPrice);
        et_available = findViewById(R.id.et_available);
        btn_buy = findViewById(R.id.btn_buy);
        clientSubscription = new ArrayList<UserSubscriptionModel>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("USER_SUBSCRIPTION");
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, clientSubscription);

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

        Intent i = getIntent();
        userId = Integer.parseInt(i.getStringExtra("userId"));
        String subscriptionId = i.getStringExtra("subscriptionId");
        String subscriptionType = i.getStringExtra("subscriptionType");
        String description = i.getStringExtra("subscriptionDescription");
        String price = i.getStringExtra("subscriptionPrice");
        String available = i.getStringExtra("subscriptionAvailable");
        et_subscriptionType.setText(subscriptionType);
        et_description.setText(description);
        et_subscriptionPrice.setText(price);
        et_available.setText(available);

        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                String date = sdf.format(new Date());

                try {
                    Date startDate = sdf.parse(date);
                    String endDate = sdf.format(new Date(startDate.getTime() + ((long) Integer.parseInt(available) * 24 * 60 * 60 * 1000)));
                    UserSubscriptionModel userSubscriptionModel = new UserSubscriptionModel();
                    userSubscriptionModel.setId((int) (maxId + 1));
                    userSubscriptionModel.setUserId(userId);
                    userSubscriptionModel.setSubscriptionId(Integer.parseInt(subscriptionId));
                    userSubscriptionModel.setDateStart(date);
                    userSubscriptionModel.setDateEnd(endDate);
                    databaseReference.push().setValue(userSubscriptionModel);
                    Toast.makeText(ClientGetSubscription.this, "Abonamentul a fost cumparat cu succes!", Toast.LENGTH_SHORT).show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }


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
