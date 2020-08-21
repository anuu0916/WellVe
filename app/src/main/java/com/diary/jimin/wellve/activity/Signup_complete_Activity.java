package com.diary.jimin.wellve.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.diary.jimin.wellve.R;

public class Signup_complete_Activity extends AppCompatActivity {


    private Button backToMainButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_complete);

        init();

        backToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signup_complete_Activity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void init() {

        backToMainButton = (Button) findViewById(R.id.backToMainButton);
    }

}