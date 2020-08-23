package com.diary.jimin.wellve.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.diary.jimin.wellve.R;

public class SettingsActivity extends AppCompatActivity {

    private Button toDeveloperButton;
    private Button logoutButton;
    private Button memberOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        init();

        toDeveloperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, DeveloperActivity.class);
                startActivity(intent);
            }
        });


    }

    void init() {
        toDeveloperButton = (Button)findViewById(R.id.toDeveloperButton);
        logoutButton = (Button)findViewById(R.id.logoutButton);
        memberOutButton = (Button)findViewById(R.id.memberOutButton);
    }

}