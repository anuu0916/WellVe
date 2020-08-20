package com.diary.jimin.wellve.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.diary.jimin.wellve.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordResetActivity extends AppCompatActivity {

    private static final String TAG = "PasswordResetActivity";

    private EditText emailEditText;
    private Button sendButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        init();

        sendButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });

    }

    void init() {
        emailEditText = (EditText)findViewById(R.id.resetEmailEditText);
        sendButton = (Button)findViewById(R.id.resetSendButton);

        mAuth = FirebaseAuth.getInstance();
    }

    /*********************** To Do List ********************/

    /** 이미 있는 계정 == email 처리 하기 **/

    /*******************************************************/

    private void send() {
        String email = emailEditText.getText().toString();


        if(email.length()>0) {
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(PasswordResetActivity.this, "이메일을 보냈습니다.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        } else {
            Toast.makeText(PasswordResetActivity.this, "이메일을 입력해주세요.",
                    Toast.LENGTH_SHORT).show();
        }

    }
}
