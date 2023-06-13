package com.example.gympass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class PopupActivity extends MainActivity {

    ImageView iv_gym;
    TextView tv_address;
    Button bt_viewGym;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        String userId = i.getStringExtra("userId");
        String gymId = i.getStringExtra("gymId");
        String image = i.getStringExtra("image");
        String name = i.getStringExtra("name");
        String street = i.getStringExtra("street");
        String number = i.getStringExtra("number");
        String city = i.getStringExtra("city");
        String county = i.getStringExtra("county");
        String open = i.getStringExtra("open");
        String close = i.getStringExtra("close");

        iv_gym = findViewById(R.id.iv_gym);
        tv_address = findViewById(R.id.tv_address);
        bt_viewGym = findViewById(R.id.bt_viewGym);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width =dm.widthPixels;
        int hight=dm.heightPixels;

        getWindow().setLayout((int)(width),(int)(hight*.6));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;

        String program;
        if(Objects.equals(open, "Non-Stop")){
            program = "Program: " + '\n' + open;
        }
        else {
            program = "Program: " + '\n' + open + " - " + close;
        }

        Glide.with(this).load(image).into(iv_gym);
        tv_address.setText(program);

        params.x=10;
        params.y=12;
        getWindow().setAttributes(params);
        bt_viewGym.setEnabled(Integer.parseInt(userId) != 0);

        bt_viewGym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PopupActivity.this, ClientGymPage.class);
                i.putExtra("userId", userId);
                i.putExtra("gymId", gymId);
                i.putExtra("image", image);
                i.putExtra("name", name);
                i.putExtra("street", street);
                i.putExtra("number", number);
                i.putExtra("city", city);
                i.putExtra("county", county);
                i.putExtra("open", open);
                i.putExtra("close", close);
                startActivity(i);
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