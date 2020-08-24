package com.diary.jimin.wellve.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.diary.jimin.wellve.R;

public class FindPasswordActivity extends AppCompatActivity {

    private Button tmpPwButton;
    private Button findIdButton;
    private Button backButton;
    private EditText findPwEmailEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);

        init();
        clickListener();

        tmpPwButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FindPasswordActivity.this, FindPasswordCompleteActivity.class);
                startActivity(intent);
            }
        });

        findIdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FindPasswordActivity.this, FindIdActivity.class);
                startActivity(intent);
                Log.d("dbwlsl", "findIdButton");
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void init() {
        tmpPwButton = (Button) findViewById(R.id.tmpPwButton);
        findIdButton = (Button) findViewById(R.id.findIdButton);
        backButton = (Button) findViewById(R.id.findPwBackButton);
        findPwEmailEditText = (EditText) findViewById(R.id.findPwEmailEditText);

        findPwEmailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tmpPwButton.setSelected(true);
                tmpPwButton.setTextColor(getResources().getColor(R.color.colorWhite));
            }

            @Override
            public void afterTextChanged(Editable s) {
                tmpPwButton.setSelected(true);
                tmpPwButton.setTextColor(getResources().getColor(R.color.colorWhite));
            }
        });
    }

    private void clickListener() {
        tmpPwButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (tmpPwButton.isSelected()) {
                }
                else if(findIdButton.isSelected()){

                }

            }
        });
    }

}