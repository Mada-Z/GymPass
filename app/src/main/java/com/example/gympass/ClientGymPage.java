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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Objects;

public class ClientGymPage extends AppCompatActivity {

    EditText et_name;
    EditText et_gymOpen;
    EditText et_gymClose;
    EditText et_gymStreet;
    EditText et_gymNumber;
    EditText et_gymCity;
    EditText et_gymCounty;
    TextView tv_gymClose;
    ImageView iv_gym;
    Button bt_classes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_gym_page);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        et_name = findViewById(R.id.et_name);
        et_gymOpen = findViewById(R.id.et_gymOpen);
        et_gymClose = findViewById(R.id.et_gymClose);
        et_gymStreet = findViewById(R.id.et_gymStreet);
        et_gymNumber = findViewById(R.id.et_gymNumber);
        et_gymCity = findViewById(R.id.et_gymCity);
        et_gymCounty = findViewById(R.id.et_gymCounty);
        tv_gymClose = findViewById(R.id.tv_gymClose);
        iv_gym = findViewById(R.id.iv_gym);
        bt_classes = findViewById(R.id.bt_classes);

        Intent i = getIntent();
        String userId = i.getStringExtra("userId");
        String gymId= i.getStringExtra("gymId");
        String image = i.getStringExtra("image");
        String name = i.getStringExtra("name");
        String street = i.getStringExtra("street");
        String number = i.getStringExtra("number");
        String city = i.getStringExtra("city");
        String county = i.getStringExtra("county");
        String open = i.getStringExtra("open");
        String close = i.getStringExtra("close");

        et_name.setText(name);
        et_gymStreet.setText(street);
        et_gymNumber.setText(number);
        et_gymCity.setText(city);
        et_gymCounty.setText(county);
        et_gymOpen.setText(open);

        if(Objects.equals(open, "Non-Stop")){
            et_gymClose.setVisibility(View.INVISIBLE);
            tv_gymClose.setVisibility(View.INVISIBLE);
        }
        else{
            et_gymClose.setText(close);
        }
        Glide.with(this).load(image).into(iv_gym);

        bt_classes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ClientGymPage.this, ClientGymClasses.class);
                i.putExtra("userId", userId);
                i.putExtra("gymId", gymId);
                startActivity(i);
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