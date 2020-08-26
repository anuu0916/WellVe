package com.diary.jimin.wellve.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.diary.jimin.wellve.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class FindPasswordCompleteActivity extends AppCompatActivity {

    private Button backLoginButton;
    private Button reSendMailButton;
    private Button backButton;

    private String getEmail;

    private FirebaseAuth auth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password_complete);

        init();

        getEmail = getIntent().getStringExtra("email");

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

        reSendMailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                auth.sendPasswordResetEmail(getEmail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {

                                } else {
                                    Toast.makeText(FindPasswordCompleteActivity.this, "이메일 주소가 틀립니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    void init() {
        backLoginButton = (Button)findViewById(R.id.backLoginButton);
        reSendMailButton = (Button)findViewById(R.id.reSendMailButton);
        backButton = (Button)findViewById(R.id.findPwCompleteBackButton);
    }
}