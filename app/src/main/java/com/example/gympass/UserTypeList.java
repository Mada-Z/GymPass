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

public class UserTypeList extends MainActivity {

    Button bt_add_userType;
    EditText et_user_type;
    ListView lv_userTypes;
    ArrayList<UserTypeModel> userTypeAL;
    DatabaseReference databaseReference;
    long maxId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        bt_add_userType = findViewById(R.id.bt_add_userType);
        et_user_type = findViewById(R.id.et_user_type);
        lv_userTypes = findViewById(R.id.lv_userTypes);
        UserTypeModel userTypeModel = new UserTypeModel();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("USER_TYPE");
        userTypeAL = new ArrayList<UserTypeModel>();

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

        getUserTypes();

        bt_add_userType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userType = et_user_type.getText().toString();
                userTypeModel.setId((int) (maxId+1));
                userTypeModel.setUserType(userType);
                if(!userType.isEmpty()) {
                    databaseReference.orderByChild("userType").equalTo(userType)
                            .addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        Toast.makeText(UserTypeList.this, "Exista deja acest tip de utilizator!", Toast.LENGTH_SHORT).show();
                                        et_user_type.setText("");
                                    } else {
                                        databaseReference.push().setValue(userTypeModel);
                                        Toast.makeText(UserTypeList.this, "Tipul de utilizator a fost adaugat cu succes!", Toast.LENGTH_SHORT).show();
                                        et_user_type.setText("");
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                });
                }
                   else {
                       Toast.makeText(UserTypeList.this, "Toate campurile sunt obligatorii!", Toast.LENGTH_SHORT).show();
                    }

            }
        });

        lv_userTypes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserTypeModel clickedUserType = (UserTypeModel) parent.getItemAtPosition(position);
                Intent i = new Intent(UserTypeList.this, UserType.class);
                i.putExtra("userTypeId", clickedUserType.getId()+"");
                i.putExtra("userType", clickedUserType.getUserType());
                startActivity(i);
            }
        });
    }

    private void getUserTypes() {
        final ArrayAdapter<UserTypeModel> adapter = new ArrayAdapter<UserTypeModel>(this, android.R.layout.simple_dropdown_item_1line, userTypeAL);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("USER_TYPE");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                userTypeAL.add(snapshot.getValue(UserTypeModel.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                userTypeAL.remove(snapshot.getValue(UserTypeModel.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        lv_userTypes.setAdapter(adapter);
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