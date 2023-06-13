package com.example.gympass;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ClientList extends MainActivity {

    final Calendar myCalendar= Calendar.getInstance();

    Button bt_add_client;
    EditText et_client_first_name;
    EditText et_client_last_name;
    EditText et_client_birth_date;
    EditText et_client_telephone;
    EditText et_client_email;
    EditText et_client_password;
    ListView lv_clients;
    ArrayList<ClientModel> clientAL;
    DatabaseReference databaseReference;
    long maxId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        bt_add_client = findViewById(R.id.bt_add_client);
        et_client_first_name = findViewById(R.id.et_client_first_name);
        et_client_last_name = findViewById(R.id.et_client_last_name);
        et_client_birth_date = findViewById(R.id.et_client_birth_date);
        et_client_telephone = findViewById(R.id.et_client_telephone);
        et_client_email = findViewById(R.id.et_client_email);
        et_client_password = findViewById(R.id.et_client_password);
        lv_clients = findViewById(R.id.lv_clients);

        ClientModel clientModel = new ClientModel();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("USER");
        clientAL = new ArrayList<ClientModel>();

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

        getClients();

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };

        et_client_birth_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ClientList.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        bt_add_client.setOnClickListener(v -> {
            String clientFirstName = et_client_first_name.getText().toString();
            String clientLastName = et_client_last_name.getText().toString();
            String clientBirthDate = et_client_birth_date.getText().toString();
            String clientTelephone = et_client_telephone.getText().toString();
            String clientEmail = et_client_email.getText().toString();
            String clientPassword = et_client_password.getText().toString();

            clientModel.setId((int) maxId + 1);
            clientModel.setUserType(4);
            clientModel.setFirst_name(clientFirstName);
            clientModel.setLast_name(clientLastName);
            clientModel.setBirth_date(clientBirthDate);
            clientModel.setTelephone(clientTelephone);
            clientModel.setEmail(clientEmail);
            clientModel.setPassword(clientPassword);

            if(clientModel.getFirst_name().isEmpty() || clientModel.getLast_name().isEmpty() || clientModel.getBirth_date().isEmpty() || clientModel.getEmail().isEmpty() || clientModel.getPassword().isEmpty() || clientModel.getTelephone().isEmpty()) {
                Toast.makeText(ClientList.this, "Toate campurile sunt obligatorii", Toast.LENGTH_SHORT).show();
            }
            else {
                databaseReference.orderByChild("email").equalTo(clientEmail)
                        .addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    Toast.makeText(ClientList.this, "Exista deja acest client!", Toast.LENGTH_SHORT).show();
                                } else {
                                    databaseReference.push().setValue(clientModel);
                                    Toast.makeText(ClientList.this, "Clientul a fost adaugat cu succes!", Toast.LENGTH_SHORT).show();
                                }
                                et_client_first_name.setText("");
                                et_client_last_name.setText("");
                                et_client_birth_date.setText("");
                                et_client_telephone.setText("");
                                et_client_email.setText("");
                                et_client_password.setText("");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }

        });

        lv_clients.setOnItemClickListener((parent, view, position, id) -> {
            ClientModel clickedclient = (ClientModel) parent.getItemAtPosition(position);
            Intent i = new Intent(ClientList.this, Client.class);
            i.putExtra("clientId", clickedclient.getId()+"");
            i.putExtra("clientFirstName", clickedclient.getFirst_name());
            i.putExtra("clientLastName", clickedclient.getLast_name());
            i.putExtra("clientBirthDate", clickedclient.getBirth_date());
            i.putExtra("clientEmail", clickedclient.getEmail());
            i.putExtra("clientPassword", clickedclient.getPassword());
            i.putExtra("clientTelephone", clickedclient.getTelephone());
            startActivity(i);
        });

    }

    private void getClients() {
        final ArrayAdapter<ClientModel> adapter = new ArrayAdapter<ClientModel>(this, android.R.layout.simple_dropdown_item_1line, clientAL);
        databaseReference = (DatabaseReference) FirebaseDatabase.getInstance().getReference().child("USER");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue(ClientModel.class).getUserType() == 4) {
                    clientAL.add(snapshot.getValue(ClientModel.class));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue(ClientModel.class).getUserType() == 4) {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue(ClientModel.class).getUserType() == 4) {
                    clientAL.remove(snapshot.getValue(ClientModel.class));
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
        lv_clients.setAdapter(adapter);
    }

    private void updateLabel(){
        String myFormat="dd.MM.yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        et_client_birth_date.setText(dateFormat.format(myCalendar.getTime()));
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