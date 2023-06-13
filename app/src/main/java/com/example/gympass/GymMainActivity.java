package com.example.gympass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class GymMainActivity extends AppCompatActivity {

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_main);

        Intent i = getIntent();
        userId = i.getStringExtra("userId");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gym_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.profile): {
                Intent i = new Intent(GymMainActivity.this, GymProfile.class);
                i.putExtra("userId", userId);
                startActivity(i);
                return true;
            }
            case (R.id.trainers): {
                Intent i = new Intent(GymMainActivity.this, GymTrainer.class);
                i.putExtra("userId", userId);
                startActivity(i);
                return true;
            }
            case (R.id.classes): {
                Intent i = new Intent(GymMainActivity.this, ClassSchedule.class);
                i.putExtra("userId", userId);
                startActivity(i);
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }
}