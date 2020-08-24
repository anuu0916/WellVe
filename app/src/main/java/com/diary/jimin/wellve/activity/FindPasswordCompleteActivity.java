package com.diary.jimin.wellve.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.diary.jimin.wellve.R;

public class FindPasswordCompleteActivity extends AppCompatActivity {

    private Button backLoginButton;
    private Button reSendMailButton;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password_complete);

        init();

        backLoginButton = findViewById(R.id.backLoginButton);
        reSendMailButton = findViewById(R.id.reSendMailButton);

        backLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FindPasswordCompleteActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    void init() {
        backLoginButton = (Button)findViewById(R.id.backLoginButton);
        reSendMailButton = (Button)findViewById(R.id.reSendMailButton);
        backButton = (Button)findViewById(R.id.findPwCompleteBackButton);
    }
}