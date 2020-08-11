package com.diary.jimin.wellve;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";

    private EditText emailEditText;
    private EditText pwEditText;
    private EditText pwCheckEditText;
    private Button signupButton;
    private Button loginButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();

        signupButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
        loginButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    void init() {
        emailEditText = (EditText)findViewById(R.id.emailEditText);
        pwEditText = (EditText)findViewById(R.id.pwEditText);
        pwCheckEditText = (EditText)findViewById(R.id.pwCheckEditText);
        signupButton = (Button)findViewById(R.id.signUpButton);
        loginButton = (Button)findViewById(R.id.loginButton);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }

    private void signUp() {
        String email = emailEditText.getText().toString();
        String password = pwEditText.getText().toString();
        String passwordCheck = pwCheckEditText.getText().toString();

        if(email.length()>0 && password.length()>0 && passwordCheck.length()>0) {
            if(password.equals(passwordCheck)) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(SignupActivity.this, "회원가입에 성공했습니다.",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);

                                    FirebaseUser user = mAuth.getCurrentUser();
////                            updateUI(user);
                                } else {

                                    /*********************** To Do List ********************/

                                    /** 여기서 password의 길이가 6이상이여야 함. exception 처리 하기 **/
                                    /** 이미 있는 계정일때 exception 처리 하기 **/

                                    /*******************************************************/

                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignupActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                                }

                            }
                        });
            } else {
                Toast.makeText(SignupActivity.this, "비밀번호가 일치하지 않습니다.",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(SignupActivity.this, "이메일 또는 비밀번호를 입력해주세요.",
                    Toast.LENGTH_SHORT).show();
        }


    }
}
