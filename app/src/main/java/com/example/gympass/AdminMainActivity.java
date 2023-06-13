package com.example.gympass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class AdminMainActivity extends AppCompatActivity {

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        Intent i = getIntent();
        userId = i.getStringExtra("userId");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.userType): {
                Intent i = new Intent(AdminMainActivity.this, UserTypeList.class);
                startActivity(i);
                return true;
            }
            case (R.id.admin): {
                Intent i = new Intent(AdminMainActivity.this, AdminList.class);
                startActivity(i);
                return true;
            }
            case (R.id.gym): {
                Intent i = new Intent(AdminMainActivity.this, GymList.class);
                startActivity(i);
                return true;
            }
            case (R.id.trainers): {
                Intent i = new Intent(AdminMainActivity.this, TrainerList.class);
                startActivity(i);
                return true;
            }
            case (R.id.clients): {
                Intent i = new Intent(AdminMainActivity.this, ClientList.class);
                startActivity(i);
                return true;
            }
            case (R.id.subscritions): {
                Intent i = new Intent(AdminMainActivity.this, SubscriptionList.class);
                startActivity(i);
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }
}