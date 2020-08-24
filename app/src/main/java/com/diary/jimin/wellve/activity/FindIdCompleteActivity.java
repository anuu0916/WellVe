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
    private Button reFindPwButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id_complete);

        init();

        findIdBackLoginButton = findViewById(R.id.findIdBackLoginButton);
        reFindPwButton = findViewById(R.id.reFindPwButton);

        findIdBackLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FindIdCompleteActivity.this, LoginActivity.class);
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
        reFindPwButton = (Button)findViewById(R.id.reFindPwButton);
        backButton = (Button)findViewById(R.id.findIdBackLoginButton);
    }
}