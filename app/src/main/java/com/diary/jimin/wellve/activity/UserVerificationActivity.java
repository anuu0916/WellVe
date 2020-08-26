package com.diary.jimin.wellve.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.diary.jimin.wellve.R;
import com.google.firebase.auth.FirebaseAuth;

public class UserVerificationActivity extends AppCompatActivity {

    //이런식으로... 앞번호 선택할수 있게 해놨는데 개발할때 불편하면 걍 없다고 생각하고 하삼 !! 추후에 없앨게

    String[] items = {"010", "011", "019"};

    private Button backButton;
    private Button userAuthNumButton;
    private Button userVerificationNextButton;
    private EditText userPhoneNumEditText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_verification);

        init();

        Spinner spinner = findViewById(R.id.singUpSpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        userVerificationNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserVerificationActivity.this, SignupActivity.class);
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
        backButton = (Button)findViewById(R.id.userVerificationBackButton);
        userAuthNumButton = (Button) findViewById(R.id.userAuthNumButton);
        userVerificationNextButton = (Button)findViewById(R.id.userVerificationNextButton);
        userPhoneNumEditText = (EditText)findViewById(R.id.userPhoneNumEditText);
    }
}