package com.example.gympass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class SignUp extends MainActivity {

    final Calendar myCalendar= Calendar.getInstance();

    EditText et_clientFirstname;
    EditText et_clientLastName;
    EditText et_clientBirthDate;
    EditText et_clientTelephone;
    EditText et_clientEmail;
    EditText et_clientPassword;
    EditText et_confirmPassword;
    TextView tv_clientFirstName;
    TextView tv_clientLastName;
    TextView tv_clientBirthDate;
    TextView tv_clientTelephone;
    TextView tv_clientEmail;
    TextView tv_clientPassword;
    TextView tv_confirmPassword;
    TextView tv_mandatory1;
    TextView tv_mandatory2;
    TextView tv_mandatory3;
    TextView tv_mandatory4;
    TextView tv_mandatory5;
    TextView tv_mandatory6;
    TextView tv_mandatory7;
    Button bt_createAccount;
    DatabaseReference databaseReference;
    long maxId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        et_clientFirstname = findViewById(R.id.et_clientFirstName);
        et_clientLastName = findViewById(R.id.et_clientLastName);
        et_clientBirthDate = findViewById(R.id.et_clientBirthDate);
        et_clientTelephone = findViewById(R.id.et_clientTelephone);
        et_clientEmail = findViewById(R.id.et_clientEmail);
        et_clientPassword = findViewById(R.id.et_clientPassword);
        et_confirmPassword = findViewById(R.id.et_confirmPassword);
        tv_clientFirstName = findViewById(R.id.tv_clientFirstName);
        tv_clientLastName = findViewById(R.id.tv_clientLastName);
        tv_clientBirthDate = findViewById(R.id.tv_clientBirthDate);
        tv_clientTelephone = findViewById(R.id.tv_clientTelephone);
        tv_clientEmail = findViewById(R.id.tv_clientEmail);
        tv_clientPassword = findViewById(R.id.tv_clientPassword);
        tv_confirmPassword = findViewById(R.id.tv_confirmPassword);
        tv_mandatory1 = findViewById(R.id.tv_mandatory1);
        tv_mandatory2 = findViewById(R.id.tv_mandatory2);
        tv_mandatory3 = findViewById(R.id.tv_mandatory3);
        tv_mandatory4 = findViewById(R.id.tv_mandatory4);
        tv_mandatory5 = findViewById(R.id.tv_mandatory5);
        tv_mandatory6 = findViewById(R.id.tv_mandatory6);
        tv_mandatory7 = findViewById(R.id.tv_mandatory7);
        bt_createAccount = findViewById(R.id.bt_createAccount);
        ClientModel clientModel = new ClientModel();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("USER");

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

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };

        et_clientBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(SignUp.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        et_clientFirstname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && et_clientFirstname.getText().toString().isEmpty()){
                    tv_mandatory1.setText("Campul este obligatoriu!");
                    tv_mandatory1.setVisibility(View.VISIBLE);
                }
                else {
                    tv_mandatory1.setVisibility(View.INVISIBLE);
                }
            }
        });

        et_clientLastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && et_clientLastName.getText().toString().isEmpty()){
                    tv_mandatory2.setText("Campul este obligatoriu!");
                    tv_mandatory2.setVisibility(View.VISIBLE);
                }
                else {
                    tv_mandatory2.setVisibility(View.INVISIBLE);
                }
            }
        });

        et_clientBirthDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && et_clientBirthDate.getText().toString().isEmpty()){
                    tv_mandatory3.setText("Campul este obligatoriu!");
                    tv_mandatory3.setVisibility(View.VISIBLE);
                }
                else {

                    if(!et_clientBirthDate.getText().toString().isEmpty()) {
                        int birthYear = myCalendar.get(Calendar.YEAR);
                        int year = 0;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            year = Year.now().getValue();
                        }

                        if (year - birthYear <= 14) {
                            tv_mandatory3.setVisibility(View.VISIBLE);
                            tv_mandatory3.setText("Trebuie sa ai minim 14 ani!");
                        } else {
                            tv_mandatory3.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
        });

        et_clientTelephone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && et_clientTelephone.getText().toString().isEmpty()){
                    tv_mandatory4.setText("Campul este obligatoriu!");
                    tv_mandatory4.setVisibility(View.VISIBLE);
                }
                else {
                    if(!PhoneNumberUtils.isGlobalPhoneNumber(et_clientTelephone.getText().toString())){
                        tv_mandatory4.setText("Numarul de telefon nu este corect");
                        tv_mandatory4.setVisibility(View.VISIBLE);
                    }
                    else {
                        tv_mandatory4.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        et_clientEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && et_clientEmail.getText().toString().isEmpty()){
                    tv_mandatory5.setVisibility(View.VISIBLE);
                    tv_mandatory5.setText("Campul este obligatoriu!");
                }
                else {
                    if(!Patterns.EMAIL_ADDRESS.matcher(et_clientEmail.getText().toString()).matches()){
                        tv_mandatory5.setVisibility(View.VISIBLE);
                        tv_mandatory5.setText("Adresa de e-mail nu este corecta!");
                    }
                    else {
                        databaseReference.orderByChild("email").equalTo(et_clientEmail.getText().toString())
                                .addListenerForSingleValueEvent(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            tv_mandatory5.setVisibility(View.VISIBLE);
                                            tv_mandatory5.setText("Cont deja existent!");
                                        } else {
                                            tv_mandatory5.setVisibility(View.INVISIBLE);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                    }
                }

            }
        });

        et_clientPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && et_clientPassword.getText().toString().isEmpty()){
                    tv_mandatory6.setText("Campul este obligatoriu!");
                    tv_mandatory6.setVisibility(View.VISIBLE);
                }
                else {
                    tv_mandatory6.setVisibility(View.INVISIBLE);
                }
            }
        });

        et_confirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && et_confirmPassword.getText().toString().isEmpty()){
                    tv_mandatory7.setText("Campul este obligatoriu!");
                    tv_mandatory7.setVisibility(View.VISIBLE);
                }
                else {
                    String password1 = et_clientPassword.getText().toString();
                    String password2 = et_confirmPassword.getText().toString();

                    if(!Objects.equals(password1, password2)){
                        tv_mandatory7.setText("Parolele nu sunt la fel!");
                        tv_mandatory7.setVisibility(View.VISIBLE);
                    }
                    else {
                        tv_mandatory7.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        et_clientFirstname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!et_clientFirstname.getText().toString().isEmpty() &&
                        !et_clientLastName.getText().toString().isEmpty() &&
                        !et_clientBirthDate.getText().toString().isEmpty() &&
                        !et_clientTelephone.getText().toString().isEmpty() &&
                        !et_clientEmail.getText().toString().isEmpty() &&
                        !et_clientPassword.getText().toString().isEmpty() &&
                        !et_confirmPassword.getText().toString().isEmpty()){
                    bt_createAccount.setEnabled(true);
                }
                else {
                    bt_createAccount.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_clientLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!et_clientFirstname.getText().toString().isEmpty() &&
                        !et_clientLastName.getText().toString().isEmpty() &&
                        !et_clientBirthDate.getText().toString().isEmpty() &&
                        !et_clientTelephone.getText().toString().isEmpty() &&
                        !et_clientEmail.getText().toString().isEmpty() &&
                        !et_clientPassword.getText().toString().isEmpty() &&
                        !et_confirmPassword.getText().toString().isEmpty()){
                    bt_createAccount.setEnabled(true);
                }
                else {
                    bt_createAccount.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_clientBirthDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!et_clientFirstname.getText().toString().isEmpty() &&
                        !et_clientLastName.getText().toString().isEmpty() &&
                        !et_clientBirthDate.getText().toString().isEmpty() &&
                        !et_clientTelephone.getText().toString().isEmpty() &&
                        !et_clientEmail.getText().toString().isEmpty() &&
                        !et_clientPassword.getText().toString().isEmpty() &&
                        !et_confirmPassword.getText().toString().isEmpty()){
                    bt_createAccount.setEnabled(true);
                }
                else {
                    bt_createAccount.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_clientTelephone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!et_clientFirstname.getText().toString().isEmpty() &&
                        !et_clientLastName.getText().toString().isEmpty() &&
                        !et_clientBirthDate.getText().toString().isEmpty() &&
                        !et_clientTelephone.getText().toString().isEmpty() &&
                        !et_clientEmail.getText().toString().isEmpty() &&
                        !et_clientPassword.getText().toString().isEmpty() &&
                        !et_confirmPassword.getText().toString().isEmpty()){
                    bt_createAccount.setEnabled(true);
                }
                else {
                    bt_createAccount.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_clientEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!et_clientFirstname.getText().toString().isEmpty() &&
                        !et_clientLastName.getText().toString().isEmpty() &&
                        !et_clientBirthDate.getText().toString().isEmpty() &&
                        !et_clientTelephone.getText().toString().isEmpty() &&
                        !et_clientEmail.getText().toString().isEmpty() &&
                        !et_clientPassword.getText().toString().isEmpty() &&
                        !et_confirmPassword.getText().toString().isEmpty()){
                    bt_createAccount.setEnabled(true);
                }
                else {
                    bt_createAccount.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_clientPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!et_clientFirstname.getText().toString().isEmpty() &&
                        !et_clientLastName.getText().toString().isEmpty() &&
                        !et_clientBirthDate.getText().toString().isEmpty() &&
                        !et_clientTelephone.getText().toString().isEmpty() &&
                        !et_clientEmail.getText().toString().isEmpty() &&
                        !et_clientPassword.getText().toString().isEmpty() &&
                        !et_confirmPassword.getText().toString().isEmpty()){
                    bt_createAccount.setEnabled(true);
                }
                else {
                    bt_createAccount.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!et_clientFirstname.getText().toString().isEmpty() &&
                        !et_clientLastName.getText().toString().isEmpty() &&
                        !et_clientBirthDate.getText().toString().isEmpty() &&
                        !et_clientTelephone.getText().toString().isEmpty() &&
                        !et_clientEmail.getText().toString().isEmpty() &&
                        !et_clientPassword.getText().toString().isEmpty() &&
                        !et_confirmPassword.getText().toString().isEmpty()){
                    bt_createAccount.setEnabled(true);
                }
                else {
                    bt_createAccount.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        bt_createAccount.setOnClickListener(v -> {
            String clientFirstName = et_clientFirstname.getText().toString();
            String clientLastName = et_clientLastName.getText().toString();
            String clientBirthDate = et_clientBirthDate.getText().toString();
            String clientTelephone = et_clientTelephone.getText().toString();
            String clientEmail = et_clientEmail.getText().toString();
            String clientPassword = et_clientPassword.getText().toString();

            clientModel.setId((int) maxId + 1);
            clientModel.setUserType(4);
            clientModel.setFirst_name(clientFirstName);
            clientModel.setLast_name(clientLastName);
            clientModel.setBirth_date(clientBirthDate);
            clientModel.setTelephone(clientTelephone);
            clientModel.setEmail(clientEmail);
            clientModel.setPassword(clientPassword);

            if(clientModel.getFirst_name().isEmpty() || clientModel.getLast_name().isEmpty() || clientModel.getBirth_date().isEmpty() || clientModel.getEmail().isEmpty() || clientModel.getPassword().isEmpty() || clientModel.getTelephone().isEmpty()) {
                Toast.makeText(SignUp.this, "Toate campurile sunt obligatorii", Toast.LENGTH_SHORT).show();
            }
            else {
                databaseReference.push().setValue(clientModel);
                Intent i = new Intent(SignUp.this, Login.class);
                startActivity(i);
            }
        });
    }

    private void updateLabel(){
        String myFormat="dd.MM.yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        et_clientBirthDate.setText(dateFormat.format(myCalendar.getTime()));
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