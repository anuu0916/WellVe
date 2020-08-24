package com.diary.jimin.wellve.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.diary.jimin.wellve.R;

public class FindIdCompleteActivity extends AppCompatActivity {

    private Button backButton;
    private Button findIdBackLoginButton;
    private Button findPwButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id_complete);

        init();

        backButton = findViewById(R.id.findIdCompleteBackButton);
        findIdBackLoginButton = findViewById(R.id.findIdBackLoginButton);
        findPwButton = findViewById(R.id.findPwButton);

        findIdBackLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FindIdCompleteActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        findPwButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FindIdCompleteActivity.this, FindPasswordActivity.class);
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
        findIdBackLoginButton = (Button)findViewById(R.id.findIdBackLoginButton);
        findPwButton = (Button)findViewById(R.id.findPwButton);
        backButton = (Button)findViewById(R.id.findIdBackLoginButton);
    }
}