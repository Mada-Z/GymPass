package com.example.gympass;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class ActiveSubscription extends ClientMainActivity {

    ImageView iv_qrCode;
    TextView tv_subscription;
    ListView lv_subscriptions;
    ArrayList<UserSubscriptionModel> clientSubscription;
    ArrayList<ClientModel> clientsAL;
    DatabaseReference databaseReference;
    String data_final;
    String prenume, nume, email, data_nasterii;
    int userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_subscription);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        iv_qrCode = findViewById(R.id.iv_qrCode);
        tv_subscription = findViewById(R.id.tv_subscription);
        lv_subscriptions = findViewById(R.id.lv_subscriptions);
        clientSubscription = new ArrayList<>();
        clientsAL =  new ArrayList<>();

        Intent i = getIntent();
        userId = Integer.parseInt(i.getStringExtra("userId"));

        getActiveSubscriptions();
        getClients();

        lv_subscriptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserSubscriptionModel clickedSubscription = (UserSubscriptionModel) parent.getItemAtPosition(position);
                tv_subscription.setText("Abonament activ");
                MultiFormatWriter writer = new MultiFormatWriter();
                boolean active = false;
                try
                {
                    for(int j = 0; j<clientsAL.size(); j++){
                        if(clientsAL.get(j).getId() ==  userId){
                            nume = clientsAL.get(j).getLast_name();
                            prenume = clientsAL.get(j).getFirst_name();
                            email = clientsAL.get(j).getEmail();
                            data_nasterii = clientsAL.get(j).getBirth_date();
                            active = true;

                        }
                    }
                    if(active) {
                        String full_data = "Nume: " + nume + '\n' +
                                "Prenume: " + prenume + '\n' +
                                "Email: " + email + '\n' +
                                "Data nasterii: " + data_nasterii + '\n' +
                                "Abonament valabil pana la: " + clickedSubscription.getDateEnd();
                        BitMatrix matrix = writer.encode(full_data, BarcodeFormat.QR_CODE, 600, 600);
                        BarcodeEncoder encoder = new BarcodeEncoder();
                        Bitmap bitmap = encoder.createBitmap(matrix);
                        iv_qrCode.setImageBitmap(bitmap);
                    }
                    else{
                        Glide.with(ActiveSubscription.this).load("https://images.pexels.com/photos/7407375/pexels-photo-7407375.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1").into(iv_qrCode);
                        tv_subscription.setText("Abonament inactiv");
                    }

                } catch (WriterException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getActiveSubscriptions() {
        final ArrayAdapter<UserSubscriptionModel> adapter = new ArrayAdapter<UserSubscriptionModel>(this, android.R.layout.simple_dropdown_item_1line, clientSubscription);
        databaseReference = (DatabaseReference) FirebaseDatabase.getInstance().getReference().child("USER_SUBSCRIPTION");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue(UserSubscriptionModel.class).getUserId() == userId) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                    try {
                        if (!new Date().after(sdf.parse(snapshot.getValue(UserSubscriptionModel.class).getDateEnd()))) {
                            clientSubscription.add(snapshot.getValue(UserSubscriptionModel.class));
                            adapter.notifyDataSetChanged();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue(UserSubscriptionModel.class).getUserId() == userId) {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue(UserSubscriptionModel.class).getUserId() == userId) {
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
        lv_subscriptions.setAdapter(adapter);
    }

    private void getClients() {
        databaseReference = (DatabaseReference) FirebaseDatabase.getInstance().getReference().child("USER");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getValue(ClientModel.class).getUserType() == 4) {
                    clientsAL.add(snapshot.getValue(ClientModel.class));
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